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

import static java.util.Arrays.stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBinaryBlock extends SSNativeObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public interface Code {
      SSObject run(Stack stack, Heap heap, SSObject[] args);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock(final Code code, final Heap heap,
         final List<String> argumentNames) {

      this.code = code;
      this.heap = heap;
      this.argumentNames = argumentNames;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock(final SSBinaryBlock other) {

      this.code = other.code;
      this.heap = other.heap;
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
         return this.code.run(stack, this.heap, args);
      }
      final var thisArgs = prependThisTo(args);
      if (this.methods != null) {
         final var block = this.methods.get(method);
         if (block != null) {
            return block.execute(stack, thisArgs);
         }
      }
      return switch (method) {
         case "arguments" -> argumentNames(stack);
         case "invoke::with:" -> invokeWith(stack, this.heap, thisArgs);
         case "addField::withValue:" ->
            addFieldWithValue(stack, this.heap, thisArgs);
         case "addMethod::using:" -> addMethod(stack, this.heap, thisArgs);
         case "asString" -> asString(stack, this.heap, thisArgs);
         case "at:" -> at(stack, this.heap, thisArgs);
         case "clone" -> new SSBinaryBlock(this);
         case "close" -> returnThis(stack, this.heap, thisArgs);
         case "collectTo:" -> collectTo(stack, this.heap, thisArgs);
         case "fields" -> getFields(stack, this.heap, thisArgs);
         case "forEach:" -> forEach(stack, this.heap, thisArgs);
         case "hash" -> hashCode(stack, this.heap, thisArgs);
         case "isEqualTo:" -> isEqualTo(stack, this.heap, thisArgs);
         case "isNotEqualTo:" -> isNotEqualTo(stack, this.heap, thisArgs);
         case "method:" -> getMethod(stack, this.heap, thisArgs);
         case "methods" -> getMethods(stack, this.heap, thisArgs);
         case "nature" -> this.heap.newString("binaryBlock");
         case "orDefault:" -> returnThis(stack, this.heap, thisArgs);
         case "selectIf:" -> selectIf(stack, this.heap, thisArgs);
         case "size" -> this.heap.newLong(1);
         case "throw" -> throwThis(stack, this.heap, thisArgs);
         case "transformUsing:" -> transformUsing(stack, this.heap, thisArgs);
         case "try:" -> try_(stack, this.heap, thisArgs);
         case "try::catch:" -> tryCatch(stack, this.heap, thisArgs);
         default -> doesNotUnderstand(stack, this.heap, method, thisArgs);
      };
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack) {

      return this.code.run(stack, this.heap, emptyArgs);
   }
   /****************************************************************************
    * Executes encompassed object performing necessary computations if needed.
    * 
    * @param stack a clean stack frame
    ***************************************************************************/
   @Override
   public SSObject execute(final Stack stack, final SSObject[] args) {

      return this.code.run(stack, this.heap, args);
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
   protected Set<SSObject> getMethods(final Stack stack) {

      final Set<SSObject> result = new HashSet<>();

      result.add(this.heap.newString("execute"));
      result.add(this.heap.newString("arguments"));
      result.add(this.heap.newString("invoke::with:"));
      result.add(this.heap.newString("addField::withValue:"));
      result.add(this.heap.newString("addMethod::using:"));
      result.add(this.heap.newString("asString"));
      result.add(this.heap.newString("at:"));
      result.add(this.heap.newString("clone"));
      result.add(this.heap.newString("close"));
      result.add(this.heap.newString("collectTo:"));
      result.add(this.heap.newString("fields"));
      result.add(this.heap.newString("forEach:"));
      result.add(this.heap.newString("hash"));
      result.add(this.heap.newString("isEqualTo:"));
      result.add(this.heap.newString("isNotEqualTo:"));
      result.add(this.heap.newString("method:"));
      result.add(this.heap.newString("methods"));
      result.add(this.heap.newString("nature"));
      result.add(this.heap.newString("orDefault:"));
      result.add(this.heap.newString("selectIf:"));
      result.add(this.heap.newString("size"));
      result.add(this.heap.newString("throw"));
      result.add(this.heap.newString("transformUsing:"));
      result.add(this.heap.newString("try:"));
      result.add(this.heap.newString("try::catch:"));
      if (this.methods != null) {
         for (final String methodName : this.methods.keySet()) {
            result.add(this.heap.newString(methodName));
         }
      }
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject doesNotUnderstand(final Stack stack, final Heap heap,
         final String method, final SSObject[] args) {

      final var message = heap.newObject();
      message.addField(stack, "nature", this.heap.newString("message"));
      message.addField(stack, "method", this.heap.newString(method));
      final var arguments = heap.newList(stream(args));
      message.addField(stack, "args", arguments);

      return doesNotUnderstand(stack, heap, new SSObject[] { this, message });
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
   private SSList argumentNames(final Stack stack) {

      return heap.newList(this.argumentNames.stream().map(this.heap::newString));
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
   private final Code code;
   private final List<String> argumentNames;
}
