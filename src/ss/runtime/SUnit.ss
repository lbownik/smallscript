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
# TestCase
#-------------------------------------------------------------------------------
!TestCase = Object new;

#-------------------------------------------------------------------------------
TestCase addMethod: "named::using:" :using: {!this !name !block |

	!case = Object new 
	        addField: "result" 
	        addField: "name"   :withValue: name
	        addField: "block"  :withValue: block;
	#----------------------------------------------------------------------------
	case addMethod: "runWith::and:" :using: {!this !assert !fail |
	     
	     this try: {
	     		this block executeWith: assert :and: fail;
	     		this result: "Passed";
	     } :catch: {!exception |
	     		this result: ("Failed: " append: (exception message));
	     };
	};
};
#-------------------------------------------------------------------------------
# TestSuite
#-------------------------------------------------------------------------------
!TestSuite = Object new;

#-------------------------------------------------------------------------------
TestSuite addMethod: "new" :using: {

	!suite = Object new
	         addImmutableField: "tests" :withValue: (List new);
   #----------------------------------------------------------------------------
	suite addMethod: "addTestNamed::using:" :using: {!this !name !block |
	       
			this tests add: (TestCase named: name :using: block);
	      this;
	};	 
	#----------------------------------------------------------------------------
	suite addMethod: "$createAssert" :using: {!this |
	
	      Object new addMethod: "that::equals:" :using: {!this !expected !actual |
	      		 
	      		 expected equals: actual ifFalse: { 
	      		    Object new addField: "nature"  :withValue: "assertionFailure"
	      		               addField: "cause"   :withValue: this
	      		               addField: "message" :withValue: 
	      		                  ("Expected: " append: (expected asString) 
	      		                               append: "but was: "
	      		                               append:  (actual asString))
	      		               throw;
	      		 };
	      };
	};   
	#----------------------------------------------------------------------------
	suite addMethod: "$createFail" :using: {!this |
	
	      Object new addMethod: "with:" :using: {!this !message |
	      		 
	      		 Object new addField: "nature"  :withValue: "assertionFailure"
	      		            addField: "cause"   :withValue: this
	      		            addField: "message" :withValue: message
	      		            throw;
	      };
	};
	#----------------------------------------------------------------------------
	suite addMethod: "run" :using: {!this |
	
		!assert = this $createAssert;
		!fail   = this $createFail;
	
		this tests forEach: {!test | test runWith: assert :and: fail };
	};
};
#-------------------------------------------------------------------------------
# ModuleObject
#-------------------------------------------------------------------------------
Object new addField: "Suite" :withValue: TestSuite;

