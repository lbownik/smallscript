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
      assertSSTrue("Object new nature equals: \"object\";");
      assertSSTrue("Object new asString startsWith: \"object#\";");
      assertSSTrue("Object new hash isGreaterThan: 0;");
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
      assertSSTrue("Object new isNotEqualTo: 'a';");
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
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void addingFields_createsAccessorMethods() throws Exception {

      assertSSNull("""
            !object = Object new;
            object addField: "a";
            object a;
            """);
      assertSSTrue("""
            !object = Object new;
            object addField: "a";
            object a: 10;
            object a equals: 10;
            """);
      assertSSTrue("""
            !object = Object new;
            object addField: "a" :withValue: 10;
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
            old addField: "test" :withValue: "a";
            new addField: "test" :withValue: 10;
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
}
