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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class StackUseCases {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void variableOnStack_canBeRetrieved() throws Exception {

      var stack = Stack.create().pushNewFrame().addVariable("a", new SSString("x"));

      assertEquals(new SSString("x"), stack.getVariable("a"));

      stack = stack.pushNewFrame();

      assertEquals(new SSString("x"), stack.getVariable("a"));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void variableOnStack_canBeChanged() throws Exception {

      var stack = Stack.create().pushNewFrame().addVariable("a", new SSString("x"));
      stack.setVariable("a", new SSString("y"));

      assertEquals(new SSString("y"), stack.getVariable("a"));

      var topFrame = stack.pushNewFrame();

      stack = topFrame.setVariable("a", new SSString("z"));

      assertEquals(new SSString("z"), stack.getVariable("a"));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addVariable_throwsException_whenVariableAlreadyExistsInTheSameFrame()
         throws Exception {

      var stack = Stack.create().pushNewFrame().addVariable("a", new SSString("x"));

      try {
         stack.addVariable("a", new SSString("y"));
         fail("Expected exception");
      } catch (RuntimeException e) {
         assertEquals("Variable 'a' already exists in this scope.", e.getMessage());
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void variableAddedToLowerFrame_isNotVisibleInHigherFrames() throws Exception {

      var stack = Stack.create();
      var lowerFrame = stack.pushNewFrame().addVariable("a", new SSString("x"));

      assertEquals(new SSString("x"), lowerFrame.getVariable("a"));

      try {
         stack.getVariable("a");
         fail("Expected exception");
      } catch (RuntimeException e) {
         assertEquals("Variable 'a' does not exist.", e.getMessage());
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addVariable_shadowsVariablesAlreadyExistingOnStack()
         throws Exception {

      var stack = Stack.create().pushNewFrame().addVariable("a", new SSString("x"));
      var topLevelFrame = stack.pushNewFrame().addVariable("a", new SSString("y"));

      assertEquals(new SSString("x"), stack.getVariable("a"));
      assertEquals(new SSString("y"), topLevelFrame.getVariable("a"));
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void setVariable_throwsException_forEmptyStack() throws Exception {

      try {
         Stack.create().setVariable("a", SSNull.instance());
         fail("Expected exception");
      } catch (RuntimeException e) {
         assertEquals("Variable 'a' does not exist.", e.getMessage());
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void getVariable_throwsException_forEmptyStack() throws Exception {

      try {
         Stack.create().getVariable("a");
         fail("Expected exception");
      } catch (RuntimeException e) {
         assertEquals("Variable 'a' does not exist.", e.getMessage());
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void topLevelVariables_canBeAddedToTopLevelFrame() throws Exception {

      var stack = Stack.create().addVariable("true", new SSString("true"));

      assertEquals(new SSString("true"), stack.getVariable("true"));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nonTopLevelVariables_canBeAddedToTopLevelFrame() throws Exception {

      try {
      Stack.create().addVariable("a", new SSString("true"));
      fail("Expected exception");
      } catch (RuntimeException e) {
         assertEquals("Variable 'a'is not allowed as top-level variable.", e.getMessage());
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}