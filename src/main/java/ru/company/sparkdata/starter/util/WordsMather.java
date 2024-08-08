package ru.company.sparkdata.starter.util;

import lombok.experimental.UtilityClass;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@UtilityClass
public class WordsMather {


    public static List<String> toWordJavaConvention(String methodName) {
        final char[] charArray = methodName.toCharArray();
        final List<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        for (char c : charArray) {
            if (Character.isUpperCase(c)) {
                words.add(word.toString());
                word.delete(0, word.length());
            }
            word.append(c);
        }
        words.add(word.toString());
        return words;
    }

    public static String findAndRemoveMatchingPiecesIfExists(Set<String> options, List<String> pieces) {
        final StringBuilder match = new StringBuilder(pieces.remove(0));
        final List<String> remainingOptions = options.stream().filter(o ->
                        o.toLowerCase().startsWith(match.toString()))
                .toList();

        if (remainingOptions.isEmpty()) {
            return "";
        }

        while (remainingOptions.size() > 1) {
            match.append(pieces.remove(0));
            remainingOptions.removeIf(option -> !option.toLowerCase().startsWith(match.toString()));
        }

        while (!remainingOptions.get(0).equalsIgnoreCase(match.toString())) {
            match.append(pieces.remove(0));
        }

        return Introspector.decapitalize(match.toString());

    }
}
