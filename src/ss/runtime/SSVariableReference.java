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

import java.util.Set;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSVariableReference implements SSObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   SSVariableReference(final String variableName) {

      this.variableName = variableName.intern();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject evaluate(final Stack stack) {
      
      return stack.getVariable(this.variableName);
   }
   /****************************************************************************
    * @return names of referenced variables
    ***************************************************************************/
   @Override
   public Set<String> referencedVariables() {

      return Set.of(this.variableName);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.variableName;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final String variableName;
}
