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

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSNull implements SSObject {
   /****************************************************************************
    * 
    ***************************************************************************/
   public SSNull(final Heap heap) {
      
      this.heap = heap; 
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final SSObject[] args) {

      return switch (method) {
         case "asString" -> this.heap.newString(toString());
         case "isEqualTo:" -> stack.get(this.equals(args[0].evaluate(stack)));
         case "hash" -> this.heap.newLong(hashCode());
         case "isNotEqualTo:" ->
            stack.get(!this.equals(args[0].evaluate(stack)));
         case "orDefault:" -> args[0].execute(stack);
         case "nature" -> this.heap.newString(name);
         case "size" -> this.heap.newLong(0);
         case "throw" -> throw new AuxiliaryException(this);
         default -> this;
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return name;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return 0;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Heap heap;
   public final static String name = "null";
}
