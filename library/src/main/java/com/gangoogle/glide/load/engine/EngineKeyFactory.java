package com.gangoogle.glide.load.engine;

import com.gangoogle.glide.load.Key;
import com.gangoogle.glide.load.Options;
import com.gangoogle.glide.load.Transformation;
import java.util.Map;

class EngineKeyFactory {

  @SuppressWarnings("rawtypes")
  EngineKey buildKey(
      Object model,
      BaseKey baseKey,
      Key signature,
      int width,
      int height,
      Map<Class<?>, Transformation<?>> transformations,
      Class<?> resourceClass,
      Class<?> transcodeClass,
      Options options) {
    return new EngineKey(
        baseKey,signature, width, height, transformations, resourceClass, transcodeClass, options);
  }
}
