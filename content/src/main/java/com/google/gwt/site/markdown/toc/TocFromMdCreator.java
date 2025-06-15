/*
 * Copyright 2014 Google Inc.
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

import java.util.Arrays;
import java.util.List;

public class TocFromMdCreator implements TocCreator {

  public String createTocForNode(MDParent root, MDNode node) {

    StringBuffer buffer = new StringBuffer();
    buffer.append("  <ul>\n");
    render(root, buffer, node);
    buffer.append("  </ul>\n");

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
    
    // Use 4 spaces to indent <li>'s, so as we have room for indenting <ul>'s
    String margin = spaces(4 * node.getDepth());

    if (node.isFolder()) {
      MDParent mdParent = node.asFolder();

      if (node.getDepth() != 0) {
        buffer.append(margin).append("<li class='folder'>");
        buffer.append("<a href='#'>");
        buffer.append(node.getDisplayName());
        buffer.append("</a>\n");
        buffer.append(margin).append("  <ul>\n");
      }

      List<MDNode> children = mdParent.getChildren();
      for (MDNode child : children) {
        render(child, buffer, tocNode);
      }

      if (node.getDepth() != 0) {
        buffer.append(margin).append("  </ul>\n");
        buffer.append(margin).append("</li>\n");
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

      buffer.append(margin).append("<li class='file'>");
      // TODO escape HTML
      buffer.append("<a href='").append(relativeUrl)
          .append("' ahref='").append(absoluteUrl)
          .append("' title='").append(node.getDescription()).append("'>")
          .append(node.getDisplayName()).append("</a>");
      buffer.append("</li>\n");
    }
  }

  private String spaces(int count) {
    final byte[] spaceBytes = new byte[count];
    Arrays.fill(spaceBytes, (byte) ' ');
    return new String(spaceBytes);
  }
}
