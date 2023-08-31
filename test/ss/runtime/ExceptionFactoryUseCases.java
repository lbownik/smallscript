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

import org.junit.Test;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class ExceptionFactoryUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void exceptioFactoryHasSetBasicProperties() throws Exception {

      assertSSTrue("Exception size equals: 1;");
      assertSSTrue("Exception nature equals: \"object\";");
      assertSSTrue("Exception asString startsWith: \"object#\";");
      assertSSTrue("Exception hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void factoryCreatesExceptionObjects() throws Exception {

      assertSSTrue("""
            !e = Exception withMessage: "abc";
            (e nature equals: "exception")  and: (e message equals: "abc")
             and: (e method: "cause" equals: null);
            """);
      assertSSTrue("""
            !e = Exception withCause: 'X' :andMessage: "abc";
            (e nature equals: "exception")  and: (e message equals: "abc")
             and: (e cause equals: 'X');
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
