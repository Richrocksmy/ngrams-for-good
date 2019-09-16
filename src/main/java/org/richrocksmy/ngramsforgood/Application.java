package org.richrocksmy.ngramsforgood;

import static org.richrocksmy.ngramsforgood.Utils.loadFileAndSanitize;

import org.richrocksmy.ngramsforgood.model.BiGramModel;

import java.io.IOException;

public class Application {

    public static void main(String... args) {
        String corpusFileLocation = args[0];
        String word = args[1];

        try {
            BiGramModel biGramModel = new BiGramModel(loadFileAndSanitize(corpusFileLocation));
            biGramModel.getNextPredictedWord(word);
        } catch (IOException e) { }
    }
}
