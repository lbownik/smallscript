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
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSTrue extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSTrue() {

      addBinaryMethod("and:", SSTrue::and);
      addBinaryMethod("clone", SSTrue::clone);
      addBinaryMethod("ifFalse:", SSTrue::ifFalse);
      addBinaryMethod("ifTrue:", SSTrue::ifTrue);
      addBinaryMethod("ifTrue::ifFalse:", SSTrue::ifTrue);
      addBinaryMethod("not", SSTrue::not);
      addBinaryMethod("or:", SSTrue::or);
      addBinaryMethod("xor:", SSTrue::xor);
      addField("nature", SSDynamicObject.nature);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject and(final Stack stack, final List<SSObject> args) {

      return args.get(1).evaluate(stack.pushNewFrame());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject ifFalse(final Stack stack, final List<SSObject> args) {

      return stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject ifTrue(final Stack stack, final List<SSObject> args) {

      return args.get(1).invoke(stack.pushNewFrame(), "execute");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private static SSObject not(final Stack stack, final List<SSObject> args) {

      return stack.getFalse();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject or(final Stack stack, final List<SSObject> args) {

      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject xor(final Stack stack, final List<SSObject> args) {

      return args.get(1).invoke(stack, "not");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return name;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return 2;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object obj) {

      return getClass().equals(obj.getClass());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public final static String name = "true";
}
