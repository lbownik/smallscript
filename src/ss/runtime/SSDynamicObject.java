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
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject implements SSObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject() {

      this(new HashMap<>(0), new HashMap<>(0));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSDynamicObject(final Map<String, SSBlock> methods,
         final Map<String, SSObject> fields) {

      this.methods = new HashMap<>(methods);
      this.fields = new HashMap<>(fields);

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
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      final var block = this.methods.get(method);
      if (block != null) {
         final ArrayList<SSObject> nArgs = new ArrayList<>();
         nArgs.add(this);
         nArgs.addAll(args);
         return block.invoke(stack, "execute", nArgs);
      } else {
         if (this.fields.containsKey(method)) {
            return this.fields.get(method);
         } else if (method.endsWith(":") && this.fields
               .containsKey(method.substring(0, method.length() - 1))) {
            return setField(method.substring(0, method.length() - 1), args.get(0));
         } else {
            return switch (method) {
               case "addMethod::using:" ->
                  addMethod(args.get(0).toString(), (SSBlock) args.get(1));
               case "addField:" -> addField(args.get(0).toString(), stack.getNull());
               case "clone" -> doClone();
               case "execute" -> evaluate(stack);
               case "asString" -> new SSString(toString());
               case "hash" -> new SSLong(hashCode());
               case "equals:" -> stack.get(this.equals(args.get(0).evaluate(stack)));
               case "isNotEqualTo:" ->
                  stack.get(!this.equals(args.get(0).evaluate(stack)));
               case "throw" -> throw new AuxiliaryException(this);
               case "try::catch:" -> tryCatch(stack, args.get(0), args.get(1));
               case "size" -> new SSLong(1);
               case "at:" -> this;
               case "forEach:" -> forEach(stack, args.get(0));
               default -> throw new RuntimeException(
                     "Method '" + method + "' is not defined.");
            };
         }
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject tryCatch(final Stack stack, final SSObject tryBlock,
         final SSObject catchBlock) {

      try {
         return tryBlock.invoke(stack.pushNewFrame(), "execute");
      } catch (final AuxiliaryException e) {
         return catchBlock.invoke(stack.pushNewFrame(), "execute",
               List.of(e.object));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject forEach(final Stack stack, final SSObject block) {
      
      block.invoke(stack.pushNewFrame(), "executeWith:", List.of(this));
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject addMethod(final String name, final SSBlock body) {

      this.methods.put(name, body);
      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject addField(final String name, final SSObject value) {

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
   protected final Map<String, SSBlock> methods;
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
