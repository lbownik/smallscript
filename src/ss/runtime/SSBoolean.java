//------------------------------------------------------------------------------
//Copyright 2023 Lukasz Bownik
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//-----------------------------------------------------------------------------
package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
final class SSBoolean {
   /****************************************************************************
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
   ****************************************************************************/
   static SSObject createTrue() {
      
      final var result = new SSDynamicObject();
      
      result.addMethod("and:", newReturnVarBlock("other", listOfThisAndOther));
      result.addMethod("clone", newReturnVarBlock("this", listOfThis));
      result.addMethod("ifFalse:", newReturnVarBlock("null", listOfThisAndfBlock));
      result.addMethod("ifTrue:", newExecuteBlock("tBlock", listOfThisAndtBlock));
      result.addMethod("ifTrue::ifFalse:", newExecuteBlock("tBlock", List.of("this", "tBlock","fBlock")));
      result.addMethod("not", newReturnVarBlock("false", listOfThis));
      result.addMethod("or:", newExecuteBlock("this", listOfThisAndOther));
      result.addMethod("orElse:", newExecuteBlock("other", "not", listOfThisAndOther));
      result.addMethod("asString", newReturnValueBlock(new SSString("true"), listOfThis));
      result.addMethod("hash", newReturnValueBlock(new SSLong(2), listOfThis));
      
      return result;
   }
   /****************************************************************************
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
   ****************************************************************************/
   static SSObject createFalse() {
      
      final var result = new SSDynamicObject();
      
      result.addMethod("and:", newReturnVarBlock("this", listOfThisAndOther));
      result.addMethod("clone", newReturnVarBlock("this", listOfThis));
      result.addMethod("ifFalse:", newExecuteBlock("fBlock", listOfThisAndfBlock));
      result.addMethod("ifTrue:", newReturnVarBlock("null", listOfThisAndtBlock));
      result.addMethod("ifTrue::ifFalse:", newExecuteBlock("fBlock", List.of("this", "tBlock","fBlock")));
      result.addMethod("not", newReturnVarBlock("true", listOfThis));
      result.addMethod("or:", newReturnVarBlock("other", listOfThisAndOther));
      result.addMethod("orElse:", newReturnVarBlock("other", listOfThisAndOther));
      result.addMethod("asString", newReturnValueBlock(new SSString("false"), listOfThis));
      result.addMethod("hash", newReturnValueBlock(new SSLong(1), listOfThis));
      
      return result;
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private static SSBlock newReturnVarBlock(final String varName,
         final List<String> argNames) {

      return new SSBlock(List.of(new SSVariableReference(varName)), argNames);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private static SSBlock newExecuteBlock(final String varName,
         final List<String> argNames) {

      return newExecuteBlock(varName, "execute", argNames);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private static SSBlock newExecuteBlock(final String varName, final String method,
         final List<String> argNames) {

      return new SSBlock(
            List.of(new SSExpression(new SSVariableReference(varName), method)),
            argNames);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private static SSBlock newReturnValueBlock(final SSObject value,
         final List<String> argNames) {

      return new SSBlock(List.of(value), argNames);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private final static List<String> listOfThis = List.of("this");
   private final static List<String> listOfThisAndOther = List.of("this", "other");
   private final static List<String> listOfThisAndtBlock = List.of("this", "tBlock");
   private final static List<String> listOfThisAndfBlock = List.of("this", "fBlock");
}
