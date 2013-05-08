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

import com.google.gwt.site.markdown.TranslaterException;

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

  public static final String CONFIG_XML = "config.xml";

  private static class FolderConfig {
    private List<String> sortingStructure;
    private String folderDisplayName;
    private String folderHref;
    private List<String> excludeList;

    public FolderConfig(
        String folderDisplayName, String folderHref, List<String> sortingStructure, List<String> excludeList) {
      this.folderDisplayName = folderDisplayName;
      this.folderHref = folderHref;
      this.sortingStructure = sortingStructure;
      this.excludeList = excludeList;
      

    }

    public List<String> getSortingStructure() {
      return sortingStructure;
    }

    public String getFolderDisplayName() {
      return folderDisplayName;
    }

    public String getFolderHref() {
      return folderHref;
    }
    
   
    public List<String> getExcludeList() {
      return excludeList;
    }
  }

  public MDParent traverse(File file) throws TranslaterException {
    MDParent mdParent = traverse(null, file, 0, "");
    removeEmptyDirs(mdParent);

    readConfig(mdParent);

    return mdParent;
  }

  private void readConfig(MDParent current) throws TranslaterException {

    if (current.getConfigFile() != null) {
      FolderConfig config = parseConfig(current.getConfigFile());

      current.setSortingStructure(config.getSortingStructure());
      if (config.getFolderDisplayName() != null
          && !config.getFolderDisplayName().trim().equals("")) {
        current.setDisplayName(config.getFolderDisplayName());
      }

      if (config.getFolderHref() != null && !config.getFolderHref().trim().equals("")) {
        current.setHref(config.getFolderHref());
      }
      
      if(config.getExcludeList() != null) {
        for(String exclude : config.getExcludeList()) {
          String fileName = exclude + ".md";
          for(MDNode node: current.getChildren()) {
            if(fileName.equals(node.getName())) {
              node.setExcludeFromToc(true);
              break;
            }
          }
        }
      }
    }

    for (MDNode mdNode : current.getChildren()) {
      if (mdNode instanceof MDParent) {
        MDParent mdParent = (MDParent) mdNode;
        readConfig(mdParent);
      }
    }

  }

  private void removeEmptyDirs(MDParent current) {
    for (MDNode mdNode : current.getChildren()) {
      if (mdNode instanceof MDParent) {
        MDParent mdParent = (MDParent) mdNode;
        removeEmptyDirs(mdParent);
      }

    }

    if (current.getChildren().size() == 0) {
      current.getParent().getChildren().remove(current);
    }

  }

  private MDParent traverse(MDParent parent, File file, int depth, String path)
      throws TranslaterException {

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
      if (file.getName().equals(CONFIG_XML)) {

        parent.setConfigFile(file);

      } else {
        MDNode mdNode = new MDNode(parent, file.getName(), file.getAbsolutePath(), depth,
            path + changeExtension(file.getName()));
        parent.addChild(mdNode);
      }

    } else {
      //TODO 
      System.out.println("how did we get here?");
    }

    return null;

  }

  private FolderConfig parseConfig(File file) throws TranslaterException {
    DocumentBuilder builder;
    List<String> sortingList = null;
    List<String> excludeList = new LinkedList<String>();

    String href = null;
    String name = null;

    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

      Document document = builder.parse(file);
      Element documentElement = document.getDocumentElement();

      if (!"folder".equalsIgnoreCase(documentElement.getTagName())) {
        throw new TranslaterException(
            "the file '" + file.getAbsolutePath() + "' does not contain a folder tag");
      }

      if (documentElement.hasAttribute("name")) {
        name = documentElement.getAttribute("name");
      }

      if (documentElement.hasAttribute("href")) {
        href = documentElement.getAttribute("href");
      }

      NodeList childNodes = documentElement.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {

        Node node = childNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }
        Element entryNode = (Element) node;

        if ("sorting".equalsIgnoreCase(entryNode.getTagName())) {
          sortingList = parseSortingStructure(entryNode);
        }
        
        if ("toc".equalsIgnoreCase(entryNode.getTagName())) {
          NodeList tocChildren = entryNode.getChildNodes();
          for (int j = 0; j < tocChildren.getLength(); j++) {

            Node tocNodes = tocChildren.item(i);
            if (tocNodes.getNodeType() != Node.ELEMENT_NODE) {
              continue;
            }
            Element tocElement = (Element) tocNodes;
            if ("excludes".equalsIgnoreCase(tocElement.getTagName())) {
              excludeList = parseExcludes(tocElement);
            }
          }
          
        }

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

    return new FolderConfig(name, href, sortingList, excludeList);
  }

  /**
   * @param entryNode
   * @return
   */
  private List<String> parseExcludes(Element excludesNode) {
    List<String> list = new LinkedList<String>();

    NodeList childNodes = excludesNode.getChildNodes();
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
    return list;
  }

  private List<String> parseSortingStructure(Element sortingNode) {
    List<String> list = new LinkedList<String>();

    NodeList childNodes = sortingNode.getChildNodes();
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
    return list;
  }

  private String changeExtension(String fileName) {
    return fileName.substring(0, fileName.length() - ".md".length()) + ".html";
  }

  private boolean ignoreFile(File file) {
    // ignore all files that do not end with .md
    return !file.isDirectory() && !file.getName().endsWith(".md")
        && !file.getName().equals(CONFIG_XML);
  }

}
