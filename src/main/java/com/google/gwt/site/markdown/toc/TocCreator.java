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
package com.google.gwt.site.markdown.toc;

import com.google.gwt.site.markdown.fs.MDNode;
import com.google.gwt.site.markdown.fs.MDParent;

import java.util.List;

public class TocCreator {

  public String createTocForNode(MDParent root, MDNode node) {

    StringBuffer buffer = new StringBuffer();
    buffer.append("<ul>");
    render(root, buffer, node);
    buffer.append("</ul>");

    return buffer.toString();
  }

  private void render(MDNode node, StringBuffer buffer, MDNode tocNode) {

    MDNode tmpNode = tocNode;
    while (tmpNode.getParent() != null) {
      if (tmpNode.isExcludeFromToc())
        return;
      tmpNode = tmpNode.getParent();
    }

    tmpNode = node;
    while (tmpNode.getParent() != null) {
      if (tmpNode.isExcludeFromToc())
        return;
      tmpNode = tmpNode.getParent();
    }

    if (node.isFolder()) {
      MDParent mdParent = node.asFolder();

      if (node.getDepth() != 0) {
        buffer.append("<li class='folder'>");
        buffer.append("<a href='#'>");
        buffer.append(node.getDisplayName());
        buffer.append("</a>");
        buffer.append("<ul>");
      }

      List<MDNode> children = mdParent.getChildren();
      for (MDNode child : children) {
        render(child, buffer, tocNode);
      }

      if (node.getDepth() != 0) {
        buffer.append("</ul>");
        buffer.append("</li>");
      }
    } else {
      StringBuffer relativeUrl = new StringBuffer();
      if (tocNode.getDepth() > 0) {
        for (int i = 1; i < tocNode.getDepth(); i++) {
          relativeUrl.append("../");
        }
      }

      StringBuffer absoluteUrl = new StringBuffer();
      absoluteUrl.append("/");
      absoluteUrl.append(node.getRelativePath());

      relativeUrl.append(node.getRelativePath());

      buffer.append("<li class='file'>");
      // TODO escape HTML
      buffer.append("<a href='" + relativeUrl.toString() + "' ahref='" + absoluteUrl.toString()
          + "' title='" + node.getDescription() + "'>" + node.getDisplayName() + "</a>");
      buffer.append("</li>");
    }
  }
}
