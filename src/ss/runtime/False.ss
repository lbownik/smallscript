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
Object new addMethod: "and:"     :using: {!this !other | this }
           addMethod: "clone"    :using: {!this | this }
           addMethod: "ifFalse:" :using: {!this !fBlock | fBlock execute }
           addMethod: "ifTrue:"  :using: {!this !tBlock | null }
           addMethod: "ifTrue::ifFalse:" :using: 
                                         {!this !tBlock !fBlock | fBlock execute }
           addMethod: "not"      :using: {!this | true }
           addMethod: "or:"      :using: {!this !other | other }
           addMethod: "orElse:"  :using: {!this !other | other }
           addMethod: "asString" :using: {!this | "false" }
           addMethod: "hash"     :using: {!this | 1 };
           
           