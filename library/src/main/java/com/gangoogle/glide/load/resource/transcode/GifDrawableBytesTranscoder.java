package com.gangoogle.glide.load.resource.transcode;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.gangoogle.glide.load.Options;
import com.gangoogle.glide.load.engine.Resource;
import com.gangoogle.glide.load.resource.bytes.BytesResource;
import com.gangoogle.glide.load.resource.gif.GifDrawable;
import com.gangoogle.glide.util.ByteBufferUtil;
import java.nio.ByteBuffer;

/**
 * An {@link com.gangoogle.glide.load.resource.transcode.ResourceTranscoder} that converts {@link
 * com.gangoogle.glide.load.resource.gif.GifDrawable} into bytes by obtaining the original bytes of
 * the GIF from the {@link com.gangoogle.glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
  @Nullable
  @Override
  public Resource<byte[]> transcode(
      @NonNull Resource<GifDrawable> toTranscode, @NonNull Options options) {
    GifDrawable gifData = toTranscode.get();
    ByteBuffer byteBuffer = gifData.getBuffer();
    return new BytesResource(ByteBufferUtil.toBytes(byteBuffer));
  }
}
