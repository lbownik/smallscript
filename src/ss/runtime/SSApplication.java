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

import static java.util.Collections.emptyList;

import java.io.InputStreamReader;
import java.util.List;

import ss.parser.Parser;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSApplication extends SSDynamicObject {
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
         case "exit:" -> exit(args.get(0));
         case "load:" -> load(stack, args.get(0));
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject exit(final SSObject code) {

      System.exit(((SSLong) code).intValue());
      return SSNull.instance();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject load(final Stack stack, final SSObject code) {

      try (final var reader = new InputStreamReader(
            getClass().getResourceAsStream(code.toString()), "utf-8")) {

         return new Parser().parse(reader).toSSObject().invoke(stack, "execute",
               emptyList());
         
      } catch (final Exception e) {
         throw new RuntimeException(e);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return "application";
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
}
