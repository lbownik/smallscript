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
//-----------------------------------------------------------------------------
package ss.runtime;

import java.util.HashMap;
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public interface Stack {
   /****************************************************************************
    * 
   ****************************************************************************/
   public class VariableNotFoud extends RuntimeException {
      /*************************************************************************
       * 
      *************************************************************************/
      public VariableNotFoud(final String message) {

         super(message);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public Stack addVariable(final String name, final SSObject value) {

      throw new RuntimeException("Stack not initialized.");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public Stack setVariable(final String name, final SSObject value) {

      throw new VariableNotFoud("Variable '" + name + "' does not exist.");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public SSObject getVariable(final String name) {

      throw new VariableNotFoud("Variable '" + name + "' does not exist.");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public SSObject getTrue() {

      return getVariable(TRUE);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public SSObject getFalse() {

      return getVariable(FALSE);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public SSObject get(final boolean bool) {

      return getVariable(bool ? TRUE : FALSE);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public SSObject getNull() {

      return getVariable(NULL);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default public Stack pushNewFrame() {

      return new Frame(this);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public static Stack create() {

      return new TopFrame();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public default Stack createClosure(final Set<String> enclosedVariables) {

      final Stack closure = terminator.pushNewFrame();

      for (final String variable : enclosedVariables) {
         try {
            closure.addVariable(variable, this.getVariable(variable));
         } catch (final VariableNotFoud e) {
            // ignore
         }
      }

      return closure;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public default Stack combine(final Stack other) {

      return new DualStack(this, other);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public static boolean isTopLevelVariable(final String name) {

      return name.equals(OBJECT) || name.equals(NULL) || name.equals(TRUE)
            || name.equals(FALSE) || name.equals(LIST) || name.equals(MAP)
            || name.equals(SET) || name.equals(EXCEPTION)
            || name.equals(APPLICATION);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public final static String OBJECT = "Object";
   public final static String NULL = "null";
   public final static String TRUE = "true";
   public final static String FALSE = "false";
   public final static String LIST = "List";
   public final static String MAP = "Map";
   public final static String SET = "Set";
   public final static String EXCEPTION = "Exception";
   public final static String APPLICATION = "application";

   final static Stack terminator = new Stack() {
   };

   /****************************************************************************
    * Inheriting from HasMap gives 10X performance boost of "pushNewFrame" and 20%
    * performance boost for "add/get/set/Variable".
    ****************************************************************************/
   public static class Frame extends HashMap<String, SSObject> implements Stack {
      /*************************************************************************
       * 
      *************************************************************************/
      private Frame(final Stack previousFrame) {

         this.previousFrame = previousFrame;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public Stack addVariable(final String name, final SSObject value) {

         if (put(name, value) == null) {
            return this;
         } else {
            throw new RuntimeException(
                  "Variable '" + name + "' already exists in this scope.");
         }
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public Stack setVariable(final String name, final SSObject value) {

         final var previousValue = replace(name, value);
         return previousValue != null ? this
               : this.previousFrame.setVariable(name, value);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public SSObject getVariable(final String name) {

         final var value = get(name);
         return value != null ? value : this.previousFrame.getVariable(name);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      protected final Stack previousFrame;
   }
   /****************************************************************************
    * Inheriting from HasMap gives 10X performance boost of "pushNewFrame" and 20%
    * performance boost for "add/get/set/Variable".
    ****************************************************************************/
   static class TopFrame extends Frame {
      /*************************************************************************
       * 
      *************************************************************************/
      private TopFrame() {

         super(Stack.terminator);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public Stack addVariable(final String name, final SSObject value) {

         if (isTopLevelVariable(name)) {
            if (put(name, value) == null) {
               return this;
            } else {
               throw new RuntimeException(
                     "Variable '" + name + "' already exists in this scope.");
            }
         } else {
            throw new RuntimeException(
                  "Variable '" + name + "'is not allowed as top-level variable.");
         }
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public Stack setVariable(final String name, final SSObject value) {

         if (isTopLevelVariable(name)) {
            throw new RuntimeException("Variable '" + name + "'is read-only.");
         } else {
            return this.previousFrame.setVariable(name, value);
         }
      }
      /*************************************************************************
       * 
      *************************************************************************/
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   final class DualStack implements Stack {
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
