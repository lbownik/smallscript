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
Object new addMethod: "and:"     :using: {!this !other | other }
           addMethod: "clone"    :using: {!this | this }
           addMethod: "ifFalse:" :using: {!this !fBlock | null }
           addMethod: "ifTrue:"  :using: {!this !tBlock | tBlock execute }
           addMethod: "ifTrue::ifFalse:" :using: 
                                         {!this !tBlock !fBlock | tBlock execute }
           addMethod: "not"      :using: {!this | false }
           addMethod: "or:"      :using: {!this !other | this }
           addMethod: "orElse:"  :using: {!this !other | other not }
           addMethod: "asString" :using: {!this | "true" }
           addMethod: "hash"     :using: {!this | 2 };
           
           