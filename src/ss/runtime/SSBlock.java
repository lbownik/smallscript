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

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;
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
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSBlock(final Map<String, SSBlock> methods,
         final Map<String, SSObject> fields, final List<SSObject> statements,
         final List<String> argumentNames) {

      super(methods, fields);
      this.statements = statements;
      this.argumentNames = argumentNames;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject doClone() {

      return new SSBlock(this.methods, this.fields, this.statements,
            this.argumentNames);
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
         return switch (method) {
            case "whileTrue:" -> whileTrue(stack, args);
            default -> super.invoke(stack, method, args);
         };
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject whileTrue(final Stack stack, final List<SSObject> args) {

      var result = stack.getNull();
      final var block = args.get(0);

      for (;;) {
         if (execute(stack.pushNewFrame(), emptyList()).equals(stack.getTrue())) {
            result = block.invoke(stack.pushNewFrame(), "execute");
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
   private SSObject execute(Stack stack, final List<SSObject> args) {

      initiateLocalVariables(stack, args);

      return this.statements.stream().map(s -> s.evaluate(stack))
            .reduce(stack.getNull(), (r, o) -> o);
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
   private final List<SSObject> statements;
   private final List<String> argumentNames;
}
