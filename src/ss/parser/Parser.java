package ss.parser;

import static java.util.Arrays.copyOf;

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
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

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
	public Block parse(final String str) throws IOException {

		return parse(new StringReader(str));
	}

	/****************************************************************************
	* 
	****************************************************************************/
	public Block parse(final Reader reader) throws IOException {

		this.reader = reader;
		try {
			this.position = -1;
			final Block result = new Block();

			do {
				final int currentChar = consumeWhitespace(read());
				if (currentChar == -1) {
					break;
				} else if (currentChar == '#') {
					skipComment();
				} else if (currentChar != ';') {
					result.add(parseExpression(currentChar));
				}
			} while (this.recentChar != -1);

			return result;
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
			} else if (currentChar == '}' | currentChar == ')') {
				throwUnexpected(currentChar);
			} else if (currentChar != ';') {
				result.add(parseValue(currentChar));
				currentChar = this.recentChar;
			}
		} while (currentChar != ';');

		return result;
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private Expression parseValue(int currentChar) throws IOException {

		return switch (currentChar) {
		case '|' -> parseVariableBlockSeparator(currentChar);
		case '{' -> parseBlock();
		case '(' -> parseBrackets();
		case '"' -> parseString();
		case '\'' -> parseCharacter();
		case '-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> parseNumber(currentChar);
		default -> parseSymbol(currentChar);
		};
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private Expression parseVariableBlockSeparator(final int currentChar) throws IOException {

		this.recentChar = read();
		return VariableBlockSeparator.instance;
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private Sentence parseBlock() throws IOException {

		final Block result = new Block();

		int currentChar = consumeWhitespace(read());
		while (currentChar != '}') {
			result.add(parseExpression(currentChar));
			
			currentChar = consumeWhitespace(read());
		}
		this.recentChar = read();

		return result;
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private Sentence parseBrackets() throws IOException {

		final Sentence result = new Sentence();

		int currentChar = consumeWhitespace(read());
		while (currentChar != ')') {
			result.add(parseValue(currentChar));
			currentChar = consumeWhitespace(this.recentChar);
		}
		this.recentChar = read();

		return result;
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private Expression parseNumber(int currentChar) throws IOException {

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
			this.recentChar = currentChar;
			return new LongConstant(integer * signum);
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
				this.recentChar = currentChar;
				return new DoubleConstant((integer + decimal) * signum);
			}
			throwUnexpected(currentChar);

		}
		return null;
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private StringConstant parseString() throws IOException {

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

		this.recentChar = read();
		return new StringConstant(new String(this.buffer, 0, this.bufIndex));
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private CharacterConstant parseCharacter() throws IOException {

		final int currentChar = read();
		if (currentChar == -1) {
			throw new EOFException();
		} else {
			final int nextChar = read();
			if (nextChar == -1) {
				throw new EOFException();
			} else if (nextChar != '\'') {
				throwUnexpected(nextChar);
			}
			this.recentChar = read();
			return new CharacterConstant((char) currentChar);
		}
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

		this.recentChar = currentChar;
		return new Symbol(new String(this.buffer, 0, this.bufIndex));
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private void skipComment() throws IOException {

		int chr;
		do {
			chr = read();
		} while (!isEndOfComment(chr));

		this.recentChar = chr;
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

		return chr == ' ' | chr == '\b' | chr == '\f' | chr == '\n' | chr == '\r' | chr == '\t';
	}

	/****************************************************************************
	* 
	****************************************************************************/
	private static boolean isEndOfValue(final int chr) {

		return chr == ' ' | chr == '\t' | chr == '\n' | chr == '\r' | chr == '|' | chr == ')' | chr == '}' | chr == ';';
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
	private Reader reader;
	private int recentChar = -1;
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

			super("Unexpected character '" + character + "' at position " + position + ".");
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
