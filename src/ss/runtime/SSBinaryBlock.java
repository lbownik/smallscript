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
import java.util.function.BiFunction;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBinaryBlock implements SSObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSBinaryBlock bb(final BiFunction<Stack, List<SSObject>, SSObject> code) {

      return new SSBinaryBlock(code, emptyList());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSBinaryBlock bb(final BiFunction<Stack, List<SSObject>, SSObject> code,
         final List<String> argumentNames) {

      return new SSBinaryBlock(code, argumentNames);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock(final BiFunction<Stack, List<SSObject>, SSObject> code, 
         final List<String> argumentNames) {

      this.code = code;
      this.argumentNames = argumentNames;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock(final SSBinaryBlock other) {

      this.code = other.code;
      this.argumentNames = other.argumentNames;
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
            case "arguments" -> argumentNames();
            case "asString" -> new SSString(toString());
            case "clone" -> new SSBinaryBlock(this);
            case "hash" -> new SSLong(hashCode());
            case "equals:" -> stack.get(this.equals(args.get(0).evaluate(stack)));
            case "isNotEqualTo:" ->
               stack.get(!this.equals(args.get(0).evaluate(stack)));
            case "nature" -> new SSString("binaryBlock");
            case "size" -> new SSLong(1);
            case "throw" -> throw new AuxiliaryException(this);
            case "try::catch:" -> tryCatch(stack, args);
            default -> doesNotUnderstand(stack, method, args);
         };
      }
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack) {

      return this.code.apply(stack, emptyList());
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack, final List<SSObject> args) {

      return this.code.apply(stack, args);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject doesNotUnderstand(final Stack stack, final String method,
         final List<SSObject> args) {

      final var message = new SSDynamicObject();
      message.addField(stack, "nature", new SSString("message"));
      message.addField(stack, "method", new SSString(method));
      message.addField(stack, "args", new SSList(args));

      return SSDynamicObject.doesNotUnderstand(stack, List.of(this, message));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject tryCatch(final Stack stack, final List<SSObject> args) {

      try {
         return args.get(0).invoke(stack, "execute");
      } catch (final AuxiliaryException e) {
         return args.get(1).invoke(stack, "execute", List.of(e.object));
      }
   }
   /****************************************************************************
    * Returns an object which can accept method calls performing necessary
    * computations if needed.
    * 
    * @param stack a clean stack frame
    ****************************************************************************/
   @Override
   public SSObject evaluate(final Stack stack) {

      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSList argumentNames() {
      
      return new SSList(this.argumentNames.stream().map(SSString::new).toList());
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
   private final List<String> argumentNames;
}
