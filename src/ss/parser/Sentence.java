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
//-----------------------------------------------------------------------------
package ss.parser;

import static java.util.Collections.emptyList;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import ss.runtime.SSExistingVariableAssignment;
import ss.runtime.SSExpression;
import ss.runtime.SSNewVariableAssignment;
import ss.runtime.SSNull;
import ss.runtime.SSObject;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Sentence extends ArrayList<Expression> implements Expression {
   /****************************************************************************
    * 
   ****************************************************************************/
   private OptionalInt findVariableBlockSaperator() {

      return range(0, size())
            .filter(i -> get(i) instanceof Symbol s && s.isVariableBlockSaperator())
            .findFirst();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   List<String> trimArgumentsDeclarations() {

      final var index = findVariableBlockSaperator();
      if (index.isPresent()) {
         final var result = getArgumentsNames(index.getAsInt());
         removeRange(0, index.getAsInt() + 1);
         return result;
      } else {
         return emptyList();
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private List<String> getArgumentsNames(final int size) {

      return stream().limit(size).map(e -> e.toString().substring(1)).toList();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject toSSObject() {

      switch (size()) {
         case 0:
            return SSNull.instance();
         case 1:
            return get(0).toSSObject();
         case 2:
            if (isAssignment()) {
               throw new RuntimeException("Syntax error. Missing assignment value.");
            } else {
               return new SSExpression(get(0).toSSObject(), get(1).toString());
            }
         default:
            if (isAssignment()) {
               if (((Symbol) get(0)).isVariableDeclaration()) {
                  return new SSNewVariableAssignment(
                        get(0).toString().substring(1),
                        subSentence(2).toSSObject());
               } else {
                  return new SSExistingVariableAssignment(get(0).toString(),
                        subSentence(2).toSSObject());
               }

            } else {
               return createExpression(get(0).toSSObject(), 1);
            }
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private boolean isAssignment() {

      return get(0) instanceof Symbol s0 && s0.isVariable()
            && get(1) instanceof Symbol s1 && s1.isAssignment();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private Sentence subSentence(final int from) {

      final var sentence = new Sentence();
      stream().skip(from).forEach(sentence::add);

      return sentence;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject createExpression(final SSObject subject, int index) {

      if (index == size()) {
         return subject;
      } else if (get(index) instanceof Symbol s) {
         if (s.isMethodWithNoArgs()) {
            return createExpression(new SSExpression(subject, s.toString()),
                  index + 1);
         } else if (s.isMethodWithArgs()) {
            final StringBuilder methodName = new StringBuilder(s.toString());
            final ArrayList<SSObject> args = new ArrayList<>();
            args.add(get(++index).toSSObject());
            while (++index < size()) {
               if (get(index) instanceof Symbol ns) {
                  if (ns.isMethodContinuation()) {
                     methodName.append(ns.toString());
                     if (ns.isMethodWithArgs()) {
                        if (++index < size()) {
                           args.add(get(index).toSSObject());
                        } else {
                           throw new RuntimeException(
                                 "Syntax error: unfinished expression.");
                        }
                     }
                  } else {
                     break;
                  }
               } else {
                  throw new RuntimeException(
                        "Syntax error: " + subject + "[" + get(index) + "]");
               }
            }
            return createExpression(
                  new SSExpression(subject, methodName.toString(), args), index);
         } else {
            return null;
         }
      } else {
         throw new RuntimeException(
               "Syntax error: " + subject + "[" + get(index) + "]");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
}
