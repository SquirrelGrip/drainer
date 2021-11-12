package com.github.squirrelgrip.drainer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DrainerCompilerTest {

    val testSubject = DrainerCompiler()
    val setOfA = setOf("A")
    val setOfB = setOf("B")
    val setOfC = setOf("C")
    val setOfAB = setOf("A", "B")

    @Test
    fun compile_GivenSingleVariable() {
        val compile = testSubject.compile("A")
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAB)).isTrue
    }

    @Test
    fun compile_GivenParamSingleVariable() {
        val compile = testSubject.compile("(A)")
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAB)).isTrue
    }

    @Test
    fun compile_GivenParamNotSingleVariable() {
        val compile = testSubject.compile("(!A)")
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAB)).isFalse
    }

    @Test
    fun compile_GivenNotSingleVariable() {
        val compile = testSubject.compile("!A")
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isTrue
        assertThat(compile.invoke(setOfAB)).isFalse
    }

    @Test
    fun compile_GivenOrVariable() {
        val compile = testSubject.compile("A|B")
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isTrue
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAB)).isTrue
    }

    @Test
    fun compile_GivenAndVariable() {
        val compile = testSubject.compile("A&B")
        assertThat(compile.invoke(setOfA)).isFalse
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAB)).isTrue
    }
    @Test
    fun compile_GivenAndNotVariable() {
        val compile = testSubject.compile("A&!B")
        assertThat(compile.invoke(setOfA)).isTrue
        assertThat(compile.invoke(setOfB)).isFalse
        assertThat(compile.invoke(setOfC)).isFalse
        assertThat(compile.invoke(setOfAB)).isFalse
    }
}