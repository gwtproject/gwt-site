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

import com.google.gwt.site.markdown.fs.MDNode;
import com.google.gwt.site.markdown.fs.MDParent;
import com.google.gwt.site.markdown.toc.TocCreator;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MDTranslater {
  private static final String SEPARATOR = File.separator;


  private final TocCreator tocCreator;

  private final MarkupWriter writer;

  private final String template;

  public MDTranslater(TocCreator tocCreator, MarkupWriter writer, String template) {
    this.tocCreator = tocCreator;
    this.writer = writer;
    this.template = template;
  }

  public void render(MDParent root) throws TranslaterException {
    renderTree(root, root);
  }

  private void renderTree(MDNode node, MDParent root) throws TranslaterException {

    if (node.isFolder()) {
      MDParent mdParent = node.asFolder();

      List<MDNode> children = mdParent.getChildren();
      for (MDNode mdNode : children) {
        renderTree(mdNode, root);
      }

    } else {
      String markDown = getNodeContent(node.getPath());
      String htmlMarkDown = markDownToHtml(markDown);

      String toc = tocCreator.createTocForNode(root, node);

      String head = createHeadForNode(node);

      String relativePath = "./";
      for (int i = 1; i < node.getDepth(); i++) {
        relativePath += "../";
      }

      String html = fillTemplate(
          adjustRelativePath(template, relativePath),
          htmlMarkDown,
          adjustRelativePath(toc, relativePath),
          adjustRelativePath(head, relativePath),
          getEditUrl(node.getPath()));

      writer.writeHTML(node, html);
    }

  }

  protected static String markDownToHtml(String markDown) {
    Set<Extension> extensions = Collections.singleton(TablesExtension.create());
    Parser parser = Parser.builder()
        .extensions(extensions).build();
    Node document = parser.parse(markDown);
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    return renderer.render(document);
  }

  private String getEditUrl(String path) {
    // TODO you should support more than one template
    if (path.endsWith("markdown/index.md")) {
      return "";
    }

    int index = path.indexOf(SEPARATOR + "src" + SEPARATOR);
    return "<a class=\"icon_editGithub\" href=\"https://github.com/gwtproject/gwt-site/edit/main/"
           + path.substring(index + 1).replace(SEPARATOR, "/") + "\"></a>";
  }

  private String createHeadForNode(MDNode node) {
    return "<link href='css/main.css' rel='stylesheet' type='text/css'>";
  }

  private String fillTemplate(String template, String html, String toc, String head, String editUrl) {
    return template
        .replace("$content", html)
        .replace("$toc", toc)
        .replace("$toc", toc)
        .replace("$head", head)
        .replace("$editLink", editUrl);
  }

  protected String adjustRelativePath(String html, String relativePath) {
    // Just using Regexp to add relative paths to certain urls.
    // If we wanted to support a more complicated syntax
    // we could parse the template with some library like jsoup
    return html.replaceAll("(href|src)=(['\"])(?:(?:/+)|(?!(?:[a-z]+:|#)))(.*?)(\\2)",
        "$1='" + relativePath + "$3'");
  }

  private String getNodeContent(String path) throws TranslaterException {
    try {
      return Util.getStringFromFile(new File(path));
    } catch (IOException e1) {
      throw new TranslaterException("can not load content from file: '" + path + "'", e1);
    }

  }
}
