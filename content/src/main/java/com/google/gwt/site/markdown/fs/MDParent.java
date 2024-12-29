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
package com.google.gwt.site.markdown.fs;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MDParent extends MDNode {

  public MDParent(MDParent parent, String name, String path, int depth, String relativePath) {
    super(parent, name, path, depth, relativePath);
  }

  private List<MDNode> children = new LinkedList<MDNode>();
  private String href;
  private File configFile;

  @Override
  public String toString() {
    return "MDParent [getName()=" + getName() + ", getDepth()=" + getDepth() + "]";
  }

  public void setChildren(List<MDNode> children) {
    this.children = children;
  }

  public void addChild(MDNode node) {
    children.add(node);
  }

  public List<MDNode> getChildren() {
    return children;
  }

  @Override
  public boolean isFolder() {
    return true;
  }

  @Override
  public String getDisplayName() {
    if (displayName == null)
      return getName();
    return displayName;
  }

  public void setHref(String href) {
    this.href = href;

  }

  public String getHref() {
    return href;
  }

  public void setConfigFile(File file) {
    this.configFile = file;

  }

  public File getConfigFile() {
    return configFile;
  }
}
