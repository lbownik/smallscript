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

import static java.util.Collections.emptyList;

import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBlock extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBlock(final List<SSObject> statements,
         final List<String> argumentNames) {

      this.statements = statements;
      this.argumentNames = argumentNames;
      setField(null, "nature", nature);
      addBinaryMethod("clone", SSBlock::clone);
      addBinaryMethod("equals:", SSBlock::equals);
      addBinaryMethod("isNotEqualTo:", SSBlock::isNotEqualTo);
      addBinaryMethod("whileTrue:", SSBlock::whileTrue);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      if (method.startsWith("execute")) {
         return doExecute(stack, args);
      } else {
         return super.invoke(stack, method, args);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject equals(final Stack stack, final List<SSObject> args) {

      var subject = args.get(0);
      var arg = args.get(1).evaluate(stack);
      
      if (arg instanceof SSClosure c) {
         return stack.get(subject.equals(c.target));
      } else {
         return stack.get(subject.equals(arg));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isNotEqualTo(final Stack stack, final List<SSObject> args) {

      var subject = args.get(0);
      var arg = args.get(1).evaluate(stack);
      
      if (arg instanceof SSClosure c) {
         return stack.get(! subject.equals(c.target));
      } else {
         return stack.get(! subject.equals(arg));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject whileTrue(final Stack stack, final List<SSObject> args) {

      var result = stack.getNull();
      final var subject = (SSBlock) args.get(0);
      final var block = args.get(1);

      for (;;) {
         if (subject.doExecute(stack, emptyList()).equals(stack.getTrue())) {
            result = block.invoke(stack, "execute");
         } else {
            return result;
         }
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private void initiateLocalVariables(final Stack stack,
         final List<SSObject> args) {

      for (int index = 0; index < this.argumentNames.size(); ++index) {
         stack.addVariable(this.argumentNames.get(index),
               args.get(index).evaluate(stack));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject doExecute(Stack stack, final List<SSObject> args) {

      stack = stack.pushNewFrame();

      initiateLocalVariables(stack, args);

      SSObject result = null;

      for (final var statement : this.statements) {
         result = statement.evaluate(stack);
      }

      return result != null ? result : stack.getNull();
   }
   /****************************************************************************
    * Returns an object which can accept method calls performing necessary
    * computations if needed.
    * 
    * @param stack a clean stack frame
    ****************************************************************************/
   @Override
   public SSObject evaluate(final Stack stack) {

      return new SSClosure(stack, this);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      var result = new StringBuilder('\n').append(this.argumentNames).append("|\n");

      this.statements.forEach(s -> result.append(s).append("\n"));

      return result.toString();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isEmpty() {

      return this.statements.isEmpty();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public int size() {

      return this.statements.size();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final List<SSObject> statements;
   private final List<String> argumentNames;

   private final static SSString nature = new SSString("block");
}
