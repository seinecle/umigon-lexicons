/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.Set;
import java.util.regex.Pattern;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.TextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class IsNegationAloneInLastSentenceFragment {

    // to check for cases like:
    // I like my bank. Not.
    private static final Set<Character> punctuationSet = Set.of('.', ',', ';', ':', '?', '!', '-', '"', '\'', '#');

    public static BooleanCondition check(String originalText, Set<String> negations) {
        BooleanCondition booleanCondition = new BooleanCondition(BooleanCondition.BooleanConditionEnum.isNegationAloneInLastSentenceFragment);
        StringBuilder sb = new StringBuilder();
        String originalTextWithoutEndingPunctuationSigns = originalText.replaceAll("[\\p{Punct}]+$", "").toLowerCase();
        for (String negation : negations) {
            if (originalTextWithoutEndingPunctuationSigns.endsWith(negation)) {
                if (originalTextWithoutEndingPunctuationSigns.length() >= negation.length() + 3) {
                    String precedingLetters = originalTextWithoutEndingPunctuationSigns.substring(originalTextWithoutEndingPunctuationSigns.lastIndexOf(negation) - 3);
                    for (char c : precedingLetters.toCharArray()) {
                        if (punctuationSet.contains(c)) {
                            booleanCondition.setTokenInvestigatedGetsMatched(Boolean.TRUE);
                            return booleanCondition;
                        }
                    }
                }
            }
        }
        return booleanCondition;
    }
}
