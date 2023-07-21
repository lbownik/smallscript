//------------------------------------------------------------------------------
//Copyright 2014 Lukasz Bownik
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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class InterpreterUseCases {

    private Stack stack = Stack.create();

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void nullProperValue_forAllOperations() throws Exception {

        assertResultEquals(SSNull.instance(), "null;");
        assertResultEquals(SSNull.instance(), "null evaluate;");
        assertResultEquals(SSLong.zero(), "null hash;");
        assertResultEquals(SSTrue.instance(), "null equals: null;");
        assertResultEquals(SSFalse.instance(), "null equals: true;");
        assertResultEquals(new SSString("null"), "null asString;");

        assertResultEquals(SSNull.instance(), "null not;");
        assertResultEquals(SSNull.instance(), "null ifTrue: false;");
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void booleanReturnsProperValue_forAllOperations() throws Exception {

        assertResultEquals(SSTrue.instance(), "true;");
        assertResultEquals(SSTrue.instance(), "true evaluate;");
        assertResultEquals(SSFalse.instance(), "true not;");
        assertResultEquals(SSTrue.instance(), "true equals: true;");
        assertResultEquals(SSFalse.instance(), "true equals: false;");
        assertResultEquals(new SSString("true"), "true asString;");

        assertResultEquals(SSTrue.instance(), "true and: true;");
        assertResultEquals(SSFalse.instance(), "true and: false;");
        assertResultEquals(SSNull.instance(), "true and: null;");
        assertResultEquals(SSLong.one(), "true and: 1;");

        assertResultEquals(SSTrue.instance(), "true or: true;");
        assertResultEquals(SSTrue.instance(), "true or: false;");
        assertResultEquals(SSTrue.instance(), "true or: null;");
        assertResultEquals(SSTrue.instance(), "true or: 1;");

        assertResultEquals(SSLong.zero(), "true ifTrue: 0;");
        assertResultEquals(SSNull.instance(), "true ifFalse: 0;");
        assertResultEquals(SSLong.zero(), "true ifTrue: 0 ifFalse: 1;");

        assertResultEquals(SSTrue.instance(), "true;");
    }

    /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void returnsSSLong_forProperExpression() throws Exception {

        assertResultEquals(new SSLong(1), "1;");
        assertResultEquals(new SSLong(3), "1;2;3;");
        assertResultEquals(new SSLong(2), "1 + 1;");
        assertResultEquals(new SSLong(4), "1 + 1; 2 * 2;");
        assertResultEquals(new SSLong(4), "(2 * 1) + 2;");
        assertResultEquals(new SSLong(6), "2 * (1 + 2);");
        assertResultEquals(new SSLong(4), "({2 * 1;} evaluate)+ 2;");
        assertResultEquals(new SSLong(6), "2 * ({1 + 2;} evaluate);");
        assertResultEquals(new SSLong(2), "(2 > 1) ifTrue: 2;");
        assertResultEquals(SSNull.instance(), "(2 > 1) ifFalse: 2;");
        assertResultEquals(new SSLong(2), "(2 > 1) ifTrue: 2 ifFalse: 3;");
        assertResultEquals(new SSLong(7), "((2 > 1) ifTrue: 2 ifFalse: 3) + 5;");
        assertResultEquals(new SSLong(8), "(2 < 1) ifTrue: 2 ifFalse: (3 + 5);");
        assertResultEquals(new SSLong(3), "(2 < 1) ifTrue: 2 ifFalse: 3 evaluate;");
        assertResultEquals(new SSLong(3),
                "((2 < 1) ifTrue: {2;} ifFalse: {3;}) evaluate;");
        assertResultEquals(new SSLong(18), "((2 * 2) + 2) * 3;");
        assertResultEquals(new SSLong(2), ":var = 2;");
        assertResultEquals(new SSLong(2), "true ifTrue: 2;");
        assertResultEquals(new SSLong(3), ":var = 2; var = 3; var;");
    }

    /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void test_block() throws Exception {

        assertResultEquals(new SSLong(2), ":y = 1; {:x | x + 1;} evaluateWith: y;");
        assertResultEquals(new SSLong(3), """
                :a = 1;
                :b = 2;
                {:x1 :x2 | x1 + x2;} evaluateWith: a and: b;
                """);
        assertResultEquals(new SSLong(6), """
                :a = 1;
                :b = 2;
                :c = 3;
                {:x1 :x2 :x3 | (x1 + x2) + x3;} evaluateWith: a and: b and: c;
                """);
        assertResultEquals(new SSLong(10), """
                :a = 1;
                :b = 2;
                :c = 3;
                :d = 4;
                {:x1 :x2 :x3 :x4| (x1 + x2) + (x3 + x4);}
                    evaluateWith: a and: b and: c and: d;
                """);
        assertResultEquals(new SSLong(15), """
                :a = 1;
                :b = 2;
                :c = 3;
                :d = 4;
                :e = 5;
                {:x1 :x2 :x3 :x4 :x5 | ((x1 + x2) + (x3 + x4)) + x5;}
                     evaluateWith: a and: b and: c and: d and: e;
                """);
        assertResultEquals(new SSLong(21), """
                :a = 1;
                :b = 2;
                :c = 3;
                :d = 4;
                :e = 5;
                :f = 6;
                {:x1 :x2 :x3 :x4 :x5 :x6| ((x1 + x2) + (x3 + x4)) + (x5 + x6);}
                   evaluateWith: a and: b and: c and: d and: e and: f;
                """);

    }

    /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void test_object_addMethod() throws Exception {

        assertResultEquals(new SSLong(1), """
                object addMethod: 'a' using: { 1;};
                object a;
                """);
    }

    /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void test_object_addField() throws Exception {

        assertResultEquals(new SSLong(10), """
                object addField: 'a';
                object a: 10;
                object a;
                """);
    }

    /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void test_object_copying() throws Exception {

        assertResultEquals(SSFalse.instance(), """
                :new = (object copy);
                new equals: object;
                """);
    }
    
    /****************************************************************************
    * 
    ****************************************************************************/
    @Test
    public void test_iteration() throws Exception {

        assertResultEquals(new SSLong(10), """
                :counter = 0;
                {counter < 10;} whileTrue: {
                    counter = (counter + 1);
                };
                """);
    }

    /****************************************************************************
     * 
     ***************************************************************************/
    private void assertResultEquals(final SSObject o, final String program)
            throws IOException {

        assertEquals(o, new Interpreter().exacute(program, this.stack));
    }
}
