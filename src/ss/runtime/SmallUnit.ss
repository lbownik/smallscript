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
!TestCase = Object new addMethod: "asString" :using: "TestCase";

#-------------------------------------------------------------------------------
TestCase addMethod: "named::using:" :using: {!this !name !block |

	!case = Object new 
	        addField: "result" 
	        addField: "name"   :withValue: name
	        addField: "block"  :withValue: block;
	#----------------------------------------------------------------------------
	case addMethod: "run" :using: {!this |
	     
	     this try: {
	     		this block execute;
	     		this result: "Passed";
	     } :catch: { !e |
	     		this result: ("Failed: " append: (e message));
	     };
	};
};
#-------------------------------------------------------------------------------
# TestSuite
#-------------------------------------------------------------------------------
!TestSuite = Object new addMethod: "asString" :using: "TestSuite"
                        addField: "$TestCase" :withValue: TestCase;

#-------------------------------------------------------------------------------
TestSuite addMethod: "new" :using: { !this |

	!suite = Object new
	         addField: "tests"     :withValue: (List new)
	         addField: "$TestCase" :withValue: (this $TestCase);
   #----------------------------------------------------------------------------
	suite addMethod: "addTestNamed::using:" :using: {!this !name !block |
	       
		  this tests add: (this $TestCase named: name :using: block);
	      this;
	};	 
	#----------------------------------------------------------------------------
	suite addMethod: "$createAssert" :using: {!this |
	
	      Object new addMethod: "that::isEqualTo:" :using: {!this !expected !actual |
	      		 
	      		 expected isEqualTo: actual ifFalse: { 
	      		    Object new addField: "nature"  :withValue: "assertionFailure"
	      		               addField: "cause"   :withValue: this
	      		               addField: "message" :withValue: 
	      		                  ("Expected: " append: (expected asString) 
	      		                                append: " but was: "
	      		                                append: (actual asString))
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
	
		this tests forEach: {!test | test run };
		this;
	};
	#----------------------------------------------------------------------------
	suite addMethod: "printResultsTo:" :using: {!this !output |
	
		output append: "====== Results =======\n";
		this tests forEach: { !test | output append: (test name) append: " - "
		     append: (test result) append: "\n"; } ;
		this;
	};
};
#-------------------------------------------------------------------------------
# ModuleObject
#-------------------------------------------------------------------------------
Object new addField: "Suite" :withValue: TestSuite;

