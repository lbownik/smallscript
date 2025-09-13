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

      assertSSTrue("\"abc\" size isEqualTo: 3;");
      assertSSTrue("\"abc\" nature isEqualTo: \"string\";");
      assertSSTrue("\"abc\" nature nature isEqualTo: \"string\";");
      assertSSTrue("\"abc\" asString isEqualTo: \"abc\";");
      assertResultEquals(createLong(96354), "\"abc\" hash;");
      assertSSTrue("""
            !o = "B";
            o orDefault: "a" isEqualTo: o;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringEualsOnlyToItself() throws Exception {

      assertSSTrue("\"abc\" isEqualTo: \"abc\";");
      assertSSFalse("\"abc\" isEqualTo: \"a\";");
      assertSSFalse("\"abc\" isEqualTo: null;");
      assertSSFalse("\"abc\" isNotEqualTo: \"abc\";");
      assertSSTrue("\"abc\" isNotEqualTo: \"a\";");
      assertSSTrue("\"abc\" isNotEqualTo: null;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void at_returnsCharacterAtPosition() throws Exception {
      
      assertResultEquals(createString("a"), "\"abc\" at: 0;");
      assertSSTrue("""
            true try: {
               \"abc\" at: -1;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Index -1 out of bounds.");
            };
            """);
      assertSSTrue("""
            true try: {
               \"abc\" at: 3;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Index 3 out of bounds.");
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringsCanBeConcatenated() throws Exception {
      
      assertResultEquals(createString("abcd"), "\"abc\" concatenate: \"d\";");
      assertResultEquals(createString("abcd"), "\"abc\" append: \"d\";");
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

      assertSSTrue("\"abc\" clone isEqualTo: \"abc\";");
      assertSSTrue("""
            !old = \"abc\";
            old addField: "test" :withValue: 1;
            !new = old clone;
            new test: 2;
            old test isEqualTo: 1;
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            "" method: "invoke::with:" arguments isEqualTo: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            "" method: "addField:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            "" method: "addField::withValue:" arguments isEqualTo: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            "" method: "addMethod::using:" arguments isEqualTo: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            "" method: "asString" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "at:" arguments isEqualTo: (List append: "index");
            """);
      assertSSTrue("""
            "" method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "collectTo:" arguments isEqualTo: (List append: "collector");
            """);
      assertSSTrue("""
            "" method: "isEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            "" method: "execute" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "fields" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "forEach:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            "" method: "method:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            "" method: "methods" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "nature" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "hash" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "isNotEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            "" method: "orDefault:" arguments isEqualTo: (List append: "default");
            """);
      assertSSTrue("""
            "" method: "size" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "selectIf:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            "" method: "throw" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            "" method: "transformUsing:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            "" method: "try::catch:" arguments isEqualTo: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            "" method: "append:" arguments isEqualTo: (List append: "text");
            """);
      assertSSTrue("""
            "" method: "concatenate:" arguments isEqualTo: (List append: "text");
            """);
      assertSSTrue("""
            "" method: "startsWith:" arguments isEqualTo: (List append: "text");
            """);
   }
}
