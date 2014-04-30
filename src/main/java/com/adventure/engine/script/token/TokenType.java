package com.adventure.engine.script.token;

/**
 * Describes a token type.
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public enum TokenType {
	IDENTIFIER,
	NUMBER,
	STRING,
	OPEN_BRACE,			// {
	CLOSE_BRACE,		// }
	OPEN_PARENTHESES,	// (
	CLOSE_PARENTHESES,	// )
	SEMICOLON,			// ;
	// Do we need to separate operators into their own enum type?
	COMMA_OP,	// ,
	DOT_OP,		// .
	ASSIGN_OP,	// =
	EQ_OP,		// ==
	NOT_OP,		// !
	NEQ_OP,		// !=
	GT_OP,		// >
	LT_OP,		// <
	GET_OP,		// >=
	LET_OP,		// <=
	PLUS_OP,	// +
	MINUS_OP,	// -
	TIMES_OP,	// *
	DIV_OP,		// /
	// For now, all word-based stuff won't be getting their own token; I guess it makes more sense for that syntactic meaning to happen elsewhere
//	AND_OP,		// and
//	OR_OP,		// or
//	XOR_OP,		// xor
//	// Booleans
//	TRUE,		// true
//	FALSE,		// false
//	// Control structures
//	IF,			// if
//	ELSE,		// else
//	// What about keywords; do they fit here?
//	// Having them here leaves them out of the name resolution, which may be what we want
//	VAR,
//	ON,
//	CONNECT,
//	DISCONNECT,
}
