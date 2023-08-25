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

import static org.junit.Assert.fail;

import org.junit.Test;

public class LongUseCases extends UseCaseBase {

   /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void long_WorksProperly_forAllOperations() throws Exception {

       assertResultEquals(new SSLong(1), "1;");
       assertResultEquals(new SSLong(2), "1 plus: 1;");
       assertResultEquals(new SSDouble(2), "1 plus: 1.0;");
       assertResultEquals(new SSLong(0), "1 minus: 1;");
       assertResultEquals(new SSDouble(0), "1 minus: 1.0;");
       assertResultEquals(new SSLong(4), "2 multipliedBy: 2;");
       assertResultEquals(new SSDouble(4), "2 multipliedBy: 2.0;");
       assertResultEquals(new SSLong(3), "6 dividedBy: 2;");
       assertResultEquals(new SSDouble(3), "6 dividedBy: 2.0;");
       try {
          assertSSNull("1 plus: true;");
          fail("\"1 plus: true;\" should have failed.");
       } catch (RuntimeException e) {
          // good
       }
       assertSSTrue("1 equals: 1;");
       assertSSFalse("1 equals: 1.0;");
       assertSSFalse("1 equals: 2;");
       assertSSFalse("1 equals: null;");
       assertSSFalse("1 isNotEqualTo: 1;");
       assertSSTrue("1 isNotEqualTo: 1.0;");
       assertSSTrue("1 isNotEqualTo: 2;");
       assertSSTrue("1 isNotEqualTo: null;");
       assertSSTrue("1 isLessOrEqualTo: 1;");
       assertSSTrue("1 isLessOrEqualTo: 2;");
       assertSSFalse("1 isLessOrEqualTo: 0;");
       assertSSTrue("2 isGreaterOrEqualTo: 1;");
       assertSSTrue("2 isGreaterOrEqualTo: 2;");
       assertSSFalse("0 isGreaterOrEqualTo: 2;");
       assertSSTrue("1 isGreaterThan: 0;");
       assertSSFalse("1 isGreaterThan: 1;");
       assertSSTrue("0 isLessThan: 1;");
       assertSSFalse("1 isLessThan: 1;");
       assertResultEquals(new SSString("1"), "1 asString;");
       assertResultEquals(new SSLong(1), "1 clone;");
       assertResultEquals(new SSLong(1), "1 asLong;");
       assertResultEquals(new SSDouble(1), "1 asDouble;");
    }
}
