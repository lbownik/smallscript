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

import static java.util.stream.Collectors.toSet;
import static ss.runtime.SSBinaryBlock.bb;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject implements SSObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject() {

      this(new MethodMap(sharedMethods));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject(final MethodMap methods) {

      this.methods = methods;
      this.fields = new MethodMap();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject(final SSDynamicObject other) {

      this.methods = new MethodMap(other.methods);
      this.fields = new MethodMap(other.fields);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static Methods putMethods(final Methods methods) {

      final var listOfBlock = List.of("block");
      
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
      methods.add("collectTo:",
            bb(SSDynamicObject::collectTo, List.of("collector")));
      methods.add("doesNotUnderstand:",
            bb(SSDynamicObject::doesNotUnderstand, List.of("message")));
      methods.add("equals:", bb(SSDynamicObject::isEqualTo, List.of("other")));
      methods.add("execute", bb(SSDynamicObject::evaluate));
      methods.add("fields", bb(SSDynamicObject::getFields));
      methods.add("forEach:", bb(SSDynamicObject::forEach, listOfBlock));
      methods.add("method:", bb(SSDynamicObject::getMethod, List.of("name")));
      methods.add("methods", bb(SSDynamicObject::getMethods));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("hash", bb(SSDynamicObject::hashCode));
      methods.add("isNotEqualTo:",
            bb(SSDynamicObject::isNotEqualTo, List.of("other")));
      methods.add("orDefault:", bb(SSDynamicObject::orDefault, List.of("default")));
      methods.add("size", bb((stack, args) -> new SSLong(1)));
      methods.add("selectIf:", bb(SSDynamicObject::selectIf, listOfBlock));
      methods.add("throw", bb(SSDynamicObject::throwThis));
      methods.add("transformUsing:",
            bb(SSDynamicObject::transformUsing, listOfBlock));
      methods.add("try::catch:",
            bb(SSDynamicObject::tryCatch, List.of("tryBlock", "catchBlock")));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected void addBinaryMethod(final String name,
         final BiFunction<Stack, List<SSObject>, SSObject> code) {

      this.methods.add(name, bb(code));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected void addBinaryMethod(final String name,
         final BiFunction<Stack, List<SSObject>, SSObject> code,
         final List<String> argumentNames) {

      this.methods.add(name, bb(code, argumentNames));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected void addMethod(final String name, final SSObject block) {

      if (this.methods == SSDynamicObject.sharedMethods) {
         this.methods = new MethodMap(SSDynamicObject.sharedMethods);
      }
      this.methods.add(name, block);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      final var block = this.methods.get(method);
      if (block != null) {
         return block.execute(stack, prependThisTo(args));
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
      final var argList = ((SSList) args.get(2).evaluate(stack)).elements;

      return subject.invoke(stack, method, argList);
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

      subject.addMethod(name, block);
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
   SSObject addField(final Stack stack, final String name, final SSObject value) {

      this.methods.add(name, bb((s, a) -> getField(s, name, a)));
      this.methods.add(name + ":",
            bb((s, a) -> setField(s, name, a), List.of("value")));

      return setField(stack, name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject addImmutableField(final Stack stack, final String name,
         final SSObject value) {

      this.methods.add(name, bb((s, a) -> getField(s, name, a)));

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

      this.fields.add(name, value.evaluate(stack));
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
   private static SSObject isEqualTo(final Stack stack, final List<SSObject> args) {

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
   protected MethodMap methods;
   final MethodMap fields;

   final static Methods sharedMethods = putMethods(new MethodMap());
   public final static SSString nature = new SSString("object");
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         this.methods.add("new", bb((stack, args) -> new SSDynamicObject()));
         this.methods.add("newOfNature:",
               bb(SSDynamicObject.Factory::newOfNature, List.of("nature")));
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
   }
}
