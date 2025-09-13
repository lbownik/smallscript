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

import static java.util.Arrays.stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject extends SSNativeObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSDynamicObject(final Heap heap, final MethodMap methods) {

      this.heap = heap;
      this.methods = methods;
      this.fields = null;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSDynamicObject(final SSDynamicObject other) {

      this.heap = other.heap;
      this.methods = other.methods.isShared() ? other.methods
            : new MethodMap(other.methods);
      this.fields = other.fields != null ? new HashMap<>(other.fields) : null;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final Heap heap, final MethodMap methods) {

      final var listOfBlock = List.of("block");

      methods.add("arguments",
            heap.newBinaryBlock((s, h, a) -> heap.newList()));
      methods.add("invoke::with:", heap.newBinaryBlock(SSDynamicObject::invokeWith,
            List.of("method", "argList")));
      methods.add("addField:",
            heap.newBinaryBlock(SSDynamicObject::addField, List.of("name")));
      methods.add("addField::withValue:", heap.newBinaryBlock(
            SSDynamicObject::addFieldWithValue, List.of("name", "value")));
      methods.add("addMethod::using:", heap
            .newBinaryBlock(SSDynamicObject::addMethod, List.of("name", "block")));
      methods.add("asString", heap.newBinaryBlock(SSDynamicObject::asString));
      methods.add("at:", heap.newBinaryBlock(SSDynamicObject::at, List.of("index")));
      methods.add("clone", heap.newBinaryBlock(SSDynamicObject::clone));
      methods.add("close", heap.newBinaryBlock(SSDynamicObject::returnThis));
      methods.add("collectTo:",
            heap.newBinaryBlock(SSDynamicObject::collectTo, List.of("collector")));
      methods.add("doesNotUnderstand:", heap
            .newBinaryBlock(SSDynamicObject::doesNotUnderstand, List.of("message")));
      methods.add("isEqualTo:",
            heap.newBinaryBlock(SSDynamicObject::isEqualTo, List.of("other")));
      methods.add("execute", heap.newBinaryBlock(SSDynamicObject::evaluate));
      methods.add("fields", heap.newBinaryBlock(SSDynamicObject::getFields));
      methods.add("forEach:",
            heap.newBinaryBlock(SSDynamicObject::forEach, listOfBlock));
      methods.add("method:",
            heap.newBinaryBlock(SSDynamicObject::getMethod, List.of("name")));
      methods.add("methods", heap.newBinaryBlock(SSDynamicObject::getMethods));
      methods.add("nature", heap.newBinaryBlock((s, h, a) -> h.newString("object")));
      methods.add("hash", heap.newBinaryBlock(SSDynamicObject::hashCode));
      methods.add("isNotEqualTo:",
            heap.newBinaryBlock(SSDynamicObject::isNotEqualTo, List.of("other")));
      methods.add("orDefault:",
            heap.newBinaryBlock(SSDynamicObject::returnThis, List.of("default")));
      methods.add("size", heap.newBinaryBlock((s, h, a) -> h.newLong(1)));
      methods.add("selectIf:",
            heap.newBinaryBlock(SSDynamicObject::selectIf, listOfBlock));
      methods.add("throw", heap.newBinaryBlock(SSDynamicObject::throwThis));
      methods.add("transformUsing:",
            heap.newBinaryBlock(SSDynamicObject::transformUsing, listOfBlock));
      methods.add("try:",
            heap.newBinaryBlock(SSDynamicObject::try_, List.of("tryBlock")));
      methods.add("try::catch:", heap.newBinaryBlock(SSDynamicObject::tryCatch,
            List.of("tryBlock", "catchBlock")));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject newFactory(final Heap heap) {

      final var result = heap.newObject();

      result.addMethod("new", heap.newBinaryBlock((s, h, a) -> h.newObject()));
      result.addMethod("newOfNature:",
            heap.newBinaryBlock(SSDynamicObject::newOfNature, List.of("nature")));
      result.addMethod("addMethod::using:", heap.newBinaryBlock(
            SSDynamicObject::addMethodToFactory, List.of("name", "block")));

      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject addMethodToFactory(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      final var name = args[1].evaluate(stack).toString();

      if (name.equals("new")) {
         return SSNativeObject.throwException(stack, heap, subject,
               "Method 'Object new' cannot be overriden.");
      } else if (name.equals("newOfNature:")) {
         return SSNativeObject.throwException(stack, heap, subject,
               "Method 'Object newOfNature:' cannot be overriden.");
      } else {
         return SSNativeObject.addMethod(stack, heap, args);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected void addMethod(final String name, final SSObject block) {

      if (this.methods.isShared()) {
         this.methods = new MethodMap(this.methods, false);
      }
      this.methods.add(name, block);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected SSObject getMethod(final String name, final SSObject defaultValue) {
      return this.methods.getOrDefault(name, defaultValue);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected Set<SSObject> getMethods(final Stack stack) {

      final Set<SSObject> result = new HashSet<>();

      for (final String methodName : this.methods.keySet()) {
         result.add(heap.newString(methodName));
      }

      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final SSObject[] args) {

      final var block = this.methods.get(method);
      if (block != null) {
         return block.execute(stack, prependThisTo(args));
      } else {
         final var message = heap.newObject();
         message.addField(stack, "nature", this.heap.newString("message"));
         message.addField(stack, "method", this.heap.newString(method));
         message.addField(stack, "args", this.heap.newList(stream(args)));
         return invoke(stack, "doesNotUnderstand:", new SSObject[] { message });
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return "object#" + hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSDynamicObject) args[0];
      return new SSDynamicObject(subject);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private static SSObject newOfNature(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var result = heap.newObject();
      result.addField(stack, "nature", args[1]);
      return result;
   }
}
