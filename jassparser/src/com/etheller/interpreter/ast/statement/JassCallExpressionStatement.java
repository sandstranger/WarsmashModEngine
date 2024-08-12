package com.etheller.interpreter.ast.statement;

import com.etheller.interpreter.ast.expression.JassExpression;

public class JassCallExpressionStatement implements JassStatement {
	private final JassExpression expression;

	public JassCallExpressionStatement(final JassExpression expression) {
		this.expression = expression;
	}

	@Override
	public <T> T accept(final JassStatementVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public JassExpression getExpression() {
		return this.expression;
	}
}