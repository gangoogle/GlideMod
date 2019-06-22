package com.gangoogle.glide.test;

import com.gangoogle.glide.annotation.Excludes;
import com.gangoogle.glide.annotation.GlideModule;
import com.gangoogle.glide.module.AppGlideModule;
import com.gangoogle.glide.test._package.LibraryModuleInPackage;

@GlideModule
@Excludes(LibraryModuleInPackage.class)
public final class AppModuleWithLibraryInPackage extends AppGlideModule {}
