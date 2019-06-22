package com.gangoogle.glide.test;

import com.gangoogle.glide.annotation.Excludes;
import com.gangoogle.glide.annotation.GlideModule;
import com.gangoogle.glide.module.AppGlideModule;

@GlideModule
@Excludes(EmptyLibraryModule.class)
public final class AppModuleWithExcludes extends AppGlideModule {}