package org.richrocksmy.ngramsforgood;

import java.text.DecimalFormat;

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
