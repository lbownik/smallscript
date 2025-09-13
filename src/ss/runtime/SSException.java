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

import static java.util.Collections.emptyList;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class SSException {
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
   public static SSObject newFactory(final Heap heap) {

      final var result = heap.newObject();

      final List<SSObject> exceptionArg = List.of(heap.newString("nature"),
            heap.newString("exception"));
      final List<SSObject> messageArg = List.of(heap.newString("message"),
            heap.newVariableReference("message"));
      final List<SSObject> causeArg = List.of(heap.newString("cause"),
            heap.newVariableReference("cause"));

      final var newObject = heap.newExpression(heap.newVariableReference("Object"), "new",
            emptyList());
      final var addNature = heap.newExpression(newObject, "addField::withValue:",
            exceptionArg);
      final var addMessage = heap.newExpression(addNature, "addField::withValue:",
            messageArg);
      final var addCause = heap.newExpression(addMessage, "addField::withValue:",
            causeArg);

      result.addMethod("withMessage:",
            heap.newBlock(List.of(addMessage), listOfThisAndMessage));
      result.addMethod("withCause::andMessage:",
            heap.newBlock(List.of(addCause), listOfThisCauseAndMessage));

      return result;
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   private final static List<String> listOfThisAndMessage = List.of("this", "message");
   private final static List<String> listOfThisCauseAndMessage = List.of("this", "cause","message");
}
