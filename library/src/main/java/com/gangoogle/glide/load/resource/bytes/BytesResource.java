package com.gangoogle.glide.load.resource.bytes;

import android.support.annotation.NonNull;
import com.gangoogle.glide.load.engine.Resource;
import com.gangoogle.glide.util.Preconditions;

/** An {@link com.gangoogle.glide.load.engine.Resource} wrapping a byte array. */
public class BytesResource implements Resource<byte[]> {
  private final byte[] bytes;

  public BytesResource(byte[] bytes) {
    this.bytes = Preconditions.checkNotNull(bytes);
  }

  @NonNull
  @Override
  public Class<byte[]> getResourceClass() {
    return byte[].class;
  }

  /**
   * In most cases it will only be retrieved once (see linked methods).
   *
   * @return the same array every time, do not mutate the contents. Not a copy returned, because
   *     copying the array can be prohibitively expensive and/or lead to OOMs.
   * @see com.gangoogle.glide.load.ResourceEncoder
   * @see com.gangoogle.glide.load.resource.transcode.ResourceTranscoder
   * @see com.gangoogle.glide.request.SingleRequest#onResourceReady
   */
  @NonNull
  @Override
  @SuppressWarnings("PMD.MethodReturnsInternalArray")
  public byte[] get() {
    return bytes;
  }

  @Override
  public int getSize() {
    return bytes.length;
  }

  @Override
  public void recycle() {
    // Do nothing.
  }
}
