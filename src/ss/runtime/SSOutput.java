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

import java.io.PrintStream;
import static java.util.Collections.emptyList;
import java.util.List;
import static ss.runtime.SSBinaryBlock.bb;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSOutput extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSOutput(final PrintStream out) {

      final var listOfObject = List.of("object");
      
      this.out = out;
      addMethod("clone", bb(SSOutput::clone));
      addMethod("writeLine:", bb(SSOutput::writeLine, listOfObject));
      addMethod("append:", bb(SSOutput::append, listOfObject));
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
   private static SSObject writeLine(final Stack stack, final List<SSObject> args) {

      final var subject = (SSOutput) args.get(0);
      try {
         subject.out.println(args.get(1).invoke(stack, "asString", emptyList()));
         return subject;
      } catch (final Exception e) {
         return throwException(stack, subject, e.getMessage());
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final List<SSObject> args) {

      final var subject = (SSOutput) args.get(0);
      try {
         subject.out.print(args.get(1).invoke(stack, "asString", emptyList()));
         return subject;
      } catch (final Exception e) {
         return throwException(stack, subject, e.getMessage());
      }
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   private final PrintStream out;
}
