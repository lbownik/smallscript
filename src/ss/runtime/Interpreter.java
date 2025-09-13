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
import static ss.runtime.Stack.APPLICATION;
import static ss.runtime.Stack.EXCEPTION;
import static ss.runtime.Stack.FALSE;
import static ss.runtime.Stack.LIST;
import static ss.runtime.Stack.MAP;
import static ss.runtime.Stack.NULL;
import static ss.runtime.Stack.OBJECT;
import static ss.runtime.Stack.SET;
import static ss.runtime.Stack.TRUE;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
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

      this.heap = new Heap();
      this.parser = new Parser(this.heap);
      this.args = args;

      this.stack = Stack.create();
      stack.addVariable(OBJECT, SSDynamicObject.newFactory(this.heap));
      stack.addVariable(NULL, this.heap.newNull());
      stack.addVariable(TRUE, SSBoolean.newTrue(this.heap));
      stack.addVariable(FALSE, SSBoolean.newFalse(this.heap));
      stack.addVariable(LIST, SSList.newFactory(this.heap));
      stack.addVariable(MAP, SSMap.newFactory(this.heap));
      stack.addVariable(SET, SSSet.newFactory(this.heap));
      stack.addVariable(EXCEPTION, SSException.newFactory(this.heap));
      stack.addVariable(APPLICATION,
            SSApplication.newApplication(this.heap, args, this::load));
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
   Heap getHeap() {

      return this.heap;
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
               out.println(this.parser.parse(line).execute(this.stack));
            } catch (final Exception e) {
               if (e instanceof AuxiliaryException ae) {
                  out.println(ae.getMessage(this.stack));
               } else {
                  out.println(e.getMessage());
               }
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
         new Interpreter(args).execute(
               Files.newBufferedReader(Paths.get(args[0]).toAbsolutePath()));
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Stack stack;
   private final Heap heap;
   private final Parser parser;
   final String[] args;
}
