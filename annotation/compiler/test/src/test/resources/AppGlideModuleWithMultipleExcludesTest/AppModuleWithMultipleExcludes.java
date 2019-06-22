package com.gangoogle.glide.test;

import com.gangoogle.glide.annotation.Excludes;
import com.gangoogle.glide.annotation.GlideModule;
import com.gangoogle.glide.module.AppGlideModule;

@GlideModule
@Excludes({EmptyLibraryModule1.class, EmptyLibraryModule2.class})
public final class AppModuleWithMultipleExcludes extends AppGlideModule {}