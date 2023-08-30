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
!null = Object new;
#-------------------------------------------------------------------------------
null addField: "nature" :withValue: "null";
#-------------------------------------------------------------------------------
null addMethod: "asString" :using: "null";
#-------------------------------------------------------------------------------
null addMethod: "hash" :using: 0;
#-------------------------------------------------------------------------------
null addMethod: "size" :using: 0;
#-------------------------------------------------------------------------------
null addMethod: "doesNotUnderstand:" :using: {};
#-------------------------------------------------------------------------------
null removeMethod: "addField:"
     removeMethod: "addField::withValue:"
     removeMethod: "addMethod::using:"
     removeMethod: "at:"
     removeMethod: "clone"
     removeMethod: "forEach:"
     removeMethod: "throw"
     removeMethod: "try::catch:"
     removeMethod: "removeMethod:";