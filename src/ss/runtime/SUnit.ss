#-------------------------------------------------------------------------------
#Copyright 2023 Lukasz Bownik
#
#Licensed under the Apache License, Version 2.0 (the "License");
#you may not use this file except in compliance with the License.
#You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#Unless required by applicable law or agreed to in writing, software
#distributed under the License is distributed on an "AS IS" BASIS,
#WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#See the License for the specific language governing permissions and
#limitations under the License.
#-------------------------------------------------------------------------------

#-------------------------------------------------------------------------------
# Test result
#-------------------------------------------------------------------------------
!TestResult = Object new addMethod: "new" :using: {

	Object new
	 		 addImmutableField: "failures" :withValue: (List new)
	       addImmutableField: "errors"   :withValue: (List new)
	       addImmutableField: "passed"   :withValue: (List new);
};
#-------------------------------------------------------------------------------
# TestCase
#-------------------------------------------------------------------------------
!TestCase = Object new addMethod: "new" :using: {

	!result = Object new 
	          addImmutableField: "result" :withValue: (TestResult new);
    
	result addMethod: "assert:" :using: {!this !condition |
	       
	       condition  ifTrue:  { this logSuccess; }
	                 :ifFalse: {
	                 
	                 };
	};
};
#-------------------------------------------------------------------------------
# TestSuite
#-------------------------------------------------------------------------------
!TestSuite = Object new addMethod: "new" :using: {

	!result = Object new
	          addImmutableField: "tests" :using: (Map new);

	result addMethod: "addTestNamed::using:" :using: {!this !name !block |
	       
	       this tests at: name :put: block;
	       this;
	};
};
#-------------------------------------------------------------------------------
# ModuleObject
#-------------------------------------------------------------------------------
Object new addImmutableField: "Suite" :withValue: TestSuite;
