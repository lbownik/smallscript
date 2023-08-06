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
package ss.parser;

import java.util.regex.Pattern;

import ss.runtime.SSObject;
import ss.runtime.SSVariableReference;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Symbol implements Expression {
   /****************************************************************************
    * 
   ****************************************************************************/
   public Symbol(final String value) {

//      ensureProperSymbol(value);
      this.value = value;
   }
//   /****************************************************************************
//    * 
//   ****************************************************************************/
//   private static void ensureProperSymbol(final String value) {
//
//      if(varDeclaration.matcher(value).matches()) {
//         return ; // proper variable declaration
//      }else if(varReference.matcher(value).matches()) { 
//         return ; // proper variable reference or no-arg method name
//      } else {
//         throw new SyntaxException(value);
//      }
//      
////      final var index = value.indexOf(':');
////      if (index == -1 | index == 0 | index == (value.length() - 1)) {
////         return;
////      } else {
////         throw new RuntimeException("Invalid symbol '" + value + "'.");
////      }
//   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String value() {

      return this.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isVariableDeclaration() {

      return this.value.charAt(0) == '!' && !this.value.contains(":");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isVariableReference() {

      return this.value.charAt(0) != '!' && !this.value.contains(":");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isVariable() {

      return isVariableDeclaration() || isVariableReference();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isMethodWithNoArgs() {

      return this.value.charAt(0) != '!' && !this.value.contains(":");
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isMethodContinuation() {

      return this.value.startsWith(":");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isMethodWithArgs() {

      return this.value.charAt(0) != '!' && this.value.endsWith(":");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isAssignment() {

      return this.value.equals("=");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public boolean isVariableBlockSaperator() {

      return this.value.equals("|");
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return this.value.hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof Symbol s && this.value.equals(s.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject toSSObject() {

      return new SSVariableReference(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final String value;
   
//   private final static Pattern varDeclaration = Pattern.compile("![a-zA-Z][a-zA-Z0-9]*");
//   private final static Pattern varReference = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
}
