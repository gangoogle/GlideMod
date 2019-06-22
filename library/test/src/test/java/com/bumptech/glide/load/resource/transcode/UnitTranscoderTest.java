package com.gangoogle.glide.load.resource.transcode;

import static com.gangoogle.glide.tests.Util.mockResource;
import static org.junit.Assert.assertEquals;

import com.gangoogle.glide.load.Options;
import com.gangoogle.glide.load.engine.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnitTranscoderTest {

  @Test
  public void testReturnsTheGivenResource() {
    Resource<Object> resource = mockResource();
    ResourceTranscoder<Object, Object> unitTranscoder = UnitTranscoder.get();

    assertEquals(resource, unitTranscoder.transcode(resource, new Options()));
  }
}
