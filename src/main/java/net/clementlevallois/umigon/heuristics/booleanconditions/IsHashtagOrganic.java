/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import static net.clementlevallois.umigon.model.classification.BooleanCondition.BooleanConditionEnum.isHashtagOrganic;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class IsHashtagOrganic {

    public static BooleanCondition check(boolean stripped, NGram hashtag, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        BooleanCondition booleanCondition = new BooleanCondition(isHashtagOrganic);
        String ngramAsString = hashtag.getCleanedAndStrippedNgramIfCondition(stripped);
        for (String term : lexiconsAndTheirConditionalExpressions.getMapH3().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (ngramAsString.startsWith(term)) {
                ngramAsString = ngramAsString.replace(term, "");
            }
        }

        for (String term : lexiconsAndTheirConditionalExpressions.getMapH9().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (ngramAsString.startsWith(term) && lexiconsAndTheirConditionalExpressions.getMapH9().get(term) != null) {
                    booleanCondition.setTokenInvestigatedGetsMatched(Boolean.TRUE);
                    booleanCondition.setTextFragmentMatched(hashtag);
            }
        }

        return booleanCondition;
    }
}
