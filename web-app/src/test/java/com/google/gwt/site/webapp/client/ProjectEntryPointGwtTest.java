package com.google.gwt.site.webapp.client;

import com.google.gwt.regexp.shared.RegExp;

/**
 * Test class for the GWTProject entry-point for running in GWT
 */
public class ProjectEntryPointGwtTest extends ProjectEntryPointTest {

  public String getModuleName() {
    return "com.google.gwt.site.webapp.Test";
  }

  protected String getOrigin() {
    return GWTProjectEntryPoint.origin;
  }

  protected RegExp getSameOriginRegex() {
    return GWTProjectEntryPoint.isSameOriginRexp;
  }
}
