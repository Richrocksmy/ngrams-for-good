package org.richrocksmy.ngramsforgood;

import static java.util.Collections.emptySortedSet;
import static java.util.stream.Collectors.groupingByConcurrent;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static org.richrocksmy.ngramsforgood.Application.Utils.loadFileAndSanitize;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Application {

    private static final String TRAINING_DATA = "Mary had a little lamb its fleece was white as snow; And everywhere that Mary went, the lamb was "
                    + "sure to go. It followed her to school one day, which was against the rule; It made the children laugh and play, to see a "
                    + "lamb at school. And so the teacher turned it out, but still it lingered near, And waited patiently about till Mary did appear. "
                    + "\"Why does the lamb love Mary so?\" the eager children cry; \"Why, Mary loves the lamb, you know\" the teacher did reply.\"";

    public static void main(final String... args) {
        new Application().run();
    }

    public void run() {
        BiGramModel biGramModel = new BiGramModel(loadFileAndSanitize(TRAINING_DATA));
        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.println("Enter input:");
            String input = scan.nextLine();

            int nGram = getNGramFromInput(input);

            if(nGram != 2) {
                System.out.println("nGram input must be two.");
            } else {
                System.out.println(biGramModel.getPredictedNextWords(getWordFromInput(input)).stream()
                                              .map(Word::toString)
                                              .collect(joining("; ")));
            }
        }
    }

    private static int getNGramFromInput(final String input) {
        return Integer.parseInt(input.split(",")[0]);
    }

    private static String getWordFromInput(final String input) {
        return input.split(",")[1];
    }

    public static class Utils {

        private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("\\W");

        public static List<String> loadFileAndSanitize(final String data) {
            return Arrays.asList(data.split(" ")).stream()
                         .map(word -> removePunctuationAndLowercase(word))
                         .collect(toList());
        }

        private static String removePunctuationAndLowercase(final String dirty) {
            String sanitized = dirty;
            sanitized = PUNCTUATION_PATTERN.matcher(sanitized).replaceAll("");

            return sanitized.toLowerCase();
        }
    }

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

        public SortedSet<Word> getPredictedNextWords(final String word) {
            if (word == null || word.isBlank()) {
                return emptySortedSet();
            }

            return model.getOrDefault(word, emptySortedSet());
        }
    }

    public class Word implements Comparable<Word> {

        private final String value;

        private final float probability;

        public Word(String value, float probability) {
            this.value = value;
            this.probability = probability;
        }

        @Override
        public int compareTo(Word word) {
            int result = Float.compare(word.getProbability(), probability);

            if(result == 0) {
                return value.compareTo(word.value);
            }

            return result;
        }

        public String getValue() {
            return value;
        }

        public float getProbability() {
            return probability;
        }

        public String toString() {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMinimumFractionDigits(3);

            return value + ", " + decimalFormat.format(probability);
        }
    }

}
