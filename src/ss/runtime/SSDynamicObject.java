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

      initMethods();
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields) {

      this.methods.putAll(methods);
      this.fields.putAll(fields);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private void initMethods() {

      addBinaryMethod("addField:", SSDynamicObject::addField);
      addBinaryMethod("addField::withValue:", SSDynamicObject::addFieldWithValue);
      addBinaryMethod("addMethod::using:", SSDynamicObject::addMethod);
      addBinaryMethod("asString", SSDynamicObject::toString);
      addBinaryMethod("at:", SSDynamicObject::at);
      addBinaryMethod("clone", SSDynamicObject::clone);
      addBinaryMethod("doesNotUnderstand:", SSDynamicObject::doesNotUnderstand);
      addBinaryMethod("equals:", SSDynamicObject::equals);
      addBinaryMethod("execute", SSDynamicObject::evaluate);
      addBinaryMethod("forEach:", SSDynamicObject::forEach);
      addBinaryMethod("method:", SSDynamicObject::getMethod);
      addBinaryMethod("hash", SSDynamicObject::hashCode);
      addBinaryMethod("isNotEqualTo:", SSDynamicObject::isNotEqualTo);
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
         message.addField("nature", new SSString("message"));
         message.addField("method", new SSString(method));
         message.addField("args", new SSList(args));
         return invoke(stack, "doesNotUnderstand:", List.of(message));
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
   private static SSObject evaluate(final Stack stack, final List<SSObject> args) {

      return args.get(0).evaluate(stack);
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
   private static SSObject toString(final Stack stack, final List<SSObject> args) {

      return new SSString(args.get(0).toString());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject hashCode(final Stack stack, final List<SSObject> args) {

      return new SSLong(args.get(0).hashCode());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return new SSDynamicObject(subject.methods, subject.fields);
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
   private static SSObject at(final Stack stack, final List<SSObject> args) {

      final var index = ((SSLong) args.get(1)).intValue();
      if (index == 0) {
         return args.get(0);
      } else {
         return throwException(args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      args.get(1).invoke(stack.pushNewFrame(), "executeWith:", List.of(subject));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addMethod(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      subject.methods.put(args.get(1).toString(),
            args.get(2).evaluate(stack.pushNewFrame()));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject getMethod(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return subject.methods.getOrDefault(args.get(1).toString(), stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addField(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return subject.addField(args.get(1).toString(), stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addFieldWithValue(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return subject.addField(args.get(1).toString(), args.get(2));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject addField(final String name, final SSObject value) {

      addBinaryMethod(name, (s, a) -> getField(s, name, a));
      addBinaryMethod(name + ":", (s, a) -> setField(s, name, a));

      return setField(name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject getField(final Stack stack, final String name,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return subject.fields.getOrDefault(name, stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject setField(final Stack stack, final String name,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return subject.setField(name, args.get(1));
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
   private static SSObject isNotEqualTo(final Stack stack,
         final List<SSObject> args) {

      return stack.get(!args.get(0).equals(args.get(1).evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject equals(final Stack stack, final List<SSObject> args) {

      return stack.get(args.get(0).equals(args.get(1).evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject doesNotUnderstand(final Stack stack,
         final List<SSObject> args) {

      final var message = args.get(1);
      final var method = message.invoke(stack.pushNewFrame(), "method");

      return throwException(message, "Method '" + method + "' is not defined.");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject throwException(final SSObject cause,
         final String message) {

      final var exception = new SSDynamicObject();
      exception.addField("nature", new SSString("exception"));
      exception.addField("cause", cause);
      exception.addField("message", new SSString(message));
      throw new AuxiliaryException(exception);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected final Map<String, SSObject> methods = new HashMap<>();
   protected final Map<String, SSObject> fields = new HashMap<>();
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addBinaryMethod("new", Factory::createNew);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject createNew(final Stack stack,
            final List<SSObject> args) {

         final var result = new SSDynamicObject();
         result.addField("nature", nature);
         return result;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public String toString() {

         return "Object#" + hashCode();
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private final static SSString nature = new SSString("object");
   }
}
