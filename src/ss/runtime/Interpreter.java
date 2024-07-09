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

import static java.lang.System.out;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import static ss.runtime.Stack.*;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Interpreter {
   /****************************************************************************
    * 
   ****************************************************************************/
   public Interpreter() {

      this(new String[0]);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public Interpreter(final String[] args) {

      this.args = args;
      stack.addVariable(OBJECT, new SSDynamicObject.Factory());
      stack.addVariable(NULL, SSNull.instance());
      stack.addVariable(TRUE, load(this.stack, "True.ss"));
      stack.addVariable(FALSE, load(this.stack, "False.ss"));
      stack.addVariable(LIST, new SSList.Factory());
      stack.addVariable(MAP, new SSMap.Factory());
      stack.addVariable(SET, new SSSet.Factory());
      stack.addVariable(EXCEPTION, load(this.stack, "Exception.ss"));
      stack.addVariable(APPLICATION, new SSApplication(this));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject execute(final String program) throws IOException {

      return this.parser.parse(program).execute(this.stack);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject execute(final Reader program) throws IOException {

      return this.parser.parse(program).execute(this.stack);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   Stack getStack() {

      return this.stack;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject load(final Stack stack, final String resourceName) {

      try (final var reader = getResource(resourceName)) {

         return this.parser.parse(reader).execute(stack);
      } catch (final Exception e) {
         throw new RuntimeException(e);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private Reader getResource(final String resourceName) throws Exception {

      return new InputStreamReader(getClass().getResourceAsStream(resourceName),
            "utf-8");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public void repl() throws IOException {

      final var console = System.console();
      if (console != null) {
         out.print(">");
         var line = console.readLine();
         while (line != null) {
            try {
               out.println(this.parser.parse(line).execute(stack));
            } catch (final Exception e) {
               out.println(e.getMessage());
            }
            out.print(">");
            line = console.readLine();
         }
      } else {
         out.println("No Console found.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public static void main(String[] args) throws Exception {

      if (args.length == 0) {
         new Interpreter(args).repl();
      } else {
         new Interpreter(args).execute(Files.newBufferedReader(
               Paths.get(args[0]).toAbsolutePath()));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Parser parser = new Parser();
   private final Stack stack = Stack.create();
   final String[] args;
}
