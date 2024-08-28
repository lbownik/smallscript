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

import static ss.runtime.SSBinaryBlock.bb;

import java.util.List;
import java.util.stream.Stream;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class SSStream extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSStream(final Stream<SSObject> stream) {

      super(new MethodMap(sharedMethods));
      this.stream = stream;
      this.fields.add("nature", nature);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static Methods putMethods(final Methods methods) {

      methods.add("collectTo:", bb(SSStream::collectTo, List.of("collector")));
      methods.add("forEach:", bb(SSStream::forEach, List.of("block")));
      methods.add("selectIf:", bb(SSStream::selectIf, List.of("block")));
      methods.add("transformUsing:", bb(SSStream::transformUsing, List.of("block")));
      
      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected void addMethod(final String name, final SSObject block) {

      if(this.methods == SSStream.sharedMethods) {
         this.methods = new MethodMap(SSStream.sharedMethods);
      }
      this.methods.add(name, block);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject collectTo(final Stack stack, final List<SSObject> args) {

      final var subject = (SSStream) args.get(0);
      subject.stream.forEach(item -> {
         var newTarget = args.get(1).invoke(stack, "append:",
               List.of(item.evaluate(stack)));
         args.set(1, newTarget);
      });
      return args.get(1);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSStream) args.get(0);
      final var target = args.get(1);

      subject.stream.forEach(item -> {
         target.invoke(stack, "executeWith:",
               List.of(item.evaluate(stack)));
      });
      return stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject selectIf(final Stack stack, final List<SSObject> args) {

      final var subject = (SSStream) args.get(0);
      final var target = args.get(1);
      final var newStream = subject.stream.filter(item -> {
         var result = target.invoke(stack, "executeWith:",
               List.of(item.evaluate(stack)));
         return result == stack.getTrue();
      });
      return new SSStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject transformUsing(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSStream) args.get(0);
      final var target = args.get(1);
      final var newStream = subject.stream
            .map(item -> target.invoke(stack, "executeWith:",
                  List.of(item.evaluate(stack))));
      return new SSStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return this == o;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Stream<SSObject> stream;
   
   final static Methods sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods));
   private final static SSString nature = new SSString("stream");
}
