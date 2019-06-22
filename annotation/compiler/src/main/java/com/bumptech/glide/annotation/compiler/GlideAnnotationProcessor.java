package com.gangoogle.glide.annotation.compiler;

import com.gangoogle.glide.annotation.GlideType;
import com.google.auto.service.AutoService;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

// Links in Javadoc will work due to build setup, even though there is no direct dependency here.
/**
 * Generates classes based on Glide's annotations that configure Glide, add support for additional
 * resource types, and/or extend Glide's API.
 *
 * <p>This processor discovers all {@code AppGlideModule} and {@code LibraryGlideModule}
 * implementations that are annotated with {@link com.gangoogle.glide.annotation.GlideModule}. Any
 * implementations missing the annotation will be ignored.
 *
 * <p>This processor also discovers all {@link com.gangoogle.glide.annotation.GlideExtension}
 * annotated classes.
 *
 * <p>Multiple classes are generated by this processor:
 *
 * <ul>
 *   <li>For {@code LibraryGlideModule}s - A GlideIndexer class in a specific package that will
 *       later be used by the processor to discover all {@code LibraryGlideModule} classes.
 *   <li>For {@code AppGlideModule}s - A single {@code AppGlideModule} implementation ({@code
 *       com.bumptech.glide.GeneratedAppGlideModule}) that calls all {@code LibraryGlideModule}s and
 *       the original {@code AppGlideModule} in the correct order when Glide is initialized.
 *   <li>{@link com.gangoogle.glide.annotation.GlideExtension}s -
 *       <ul>
 *         <li>A {@code com.bumptech.glide.request.RequestOptions} implementation that contains
 *             static versions of all builder methods in the base class and both static and instance
 *             versions of methods in all {@link com.gangoogle.glide.annotation.GlideExtension}s.
 *         <li>If one or more methods in one or more {@link
 *             com.gangoogle.glide.annotation.GlideExtension} annotated classes are annotated with
 *             {@link GlideType}:
 *             <ul>
 *               <li>A {@code com.bumptech.glide.RequestManager} implementation containing a
 *                   generated method for each method annotated with {@link GlideType}.
 *               <li>A {@code
 *                   com.bumptech.glide.manager.RequestManagerRetriever.RequestManagerFactory}
 *                   implementation that produces the generated {@code
 *                   com.bumptech.glide.RequestManager}s.
 *               <li>A {@code com.bumptech.glide.Glide} look-alike that implements all static
 *                   methods in the {@code com.bumptech.glide.Glide} singleton and returns the
 *                   generated {@code com.bumptech.glide.RequestManager} implementation when
 *                   appropriate.
 *             </ul>
 *       </ul>
 * </ul>
 *
 * <p>{@code AppGlideModule} implementations must only be included in applications, not in
 * libraries. There must be exactly one {@code AppGlideModule} implementation per Application. The
 * {@code AppGlideModule} class is used as a signal that all modules have been found and that the
 * final merged {@code com.bumptech.glide.GeneratedAppGlideModule} impl can be created.
 */
@AutoService(Processor.class)
public final class GlideAnnotationProcessor extends AbstractProcessor {
  static final boolean DEBUG = false;
  private ProcessorUtil processorUtil;
  private LibraryModuleProcessor libraryModuleProcessor;
  private AppModuleProcessor appModuleProcessor;
  private boolean isGeneratedAppGlideModuleWritten;
  private ExtensionProcessor extensionProcessor;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    processorUtil = new ProcessorUtil(processingEnvironment);
    IndexerGenerator indexerGenerator = new IndexerGenerator(processorUtil);
    libraryModuleProcessor = new LibraryModuleProcessor(processorUtil, indexerGenerator);
    appModuleProcessor = new AppModuleProcessor(processingEnvironment, processorUtil);
    extensionProcessor =
        new ExtensionProcessor(processingEnvironment, processorUtil, indexerGenerator);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> result = new HashSet<>();
    result.addAll(libraryModuleProcessor.getSupportedAnnotationTypes());
    result.addAll(extensionProcessor.getSupportedAnnotationTypes());
    return result;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  /**
   * Each round we do the following:
   *
   * <ol>
   *   <li>Find all {@code AppGlideModule}s and save them to an instance variable (throw if > 1).
   *   <li>Find all {@code LibraryGlideModule}s
   *   <li>For each {@code LibraryGlideModule}, write an {@code Indexer} with an Annotation with the
   *       class name.
   *   <li>If we wrote any {@code Indexer}s, return and wait for the next round.
   *   <li>If we didn't write any {@code Indexer}s and there is a {@code AppGlideModule}, write the
   *       {@code GeneratedAppGlideModule}. Once the {@code GeneratedAppGlideModule} is written, we
   *       expect to be finished. Any further generation of related classes will result in errors.
   * </ol>
   */
  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
    processorUtil.process();
    boolean newModulesWritten = libraryModuleProcessor.processModules(env);
    boolean newExtensionWritten = extensionProcessor.processExtensions(env);
    appModuleProcessor.processModules(set, env);

    if (newExtensionWritten || newModulesWritten) {
      if (isGeneratedAppGlideModuleWritten) {
        throw new IllegalStateException("Cannot process annotations after writing AppGlideModule");
      }
      return true;
    }

    if (!isGeneratedAppGlideModuleWritten) {
      isGeneratedAppGlideModuleWritten = appModuleProcessor.maybeWriteAppModule();
    }
    return true;
  }
}
