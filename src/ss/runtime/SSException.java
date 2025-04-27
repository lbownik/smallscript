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
final class SSException {
   /****************************************************************************
    Object new addMethod: "withMessage:" :using: 
           {!this !message |
                  Object new addField: "nature"  :withValue: "exception"
                             addField: "message" :withValue: message;
           }
           addMethod: "withCause::andMessage:" :using: 
           {!this !cause !message |
           
                  Object new addField: "nature"  :withValue: "exception"
                             addField: "message" :withValue: message
                             addField: "cause"   :withValue: cause;
           };
    ****************************************************************************/
   static SSObject createException() {

      final var result = new SSDynamicObject();

      final List<SSObject> exceptionArg = List.of(new SSString("nature"),
            new SSString("exception"));
      final List<SSObject> messageArg = List.of(new SSString("message"),
            new SSVariableReference("message"));
      final List<SSObject> causeArg = List.of(new SSString("cause"),
            new SSVariableReference("cause"));

      final var newObject = new SSExpression(new SSVariableReference("Object"),
            "new");
      final var addNature = new SSExpression(newObject, "addField::withValue:",
            exceptionArg);
      final var addMessage = new SSExpression(addNature, "addField::withValue:",
            messageArg);
      final var addCause = new SSExpression(addMessage, "addField::withValue:",
            causeArg);

      result.addMethod("withMessage:",
            new SSBlock(List.of(addMessage), listOfThisAndMessage));
      result.addMethod("withCause::andMessage:",
            new SSBlock(List.of(addCause), listOfThisCauseAndMessage));

      return result;
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private final static List<String> listOfThisAndMessage = List.of("this", "message");
   private final static List<String> listOfThisCauseAndMessage = List.of("this", "cause","message");
}
