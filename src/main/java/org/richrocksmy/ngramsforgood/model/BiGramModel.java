package org.richrocksmy.ngramsforgood.model;

import static java.util.Collections.emptySortedSet;
import static java.util.stream.Collectors.groupingByConcurrent;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

import org.richrocksmy.ngramsforgood.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class BiGramModel {

    private List<String> biGrams;

    private final List<String> words;

    private final Map<String, SortedSet<Word>> model;

    public BiGramModel(final List<String> words) {
        if(words.size() < 2) {
            throw new IllegalArgumentException("Input word list must have two words or more!");
        }

        this.words = words;

        biGrams = generateBiGrams();
        model = buildModel();
    }

    private ArrayList<String> generateBiGrams() {
        ArrayList<String> biGrams = new ArrayList<>();

        IntStream.range(0, words.size() - 1)
                 .forEach(i -> biGrams.add(words.get(i) + " " + words.get(i + 1)));

        return biGrams;
    }

    private Map<String, SortedSet<Word>> buildModel() {
        return biGrams.stream()
                      .distinct()
                      .collect(groupingByConcurrent(this::extractAnchorWord, mapping(this::transformBiGramToTargetWord, toCollection(TreeSet::new))));
    }

    private String extractAnchorWord(final String biGram) {
        return biGram.split(" ")[0];
    }

    private Word transformBiGramToTargetWord(final String biGram) {
        return new Word(biGram.split(" ")[1], calculateProbabilityForTargetWord(biGram));
    }

    private float calculateProbabilityForTargetWord(final String biGram) {
        long biGramOccurrences = biGrams.stream()
                                         .filter(word -> biGram.equals(word))
                                         .count();

        String anchorWord = biGram.split(" ")[0];
        long anchorWordOccurrences = words.stream()
                                          .filter(word -> anchorWord.equals(word))
                                          .count();

        return ((float) biGramOccurrences / (float) anchorWordOccurrences);
    }

    public String getNextPredictedWord(final String word) {
        if(word == null || word.isBlank()) {
            return "";
        }

        return model.getOrDefault(word, emptySortedSet()).stream()
                    .findFirst()
                    .map(Word::getValue)
                    .orElse("");
    }
}
