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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static ss.runtime.SSBinaryBlock.bb;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject extends SSNativeObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject() {

      this(sharedMethods);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject(final MethodMap methods) {

      this.methods = methods;
      this.fields = null;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject(final SSDynamicObject other) {

      if (other.methods.isShared()) {
         this.methods = other.methods;
      } else {
         this.methods = new MethodMap(other.methods);
      }
      if (other.fields != null) {
         this.fields = new HashMap<>(other.fields);
      } else {
         this.fields = null;
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final MethodMap methods) {

      final var listOfBlock = List.of("block");

      methods.add("arguments", bb((s, a) -> new SSList()));
      methods.add("invoke::with:",
            bb(SSDynamicObject::invokeWith, List.of("method", "argList")));
      methods.add("addField:", bb(SSDynamicObject::addField, List.of("name")));
      methods.add("addField::withValue:",
            bb(SSDynamicObject::addFieldWithValue, List.of("name", "value")));
      methods.add("addMethod::using:",
            bb(SSDynamicObject::addMethod, List.of("name", "block")));
      methods.add("asString", bb(SSDynamicObject::asString));
      methods.add("at:", bb(SSDynamicObject::at, List.of("index")));
      methods.add("clone", bb(SSDynamicObject::clone));
      methods.add("close", bb(SSDynamicObject::returnThis));
      methods.add("collectTo:",
            bb(SSDynamicObject::collectTo, List.of("collector")));
      methods.add("doesNotUnderstand:",
            bb(SSDynamicObject::doesNotUnderstand, List.of("message")));
      methods.add("isEqualTo:", bb(SSDynamicObject::isEqualTo, List.of("other")));
      methods.add("execute", bb(SSDynamicObject::evaluate));
      methods.add("fields", bb(SSDynamicObject::getFields));
      methods.add("forEach:", bb(SSDynamicObject::forEach, listOfBlock));
      methods.add("method:", bb(SSDynamicObject::getMethod, List.of("name")));
      methods.add("methods", bb(SSDynamicObject::getMethods));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("hash", bb(SSDynamicObject::hashCode));
      methods.add("isNotEqualTo:",
            bb(SSDynamicObject::isNotEqualTo, List.of("other")));
      methods.add("orDefault:", bb(SSDynamicObject::returnThis, List.of("default")));
      methods.add("size", bb((stack, args) -> new SSLong(1)));
      methods.add("selectIf:", bb(SSDynamicObject::selectIf, listOfBlock));
      methods.add("throw", bb(SSDynamicObject::throwThis));
      methods.add("transformUsing:",
            bb(SSDynamicObject::transformUsing, listOfBlock));
      methods.add("try:", bb(SSDynamicObject::try_, List.of("tryBlock")));
      methods.add("try::catch:",
            bb(SSDynamicObject::tryCatch, List.of("tryBlock", "catchBlock")));

      return methods;
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
   protected Set<SSObject> getMethods() {
      
      return this.methods.keySet().stream().map(SSString::new).collect(toSet());
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
         final var message = new SSDynamicObject();
         message.addField(stack, "nature", new SSString("message"));
         message.addField(stack, "method", new SSString(method));
         message.addField(stack, "args", new SSList(asList(args)));
         return invoke(stack, "doesNotUnderstand:", new SSObject[] {message});
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return Factory.nature.toString() + "#" + hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final SSObject[] args) {

      final var subject = (SSDynamicObject) args[0];
      return new SSDynamicObject(subject);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   final static MethodMap sharedMethods = putMethods(new MethodMap());
   public final static SSString nature = new SSString("object");
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addMethod("new", bb((stack, args) -> new SSDynamicObject()));
         addMethod("newOfNature:",
               bb(SSDynamicObject.Factory::newOfNature, List.of("nature")));
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject newOfNature(final Stack stack,
            final SSObject[] args) {

         final var result = new SSDynamicObject();
         result.addField(stack, "nature", args[1]);
         return result;
      }
      /*************************************************************************
       * 
      *************************************************************************/
   }
}
