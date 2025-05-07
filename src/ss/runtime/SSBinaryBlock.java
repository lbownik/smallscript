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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBinaryBlock extends SSNativeObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSBinaryBlock bb(final BiFunction<Stack, SSObject[], SSObject> code) {

      return new SSBinaryBlock(code, emptyList());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSBinaryBlock bb(final BiFunction<Stack, SSObject[], SSObject> code,
         final List<String> argumentNames) {

      return new SSBinaryBlock(code, argumentNames);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock(final BiFunction<Stack, SSObject[], SSObject> code,
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
      this.methods = other.methods != null ? new MethodMap(other.methods) : null;
      this.fields = other.fields != null ? new HashMap<>(other.fields) : null;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final SSObject[] args) {

      if (method.startsWith("execute")) {
         return this.code.apply(stack, args);
      }
      final var thisArgs = prependThisTo(args);
      if (this.methods != null) {
         final var block = this.methods.get(method);
         if (block != null) {
            return block.execute(stack, thisArgs);
         }
      }
      return switch (method) {
         case "arguments" -> argumentNames();
         case "invoke::with:" -> invokeWith(stack, thisArgs);
         case "addField::withValue:" -> addFieldWithValue(stack, thisArgs);
         case "addMethod::using:" -> addMethod(stack, thisArgs);
         case "asString" -> asString(stack, thisArgs);
         case "at:" -> at(stack, thisArgs);
         case "clone" -> new SSBinaryBlock(this);
         case "close" -> returnThis(stack, thisArgs);
         case "collectTo:" -> collectTo(stack, thisArgs);
         case "fields" -> getFields(stack, thisArgs);
         case "forEach:" -> forEach(stack, thisArgs);
         case "hash" -> hashCode(stack, thisArgs);
         case "isEqualTo:" -> isEqualTo(stack, thisArgs);
         case "isNotEqualTo:" -> isNotEqualTo(stack, thisArgs);
         case "method:" -> getMethod(stack, thisArgs);
         case "methods" -> getMethods(stack, thisArgs);
         case "nature" -> new SSString("binaryBlock");
         case "orDefault:" -> returnThis(stack, thisArgs);
         case "selectIf:" -> selectIf(stack, thisArgs);
         case "size" -> new SSLong(1);
         case "throw" -> throwThis(stack, thisArgs);
         case "transformUsing:" -> transformUsing(stack, thisArgs);
         case "try:" -> try_(stack, thisArgs);
         case "try::catch:" -> tryCatch(stack, thisArgs);
         default -> doesNotUnderstand(stack, method, thisArgs);
      };
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack) {

      return this.code.apply(stack, emptyArgs);
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack, final SSObject[] args) {

      return this.code.apply(stack, args);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected void addMethod(final String name, final SSObject block) {

      if (this.methods == null) {
         this.methods = new MethodMap(this.methods, false);
      }
      this.methods.add(name, block);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected SSObject getMethod(final String name, final SSObject defaultValue) {
      return defaultValue;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected Set<SSObject> getMethods() {

      return Collections.emptySet();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject doesNotUnderstand(final Stack stack, final String method,
         final SSObject[] args) {

      final var message = new SSDynamicObject();
      message.addField(stack, "nature", new SSString("message"));
      message.addField(stack, "method", new SSString(method));
      message.addField(stack, "args", new SSList(asList(args)));

      return doesNotUnderstand(stack, new SSObject[] { this, message });
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
   private final BiFunction<Stack, SSObject[], SSObject> code;
   private final List<String> argumentNames;
}
