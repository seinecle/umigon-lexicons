/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.NGram;
import static net.clementlevallois.umigon.model.classification.BooleanCondition.BooleanConditionEnum.isImmediatelyFollowedByStrongWord;

/**
 *
 * @author LEVALLOIS
 */
public class IsImmediatelyFollowedByStrongWord {

    public static BooleanCondition check(boolean stripped, List<NGram> ngrams, NGram ngram, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        BooleanCondition booleanCondition = new BooleanCondition(isImmediatelyFollowedByStrongWord);

        Set<String> strongWords = new HashSet();
        strongWords.addAll(lexiconsAndTheirConditionalExpressions.getMapH3().keySet());


        List<NGram> ngramsFoundAtIndexPlusOne = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(ngrams, ngram, 1);
        List<NGram> ngramsFoundAtIndexPlusTwo = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(ngrams, ngram, 2);

        List<NGram> allNgramsFound = new ArrayList();
        allNgramsFound.addAll(ngramsFoundAtIndexPlusOne);
        allNgramsFound.addAll(ngramsFoundAtIndexPlusTwo);

        List<NGram> nGramsThatMatchedASpecificTerm = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, allNgramsFound, strongWords);
        booleanCondition.setTokenInvestigatedGetsMatched(!nGramsThatMatchedASpecificTerm.isEmpty());
        if (!nGramsThatMatchedASpecificTerm.isEmpty()) {
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatchedASpecificTerm);
            booleanCondition.setTextFragmentMatched(ngram);
        }
        return booleanCondition;
    }
}
