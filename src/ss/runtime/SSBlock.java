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
import static java.util.Collections.emptySet;
import static ss.runtime.Stack.isTopLevelVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
      this.enclosedVariables = referencedVariables();
      final var declaredVariables = declaredVariables();
      this.enclosedVariables.removeAll(declaredVariables);
      this.declaresVariables = declaredVariables.size() > 0;

      setField(null, "nature", nature);
      addBinaryMethod("arguments", SSBlock::getArguments);
      addBinaryMethod("clone", SSBlock::clone);
      addBinaryMethod("equals:", SSBlock::equals);
      addBinaryMethod("isNotEqualTo:", SSBlock::isNotEqualTo);
      addBinaryMethod("whileTrue:", SSBlock::whileTrue);
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack) {

      return execute(stack, emptyList());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      if (method.startsWith("execute")) {
         return execute(stack, args);
      } else {
         return super.invoke(stack, method, args);
      }
   }
   /****************************************************************************
    * @return names of referenced variables
    ***************************************************************************/
   @Override
   public Set<String> referencedVariables() {

      final var result = new HashSet<String>();
      for (final var statement : this.statements) {
         for (final var variable : statement.referencedVariables()) {
            if (!isTopLevelVariable(variable)) {
               result.add(variable);
            }
         }
      }

      return result.isEmpty() ? emptySet() : result; //optimization
   }
   /****************************************************************************
    * @return names of referenced variables
    ***************************************************************************/
   @Override
   public Set<String> declaredVariables() {

      final var result = new HashSet<String>(this.argumentNames);
      this.statements.forEach(s -> result.addAll(s.declaredVariables()));

      return result.isEmpty() ? emptySet() : result; //optimization
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
   private static SSObject isNotEqualTo(final Stack stack,
         final List<SSObject> args) {

      var subject = args.get(0);
      var arg = args.get(1).evaluate(stack);

      if (arg instanceof SSClosure c) {
         return stack.get(!subject.equals(c.target));
      } else {
         return stack.get(!subject.equals(arg));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject getArguments(final Stack stack,
         final List<SSObject> args) {
      
      final var subject = (SSBlock) args.get(0);
      return new SSList(subject.argumentNames.stream().map(SSString::new).toList());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject whileTrue(final Stack stack, final List<SSObject> args) {

      var result = stack.getNull();
      final var subject = (SSBlock) args.get(0);
      final var block = args.get(1);

      for (;;) {
         if (subject.execute(stack, emptyList()).equals(stack.getTrue())) {
            result = block.execute(stack);
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
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(Stack stack, final List<SSObject> args) {

      if(this.declaresVariables) {
         stack = stack.pushNewFrame();
         initiateLocalVariables(stack, args);
      }

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

      return this.enclosedVariables.isEmpty() ? this : new SSClosure(stack, this);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      final var result = new StringBuilder("{");
      
      if(this.argumentNames.size() > 0) {
         this.argumentNames.forEach(arg -> result.append("!").append(arg));
         result.append("|\n");
      } else {
         result.append("\n");
      }

      this.statements.forEach(s -> result.append(s).append(";\n"));
      result.append("};");

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
   private final Set<String> enclosedVariables;
   private final boolean declaresVariables;

   private final static SSString nature = new SSString("block");
}
