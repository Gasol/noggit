/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.noggit;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.*;

/**
 * @author yonik
 * @version $Id: TestJSONWriter.java 1211150 2011-12-06 21:10:01Z yonik $
 */
public class TestJSONWriter extends TestCase {

  // note - TestObjectBuilder also exercises JSONWriter

  public void test(String expected, Object val, int indent) throws IOException {
    expected = expected.replace('\'','"');
    String s1 = JSONUtil.toJSON(val, indent);
    assertEquals(s1, expected);
  }

  public static List L(Object... lst) {
     return Arrays.asList(lst);
  }
  public static Object[] A(Object... lst) {
     return lst;
  }
  public static Map O(Object... lst) {
    LinkedHashMap map = new LinkedHashMap();
    for (int i=0; i<lst.length; i+=2) {
      map.put(lst[i].toString(), lst[i+1]);
    }
    return map;
  }

  // NOTE: the specifics of indentation may change in the future!
  public void testWriter() throws Exception {
    test("[]",L(),2);
    test("{}",O(),2);
    test("[\n  10,\n  20]", L(10,20), 2);
    test("{\n 'a':10,\n 'b':{\n  'c':20,\n  'd':30}}", O("a",10,"b",O("c",20,"d",30)), 1);

    test("['\\r\\n\\u0000\\'']", L("\r\n\u0000\""),2);

  }

  public static class Custom implements JSONWriter.Writable {
      public void write(JSONWriter writer) {
          Map val = new LinkedHashMap();
          val.put("a",1);
          val.put("b",2);
          writer.write(val);
      }
  }

  public void testWritable() throws Exception {
    test("[{'a':1,'b':2}]", L(new Custom()), -1);
    test("[10,{'a':1,'b':2},20]", L(10, new Custom(), 20), -1);
  }
}
