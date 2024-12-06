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
public class SmallUnitUseCases extends UseCaseBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void library_loadsProperly() throws Exception {

      assertSSTrue("""
            !SmallUnit = application load: "SmallUnit.ss";
            !suite = SmallUnit Suite new;
            
            suite addTestNamed: "test1" :using: { 
            
               assert that: 10 :isEqualTo: 10;
            };
            
            suite addTestNamed: "test2" :using: { 
               
               fail with: "abc";
            };
            
            suite run;
            
            (suite tests at: 0 result isEqualTo: "Passed") and:
            (suite tests at: 1 result isEqualTo: "Failed: abc");
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
