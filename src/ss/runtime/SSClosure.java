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

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class SSClosure implements SSObject {

   /****************************************************************************
    * 
    ****************************************************************************/
   public SSClosure(final Stack stack, final SSObject target) {

      this.enclosedStack = Stack.createClosure();
      target.referencedVariables().forEach(
            variable -> this.enclosedStack.copyVariableFrom(stack, variable));
      this.target = target;
   }
   /****************************************************************************
    * Calls method of the encompassed object performing necessary computations if
    * needed.
    * 
    * @param stack a clean stack frame
    ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return this.target.invoke(new DualStack(stack, this.enclosedStack), method,
            args);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Stack enclosedStack;
   final SSObject target;
   /****************************************************************************
    * 
   ****************************************************************************/
   private final class DualStack implements Stack {
      /*************************************************************************
       * 
      *************************************************************************/
      public DualStack(final Stack current, final Stack enclosed) {

         this.current = current;
         this.enclosed = enclosed;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public Stack addVariable(final String name, final SSObject value) {

         return this.current.addVariable(name, value);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public Stack setVariable(final String name, final SSObject value) {

         try {
            return this.current.setVariable(name, value);
         } catch (Stack.VariableNotFoud e) {
            return this.enclosed.setVariable(name, value);
         }
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public SSObject getVariable(final String name) {

         try {
            return this.current.getVariable(name);
         } catch (Stack.VariableNotFoud e) {
            return this.enclosed.getVariable(name);
         }
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private final Stack current;
      private final Stack enclosed;
   }
}
