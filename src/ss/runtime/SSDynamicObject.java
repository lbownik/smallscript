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
import static java.util.stream.Collectors.toSet;
import static ss.runtime.SSBinaryBlock.bb;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject implements SSObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject() {

      this.methods = new HashMap<>(30);

      putMethods(this.methods);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject(final SSDynamicObject other) {

      this.methods = new HashMap<>(other.methods);
      this.fields.putAll(other.fields);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static Map<String, SSObject> putMethods(final Map<String, SSObject> methods) {

      methods.put("invoke::with:", bb(SSDynamicObject::invokeWith));
      methods.put("addField:", bb(SSDynamicObject::addField));
      methods.put("addField::withValue:", bb(
            SSDynamicObject::addFieldWithValue));
      methods.put("addImmutableField::withValue:", bb(
            SSDynamicObject::addImmutableFieldWithValue));
      methods.put("addMethod::using:", bb(SSDynamicObject::addMethod));
      methods.put("asString", bb(SSDynamicObject::asString));
      methods.put("at:", bb(SSDynamicObject::at));
      methods.put("clone", bb(SSDynamicObject::clone));
      methods.put("collectTo:", bb(SSDynamicObject::collectTo));
      methods.put("doesNotUnderstand:", bb(
            SSDynamicObject::doesNotUnderstand));
      methods.put("equals:", bb(SSDynamicObject::equals));
      methods.put("execute", bb(SSDynamicObject::evaluate));
      methods.put("fields", bb(SSDynamicObject::getFields));
      methods.put("forEach:", bb(SSDynamicObject::forEach));
      methods.put("method:", bb(SSDynamicObject::getMethod));
      methods.put("methods", bb(SSDynamicObject::getMethods));
      methods.put("hash", bb(SSDynamicObject::hashCode));
      methods.put("isNotEqualTo:", bb(
            SSDynamicObject::isNotEqualTo));
      methods.put("orDefault:", bb(SSDynamicObject::orDefault));
      methods.put("removeMethod:", bb(
            SSDynamicObject::removeMethod));
      methods.put("size", bb((stack, args) -> new SSLong(1)));
      methods.put("selectIf:", bb(SSDynamicObject::selectIf));
      methods.put("throw", bb(SSDynamicObject::throwThis));
      methods.put("transformUsing:", bb(
            SSDynamicObject::transformUsing));
      methods.put("try::catch:", bb(SSDynamicObject::tryCatch));

      return methods;
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
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      final var block = this.methods.get(method);
      if (block != null) {
         return block.invoke(stack, "execute", prependThisTo(args));
      } else {
         final var message = new SSDynamicObject();
         message.addField(stack, "nature", new SSString("message"));
         message.addField(stack, "method", new SSString(method));
         message.addField(stack, "args", new SSList(args));
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
   private static SSObject orDefault(final Stack stack, final List<SSObject> args) {

      return args.get(0);
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
   private static SSObject invokeWith(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      final var method = args.get(1).evaluate(stack).toString();
      final var mArgs = ((SSList) args.get(2).evaluate(stack)).elements;

      return subject.invoke(stack, method, mArgs);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asString(final Stack stack, final List<SSObject> args) {

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
      return new SSDynamicObject(subject);
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
         return args.get(1).invoke(stack, "execute");
      } catch (final AuxiliaryException e) {
         return args.get(2).invoke(stack, "execute", List.of(e.object));
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
         return throwException(stack, args.get(1),
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      args.get(1).invoke(stack, "executeWith:", List.of(subject));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addMethod(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      final var name = args.get(1).evaluate(stack).toString();
      final var block = args.get(2).evaluate(stack);

      subject.methods.put(name, block);
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
   private static SSObject getMethods(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return new SSSet(
            subject.methods.keySet().stream().map(SSString::new).collect(toSet()));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeMethod(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      final var name = args.get(1).evaluate(stack).toString();
      subject.methods.remove(name);
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addField(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      final var name = args.get(1).evaluate(stack).toString();
      return subject.addField(stack, name, stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addFieldWithValue(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      final var name = args.get(1).evaluate(stack).toString();
      final var value = args.get(2).evaluate(stack);
      return subject.addField(stack, name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject addImmutableFieldWithValue(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      final var name = args.get(1).evaluate(stack).toString();
      final var value = args.get(2).evaluate(stack);
      return subject.addImmutableField(stack, name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject addField(final Stack stack, final String name, final SSObject value) {

      addBinaryMethod(name, (s, a) -> getField(s, name, a));
      addBinaryMethod(name + ":", (s, a) -> setField(s, name, a));

      return setField(stack, name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject addImmutableField(final Stack stack, final String name,
         final SSObject value) {

      addBinaryMethod(name, (s, a) -> getField(s, name, a));

      return setField(stack, name, value);
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
   private static SSObject getFields(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);
      return new SSSet(
            subject.fields.keySet().stream().map(SSString::new).collect(toSet()));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject setField(final Stack stack, final String name,
         final List<SSObject> args) {

      final var subject = (SSDynamicObject) args.get(0);

      return subject.setField(stack, name, args.get(1));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject setField(final Stack stack, final String name, final SSObject value) {

      this.fields.put(name, value.evaluate(stack));
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
   static SSObject doesNotUnderstand(final Stack stack, final List<SSObject> args) {

      final var message = args.get(1);
      final var method = message.invoke(stack, "method");
      return throwException(stack, message,
            "Method '" + method + "' is not defined.");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject selectIf(final Stack stack, final List<SSObject> args) {

      final var subject = args.get(0);

      var result = args.get(1).invoke(stack, "executeWith:",
            List.of(subject.evaluate(stack)));

      return result == stack.getTrue() ? subject : stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject transformUsing(final Stack stack,
         final List<SSObject> args) {

      final var subject = args.get(0);

      return args.get(1).invoke(stack, "executeWith:",
            List.of(subject.evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject collectTo(final Stack stack, final List<SSObject> args) {

      return args.get(1).invoke(stack, "append:",
            List.of(args.get(0).evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject throwException(final Stack stack, final SSObject cause,
         final String message) {

      final var exception = new SSDynamicObject();
      exception.addField(stack, "nature", new SSString("exception"));
      exception.addField(stack, "cause", cause);
      exception.addField(stack, "message", new SSString(message));
      throw new AuxiliaryException(exception);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   final Map<String, SSObject> methods;
   final Map<String, SSObject> fields = new HashMap<>();

   public final static SSString nature = new SSString("object");
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addBinaryMethod("new", SSDynamicObject.Factory::createNew);
         addBinaryMethod("newOfNature:", SSDynamicObject.Factory::newOfNature);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject createNew(final Stack stack,
            final List<SSObject> args) {

         final var result = new SSDynamicObject();
         result.addField(stack, "nature", nature);
         return result;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject newOfNature(final Stack stack,
            final List<SSObject> args) {

         final var result = new SSDynamicObject();
         result.addField(stack, "nature", args.get(1));
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
   }
}
