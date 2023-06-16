package ss;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*******************************************************************************
 *
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public abstract class ParserUseCasesBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertUnexpected(final String str, final char unexpectedChar)
           throws IOException {

      try {
         Object result = parse(str);
         fail("Unexpected character failed. Result: ".concat(result.toString()));
      } catch (final Parser.UnexpectedCharacterException e) {
         assertEquals(unexpectedChar, e.character);
      }
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertIllegalArgumentException(final Runnable task)
           throws IOException {

      try {
         task.run();
         fail("IllegalArgumentException failed.");
      } catch (final IllegalArgumentException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertLongEquals(final long expected, final String str)
           throws Exception {

      assertEquals(Long.valueOf(expected), parse(str).get(0).value());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertDoubleEquals(final double expected, final String str)
           throws Exception {

      assertEquals(Double.valueOf(expected), parse(str).get(0).value());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertStringEquals(final String expected, final String str)
           throws Exception {

      assertEquals(expected, parse(str).get(0).value());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertCharacterEquals(final Character expected, final String str)
           throws Exception {

      assertEquals(expected, parse(str).get(0).value());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertListquals(final List expected, final String str)
           throws Exception {

      assertEquals(expected, parse(str));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertMapEquals(final Map expected, final String str)
           throws Exception {

      assertEquals(expected, parse(str));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected Map asMap(final String key, final Object value)
           throws Exception {

      final HashMap result = new HashMap();
      result.put(key, value);
      return result;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected Map asMap(final String key1, final Object value1,
           final String key2, final Object value2)
           throws Exception {

      final HashMap result = new HashMap();
      result.put(key1, value1);
      result.put(key2, value2);
      return result;
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertEmptyList(final String str)
           throws Exception {

      assertTrue(((List) parse(str)).isEmpty());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertEmptySequence(final String str)
           throws Exception {

      assertTrue(((Sequence) parse(str)).isEmpty());
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   protected void assertEOF(final String str) throws IOException {

      try {
         Object result = parse(str);
         fail("EOF failed. Result: ".concat(result.toString()));
      } catch (final EOFException e) {
         assertTrue(true);
      }
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   protected Sequence parse(final String str) throws IOException {

      return new Parser().parse(str);
   }
}