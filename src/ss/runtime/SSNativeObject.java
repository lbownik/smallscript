
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

import static java.lang.System.arraycopy;
import static java.util.stream.Collectors.toSet;
import static ss.runtime.SSBinaryBlock.bb;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/*******************************************************************************
 * Contains implementation of common methods for most basic language objects
 * 
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public abstract class SSNativeObject implements SSObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   protected abstract void addMethod(final String name, final SSObject block);
   /****************************************************************************
    * 
   ****************************************************************************/
   protected abstract SSObject getMethod(final String name, final SSObject defaultValue);
   /****************************************************************************
    * 
   ****************************************************************************/
   protected abstract Set<SSObject> getMethods();
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject addField(final Stack stack, String name, final SSObject value) {

      final String iName = name.intern();
      addMethod(name, bb((s, a) -> getField(s, iName, a)));
      addMethod(name + ":", bb((s, a) -> setField(s, iName, a), List.of("value")));

      if (this.fields == null) {
         this.fields = new HashMap<>();
      }
      return setField(stack, iName, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject setField(final Stack stack, final String name,
         final SSObject value) {

      this.fields.put(name, value.evaluate(stack));
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject[] prependThisTo(final SSObject[] args) {

      final SSObject[] result = new SSObject[args.length + 1];
      result[0] = this;
      arraycopy(args, 0, result, 1, args.length);
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject addField(final Stack stack, final SSObject[] args) {

      final var subject = (SSDynamicObject) args[0];
      final var name = args[1].evaluate(stack).toString().intern();
      return subject.addField(stack, name, stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject addFieldWithValue(final Stack stack, final SSObject[] args) {

      final var subject = (SSDynamicObject) args[0];
      final var name = args[1].evaluate(stack).toString().intern();
      final var value = args[2].evaluate(stack);
      return subject.addField(stack, name, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject addMethod(final Stack stack, final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      final var name = args[1].evaluate(stack).toString();
      final var block = args[2].evaluate(stack);

      subject.addMethod(name, block);
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject asString(final Stack stack, final SSObject[] args) {

      return new SSString(args[0].toString());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject at(final Stack stack, final SSObject[] args) {

      final var index = ((SSLong) args[1]).intValue();
      if (index == 0) {
         return args[0];
      } else {
         return throwException(stack, args[1], "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject doesNotUnderstand(final Stack stack, final SSObject[] args) {

      final var message = args[1];
      final var method = message.invoke(stack, "method");
      return throwException(stack, message,
            "Method '" + method + "' is not defined.");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   static SSObject evaluate(final Stack stack, final SSObject[] args) {

      return args[0].evaluate(stack);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final SSObject[] args) {

      final var subject = args[0];
      args[1].invoke(stack, "executeWith:", new SSObject[] { subject });
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject getField(final Stack stack, final String name,
         final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      return subject.fields.getOrDefault(name, stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject getMethod(final Stack stack, final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      return subject.getMethod(args[1].toString(), stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject getMethods(final Stack stack, final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      return new SSSet(subject.getMethods());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject hashCode(final Stack stack, final SSObject[] args) {

      return new SSLong(args[0].hashCode());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject invokeWith(final Stack stack, final SSObject[] args) {

      final var subject = (SSObject) args[0];
      final var method = args[1].evaluate(stack).toString();
      final var argList = ((SSList) args[2].evaluate(stack)).elements.toArray(emptyArgs);

      return subject.invoke(stack, method, argList);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject isNotEqualTo(final Stack stack, final SSObject[] args) {

      return stack.get(!args[0].equals(args[1].evaluate(stack)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject isEqualTo(final Stack stack, final SSObject[] args) {

      return stack.get(args[0].equals(args[1].evaluate(stack)));
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   static SSObject returnThis(final Stack stack, final SSObject[] args) {

      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject setField(final Stack stack, final String name,
         final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];

      return subject.setField(stack, name, args[1]);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject selectIf(final Stack stack, final SSObject[] args) {

      final var subject = args[0];

      var result = args[1].invoke(stack, "executeWith:",
            new SSObject[] { subject.evaluate(stack) });

      return result == stack.getTrue() ? subject : stack.getNull();
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
   static SSObject throwThis(final Stack stack, final SSObject[] args) {

      throw new AuxiliaryException(args[0]);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject transformUsing(final Stack stack, final SSObject[] args) {

      return args[1].invoke(stack, "executeWith:",
            new SSObject[] { args[0].evaluate(stack) });
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject collectTo(final Stack stack, final SSObject[] args) {

      return args[1].invoke(stack, "append:",
            new SSObject[] { args[0].evaluate(stack) });
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject tryCatch(final Stack stack, final SSObject[] args) {

      final var subject = args[0];
      try {
         return args[1].invoke(stack, "execute", new SSObject[] { subject });
      } catch (final AuxiliaryException e) {
         return args[2].invoke(stack, "execute", new SSObject[] { e.object });
      } finally {
         subject.invoke(stack, "close");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject try_(final Stack stack, final SSObject[] args) {

      final var subject = args[0];
      try {
         return args[1].invoke(stack, "execute", new SSObject[] { subject });
      } finally {
         subject.invoke(stack, "close");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject getFields(final Stack stack, final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      if (subject.fields != null) {
         return new SSSet(
               subject.fields.keySet().stream().map(SSString::new).collect(toSet()));
      } else {
         return new SSSet();
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   protected HashMap<String, SSObject> fields;
   protected MethodMap methods;
}
