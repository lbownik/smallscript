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
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Stream;
import static ss.runtime.SSBinaryBlock.bb;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSApplication extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSApplication(final Interpreter interpreter, final BufferedReader in,
         final PrintStream out, final PrintStream err) {

      this.interpreter = interpreter;

      addField(null, "input", new SSInput(in));
      addField(null, "output", new SSOutput(out));
      addField(null, "error", new SSOutput(err));

      this. methods.add("clone", bb(SSApplication::clone));
      this. methods.add("exit:", bb(SSApplication::exit));
      this. methods.add("load:", bb(SSApplication::load));

      addImmutableField(null, "arguments",
            new SSList(Stream.of(interpreter.args).map(SSString::new).toList()));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSApplication(final Interpreter interpreter) {

      this(interpreter, new BufferedReader(new InputStreamReader(System.in)),
            System.out, System.err);
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
      return stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject load(final Stack stack, final List<SSObject> args) {

      try {
         final var app = (SSApplication) args.get(0);
         return app.interpreter.load(stack, args.get(1).toString());
      } catch (final Exception e) {
         return throwException(stack, args.get(0), e.getMessage());
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
   private final Interpreter interpreter;
}
