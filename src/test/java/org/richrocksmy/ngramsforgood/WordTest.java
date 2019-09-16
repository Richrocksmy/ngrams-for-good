package org.richrocksmy.ngramsforgood;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WordTest {

    @Test
    public void comparingInputWordWithGreaterProbabilityShouldIndicateGreaterThan() {
        // Given
        Word testWord = new Word("testWord", (float) 0.5);
        Word inputWord = new Word("inputWord", (float) 0.9);

        // When
        int comparisonOutcome = testWord.compareTo(inputWord);

        // Then
        assertThat(comparisonOutcome).isEqualTo(1);
    }

    @Test
    public void comparingWordWithLowerProbabilityShouldIndicateLessThan() {
        // Given
        Word testWord = new Word("testWord", (float) 0.9);
        Word inputWord = new Word("inputWord", (float) 0.5);

        // When
        int comparisonOutcome = testWord.compareTo(inputWord);

        // Then
        assertThat(comparisonOutcome).isEqualTo(-1);
    }

    @Test
    public void comparingWordWithSameProbabilityDifferentWordShouldIndicateGreaterThan() {
        // Given
        Word testWord = new Word("testWord", (float) 0.5);
        Word inputWord = new Word("notTestWord", (float) 0.5);

        // When
        int comparisonOutcome = testWord.compareTo(inputWord);

        // Then
        assertThat(comparisonOutcome).isEqualTo(6);
    }

    @Test
    public void comparingWordWithSameProbabilityDifferentWordShouldIndicateLessThan() {
        // Given
        Word testWord = new Word("notTestWord", (float) 0.5);
        Word inputWord = new Word("testWord", (float) 0.5);

        // When
        int comparisonOutcome = testWord.compareTo(inputWord);

        // Then
        assertThat(comparisonOutcome).isEqualTo(-6);
    }

    @Test
    public void comparingWordWithSameProbabilityWhenSameWordShouldIndicateEquality() {
        // Given
        Word testWord = new Word("testWord", (float) 0.5);
        Word inputWord = new Word("testWord", (float) 0.5);

        // When
        int comparisonOutcome = testWord.compareTo(inputWord);

        // Then
        assertThat(comparisonOutcome).isEqualTo(0);
    }

}