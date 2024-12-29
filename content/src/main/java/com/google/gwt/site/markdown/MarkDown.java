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

public class MarkDown {

  public static void main(String[] args) throws MDHelperException, TranslaterException {

    if (args.length < 3) {
      System.out.println("Usage MarkDown <sourceDir> <outputDir> <templateFile> [templateTOC]");
      throw new IllegalArgumentException("Usage MarkDown <sourceDir> <outputDir> <templateFile> [templateTOC]");
    }

    String sourceDir = args[0];
    System.out.println("source directory: '" + sourceDir + "'");

    String outputDir = args[1];
    System.out.println("output directory: '" + outputDir + "'");

    String templateFile = args[2];
    System.out.println("template file: '" + templateFile + "'");

    String templateToc = args.length > 3 ? args[3] : null;
    System.out.println("template TOC file: '" + templateToc + "'");

    MDHelper helper = new MDHelper();
    try {
      helper.setOutputDirectory(outputDir)
         .setSourceDirectory(sourceDir)
         .setTemplateFile(templateFile)
         .setTemplateToc(templateToc)
         .create()
         .translate();
    } catch (MDHelperException e) {
      e.printStackTrace();
      throw e;
    } catch (TranslaterException e) {
      e.printStackTrace();
      throw e;
    }

  }

}
