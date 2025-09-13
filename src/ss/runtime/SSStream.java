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
import java.util.stream.Stream;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class SSStream extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSStream(final Heap heap, final MethodMap methods,
         final Stream<SSObject> stream) {

      super(heap, methods);
      this.stream = stream;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final Heap heap, final MethodMap methods) {

      final var listOfBlock = List.of("block");

      methods.add("collectTo:",
            heap.newBinaryBlock(SSStream::collectTo, List.of("collector")));
      methods.add("forEach:", heap.newBinaryBlock(SSStream::forEach, listOfBlock));
      methods.add("selectIf:", heap.newBinaryBlock(SSStream::selectIf, listOfBlock));
      methods.add("transformUsing:",
            heap.newBinaryBlock(SSStream::transformUsing, listOfBlock));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject collectTo(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSStream) args[0];
      subject.stream.forEach(item -> {
         var newTarget = args[1].invoke(stack, "append:",
               new SSObject[] { item.evaluate(stack) });
         args[1] = newTarget;
      });
      return args[1];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSStream) args[0];
      final var target = args[1];

      subject.stream.forEach(item -> {
         target.invoke(stack, "executeWith:",
               new SSObject[] { item.evaluate(stack) });
      });
      return stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject selectIf(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSStream) args[0];
      final var target = args[1];
      final var newStream = subject.stream.filter(item -> {
         var result = target.invoke(stack, "executeWith:",
               new SSObject[] { item.evaluate(stack) });
         return result == stack.getTrue();
      });
      return heap.newStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject transformUsing(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSStream) args[0];
      final var target = args[1];
      final var newStream = subject.stream.map(item -> target.invoke(stack,
            "executeWith:", new SSObject[] { item.evaluate(stack) }));
      return heap.newStream(newStream);
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
}
