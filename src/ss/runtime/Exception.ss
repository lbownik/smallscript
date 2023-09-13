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
Object new addMethod: "withMessage:" :using: 
           {!this !message |
                  Object new addField: "nature"  :withValue: "exception"
                             addField: "message" :withValue: message;
           }
           addMethod: "withCause::andMessage:" :using: 
           {!this !cause !message |
           
                  Object new addField: "nature"  :withValue: "exception"
                             addField: "cause"   :withValue: cause
                             addField: "message" :withValue: message;
           };