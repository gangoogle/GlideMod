package com.gangoogle.glide.load.model.stream;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

import com.gangoogle.glide.load.Options;
import com.gangoogle.glide.load.data.DataFetcher;
import com.gangoogle.glide.load.data.HttpUrlFetcher;
import com.gangoogle.glide.load.model.GlideUrl;
import com.gangoogle.glide.util.Preconditions;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpGlideUrlLoaderTest {
  private HttpGlideUrlLoader loader;
  private GlideUrl model;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    loader = new HttpGlideUrlLoader();
    model = mock(GlideUrl.class);
  }

  @Test
  public void testReturnsValidFetcher() {
    DataFetcher<InputStream> result =
        Preconditions.checkNotNull(loader.buildLoadData(model, 100, 100, new Options())).fetcher;
    assertThat(result).isInstanceOf(HttpUrlFetcher.class);
  }
}
