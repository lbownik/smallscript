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

import java.io.BufferedReader;
import java.io.InputStreamReader;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSInput extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSInput(final BufferedReader reader) {

      this.reader = reader;
      addMethod("clone", bb(SSInput::clone));
      addMethod("readLine", bb(SSInput::readLine));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSInput() {

      this(new BufferedReader(new InputStreamReader(System.in)));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final SSObject[] args) {

      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject readLine(final Stack stack, final SSObject[] args) {

      final var subject = (SSInput) args[0];
      try {
         final var line = subject.reader.readLine();
         return line != null ? new SSString(line) : stack.getNull();
      } catch (final Exception e) {
         return throwException(stack, subject, e.getMessage());
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final BufferedReader reader;
}
