package org.richrocksmy.ngramsforgood;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class UtilsTest {

    private Utils utils;

    @BeforeEach
    public void before() {
        utils = new Utils();
    }

    @Test
    public void shouldLoadValidFileToList() throws Exception {
        // Given
        String file = getAbsoluteFilePath("corpus.txt");

        // When
        List<String> words = Utils.loadFileAndSanitize(file);

        // Then
        assertThat(words).containsExactly("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
    }

    @Test
    public void shouldThrowExceptionWhenLoadingNonExistentFile() {
        // Given
        String file = "iDoNotExist.txt";

        // When / Then
        assertThatThrownBy(() -> Utils.loadFileAndSanitize(file)).isInstanceOf(IOException.class);
    }

    @Test
    public void shouldRemovePunctuationAndConvertToLowercase() throws Exception {
         // Given
        String file = getAbsoluteFilePath("corpusWithPunctuationAndUppercase.txt");

        // When
        List<String> words = Utils.loadFileAndSanitize(file);

        // Then
        assertThat(words).containsExactly("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
    }

    @Test
    public void shouldHandleEmptyFile() throws Exception{
        // Given
        String file = getAbsoluteFilePath("corpusEmpty.txt");

        // When
        List<String> words = Utils.loadFileAndSanitize(file);

        // Then
        assertThat(words).isEmpty();
    }

    private String getAbsoluteFilePath(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(filename).getFile()).getPath();
    }
}