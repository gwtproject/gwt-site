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

import com.google.gwt.site.markdown.TranslaterException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FileSystemTraverser {

  public static final String CONFIG_XML = "config.xml";

  private static class FolderConfig {
    private String folderHref;
    private List<String> excludeList;
    private List<Entry> folderEntries;

    public FolderConfig(String folderHref, List<String> excludeList, List<Entry> folderEntries) {

      this.folderHref = folderHref;
      this.excludeList = excludeList;
      this.folderEntries = folderEntries;

    }

    public String getFolderHref() {
      return folderHref;
    }

    public List<String> getExcludeList() {
      return excludeList;
    }

    public List<Entry> getFolderEntries() {
      return folderEntries;
    }
  }

  private static class Entry {
    private String name;
    private String displayName;
    private String description;

    public Entry(String name, String displayName, String description) {
      this.name = name;
      this.displayName = displayName;
      this.description = description;
    }

    public String getDescription() {
      return description;
    }

    public String getDisplayName() {
      return displayName;
    }

    public String getName() {
      return name;
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

      if (config.getFolderHref() != null && !config.getFolderHref().trim().equals("")) {
        current.setHref(config.getFolderHref());
      }

      if (config.getExcludeList() != null) {
        for (String exclude : config.getExcludeList()) {
          String fileName = exclude + ".md";
          for (MDNode node : current.getChildren()) {
            if (fileName.equals(node.getName())) {
              node.setExcludeFromToc(true);
              break;
            }
          }
        }
      }

      if (config.getFolderEntries() == null) {
        sortChildrenAlphaBetically(current.getChildren());
      } else {
        for (Entry e : config.getFolderEntries()) {

          if (e.getName() == null) {
            // TODO log warning
            continue;
          }

          boolean folder = e.getName().endsWith("/");
          String nameInTree = null;
          if (folder) {
            nameInTree = e.getName().substring(0, e.getName().length() - "/".length());
          } else {
            nameInTree = e.getName() + ".md";
          }

          for (MDNode node : current.getChildren()) {

            if (nameInTree.equals(node.getName())) {
              node.setDisplayName(e.getDisplayName());
              node.setDescription(e.getDescription());
              break;
            }

          }

        }
        // sort
        sortChildren(config.getFolderEntries(), current);
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
      return null;

    } else {
      throw new RuntimeException("how did we get here?");
    }

  }

  private FolderConfig parseConfig(File file) throws TranslaterException {
    DocumentBuilder builder;
    List<String> excludeList = new LinkedList<String>();

    List<Entry> folderEntries = new LinkedList<Entry>();

    String href = null;

    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

      Document document = builder.parse(file);
      Element documentElement = document.getDocumentElement();

      if (!"folder".equalsIgnoreCase(documentElement.getTagName())) {
        throw new TranslaterException(
            "the file '" + file.getAbsolutePath() + "' does not contain a folder tag");
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

        if ("toc".equalsIgnoreCase(entryNode.getTagName())) {
          NodeList tocChildren = entryNode.getChildNodes();
          for (int j = 0; j < tocChildren.getLength(); j++) {

            Node tocNodes = tocChildren.item(j);
            if (tocNodes.getNodeType() != Node.ELEMENT_NODE) {
              continue;
            }
            Element tocElement = (Element) tocNodes;

            if ("excludes".equalsIgnoreCase(tocElement.getTagName())) {
              excludeList = parseExcludes(tocElement);
            }

            if ("entries".equalsIgnoreCase(tocElement.getTagName())) {
              folderEntries = parseFolderEntries(tocElement);
            }
          }

        }

      }

    } catch (ParserConfigurationException e) {
      throw new TranslaterException("can not construct xml parser", e);
    } catch (SAXException e) {
      throw new TranslaterException("error while parsing xml", e);
    } catch (IOException e) {
      throw new TranslaterException("can not read file", e);
    }

    return new FolderConfig(href, excludeList, folderEntries);
  }

  private List<Entry> parseFolderEntries(Element entriesNode) {
    List<Entry> list = new LinkedList<Entry>();

    NodeList childNodes = entriesNode.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {

      Node node = childNodes.item(i);
      if (node.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }
      Element entryNode = (Element) node;

      if (entryNode.getChildNodes().getLength() != 1) {
        // TODO error
      }

      String name = entryNode.getAttribute("name");
      String displayName = entryNode.getAttribute("displayName");
      String description = entryNode.getAttribute("description");
      list.add(new Entry(name, displayName, description));
    }
    return list;
  }

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

  private String changeExtension(String fileName) {
    return fileName.substring(0, fileName.length() - ".md".length()) + ".html";
  }

  private boolean ignoreFile(File file) {
    // ignore all files that do not end with .md
    return !file.isDirectory() && !file.getName().endsWith(".md")
        && !file.getName().equals(CONFIG_XML);
  }

  private void sortChildrenAlphaBetically(List<MDNode> nodes) {
    Collections.sort(nodes, new Comparator<MDNode>() {

        @Override
      public int compare(MDNode o1, MDNode o2) {
        if (o1 instanceof MDParent && !(o2 instanceof MDParent)) {
          return -1;
        }
        if (!(o1 instanceof MDParent) && o2 instanceof MDParent) {
          return 1;
        }

        return o1.getName().compareTo(o2.getName());
      }
    });

  }

  private void sortChildren(List<Entry> entries, MDParent node) {
    List<MDNode> children = node.getChildren();

    List<MDNode> sortedChildren = new LinkedList<MDNode>();
    Set<MDNode> set = new HashSet<MDNode>(children);
    for (Entry entry : entries) {
      String nodeName = entry.getName();
      boolean isFolder = nodeName.endsWith("/");
      if (isFolder) {
        nodeName = nodeName.substring(0, nodeName.length() - "/".length());
      }
      for (MDNode child : children) {
        String childName = child.getName();
        if (childName.endsWith(".md")) {
          childName = childName.substring(0, childName.length() - ".md".length());
        }

        if (nodeName.equals(childName) && isFolder == child.isFolder()) {
          sortedChildren.add(child);
          set.remove(child);
          break;
        }
      }
    }

    if (!set.isEmpty()) {
      List<MDNode> list = new ArrayList<MDNode>(set.size());
      list.addAll(set);
      sortChildrenAlphaBetically(list);

      for (MDNode mdNode : list) {
        sortedChildren.add(mdNode);
      }

    }

    node.setChildren(sortedChildren);
  }

}
