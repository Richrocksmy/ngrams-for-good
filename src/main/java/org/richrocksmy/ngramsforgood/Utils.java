package org.richrocksmy.ngramsforgood;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {

    // This will also remove apostrophes - not clear on what behaviour we need here?
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("\\W");

    public static List<String> loadFileAndSanitize(final String fileLocation) throws IOException {
        List<String> words = new ArrayList<>();

        new Scanner(new File(fileLocation)).forEachRemaining(word -> words.add(removePunctuationAndLowercase(word)));

        return words;
    }

    private static String removePunctuationAndLowercase(final String dirty) {
        String sanitized = dirty;
        sanitized = PUNCTUATION_PATTERN.matcher(sanitized).replaceAll("");
        sanitized = sanitized.toLowerCase();

        return sanitized;
    }
}
