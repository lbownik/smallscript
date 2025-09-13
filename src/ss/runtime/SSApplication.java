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

import static java.util.Arrays.stream;
import static ss.runtime.SSNativeObject.throwException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.BiFunction;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSApplication {

   /****************************************************************************
    * 
    ****************************************************************************/
   public static SSObject newApplication(final Heap heap, final String[] args,
         final BiFunction<Stack, String, SSObject> load) {

      final var result = heap.newObject();

      result.addField(null, "arguments",
            heap.newList(stream(args).map(heap::newString)));
      result.addField(null, "input",
            heap.newInput(new BufferedReader(new InputStreamReader(System.in))));
      result.addField(null, "output", heap.newOutput(System.out));
      result.addField(null, "error", heap.newOutput(System.err));

      result.addMethod("clone", heap.newBinaryBlock(SSApplication::clone));
      result.addMethod("exit:", heap.newBinaryBlock(SSApplication::exit));
      result.addMethod("load:", heap.newBinaryBlock((s, h, a) -> {
         try {
            return load.apply(s, a[1].toString());
         } catch (final Exception e) {
            return throwException(s, h, a[0], e.getMessage());
         }
      }));

      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final Heap heap,
         final SSObject[] args) {

      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject exit(final Stack stack, final Heap heap,
         final SSObject[] args) {

      System.exit(((SSLong) args[1]).intValue());
      return stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
}
