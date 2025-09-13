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

      assertSSTrue("Object new size isEqualTo: 1;");
      assertSSTrue("Object new arguments isEqualTo: (List new);");
      assertSSTrue("Object new nature isEqualTo: \"object\";");
      assertSSTrue("Object newOfNature: \"xyz\" nature isEqualTo: \"xyz\";");
      assertSSTrue("Object new asString startsWith: \"object#\";");
      assertSSTrue("Object new hash isGreaterThan: 0;");
      assertSSTrue("""
            !o = Object new;
            o orDefault: "a" isEqualTo: o;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void objectEqualsToItself() throws Exception {

      assertSSTrue("!o = Object new; o isEqualTo: o;");
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

      assertSSTrue("!o = Object new; o execute isEqualTo: o;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void objectIsEquivalentTo_aListWithASingleElement_ofThis()
         throws Exception {

      assertSSTrue("Object new size isEqualTo: 1;");
      assertSSTrue("!o = Object new; o at: 0 isEqualTo: o;");
      assertSSTrue("""
            !o = Object new;
            o try: {
              o at: -1;
            } :catch: {!e |
              (e nature isEqualTo: "exception") and:
              (e message isEqualTo: "Index -1 out of bounds.");
            };
            """);
      assertSSTrue("""
            !o = Object new;
            o try: {
              o at: 1;
            } :catch: {!e |
              (e nature isEqualTo: "exception") and:
              (e message isEqualTo: "Index 1 out of bounds.");
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
            (counter isEqualTo: 1) and: (result isEqualTo: o) and: (innerItem isEqualTo: o);
            """);
      assertSSTrue("""
            !o = Object new;
            !list = o collectTo: List;
            (list size isEqualTo: 1) and: (list at: 0 isEqualTo: o);
            """);
      assertSSTrue("""
            !o = Object new;
            !list = o selectIf: {!item | item isEqualTo: o} collectTo: List;
            (list size isEqualTo: 1) and: (list at: 0 isEqualTo: o);
            """);
      assertSSTrue("""
            !o = Object new;
            !list = o transformUsing: {!item | item } collectTo: List;
            (list size isEqualTo: 1) and: (list at: 0 isEqualTo: o);
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
              e isEqualTo: o;
            };
            """);
      
      assertSSTrue("""
            !o = Object new;
            o try: { !this |
              this throw;
            } :catch: {!e |
              e isEqualTo: o;
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
            
            o closed isEqualTo: true;
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
            
            (outerThis isEqualTo: o) and: (o closed isEqualTo: true);
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
            object a isEqualTo: 1;
            """);
      assertSSTrue("""
            !object = Object new;
            !methodName = "a";
            !block = { 1;};
            object addMethod: methodName :using: block;
            object a isEqualTo: 1;
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
            object a isEqualTo: 10;
            """);
      assertSSTrue("""
            !object = Object new;
            !fieldName = "a";
            !value = 10;
            object addField: fieldName :withValue: value;
            object a isEqualTo: 10;
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
            object test isEqualTo: true;
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
   public void method_Object_new_cannotBeOverriden() throws Exception {

      assertSSTrue("""
            Object try: {
               Object addMethod: "new" :using: {};
               false;
            } :catch: {!e | 
               e message isEqualTo: "Method 'Object new' cannot be overriden.";
            };
            """);
   }
   
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void method_Object_newOfNature_cannotBeOverriden() throws Exception {

      assertSSTrue("""
            Object try: {
               Object addMethod: "newOfNature:" :using: {};
               false;
            } :catch: {!e | 
               e message isEqualTo: "Method 'Object newOfNature:' cannot be overriden.";
            };
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
            (old test isEqualTo: "a") and: (new test isEqualTo: "b");
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
                     (e nature isEqualTo: "exception") and: (e message isEqualTo: "Method 'test' is not defined.");
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
               (methods size isGreaterThan: 0) and: (methods nature isEqualTo: "set");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void method_resturnsBlock() throws Exception {

      assertSSTrue("""
               true method: "ifTrue:" nature isEqualTo: "block";
            """);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void fields_resturnsSetOfFields() throws Exception {

      assertSSTrue("""
               !fields = Object new fields;
               (fields size isEqualTo: 0) and: (fields nature isEqualTo: "set");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void invoke_invekesMethodByName() throws Exception {

      assertSSTrue("""
               "abc" invoke: "size" :with: (List new) isEqualTo: 3;
            """);
   }
   
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void close_doesNothing_andReturnsThis() throws Exception {

      assertSSTrue("""
               Object close isEqualTo: Object;
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            Object method: "invoke::with:" arguments isEqualTo: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            Object method: "addField:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            Object method: "addField::withValue:" arguments isEqualTo: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Object method: "addMethod::using:" arguments isEqualTo: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            Object method: "asString" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "at:" arguments isEqualTo: (List append: "index");
            """);
      assertSSTrue("""
            Object method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "close" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "collectTo:" arguments isEqualTo: (List append: "collector");
            """);
      assertSSTrue("""
            Object method: "doesNotUnderstand:" arguments isEqualTo: (List append: "message");
            """);
      assertSSTrue("""
            Object method: "isEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            Object method: "execute" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "fields" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "forEach:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Object method: "method:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            Object method: "methods" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "nature" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "hash" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "isNotEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            Object method: "orDefault:" arguments isEqualTo: (List append: "default");
            """);
      assertSSTrue("""
            Object method: "size" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "selectIf:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Object method: "throw" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Object method: "transformUsing:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Object method: "try::catch:" arguments isEqualTo: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            Object addField: "xyz";
            Object method: "xyz:" arguments isEqualTo: (List append: "value");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
}
