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
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSList extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSList() {

      this.elements = new ArrayList<>();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSList(final ArrayList<SSObject> elements) {

      this.elements = new ArrayList<>(elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject doClone() {

      return new SSList(this.elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "size" -> new SSLong(this.elements.size());
         case "at:" -> this.elements.get(((SSLong) args.get(0)).intValue());
         case "append:" -> append(args.get(0));
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject append(final SSObject item) {
      
      this.elements.add(item);
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.elements.toString();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return this.elements.hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof SSList s && this.elements.equals(s.elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final ArrayList<SSObject> elements;
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

         final var result = new SSList();
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
      private final static SSString nature = new SSString("list");
   }
}
