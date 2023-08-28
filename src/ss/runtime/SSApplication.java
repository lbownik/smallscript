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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import ss.parser.Parser;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSApplication extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSApplication(final BufferedReader in, final PrintStream out,
         final PrintStream err) {

      setField("input", new SSInput(in));
      setField("output", new SSOutput(out));
      setField("error", new SSOutput(err));

      addBinaryMethod("clone", SSApplication::clone);
      addBinaryMethod("exit:", SSApplication::exit);
      addBinaryMethod("load:", SSApplication::load);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSApplication() {

      this(new BufferedReader(new InputStreamReader(System.in)), System.out,
            System.err);
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
   private static SSObject exit(final Stack stack, final List<SSObject> args) {

      System.exit(((SSLong) args.get(1)).intValue());
      return SSNull.instance();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject load(final Stack stack, final List<SSObject> args) {

      try (final var reader = new InputStreamReader(
            SSApplication.class.getResourceAsStream(args.get(1).toString()),
            "utf-8")) {

         return new Parser().parse(reader).toSSObject().invoke(stack, "execute",
               emptyList());

      } catch (final Exception e) {
         return throwException(args.get(0), e.getMessage());
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
