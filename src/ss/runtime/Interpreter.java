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
import static java.util.Collections.emptyList;

import java.io.IOException;

import ss.parser.Parser;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Interpreter {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject exacute(final String program, final Stack stack)
         throws IOException {

      return new Parser().parse(program).toSSObject().invoke(stack, "execute");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public void repl() throws IOException {

      final var console = System.console();
      if (console != null) {
         final var stack = Stack.create();
         stack.addVariable("application", new SSApplication());
         final var parser = new Parser();

         out.print(">");
         var line = console.readLine();
         while (line != null) {
            out.println(parser.parse(line).toSSObject().invoke(stack, "execute",
                  emptyList()));
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

      new Interpreter().repl();
   }
}
