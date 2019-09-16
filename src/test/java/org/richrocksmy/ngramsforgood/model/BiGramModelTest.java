package org.richrocksmy.ngramsforgood.model;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class BiGramModelTest {

    @Test
    public void shouldThrowExceptionWhenInputListIsNotTwoWordsOrGreater() {
        // Given
        List<String> words = singletonList("word");

        // When / Then
        assertThatThrownBy(() -> new BiGramModel(words)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldPredictNextWorkGivenTextOne() {
        // Given
        List<String> words = convertStringToList("This is some text about cows. It's not very interesting but it's definitely about different "
                                                                 + "cow breeds. There's many types of cow breed around the world. Cows come in all "
                                                                 + "shapes and sizes. The main cow breed in the UK is a Jersey cow.");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord("cow");

        // Then
        assertThat(nextWord).isEqualTo("breed");
    }

    @Test
    public void shouldPredictNextWorkGivenTextTwo() {
        // Given
        List<String> words = convertStringToList("When constructing a house many different things are required. In the UK a typical "
                                                                 + "construction would be a brick house. However, there are other materials - a "
                                                                 + "wooden house, a concrete house. The advantage of a brick house is that bricks are"
                                                                 + "relatively inexpensive to make and purchase and a brick house is very strong and "
                                                                 + "regulates temperature well. A wooden house doesn't perform quite as well, there "
                                                                 + "are also other concerns: a brick is much more resistant to fire.");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord("brick");

        // Then
        assertThat(nextWord).isEqualTo("house");
    }

    @Test
    public void shouldPredictNextWorkGivenTextThree() {
        // Given
        List<String> words = convertStringToList("Tyger Tyger burning bright In the forests of the night What immortal hand or eye Could frame"
                                                                 + "thy fearful symmetry In what distant deeps or skies Burnt the fire of thine eyes"
                                                                 + "On what wings dare he aspire What the hand dare seize the fire And what shoulder"
                                                                 + "what art Could twist the sinews of thy heart And when thy heart began to beat"
                                                                 + "What dread hand what dread feet What the hammer what the chain In what furnace was"
                                                                 + " thy brain What the anvil what dread grasp Dare its deadly terrors clasp When the"
                                                                 + " stars threw down their spears And waterd heaven with their tears Did he smile his"
                                                                 + " work to see Did he who made the Lamb make thee Tyger Tyger burning bright In the"
                                                                 + " forests of the night What immortal hand or eye Dare frame thy fearful symmetry");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord("Tyger");

        // Then
        assertThat(nextWord).isEqualTo("Tyger");
    }

    @Test
    public void shouldReturnEmptyStringWhenNoNextWordPrediction() {
        // Given
        List<String> words = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord("tea");

        // Then
        assertThat(nextWord).isEqualTo("");
    }

    @Test
    public void shouldReturnEmptyStringWhenInputIsEmptyString() {
        // Given
        List<String> words = Arrays.asList("word", "word");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord("");

        // Then
        assertThat(nextWord).isEqualTo("");
    }

    @Test
    public void shouldReturnEmptyStringWhenInputIsStringOfSpaces() {
        // Given
        List<String> words = Arrays.asList("word", "word");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord("   ");

        // Then
        assertThat(nextWord).isEqualTo("");
    }

    @Test
    public void shouldReturnEmptyStringWhenInputStringIsNull() {
        // Given
        List<String> words = Arrays.asList("word", "word");
        BiGramModel biGramModel = new BiGramModel(words);

        // When
        String nextWord = biGramModel.getNextPredictedWord(null);

        // Then
        assertThat(nextWord).isEqualTo("");
    }

    private List<String> convertStringToList(String input) {
        String[] delimitedInput = input.split(" ");
        return Arrays.asList(delimitedInput);
    }

}