/*
 * Copyright 2013 Daniel Kurka
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

import org.pegdown.PegDownProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MDTranslater {

	private PegDownProcessor pegDownProcessor = new PegDownProcessor();

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
		  System.out.println(node.getPath());
			String markDown = getNodeContent(node.getPath());

			// parse description for TOC

			String htmlMarkDown = pegDownProcessor.markdownToHtml(markDown);
			// RootNode rootNode =
			// pegDownProcessor.parseMarkdown(markDown.toCharArray());

			String toc = tocCreator.createTocForNode(root, node);

			String head = createHeadForNode(node);

			String html = fillTemplate(htmlMarkDown, toc, head);

			writer.writeHTML(node, html);

		}

	}

	private String createHeadForNode(MDNode node) {
		int depth = node.getDepth();
		StringBuffer buffer = new StringBuffer();

		for (int i = 1; i < depth; i++) {
			buffer.append("../");
		}

		String baseUrl = buffer.toString();
		StringBuffer headBuffer = new StringBuffer();

		headBuffer.append("<link href='");
		headBuffer.append(baseUrl);
		headBuffer.append("css/main.css' rel='stylesheet' type='text/css'>");

		headBuffer.append("<script src='");
		headBuffer.append(baseUrl);
		headBuffer.append("js/main.js'></script>");

		return headBuffer.toString();
	}

	private String fillTemplate(String html, String toc, String head) {
		return template.replace("$content", html).replace("$toc", toc).replace("$head", head);
	}

	private String getNodeContent(String path) throws TranslaterException {
		try {
			return Util.getStringFromFile(new File(path));
		} catch (IOException e1) {
			throw new TranslaterException("can not load content from file: '" + path + "'", e1);
		}

	}
}
