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

import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class ExceptionFactory extends SSDynamicObject {

   public ExceptionFactory() {

      addField("nature", SSDynamicObject.nature);
      
      addBinaryMethod("withMessage:", ExceptionFactory::withMessage);
      addBinaryMethod("withCause::andMessage:", ExceptionFactory::withCauseAndMessage);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject withMessage(final Stack stack, final List<SSObject> args) {

      final var result = new SSDynamicObject();
      result.addField("nature", nature);
      result.addField("message", args.get(1));
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject withCauseAndMessage(final Stack stack,
         final List<SSObject> args) {

      final var result = new ExceptionFactory();
      result.addField("nature", nature);
      result.addField("cause", args.get(1));
      result.addField("message", args.get(2));
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final static SSString nature = new SSString("exception");
}
