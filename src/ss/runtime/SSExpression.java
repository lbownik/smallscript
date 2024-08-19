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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSExpression implements SSObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSExpression(final SSObject object, final String method,
         final List<SSObject> args) {

      this.object = object;
      this.method = method;
      this.args = args;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   public SSExpression(final SSObject object, final String method) {

      this(object, method, emptyList());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject evaluate(final Stack stack) {

      return this.object.invoke(stack, this.method, this.args);
   }
   /****************************************************************************
    * @return names of referenced variables
    ***************************************************************************/
   @Override
   public Set<String> referencedVariables() {

      final var result = new HashSet<String>();
      result.addAll(this.object.referencedVariables());
      this.args.forEach(arg -> result.addAll(arg.referencedVariables()));
      
      return result;
   }
   /****************************************************************************
    * @return names of declared variables
    ***************************************************************************/
   @Override
   public Set<String> declaredVariables() {

      final var result = new HashSet<String>();
      result.addAll(this.object.declaredVariables());
      this.args.forEach(arg -> result.addAll(arg.declaredVariables()));
      
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.object + " " + this.method + " " + this.args + "\n";
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final SSObject object;
   private final String method;
   private final List<SSObject> args;
}
