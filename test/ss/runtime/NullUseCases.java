//------------------------------------------------------------------------------
//Copyright 2014 Lukasz Bownik
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class NullUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void null_WorksProperly_forAllOperations() throws Exception {

      assertSSNull("null;");
      assertSSNull("null evaluate;");
      assertResultEquals(new SSLong(0), "null hash;");
      assertSSTrue("null equals: null;");
      assertSSFalse("null isNotEqualTo: null;");
      assertSSFalse("null equals: true;");
      assertSSTrue("null isNotEqualTo: true;");
      assertResultEquals(new SSString("null"), "null asString;");

      assertSSNull("null not;");
      assertSSNull("null ifTrue: false;");
      assertSSNull("null execute;");

      assertResultEquals(new SSLong(0), "null size;");
      // assertResultEquals(new SSLong(0), "null at: 0;");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
