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

public class MDNode {
  private final String name;
  private final MDParent parent;
  private final String path;

  private String description;
  private final int depth;
  private final String relativePath;

  protected String displayName;

  private boolean excludeFromToc;

  public MDNode(MDParent parent, String name, String path, int depth, String relativePath) {
    this.parent = parent;
    this.name = name;
    this.path = path;
    this.depth = depth;
    this.relativePath = relativePath;
    this.description = "";
  }

  public String getName() {
    return name;
  }

  public MDParent getParent() {
    return parent;
  }

  public String getPath() {
    return path;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public int getDepth() {
    return depth;
  }

  @Override
  public String toString() {
    return "MDNode [name=" + name + ", depth=" + depth + "]";
  }

  public String getRelativePath() {
    return relativePath;
  }

  public boolean isFolder() {
    return false;
  }

  public MDParent asFolder() {
    return (MDParent) this;
  }

  /**
   * @param displayName the displayName to set
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * @return the displayName
   */
  public String getDisplayName() {
    if (displayName == null)
      return getName().substring(0, getName().length() - ".md".length());
    return displayName;
  }

  public void setExcludeFromToc(boolean excludeFromToc) {
    this.excludeFromToc = excludeFromToc;
  }

  public boolean isExcludeFromToc() {
    return excludeFromToc;
  }
}
