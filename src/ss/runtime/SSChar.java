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

import java.util.List;
import java.util.Map;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSChar extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSChar(final char value) {

      this.value = value;
      
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSChar(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields, final char value) {

      super(methods, fields);
      //addBinaryMethod("isGreaterThanz:", this::isGreaterThan);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject doClone() {

      return new SSChar(this.methods, this.fields, this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "isGreaterThan:" -> stack.get(this.value > evaluateFirst(args, stack));
         case "isLessThan:" -> stack.get(this.value < evaluateFirst(args, stack));
         case "isGreaterOrEqualTo:" ->
            stack.get(this.value >= evaluateFirst(args, stack));
         case "isLessOrEqualTo:" ->
            stack.get(this.value <= evaluateFirst(args, stack));
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static char evaluateFirst(final List<SSObject> args, final Stack stack) {

      return ((SSChar) args.get(0).evaluate(stack.pushNewFrame())).value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static char evaluateSecond(final Stack stack, final List<SSObject> args) {

      return ((SSChar) args.get(1).evaluate(stack.pushNewFrame())).value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return Character.toString(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return Character.hashCode(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return getClass() == o.getClass() && this.value == ((SSChar) o).value;

   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject isGreaterThan(final Stack stack, final List<SSObject> args) {

      return stack.get(this.value > evaluateSecond(stack, args));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final char value;
}
