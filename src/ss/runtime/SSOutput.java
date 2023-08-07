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
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSOutput extends SSDynamicObject {
   
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSOutput(final PrintStream out) {
      
      this.out = out;
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
         case "writeLine:" -> writeLine(stack, args.get(0));
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject writeLine(final Stack stack, final SSObject arg) {

      try {
         out.println(arg);
         return this;
      } catch (final Exception e) {
         throw new RuntimeException(e);
      }
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   private final PrintStream out;
}
