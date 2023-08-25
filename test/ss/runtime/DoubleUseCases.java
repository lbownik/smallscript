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

public class DoubleUseCases extends UseCaseBase {

   /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void double_WorksProperly_forAllOperations() throws Exception {

       assertResultEquals(new SSDouble(2.1), "1.0 plus: 1.1;");
       assertResultEquals(new SSDouble(2.0), "1.0 plus: 1;");
       assertResultEquals(new SSDouble(0), "1.0 minus: 1.0;");
       assertResultEquals(new SSDouble(0), "1.0 minus: 1;");
       assertResultEquals(new SSDouble(4.0), "2.0 multipliedBy: 2.0;");
       assertResultEquals(new SSDouble(4.0), "2.0 multipliedBy: 2;");
       assertResultEquals(new SSDouble(3.0), "6.0 dividedBy: 2;");
       assertResultEquals(new SSDouble(3.0), "6 dividedBy: 2.0;");
       try {
          assertSSNull("1.0 plus: true;");
          fail("\"1.0 plus: true;\" should have failed.");
       } catch (RuntimeException e) {
          // good
       }
       assertSSTrue("1.0 equals: 1.0;");
       assertSSFalse("1.0 equals: 1;");
       assertSSFalse("1.0 equals: 2.0;");
       assertSSFalse("1.0 equals: null;");
       assertSSFalse("1.0 isNotEqualTo: 1.0;");
       assertSSTrue("1.0 isNotEqualTo: 2.0;");
       assertSSTrue("1.0 isLessOrEqualTo: 1.0;");
       assertSSTrue("1.0 isLessOrEqualTo: 2.0;");
       assertSSFalse("1.0 isLessOrEqualTo: 0.0;");
       assertSSTrue("2.0 isGreaterOrEqualTo: 1.0;");
       assertSSTrue("2.0 isGreaterOrEqualTo: 2.0;");
       assertSSFalse("0.0 isGreaterOrEqualTo: 2.0;");
       assertSSTrue("1.0 isGreaterThan: 0.0;");
       assertSSFalse("1.0 isGreaterThan: 1.0;");
       assertSSTrue("0.0 isLessThan: 1.0;");
       assertSSFalse("1.0 isLessThan: 1.0;");
       assertResultEquals(new SSString("1.0"), "1.0 asString;");
       assertResultEquals(new SSLong(1072693248), "1.0 hash;");
       assertResultEquals(new SSDouble(1), "1.0 clone;");
       assertResultEquals(new SSDouble(1), "1.0 asDouble;");
       assertResultEquals(new SSLong(1), "1.0 asLong;");
    }
}
