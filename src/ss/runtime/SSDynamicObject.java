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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject implements SSObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject() {

      this(new HashMap<>(), new HashMap<>(0));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSDynamicObject(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields) {

      this.methods = new HashMap<>(methods);
      this.fields = new HashMap<>(fields);

      initMethods();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private void initMethods() {
      
      addBinaryMethod("addField:", this::addField);
      addBinaryMethod("addField::withValue:", this::addFieldWithValue);
      addBinaryMethod("addMethod::using:", this::addMethod);
      addBinaryMethod("asString", (stack, args) -> new SSString(toString()));
      addBinaryMethod("at:", (stack, args) -> this);
      addBinaryMethod("clone", (stack, args) -> doClone());
      addBinaryMethod("doesNotUnderstand:", this::doesNotUnderstand);
      addBinaryMethod("equals:", this::equals);
      addBinaryMethod("execute", (stack, args) -> evaluate(stack));
      addBinaryMethod("forEach:", this::forEach);
      addBinaryMethod("getMethod:", this::getMethod);
      addBinaryMethod("hash", (stack, args) -> new SSLong(hashCode()));
      addBinaryMethod("isNotEqualTo:", this::isNotEqualTo);
      addBinaryMethod("size", (stack, args) -> new SSLong(1));
      addBinaryMethod("throw", SSDynamicObject::throwThis);
      addBinaryMethod("try::catch:", SSDynamicObject::tryCatch);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected void addBinaryMethod(final String name,
         final BiFunction<Stack, List<SSObject>, SSObject> code) {

      this.methods.put(name, new SSBinaryBlock(code));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      final var block = this.methods.get(method);
      if (block != null) {
         return block.invoke(stack, "execute", prependThisTo(args));
      } else {
         final var message = new SSDynamicObject();
         message.addField(stack, "method", new SSString(method));
         message.addField(stack, "args", new SSList(args));
         return doesNotUnderstand(stack, List.of(this, message));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private List<SSObject> prependThisTo(final List<SSObject> args) {

      final ArrayList<SSObject> result = new ArrayList<>();
      result.add(this);
      result.addAll(args);
      return result;
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
   protected SSObject doClone() {

      return new SSDynamicObject(this.methods, this.fields);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject throwThis(final Stack stack, final List<SSObject> args) {

      throw new AuxiliaryException(args.get(0));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject tryCatch(final Stack stack, final List<SSObject> args) {

      try {
         return args.get(1).invoke(stack.pushNewFrame(), "execute");
      } catch (final AuxiliaryException e) {
         return args.get(2).invoke(stack.pushNewFrame(), "execute",
               List.of(e.object));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject forEach(final Stack stack, final List<SSObject> args) {

      args.get(1).invoke(stack.pushNewFrame(), "executeWith:", List.of(this));
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject addMethod(final Stack stack, final List<SSObject> args) {

      this.methods.put(args.get(1).toString(), (SSBlock) args.get(2));
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject getMethod(final Stack stack, final List<SSObject> args) {

      return this.methods.get(args.get(1).toString());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject addField(final Stack stack, final List<SSObject> args) {

      return addField(stack, args.get(1).toString(), stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject addFieldWithValue(final Stack stack, final List<SSObject> args) {

      return addField(stack, args.get(1).toString(), args.get(2));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject addField(final Stack stack, final String name,
         final SSObject value) {

      addBinaryMethod(name, (s, a) -> this.fields.get(name));
      addBinaryMethod(name + ":", (s, a) -> setField(name, a.get(1)));

      return setField(name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject setField(final String name, final SSObject value) {

      this.fields.put(name, value);
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject isNotEqualTo(final Stack stack, final List<SSObject> args) {

      return stack.get(!this.equals(args.get(1).evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject equals(final Stack stack, final List<SSObject> args) {

      return stack.get(this.equals(args.get(1).evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject doesNotUnderstand(final Stack stack, final List<SSObject> args) {

      final var message = args.get(1);
      final var method = message.invoke(stack.pushNewFrame(), "method");

      throw new RuntimeException("Method '" + method + "' is not defined.");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected final Map<String, SSObject> methods;
   protected final Map<String, SSObject> fields;
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      protected SSObject doClone() {

         return new SSDynamicObject.Factory();
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

         return method.equals("new") ? createNew()
               : super.invoke(stack, method, args);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private SSObject createNew() {

         final var result = new SSDynamicObject();
         result.setField("nature", nature);
         return result;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public String toString() {

         return "Object";
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private final static SSString nature = new SSString("object");
   }
}
