package com.gangoogle.glide;

import android.content.Context;
import androidx.annotation.NonNull;
import com.gangoogle.glide.manager.Lifecycle;
import com.gangoogle.glide.manager.RequestManagerRetriever;
import com.gangoogle.glide.manager.RequestManagerTreeNode;
import com.gangoogle.glide.test.GlideRequests;

/**
 * Generated code, do not modify
 */
final class GeneratedRequestManagerFactory implements RequestManagerRetriever.RequestManagerFactory {
  @Override
  @NonNull
  public RequestManager build(@NonNull Glide glide, @NonNull Lifecycle lifecycle,
      @NonNull RequestManagerTreeNode treeNode, @NonNull Context context) {
    return new GlideRequests(glide, lifecycle, treeNode, context);
  }
}
