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
import java.io.IOException;
import java.util.Stack;

import com.google.gwt.site.markdown.fs.MDNode;
import com.google.gwt.site.markdown.fs.MDParent;

public class MarkupWriter {

  private final File rootFile;

  public MarkupWriter(File rootFile) {
    this.rootFile = rootFile;
  }

  public void writeHTML(MDNode node, String html) throws TranslaterException {

    if (node.isFolder()) {
      throw new IllegalArgumentException();
    }

    Stack<MDParent> stack = new Stack<MDParent>();

    MDParent tmp = node.getParent();
    stack.add(tmp);

    while (tmp.getParent() != null) {
      tmp = tmp.getParent();
      stack.add(tmp);
    }

    // get rootnode from stack
    stack.pop();

    File currentDir = rootFile;
    ensureDirectory(currentDir);
    while (!stack.isEmpty()) {
      MDParent pop = stack.pop();
      currentDir = new File(currentDir, pop.getName());
      ensureDirectory(currentDir);
    }

    String fileName =
        node.getName().substring(0, node.getName().length() - ".md".length()) + ".html";
    File fileToWrite = new File(currentDir, fileName);

    try {
      Util.writeStringToFile(fileToWrite, html);
    } catch (IOException e) {
      throw new TranslaterException("can not write markup to file: '" + fileToWrite + "'", e);

    }
  }

  private void ensureDirectory(File dir) throws TranslaterException {
    if (!dir.exists()) {
      boolean created = dir.mkdir();
      if (!created) {
        throw new TranslaterException("can not create directory: '" + dir + "'");
      }
    }
  }
}
