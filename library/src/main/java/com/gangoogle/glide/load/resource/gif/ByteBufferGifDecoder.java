package com.gangoogle.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.gangoogle.glide.GlideMod;
import com.gangoogle.glide.gifdecoder.GifDecoder;
import com.gangoogle.glide.gifdecoder.GifHeader;
import com.gangoogle.glide.gifdecoder.GifHeaderParser;
import com.gangoogle.glide.gifdecoder.StandardGifDecoder;
import com.gangoogle.glide.load.DecodeFormat;
import com.gangoogle.glide.load.ImageHeaderParser;
import com.gangoogle.glide.load.ImageHeaderParser.ImageType;
import com.gangoogle.glide.load.ImageHeaderParserUtils;
import com.gangoogle.glide.load.Options;
import com.gangoogle.glide.load.ResourceDecoder;
import com.gangoogle.glide.load.Transformation;
import com.gangoogle.glide.load.engine.bitmap_recycle.ArrayPool;
import com.gangoogle.glide.load.engine.bitmap_recycle.BitmapPool;
import com.gangoogle.glide.load.resource.UnitTransformation;
import com.gangoogle.glide.util.LogTime;
import com.gangoogle.glide.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;

/**
 * An {@link com.gangoogle.glide.load.ResourceDecoder} that decodes {@link
 * com.gangoogle.glide.load.resource.gif.GifDrawable} from {@link java.io.InputStream} data.
 */
public class ByteBufferGifDecoder implements ResourceDecoder<ByteBuffer, GifDrawable> {
  private static final String TAG = "BufferGifDecoder";
  private static final GifDecoderFactory GIF_DECODER_FACTORY = new GifDecoderFactory();
  private static final GifHeaderParserPool PARSER_POOL = new GifHeaderParserPool();

  private final Context context;
  private final List<ImageHeaderParser> parsers;
  private final GifHeaderParserPool parserPool;
  private final GifDecoderFactory gifDecoderFactory;
  private final GifBitmapProvider provider;

  // Public API.
  @SuppressWarnings("unused")
  public ByteBufferGifDecoder(Context context) {
    this(
        context,
        GlideMod.get(context).getRegistry().getImageHeaderParsers(),
        GlideMod.get(context).getBitmapPool(),
        GlideMod.get(context).getArrayPool());
  }

  public ByteBufferGifDecoder(
      Context context,
      List<ImageHeaderParser> parsers,
      BitmapPool bitmapPool,
      ArrayPool arrayPool) {
    this(context, parsers, bitmapPool, arrayPool, PARSER_POOL, GIF_DECODER_FACTORY);
  }

  @VisibleForTesting
  ByteBufferGifDecoder(
      Context context,
      List<ImageHeaderParser> parsers,
      BitmapPool bitmapPool,
      ArrayPool arrayPool,
      GifHeaderParserPool parserPool,
      GifDecoderFactory gifDecoderFactory) {
    this.context = context.getApplicationContext();
    this.parsers = parsers;
    this.gifDecoderFactory = gifDecoderFactory;
    this.provider = new GifBitmapProvider(bitmapPool, arrayPool);
    this.parserPool = parserPool;
  }

  @Override
  public boolean handles(@NonNull ByteBuffer source, @NonNull Options options) throws IOException {
    return !options.get(GifOptions.DISABLE_ANIMATION)
        && ImageHeaderParserUtils.getType(parsers, source) == ImageType.GIF;
  }

  @Override
  public GifDrawableResource decode(
      @NonNull ByteBuffer source, int width, int height, @NonNull Options options) {
    final GifHeaderParser parser = parserPool.obtain(source);
    try {
      return decode(source, width, height, parser, options);
    } finally {
      parserPool.release(parser);
    }
  }

  @Nullable
  private GifDrawableResource decode(
      ByteBuffer byteBuffer, int width, int height, GifHeaderParser parser, Options options) {
    long startTime = LogTime.getLogTime();
    try {
      final GifHeader header = parser.parseHeader();
      if (header.getNumFrames() <= 0 || header.getStatus() != GifDecoder.STATUS_OK) {
        // If we couldn't decode the GIF, we will end up with a frame count of 0.
        return null;
      }

      Bitmap.Config config =
          options.get(GifOptions.DECODE_FORMAT) == DecodeFormat.PREFER_RGB_565
              ? Bitmap.Config.RGB_565
              : Bitmap.Config.ARGB_8888;

      int sampleSize = getSampleSize(header, width, height);
      GifDecoder gifDecoder = gifDecoderFactory.build(provider, header, byteBuffer, sampleSize);
      gifDecoder.setDefaultBitmapConfig(config);
      gifDecoder.advance();
      Bitmap firstFrame = gifDecoder.getNextFrame();
      if (firstFrame == null) {
        return null;
      }

      Transformation<Bitmap> unitTransformation = UnitTransformation.get();

      GifDrawable gifDrawable =
          new GifDrawable(context, gifDecoder, unitTransformation, width, height, firstFrame);

      return new GifDrawableResource(gifDrawable);
    } finally {
      if (Log.isLoggable(TAG, Log.VERBOSE)) {
        Log.v(TAG, "Decoded GIF from stream in " + LogTime.getElapsedMillis(startTime));
      }
    }
  }

  private static int getSampleSize(GifHeader gifHeader, int targetWidth, int targetHeight) {
    int exactSampleSize =
        Math.min(gifHeader.getHeight() / targetHeight, gifHeader.getWidth() / targetWidth);
    int powerOfTwoSampleSize = exactSampleSize == 0 ? 0 : Integer.highestOneBit(exactSampleSize);
    // Although functionally equivalent to 0 for BitmapFactory, 1 is a safer default for our code
    // than 0.
    int sampleSize = Math.max(1, powerOfTwoSampleSize);
    if (Log.isLoggable(TAG, Log.VERBOSE) && sampleSize > 1) {
      Log.v(
          TAG,
          "Downsampling GIF"
              + ", sampleSize: "
              + sampleSize
              + ", target dimens: ["
              + targetWidth
              + "x"
              + targetHeight
              + "]"
              + ", actual dimens: ["
              + gifHeader.getWidth()
              + "x"
              + gifHeader.getHeight()
              + "]");
    }
    return sampleSize;
  }

  @VisibleForTesting
  static class GifDecoderFactory {
    GifDecoder build(
        GifDecoder.BitmapProvider provider, GifHeader header, ByteBuffer data, int sampleSize) {
      return new StandardGifDecoder(provider, header, data, sampleSize);
    }
  }

  @VisibleForTesting
  static class GifHeaderParserPool {
    private final Queue<GifHeaderParser> pool = Util.createQueue(0);

    synchronized GifHeaderParser obtain(ByteBuffer buffer) {
      GifHeaderParser result = pool.poll();
      if (result == null) {
        result = new GifHeaderParser();
      }
      return result.setData(buffer);
    }

    synchronized void release(GifHeaderParser parser) {
      parser.clear();
      pool.offer(parser);
    }
  }
}
