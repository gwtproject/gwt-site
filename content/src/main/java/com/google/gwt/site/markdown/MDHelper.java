/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.site.markdown;

import java.io.File;

import com.google.gwt.site.markdown.fs.FileSystemTraverser;
import com.google.gwt.site.markdown.fs.MDParent;
import com.google.gwt.site.markdown.toc.TocCreator;
import com.google.gwt.site.markdown.toc.TocFromMdCreator;
import com.google.gwt.site.markdown.toc.TocFromTemplateCreator;

public class MDHelper {
  private String sourceDirectory;
  private String outputDirectory;

  private String templateFile;
  private String templateTocFile;

  private boolean created = false;
  private File sourceDirectoryFile;
  private File outputDirectoryFile;
  private String template;
  private String templateToc;

  public MDHelper setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
    return this;
  }

  public MDHelper setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    return this;
  }

  public MDHelper setTemplateFile(String templateFile) {
    this.templateFile = templateFile;
    return this;
  }

  public MDHelper setTemplateToc(String templateTocFile) {
    this.templateTocFile = templateTocFile;
    return this;
  }

  public MDHelper create() throws MDHelperException {
    // Source dir
    if (sourceDirectory == null) {
      throw new MDHelperException("no sourceDirectory set");
    }
    sourceDirectoryFile = new File(sourceDirectory);

    if (!sourceDirectoryFile.exists()) {
      throw new MDHelperException("sourceDirectory ('" + sourceDirectory + "') does not exist");
    }

    if (!sourceDirectoryFile.isDirectory()) {
      throw new MDHelperException("sourceDirectory ('" + sourceDirectory + "') is no directory");
    }

    if (!sourceDirectoryFile.canRead()) {
      throw new MDHelperException(
          "sourceDirectory ('" + sourceDirectory + "') can not read source directory");
    }

    // output dir
    if (outputDirectory == null) {
      throw new MDHelperException("no outputDirectory set");
    }
    outputDirectoryFile = new File(outputDirectory);
    if (!outputDirectoryFile.exists()) {
      if (!outputDirectoryFile.mkdirs()) {
        throw new MDHelperException(
            "outputDirectory ('" + outputDirectoryFile + "') can not be created");
      }

    }

    if (!outputDirectoryFile.isDirectory()) {
      throw new MDHelperException(
          "outputDirectory ('" + outputDirectoryFile + "') is no directory");
    }

    if (!outputDirectoryFile.canWrite()) {
      throw new MDHelperException(
          "outputDirectory ('" + outputDirectoryFile + "') can not write to output directory");
    }

    // read template
    if (templateFile == null) {
      throw new MDHelperException("no templateFile set");
    }
    template = readFile(templateFile);

    // read template TOC if parameter is provided
    if (templateTocFile != null) {
      templateToc = MDTranslater.markDownToHtml(readFile(templateTocFile));
    }

    created = true;
    return this;
  }

  private String readFile(String filePath) throws MDHelperException {
    File file = new File(filePath);
    try {
        return Util.getStringFromFile(file);
     } catch (Exception e) {
       throw new MDHelperException("can not read file" + filePath, e);
     }
  }

  public void translate() throws TranslaterException {

    if (!created) {
      throw new IllegalStateException();
    }

    FileSystemTraverser traverser = new FileSystemTraverser();
    MDParent mdRoot = traverser.traverse(sourceDirectoryFile);

    TocCreator tocCreator = templateToc != null ? new TocFromTemplateCreator(templateToc) : new TocFromMdCreator();

    new MDTranslater(tocCreator, new MarkupWriter(outputDirectoryFile), template)
       .render(mdRoot);
  }

}
