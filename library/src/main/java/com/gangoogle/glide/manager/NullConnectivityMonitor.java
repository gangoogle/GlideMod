package com.gangoogle.glide.manager;

/** A no-op {@link com.gangoogle.glide.manager.ConnectivityMonitor}. */
class NullConnectivityMonitor implements ConnectivityMonitor {

  @Override
  public void onStart() {
    // Do nothing.
  }

  @Override
  public void onStop() {
    // Do nothing.
  }

  @Override
  public void onDestroy() {
    // Do nothing.
  }
}
