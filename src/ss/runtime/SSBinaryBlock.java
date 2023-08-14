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

import java.util.List;
import java.util.function.BiFunction;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBinaryBlock implements SSObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock(final BiFunction<Stack, List<SSObject>, SSObject> code) {

      this.code = code;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      if (method.startsWith("execute")) {
         return this.code.apply(stack, args);
      } else {
         return switch (method) {
            case "nature" -> new SSString("binaryBlock");
            case "asString" -> new SSString(toString());
            case "hash" -> new SSLong(hashCode());
            case "equals:" -> stack.get(this.equals(args.get(0).evaluate(stack)));
            case "isNotEqualTo:" ->
               stack.get(!this.equals(args.get(0).evaluate(stack)));
            default -> doesNotUnderstand(stack, method, args);
         };
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject doesNotUnderstand(final Stack stack, final String method,
         final List<SSObject> args) {

      final var message = new SSDynamicObject();
      message.addField("nature", new SSString("message"));
      message.addField("method", new SSString(method));
      message.addField("args", new SSList(args));
      
      return SSDynamicObject.doesNotUnderstand(stack, List.of(this, message));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return "binaryBlock#" + hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final BiFunction<Stack, List<SSObject>, SSObject> code;
}