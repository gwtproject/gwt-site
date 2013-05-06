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
package com.google.gwt.site.markdown.fs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FileSystemTraverser {

  public static final String ORDER_XML = "order.xml";

  public MDParent traverse(File file) {
    MDParent mdParent = traverse(null, file, 0, "");
    removeEmptyDirs(mdParent);
    return mdParent;
  }

  private void removeEmptyDirs(MDParent current) {
    for (MDNode mdNode : current.getChildren()) {
      if (mdNode instanceof MDParent) {
        MDParent mdParent = (MDParent) mdNode;
        removeEmptyDirs(mdParent);
      }

    }

    if(current.getChildren().size() == 0) {
      current.getParent().getChildren().remove(current);
    }
      
     

  }

  private MDParent traverse(MDParent parent, File file, int depth, String path) {

    if (ignoreFile(file)) {
      return null;
    }

    if (file.isDirectory()) {
      MDParent mdParent;

      if (parent == null) {
        mdParent = new MDParent(null, "ROOT", null, depth, "");
      } else {
        mdParent = new MDParent(
            parent, file.getName(), file.getAbsolutePath(), depth, path + file.getName() + "/");
        parent.addChild(mdParent);

      }

      File[] listFiles = file.listFiles();
      for (File newFile : listFiles) {
        traverse(mdParent, newFile, depth + 1, mdParent.getRelativePath());
      }

      return mdParent;

    } else if (file.isFile()) {
      if (file.getName().equals(ORDER_XML)) {
        List<String> sortingStructure = parseSortingStructure(file);
        parent.setSortingStructure(sortingStructure);

      } else {
        MDNode mdNode = new MDNode(parent, file.getName(), file.getAbsolutePath(), depth,
            path + changeExtension(file.getName()));
        parent.addChild(mdNode);
      }

    } else {
      System.out.println("how did we get here?");
    }

    return null;

  }

  private List<String> parseSortingStructure(File file) {
    DocumentBuilder builder;
    List<String> list = new LinkedList<String>();
    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

      Document document = builder.parse(file);
      Element documentElement = document.getDocumentElement();

      NodeList childNodes = documentElement.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {

        Node node = childNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }
        Element entryNode = (Element) node;

        if (entryNode.getChildNodes().getLength() != 1) {
          // TODO error
        }
        list.add(entryNode.getChildNodes().item(0).getNodeValue());
      }

    } catch (ParserConfigurationException e) {

      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return list;
  }

  private String changeExtension(String fileName) {
    return fileName.substring(0, fileName.length() - ".md".length()) + ".html";
  }

  private boolean ignoreFile(File file) {
    // ignore all files that do not end with .md
    return !file.isDirectory() && !file.getName().endsWith(".md")
        && !file.getName().equals(ORDER_XML);
  }

}
