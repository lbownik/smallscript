//------------------------------------------------------------------------------
//Copyright 2014 Lukasz Bownik
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//------------------------------------------------------------------------------
package ss.runtime;

import org.junit.Test;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class StringUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void charHasSetBasicProperties() throws Exception {

      assertSSTrue("\"abc\" size equals: 3;");
      assertSSTrue("\"abc\" nature equals: \"string\";");
      assertSSTrue("\"abc\" nature nature equals: \"string\";");
      assertSSTrue("\"abc\" asString equals: \"abc\";");
      assertResultEquals(new SSLong(96354), "\"abc\" hash;");
      assertSSTrue("""
            !o = "B";
            o orDefault: "a" equals: o;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringEualsOnlyToItself() throws Exception {

      assertSSTrue("\"abc\" equals: \"abc\";");
      assertSSFalse("\"abc\" equals: \"a\";");
      assertSSFalse("\"abc\" equals: null;");
      assertSSFalse("\"abc\" isNotEqualTo: \"abc\";");
      assertSSTrue("\"abc\" isNotEqualTo: \"a\";");
      assertSSTrue("\"abc\" isNotEqualTo: null;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void at_returnsCharacterAtPosition() throws Exception {
      
      assertResultEquals(new SSString("a"), "\"abc\" at: 0;");
      assertSSTrue("""
            true try: {
               \"abc\" at: -1;
               false;
            } :catch: {!e|
               (e nature equals: "exception") and:
               (e message equals: "Index -1 out of bounds.");
            };
            """);
      assertSSTrue("""
            true try: {
               \"abc\" at: 3;
               false;
            } :catch: {!e|
               (e nature equals: "exception") and:
               (e message equals: "Index 3 out of bounds.");
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringsCanBeConcatenated() throws Exception {
      
      assertResultEquals(new SSString("abcd"), "\"abc\" concatenate: \"d\";");
      assertResultEquals(new SSString("abcd"), "\"abc\" append: \"d\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringCanStartWithOtherString() throws Exception {
      
      assertSSTrue("\"abc\" startsWith: \"ab\";");
      assertSSTrue("\"abc\" startsWith: \"abc\";");
      assertSSFalse("\"abc\" startsWith: \"c\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringCanBeCloned() throws Exception {

      assertSSTrue("\"abc\" clone equals: \"abc\";");
      assertSSTrue("""
            !old = \"abc\";
            old addField: "test" :withValue: 1;
            !new = old clone;
            new test: 2;
            old test equals: 1;
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            "" method: "invoke::with:" arguments equals: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            "" method: "addField:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            "" method: "addField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            "" method: "addMethod::using:" arguments equals: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            "" method: "asString" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "at:" arguments equals: (List append: "index");
            """);
      assertSSTrue("""
            "" method: "clone" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "collectTo:" arguments equals: (List append: "collector");
            """);
      assertSSTrue("""
            "" method: "equals:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            "" method: "execute" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "fields" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "forEach:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            "" method: "method:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            "" method: "methods" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "nature" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "hash" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "isNotEqualTo:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            "" method: "orDefault:" arguments equals: (List append: "default");
            """);
      assertSSTrue("""
            "" method: "size" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "selectIf:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            "" method: "throw" arguments equals: (List new);
            """);
      assertSSTrue("""
            "" method: "transformUsing:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            "" method: "try::catch:" arguments equals: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            "" method: "append:" arguments equals: (List append: "text");
            """);
      assertSSTrue("""
            "" method: "concatenate:" arguments equals: (List append: "text");
            """);
      assertSSTrue("""
            "" method: "startsWith:" arguments equals: (List append: "text");
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
