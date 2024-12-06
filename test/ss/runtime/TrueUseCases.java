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
public class TrueUseCases  extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void trueHasSetBasicProperties() throws Exception {

      assertSSTrue("true size isEqualTo: 1;");
      assertSSTrue("true nature isEqualTo: \"object\";");
      assertSSTrue("true asString isEqualTo: \"true\";");
      assertSSTrue("true hash isEqualTo: 2;");
      assertSSTrue("""
            !o = true;
            o orDefault: "a" isEqualTo: o;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void trueEualsOnlyToItself() throws Exception {

      assertSSTrue("true isEqualTo: true;");
      assertSSTrue("true isNotEqualTo: false;");
      assertSSTrue("true isNotEqualTo: null;");

      assertSSFalse("true isEqualTo: false;");
      assertSSFalse("true isEqualTo: null;");
      assertSSFalse("true isNotEqualTo: true;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingTrueDoesNothingAndReturnTrue() throws Exception {

      assertSSTrue("true execute;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void negationOfTrueIsFalse() throws Exception {

      assertSSFalse("true not;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void invokingUndefinedMethodsOnTrue_throwsException()
         throws Exception {

      assertSSTrue("""
            !o = Object new;
            o try: {
              true test; 
              false;
            } :catch: {!e |
              (e nature isEqualTo: "exception") and:
              (e message isEqualTo: "Method 'test' is not defined.");
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void trueOrSomethigElse_returnsTrue() throws Exception {

      assertSSTrue("true or: true;");
      assertSSTrue("true or: false;");
      assertSSTrue("true or: null;");
      assertSSTrue("true or: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void trueAndSomethigElse_returnsSomethingElese() throws Exception {

      assertSSTrue("true and: true;");
      assertSSFalse("true and: false;");
      assertSSNull("true and: null;");
      assertResultEquals(new SSLong(1), "true and: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void trueOrElseSomethigElse_returnsSomethingElese_Not() throws Exception {

      assertSSFalse("true orElse: true;");
      assertSSTrue("true orElse: false;");
      assertSSNull("true orElse: null;");
      assertSSTrue("""
           !o = Object new;
           o try: {
             true orElse: 1;
             false;
           } :catch: {!e |
             (e nature isEqualTo: "exception") and:
             (e message isEqualTo: "Method 'not' is not defined.");
           };
           """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void ifTrue_executesGivenBlock() throws Exception {

      assertResultEquals(new SSLong(0), "true ifTrue: {0;};");
      assertResultEquals(new SSLong(0), "true ifTrue: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void ifFalse_doesNothing() throws Exception {

      assertSSNull("true ifFalse: {0;};");
      assertSSTrue("""
            !x = null;
            true ifFalse: { x = 0;};
            x isEqualTo: null;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void ifTrueIfFalse_executesGivenTrueBlock() throws Exception {

      assertSSTrue("""
            !x = null;
            !y = true ifTrue: {0} :ifFalse: { x = 0;};
            (x isEqualTo: null) and: (y isEqualTo: 0);
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void true_cannotBeCloned() throws Exception {

      assertSSTrue("true clone;");
      assertSSTrue("true clone isEqualTo: true;");
      assertSSTrue("""
            true addField: "test" :withValue: 1;
            !new = true clone;
            true test: 2;
            new test isEqualTo: 2;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
