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
//------------------------------------------------------------------------------
package ss.runtime;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertSSNull(final String program) throws IOException {

      assertResultEquals(SSNull.instance(), program);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertSSFalse(final String program) throws IOException {

      assertResultEquals(new SSFalse(), program);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertSSTrue(final String program) throws IOException {

      assertResultEquals(new SSTrue(), program);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertResultEquals(final SSObject o, final String program)
         throws IOException {

      assertEquals(o, new Interpreter().exacute(program, this.stack));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected SSObject execute(final String program)
         throws IOException {

      return new Interpreter().exacute(program, this.stack);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected Stack stack = Stack.create();
}
