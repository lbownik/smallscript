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
package ss.runtime;

import static java.util.Arrays.copyOf;
import static java.util.Collections.emptyList;
import static java.util.stream.IntStream.range;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Parser {
   /****************************************************************************
    * Creates a new parser.
    ***************************************************************************/
   public Parser() {

      this.buffer = new char[16];
      this.bufferSize = this.buffer.length;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   public SSBlock parse(final String str) throws IOException {

      return parse(new StringReader(str));
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   public SSBlock parse(final Reader reader) throws IOException {

      this.reader = new PushbackReader(reader);
      try {
         this.position = -1;
         final ArrayList<Sentence> sentences = new ArrayList<>();
         int currentChar;

         do {
            currentChar = consumeWhitespace(read());
            if (currentChar == -1) {
               break;
            } else if (currentChar == '#') {
               skipComment();
            } else if (currentChar != ';') {
               sentences.add(parseExpression(currentChar));
            }
         } while (currentChar != -1);

         final List<String> argNames = sentences.isEmpty() ? emptyList()
               : sentences.get(0).trimArgumentsDeclarations();

         return new SSBlock(sentences.stream().map(Supplier::get).toList(),
               argNames);

      } finally {
         this.reader = null;
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Sentence parseExpression(int currentChar) throws IOException {

      final Sentence result = new Sentence();

      do {
         if (isWhitespace(currentChar)) {
            currentChar = consumeWhitespace(read());
         }
         if (currentChar == '#') {
            skipComment();
            currentChar = consumeWhitespace(read());
         } else if (currentChar == '}' | currentChar == ')') {
            throwUnexpected(currentChar);
         } else if (currentChar != ';') {
            result.add(parseValue(currentChar));
            currentChar = consumeWhitespace(read());
         }
      } while (currentChar != ';' & currentChar != '}');
      if (currentChar == '}') {
         unread(currentChar);
      }
      return result;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Supplier<SSObject> parseValue(int currentChar) throws IOException {

      return switch (currentChar) {
         case '{' -> parseBlock();
         case '(' -> parseBrackets();
         case '"' -> parseString();
         case '-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' ->
            parseNumber(currentChar);
         default -> parseSymbol(currentChar);
      };
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Supplier<SSObject> parseBlock() throws IOException {

      final ArrayList<Sentence> sentences = new ArrayList<>();

      int currentChar = consumeWhitespace(read());
      while (currentChar != '}') {
         sentences.add(parseExpression(currentChar));

         currentChar = consumeWhitespace(read());
      }

      final List<String> argNames = sentences.isEmpty() ? emptyList()
            : sentences.get(0).trimArgumentsDeclarations();
      return () -> {
         return new SSBlock(sentences.stream().map(Supplier::get).toList(),
               argNames);
      };
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Sentence parseBrackets() throws IOException {

      final Sentence result = new Sentence();

      int currentChar = consumeWhitespace(read());
      while (currentChar != ')') {
         result.add(parseValue(currentChar));
         currentChar = consumeWhitespace(read());
      }

      return result;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Supplier<SSObject> parseNumber(int currentChar) throws IOException {

      int signum = 1;
      long integer = 0;
      if (currentChar == '-') {
         signum = -1;
         currentChar = read();
         if (currentChar == -1) {
            throw new EOFException();
         }
         if (currentChar == '.') {
            throwUnexpected(currentChar);
         }
         if (isEndOfValue(currentChar)) {
            throwUnexpected(currentChar);
         }
      }
      while (isDigit(currentChar)) {
         integer = 10 * integer + (currentChar - '0');
         currentChar = read();
      }
      if (isEndOfValue(currentChar)) {
         // integer - no exponent
         unread(currentChar);
         final long result = integer * signum;
         return () -> new SSLong(result);
         // return new LongConstant(integer * signum);
      } else if (currentChar == '.') {
         // floating point
         currentChar = read();
         if (currentChar == -1) {
            throw new EOFException();
         }
         if (!isDigit(currentChar)) {
            throwUnexpected(currentChar);
         }
         double decimal = 0.1 * (currentChar - '0');
         double factor = 0.01;
         currentChar = read();
         while (isDigit(currentChar)) {
            decimal += factor * (currentChar - '0');
            factor /= 10;
            currentChar = read();
         }
         // floating point without exponent
         if (isEndOfValue(currentChar)) {
            unread(currentChar);
            final double result = (integer + decimal) * signum;
            return () -> new SSDouble(result);
         }
         throwUnexpected(currentChar);

      }
      return null;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Supplier<SSObject> parseString() throws IOException {

      this.bufIndex = 0;
      int currentChar = read();

      while (currentChar != '"') {
         append(switch (currentChar) {
            case -1 -> throw new EOFException();
            case '\\' -> parseEscapedCharacter();
            default -> (char) currentChar;
         });
         currentChar = read();
      }

      final var result = new String(this.buffer, 0, this.bufIndex).intern();
      return () -> new SSString(result);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private Symbol parseSymbol(int currentChar) throws IOException {

      this.bufIndex = 0;

      do {
         if (currentChar == -1) {
            throw new EOFException();
         } else {
            append((char) currentChar);
            currentChar = read();
         }
      } while (!isEndOfValue(currentChar));

      unread(currentChar);
      return new Symbol(new String(this.buffer, 0, this.bufIndex));
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private int skipComment() throws IOException {

      int chr;

      do {
         chr = read();
      } while (!isEndOfComment(chr));

      return chr;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private char parseEscapedCharacter() throws IOException {

      int currentChar = read();
      return switch (currentChar) {
         case '\\' -> '\\';
         case '"' -> '\"';
         case '/' -> '/';
         case 'b' -> '\b';
         case 'f' -> '\f';
         case 'n' -> '\n';
         case 'r' -> '\r';
         case 't' -> '\t';
         case 'u' -> parseHexadecimalCharacter();
         case -1 -> throw new EOFException();
         default -> throwUnexpected((char) currentChar);
      };
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private char parseHexadecimalCharacter() throws IOException {

      int chr = decimalFromHEX(read()) << 12;
      chr += decimalFromHEX(read()) << 8;
      chr += decimalFromHEX(read()) << 4;
      chr += decimalFromHEX(read());
      return (char) chr;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private int decimalFromHEX(final int currentChar) throws IOException {

      return switch (currentChar) {
         case '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> currentChar - '0';
         case 'A', 'B', 'C', 'D', 'E', 'F' -> currentChar - 'A' + 10;
         case 'a', 'b', 'c', 'd', 'e', 'f' -> currentChar - 'a' + 10;
         case -1 -> throw new EOFException();
         default -> throwUnexpected(currentChar);
      };
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private char throwUnexpected(final int chr) throws IOException {

      throw new UnexpectedCharacterException(this.position, (char) chr);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private static boolean isDigit(final int chr) {

      return chr >= '0' & chr <= '9';
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private int consumeWhitespace(int chr) throws IOException {

      while (isWhitespace(chr)) {
         chr = read();
      }
      return chr;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private static boolean isWhitespace(final int chr) {

      return chr == ' ' | chr == '\b' | chr == '\f' | chr == '\n' | chr == '\r'
            | chr == '\t';
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private static boolean isEndOfValue(final int chr) {

      return chr == ' ' | chr == '\t' | chr == '\n' | chr == '\r' | chr == '|'
            | chr == ')' | chr == '}' | chr == ';';
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private static boolean isEndOfComment(final int chr) {

      return chr == -1 | chr == '\n' | chr == '\r';
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private void append(final char chr) {

      if (this.bufIndex == this.bufferSize) {
         this.bufferSize *= 2;
         this.buffer = copyOf(this.buffer, this.bufferSize);
      }
      this.buffer[this.bufIndex++] = chr;
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private int read() throws IOException {

      ++this.position;
      return this.reader.read();
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private void unread(final int chr) throws IOException {

      --this.position;
      this.reader.unread(chr);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   private PushbackReader reader;
   private int position = 0;
   private char[] buffer;
   private int bufferSize;
   private int bufIndex = 0;
   /****************************************************************************
    * An exception thrown when parser encounters malformed syntax
    ***************************************************************************/
   public final static class UnexpectedCharacterException extends IOException {
      /*************************************************************************
       * 
       * @param position  position of unexpected character.
       * @param character unexpected character value (zero based).
       ************************************************************************/
      UnexpectedCharacterException(final int position, final char character) {

         super("Unexpected character '" + character + "' at position " + position
               + ".");
         this.position = position;
         this.character = character;
      }
      /*************************************************************************
       * 
       ************************************************************************/
      /** Unexpected character value. */
      public final char character;
      /** Position of unexpected character (zero based). */
      public final int position;
   }
   /****************************************************************************
   * 
   ***************************************************************************/
   private final static class StringReader extends Reader {
      /*************************************************************************
       * 
       ************************************************************************/
      StringReader(final String s) {

         this.s = s;
         this.length = s.length();
      }
      /*************************************************************************
       * 
       ************************************************************************/
      @Override
      public int read() throws IOException {

         return this.pos < this.length ? this.s.charAt(this.pos++) : -1;
      }
      /*************************************************************************
       * 
       ************************************************************************/
      public int read(final char[] cbuf, int off, final int len) throws IOException {

         return -1; // not implemented
      }
      /*************************************************************************
       * 
       ************************************************************************/
      public void close() throws IOException {
      }
      /*************************************************************************
       * 
       ************************************************************************/
      private final String s;
      private int pos = 0;
      private final int length;
   }
}

/*******************************************************************************
 * 
 ******************************************************************************/
final class Symbol implements Supplier<SSObject> {
   /****************************************************************************
    * 
   ****************************************************************************/
   public Symbol(final String value) {

      this.value = value;
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
   public SSObject get() {

      return new SSVariableReference(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final String value;
}

/*******************************************************************************
 * 
 ******************************************************************************/
final class Sentence extends ArrayList<Supplier<SSObject>>
      implements Supplier<SSObject> {
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
   @Override
   public SSObject get() {

      switch (size()) {
         case 0:
            return new SSVariableReference("null");
         case 1:
            return get(0).get();
         case 2:
            if (isAssignment()) {
               throw new RuntimeException("Syntax error. Missing assignment value.");
            } else {
               return new SSExpression(get(0).get(), get(1).toString());
            }
         default:
            if (isAssignment()) {
               if (((Symbol) get(0)).isVariableDeclaration()) {
                  return new SSNewVariableAssignment(get(0).toString().substring(1),
                        subSentence(2).get());
               } else {
                  return new SSExistingVariableAssignment(get(0).toString(),
                        subSentence(2).get());
               }
            } else {
               return createExpression(get(0).get(), 1);
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
            args.add(get(++index).get());
            while (++index < size()) {
               if (get(index) instanceof Symbol ns) {
                  if (ns.isMethodContinuation()) {
                     methodName.append(ns.toString());
                     if (ns.isMethodWithArgs()) {
                        if (++index < size()) {
                           args.add(get(index).get());
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
