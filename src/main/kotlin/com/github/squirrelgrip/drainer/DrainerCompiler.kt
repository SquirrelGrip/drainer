package com.github.squirrelgrip.drainer

import DrainerBaseVisitor
import DrainerLexer
import DrainerParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream


class DrainerCompiler {

    fun compile(input: String): (Set<String>) -> Boolean {
        val charStream = CharStreams.fromString(input)
        val lexer = DrainerLexer(charStream)
        val tokens = CommonTokenStream(lexer)
        val parser = DrainerParser(tokens)
        val visitor: DrainerBaseVisitor<(Set<String>) -> Boolean> = object : DrainerBaseVisitor<(Set<String>) -> Boolean>() {
            override fun visitAndExpression(ctx: DrainerParser.AndExpressionContext): (Set<String>) -> Boolean = {
                visit(ctx.expression(0)).invoke(it) && visit(ctx.expression(1)).invoke(it)
            }

            override fun visitOrExpression(ctx: DrainerParser.OrExpressionContext): (Set<String>) -> Boolean = {
                visit(ctx.expression(0)).invoke(it) || visit(ctx.expression(1)).invoke(it)
            }

            override fun visitNotExpression(ctx: DrainerParser.NotExpressionContext): (Set<String>) -> Boolean = {
                !visit(ctx.expression()).invoke(it)
            }

            override fun visitVariableExpression(ctx: DrainerParser.VariableExpressionContext): (Set<String>) -> Boolean = {
                ctx.variable().text in it
            }

            override fun visitParenExpression(ctx: DrainerParser.ParenExpressionContext): (Set<String>) -> Boolean = {
                visit(ctx.expression()).invoke(it)
            }

            override fun visitPredicate(ctx: DrainerParser.PredicateContext): (Set<String>) -> Boolean = {
                visit(ctx.expression()).invoke(it)
            }
        }
        return visitor.visit(parser.predicate())
    }

}