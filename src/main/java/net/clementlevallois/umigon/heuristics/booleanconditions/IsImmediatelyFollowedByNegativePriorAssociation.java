/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.ArrayList;
import java.util.List;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.BooleanCondition;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isImmediatelyFollowedByNegativePriorAssociation;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class IsImmediatelyFollowedByNegativePriorAssociation {

    public static BooleanCondition check(boolean stripped, List<NGram> textFragmentsThatAreNGrams, NGram ngram, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        BooleanCondition booleanCondition = new BooleanCondition(isImmediatelyFollowedByNegativePriorAssociation);

        List<NGram> ngramsFoundAtIndexPlusOne = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, 1);
        List<NGram> ngramsFoundAtIndexPlusTwo = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, 2);

        List<NGram> allNgramsFound = new ArrayList();
        allNgramsFound.addAll(ngramsFoundAtIndexPlusOne);
        allNgramsFound.addAll(ngramsFoundAtIndexPlusTwo);

        List<NGram> nGramsThatMatchedAPositivePriorAssociation = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, allNgramsFound, lexiconsAndTheirConditionalExpressions.getSetNegativePriorAssociations());
        booleanCondition.setTokenInvestigatedGetsMatched(!nGramsThatMatchedAPositivePriorAssociation.isEmpty());
        if (!nGramsThatMatchedAPositivePriorAssociation.isEmpty()) {
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatchedAPositivePriorAssociation);
            booleanCondition.setTextFragmentMatched(ngram);
        }
        return booleanCondition;
    }
}
