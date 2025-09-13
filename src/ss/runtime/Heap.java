//------------------------------------------------------------------------------
//Copyright 2023 Lukasz Bownik
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

import static java.util.Collections.emptyList;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class Heap {
   
   /****************************************************************************
    * 
   ****************************************************************************/
   public Heap() {

      this.objectSharedMethods = SSDynamicObject.putMethods(this,
            new MethodMap());
      this.blockSharedMethods = SSBlock.putMethods(this, newDerivedMethodsMap());
      this.doubleSharedMethods = SSDouble.putMethods(this, newDerivedMethodsMap());
      this.listSharedMethods = SSList.putMethods(this, newDerivedMethodsMap());
      this.longSharedMethods = SSLong.putMethods(this, newDerivedMethodsMap());
      this.mapSharedMethods = SSMap.putMethods(this, newDerivedMethodsMap());
      this.setSharedMethods = SSSet.putMethods(this, newDerivedMethodsMap());
      this.streamSharedMethods = SSStream.putMethods(this, newDerivedMethodsMap());
      this.stringSharedMethods = SSString.putMethods(this, newDerivedMethodsMap());

   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private MethodMap newDerivedMethodsMap() {
      return new MethodMap(this.objectSharedMethods, true);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSNull newNull() {

      return new SSNull(this);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDynamicObject newObject() {

      return new SSDynamicObject(this, this.objectSharedMethods);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBlock newBlock(final List<SSObject> statements,
         final List<String> argumentNames) {

      return new SSBlock(this, this.blockSharedMethods, statements, argumentNames);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock newBinaryBlock(final SSBinaryBlock.Code code,
         final List<String> argumentNames) {

      return new SSBinaryBlock(code, this, argumentNames);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSBinaryBlock newBinaryBlock(final SSBinaryBlock.Code code) {

      return new SSBinaryBlock(code, this, emptyList());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSString newString(final String value) {

      return new SSString(this, this.stringSharedMethods, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSLong newLong(final long value) {

      return new SSLong(this, this.longSharedMethods, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDouble newDouble(final double value) {

      return new SSDouble(this, this.doubleSharedMethods, value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSList newList() {

      return new SSList(this, this.listSharedMethods);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSList newList(final Stream<? extends SSObject> elements) {

      final var result = newList();
      elements.forEach(result::add);
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSSet newSet() {

      return new SSSet(this, this.setSharedMethods);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSSet newSet(final Stream<? extends SSObject> elements) {

      final var result = newSet();
      elements.forEach(result::add);
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSMap newMap(final Map<SSObject, SSObject> elements) {

      return new SSMap(this, this.mapSharedMethods, elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSStream newStream(final Stream<SSObject> stream) {

      return new SSStream(this, this.streamSharedMethods, stream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSVariableReference newVariableReference(final String variableName) {

      return new SSVariableReference(variableName);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSNewVariableAssignment newNewVariableAssignment(final String variableName,
         final SSObject arg) {

      return new SSNewVariableAssignment(variableName, arg);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSExistingVariableAssignment newExistingVariableAssignment(
         final String variableName, final SSObject arg) {

      return new SSExistingVariableAssignment(variableName, arg);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSExpression newExpression(final SSObject object, final String method,
         final List<SSObject> args) {

      return new SSExpression(object, method, args);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public SSClosure newClosure(final Stack stack, final SSNativeObject target) {

      return new SSClosure(stack, target);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   public SSInput newInput(final BufferedReader reader) {

      return new SSInput(this, this.objectSharedMethods, reader);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   public SSOutput newOutput(final PrintStream out) {

      return new SSOutput(this, this.objectSharedMethods, out);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public SSBlock newReturnVarBlock(final String varName,
         final List<String> argNames) {

      return newBlock(List.of(newVariableReference(varName)), argNames);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public SSBlock newExecuteBlock(final String varName,
         final List<String> argNames) {

      return newExecuteBlock(varName, "execute", argNames);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   public SSBlock newExecuteBlock(final String varName, final String method,
         final List<String> argNames) {

      return newBlock(
            List.of(
                  newExpression(newVariableReference(varName), method, emptyList())),
            argNames);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   public SSBlock newReturnValueBlock(final SSObject value,
         final List<String> argNames) {

      return newBlock(List.of(value), argNames);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private final MethodMap objectSharedMethods;
   private final MethodMap blockSharedMethods;
   private final MethodMap doubleSharedMethods;
   private final MethodMap listSharedMethods;
   private final MethodMap longSharedMethods;
   private final MethodMap mapSharedMethods;
   private final MethodMap setSharedMethods;
   private final MethodMap streamSharedMethods;
   private final MethodMap stringSharedMethods;
}
