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

      this.enclosedStack = stack.createClosure(target.referencedVariables());
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
         final SSObject[] args) {

      return this.target.invoke(stack.combine(this.enclosedStack), method, args);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Stack enclosedStack;
   final SSObject target;

}
