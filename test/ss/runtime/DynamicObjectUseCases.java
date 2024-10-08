//------------------------------------------------------------------------------
//Copyright 2023 Lukasz Bownik
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
public class DynamicObjectUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void newlyCreatedObject_hasSetBasicProperties() throws Exception {

      assertSSTrue("Object new size equals: 1;");
      assertSSTrue("Object new arguments equals: (List new);");
      assertSSTrue("Object new nature equals: \"object\";");
      assertSSTrue("Object newOfNature: \"xyz\" nature equals: \"xyz\";");
      assertSSTrue("Object new asString startsWith: \"object#\";");
      assertSSTrue("Object new hash isGreaterThan: 0;");
      assertSSTrue("""
            !o = Object new;
            o orDefault: "a" equals: o;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void objectEqualsToItself() throws Exception {

      assertSSTrue("!o = Object new; o equals: o;");
      assertSSFalse("!o = Object new; o isNotEqualTo: o;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void newlyCreatedObject_isNotEqualToAnyOtherObject() throws Exception {

      assertSSTrue("Object new isNotEqualTo: null;");
      assertSSTrue("Object new isNotEqualTo: true;");
      assertSSTrue("Object new isNotEqualTo: false;");
      assertSSTrue("Object new isNotEqualTo: 1;");
      assertSSTrue("Object new isNotEqualTo: 1.0;");
      assertSSTrue("Object new isNotEqualTo: \"a\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingObject_doesNothingAndReturnsThisObject() throws Exception {

      assertSSTrue("!o = Object new; o execute equals: o;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void objectIsEquivalentTo_aListWithASingleElement_ofThis()
         throws Exception {

      assertSSTrue("Object new size equals: 1;");
      assertSSTrue("!o = Object new; o at: 0 equals: o;");
      assertSSTrue("""
            !o = Object new;
            o try: {
              o at: -1;
            } :catch: {!e |
              (e nature equals: "exception") and:
              (e message equals: "Index -1 out of bounds.");
            };
            """);
      assertSSTrue("""
            !o = Object new;
            o try: {
              o at: 1;
            } :catch: {!e |
              (e nature equals: "exception") and:
              (e message equals: "Index 1 out of bounds.");
            };
            """);
      assertSSTrue("""
            !o = Object new;
            !counter = 0;
            !innerItem = null;
            !result = o forEach: {!item |
               counter = counter plus: 1;
               innerItem = item;
            };
            (counter equals: 1) and: (result equals: o) and: (innerItem equals: o);
            """);
      assertSSTrue("""
            !o = Object new;
            !list = o collectTo: List;
            (list size equals: 1) and: (list at: 0 equals: o);
            """);
      assertSSTrue("""
            !o = Object new;
            !list = o selectIf: {!item | item equals: o} collectTo: List;
            (list size equals: 1) and: (list at: 0 equals: o);
            """);
      assertSSTrue("""
            !o = Object new;
            !list = o transformUsing: {!item | item } collectTo: List;
            (list size equals: 1) and: (list at: 0 equals: o);
            """);

   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void objectCanThrowItself_andExceuteTryCach() throws Exception {

      assertSSTrue("""
            !o = Object new;
            o try: {
              o throw;
            } :catch: {!e |
              e equals: o;
            };
            """);
      
      assertSSTrue("""
            !o = Object new;
            o try: { !this |
              this throw;
            } :catch: {!e |
              e equals: o;
            };
            """);
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void tryChatch_invokesClose() throws Exception {

      assertSSTrue("""
            !o = Object new;
            o addField: "closed" :withValue: false;
            o addMethod: "close" :using: { !this |
                  this closed: true;
            };
            
            o try: {
              o throw;
            } :catch: {};
            
            o closed equals: true;
            """);
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void try_invokesClose() throws Exception {

      assertSSTrue("""
            !o = Object new;
            o addField: "closed" :withValue: false;
            o addMethod: "close" :using: { !this |
                  this closed: true;
            };
            !outerThis = null;
            
            
            o try: { !this |
              outerThis = this;
            } ;
            
            (outerThis equals: o) and: (o closed equals: true);
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void addMethod_createsNewMethod() throws Exception {

      assertSSTrue("""
            !object = Object new;
            object addMethod: "a" :using: { 1;};
            object a equals: 1;
            """);
      assertSSTrue("""
            !object = Object new;
            !methodName = "a";
            !block = { 1;};
            object addMethod: methodName :using: block;
            object a equals: 1;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void addingFields_createsAccessorMethods() throws Exception {

      assertSSNull("""
            !object = Object new;
            !fieldName = "a";
            object addField: fieldName;
            object a;
            """);
      assertSSTrue("""
            !object = Object new;
            object addField: "a";
            !ten = 10;
            object a: ten;
            object a equals: 10;
            """);
      assertSSTrue("""
            !object = Object new;
            !fieldName = "a";
            !value = 10;
            object addField: fieldName :withValue: value;
            object a equals: 10;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void overwritingDoesNotUnderstand_works() throws Exception {

      assertSSTrue("""
            !object = Object new;
            object addMethod: "doesNotUnderstand:" :using: { !this |
               this addField: "test" :withValue: true;
            };
            object a; #calling inexisting method
            object test equals: true;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void askingForInexistentMethod_returnsNull() throws Exception {

      assertSSNull("Object new method: \"abc\";");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void overridingMethodsWoks() throws Exception {

      assertSSTrue("""
            !object = Object new;
            !oldMethod = object method: "asString";
            object addMethod: "$super_asString" :using: oldMethod;
            object addMethod: "asString" :using: {!this |
               "@" concatenate: (this $super_asString);
            };
            object asString startsWith: "@object#";
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void cloneingCreatesNewObject() throws Exception {

      assertSSTrue("""
            !o = Object new;
            !new = (o clone);
            new isNotEqualTo: o;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void clonedObjectsHaveIndependentFields() throws Exception {

      assertSSTrue("""
            !old = Object new;
            !new = (old clone);
            !ten = 10;
            old addField: "test" :withValue: "a";
            new addField: "test" :withValue: ten;
            (old test) isNotEqualTo: (new test);
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void cloningObjectWithFieldsCreatesNewObjectWithIndependentFields()
         throws Exception {

      assertSSTrue("""
            !old = Object new;
            old addField: "test" :withValue: "a";
            !new = old clone;
            new test: "b";
            (old test equals: "a") and: (new test equals: "b");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void callingInexistentMethodThrowsException() throws Exception {

      assertSSTrue(
            """
                  !old = Object new;
                  old try: {
                     old test;
                  } :catch: {!e |
                     (e nature equals: "exception") and: (e message equals: "Method 'test' is not defined.");
                  };
                  """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void methods_resturnsSetOfMethods() throws Exception {

      assertSSTrue("""
               !methods = Object new methods;
               (methods size isGreaterThan: 0) and: (methods nature equals: "set");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void method_resturnsBlock() throws Exception {

      assertSSTrue("""
               true method: "ifTrue:" nature equals: "block";
            """);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void fields_resturnsSetOfFields() throws Exception {

      assertSSTrue("""
               !fields = Object new fields;
               (fields size equals: 0) and: (fields nature equals: "set");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void invoke_invekesMethodByName() throws Exception {

      assertSSTrue("""
               "abc" invoke: "size" :with: (List new) equals: 3;
            """);
   }
   
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void close_doesNothing_andReturnsThis() throws Exception {

      assertSSTrue("""
               Object close equals: Object;
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            Object method: "invoke::with:" arguments equals: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            Object method: "addField:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            Object method: "addField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Object method: "addMethod::using:" arguments equals: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            Object method: "asString" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "at:" arguments equals: (List append: "index");
            """);
      assertSSTrue("""
            Object method: "clone" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "close" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "collectTo:" arguments equals: (List append: "collector");
            """);
      assertSSTrue("""
            Object method: "doesNotUnderstand:" arguments equals: (List append: "message");
            """);
      assertSSTrue("""
            Object method: "equals:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            Object method: "execute" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "fields" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "forEach:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Object method: "method:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            Object method: "methods" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "nature" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "hash" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "isNotEqualTo:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            Object method: "orDefault:" arguments equals: (List append: "default");
            """);
      assertSSTrue("""
            Object method: "size" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "selectIf:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Object method: "throw" arguments equals: (List new);
            """);
      assertSSTrue("""
            Object method: "transformUsing:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Object method: "try::catch:" arguments equals: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            Object addField: "xyz";
            Object method: "xyz:" arguments equals: (List append: "value");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
}
