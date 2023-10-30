!SUnit = application load: "SUnit.ss";
!suite = SUnit Suite new;

suite addTestNamed: "test1" :using: { !assert !fail |

   assert that: 10 :equals: 10;
};

suite addTestNamed: "test2" :using: { !assert !fail |
   
   fail with: "abc";
};

suite run printResultsTo: (application output);