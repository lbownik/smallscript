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
public class FalseUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void falseHasSetBasicProperties() throws Exception {

      assertSSTrue("false size equals: 1;");
      assertSSTrue("false nature equals: \"object\";");
      assertSSTrue("tfalserue asString equals: \"false\";");
      assertSSTrue("true hash equals: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void falseEualsOnlyToItself() throws Exception {

      assertSSTrue("false equals: false;");
      assertSSTrue("false isNotEqualTo: true;");
      assertSSTrue("false isNotEqualTo: null;");

      assertSSFalse("false equals: true;");
      assertSSFalse("false equals: null;");
      assertSSFalse("false isNotEqualTo: false;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingFalseDoesNothingAndReturnNull() throws Exception {

      assertSSFalse("false execute;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void negationOfFalseIsTrue() throws Exception {

      assertSSTrue("false not;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void falseAndSomethigElse_returnsFalse() throws Exception {

      assertSSFalse("false and: false;");
      assertSSFalse("false and: true;");
      assertSSFalse("false and: null;");
      assertSSFalse("false and: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void falseOrSomethigElse_returnsSomethingElese() throws Exception {

      assertSSTrue("false or: true;");
      assertSSFalse("false or: false;");
      assertSSNull("false or: null;");
      assertResultEquals(new SSLong(1), "false or: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void test() throws Exception {
      
     
      assertSSTrue("false or: true;");
      assertSSFalse("false or: false;");
      assertSSNull("false or: null;");
      assertResultEquals(new SSLong(1), "false or: 1;");
      assertSSTrue("false xor: true;");
      assertSSFalse("false xor: false;");
      assertSSNull("false xor: null;");
      assertResultEquals(new SSLong(1), "false or: 1;");
      assertSSNull("false ifTrue: 0;");
      assertResultEquals(new SSLong(0), "false ifFalse: 0;");
      assertResultEquals(new SSLong(1), "false ifTrue: 0 :ifFalse: 1;");
      assertResultEquals(new SSLong(1), "false hash;");
      assertSSFalse("false clone;");
   }

}
