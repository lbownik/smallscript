
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

/*******************************************************************************
 * Contains implementation of common methods for most basic language objects
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public abstract class SSNativeObject implements SSObject {

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
         return throwException(stack, args[1],
               "Index " + index + " out of bounds.");
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
   static SSObject forEach(final Stack stack, final SSObject[] args) {

      final var subject = args[0];
      args[1].invoke(stack, "executeWith:", new SSObject[]{subject});
      return subject;
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
   static SSObject isNotEqualTo(final Stack stack,
         final SSObject[] args) {

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
   static SSObject selectIf(final Stack stack, final SSObject[] args) {

      final var subject = args[0];

      var result = args[1].invoke(stack, "executeWith:",
            new SSObject[]{subject.evaluate(stack)});

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
            new SSObject[]{args[0].evaluate(stack)});
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject tryCatch(final Stack stack, final SSObject[] args) {

      final var subject = args[0];
      try {
         return args[1].invoke(stack, "execute", new SSObject[]{subject});
      } catch (final AuxiliaryException e) {
         return args[2].invoke(stack, "execute", new SSObject[]{e.object});
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
         return args[1].invoke(stack, "execute", new SSObject[]{subject});
      } finally {
         subject.invoke(stack, "close");
      }
   }
}
