package com.google.refine.expr.functions.strings;

import java.util.Properties;

import org.apache.commons.text.similarity.LevenshteinDistance;

import com.google.refine.grel.Function;

public class EditDistance implements Function {

    @Override
    public Object call(Properties bindings,Object[] args) {
        if (args.length != 2 || !(args[0] instanceof String) || !(args[1] instanceof String)) {
            throw new IllegalArgumentException("editDistance function takes exactly two string arguments.");
        }

        String s1 = (String) args[0];
        String s2 = (String) args[1];

        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return levenshteinDistance.apply(s1, s2);
    }



    @Override
    public String getDescription() {
        return "Calculates the Levenshtein edit distance between two strings.";
    }

    @Override
    public String getReturns() {
        return null;
    }
}
