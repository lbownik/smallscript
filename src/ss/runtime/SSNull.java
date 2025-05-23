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
   ****************************************************************************/
   public static SSNull instance() {

      return instance;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final SSObject[] args) {

      return switch (method) {
         case "asString" -> new SSString(toString());
         case "isEqualTo:" -> stack.get(this.equals(args[0].evaluate(stack)));
         case "hash" -> new SSLong(hashCode());
         case "isNotEqualTo:" ->
            stack.get(!this.equals(args[0].evaluate(stack)));
         case "orDefault:" -> args[0].execute(stack);
         case "nature" -> new SSString(name);
         case "size" -> new SSLong(0);
         case "throw" -> throw new AuxiliaryException(this);
         default -> this;
      };
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private SSNull() {

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
   final static SSNull instance = new SSNull();
   public final static String name = "null";
}
