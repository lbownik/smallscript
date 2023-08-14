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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSInput extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSInput(final BufferedReader reader) {

      this.reader = reader;
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
   protected SSObject doClone() {

      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "readLine" -> readLine(stack);
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject readLine(final Stack stack) {

      try {
         final var line = this.reader.readLine();
         return line != null ? new SSString(line) : stack.getNull();
      } catch (final Exception e) {
         throw new RuntimeException(e);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final BufferedReader reader;
}