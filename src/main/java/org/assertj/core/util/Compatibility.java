/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

/**
 * Compatibility with older Android (Java 1.6)
 *
 * @author Felix von Ferey
 */
public final class Compatibility {
  
  public final static class System {
    static String lineSeparator = java.lang.System.getProperty("line.separator");
    
    public static String lineSeparator() {
      return lineSeparator;
    }
  
    private System() {} // override public constructor
  }
  
  private Compatibility() {} // override public constructor
}
