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
import static net.clementlevallois.umigon.model.classification.BooleanCondition.BooleanConditionEnum.isImmediatelyPrecededByStrongWord;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class IsImmediatelyPrecededByStrongWord {

    public static BooleanCondition check(boolean stripped, List<NGram> textFragmentsThatAreNGrams, NGram ngram, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        BooleanCondition booleanCondition = new BooleanCondition(isImmediatelyPrecededByStrongWord);

        Set<String> strongWords = new HashSet();
        strongWords.addAll(lexiconsAndTheirConditionalExpressions.getMapH3().keySet());
 
        List<NGram> ngramsFoundAtIndexMinusOne = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, -1);
        List<NGram> ngramsFoundAtIndexMinusTwo = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, -2);

        List<NGram> allNgramsFound = new ArrayList();
        allNgramsFound.addAll(ngramsFoundAtIndexMinusOne);
        allNgramsFound.addAll(ngramsFoundAtIndexMinusTwo);

        List<NGram> nGramsThatMatchedASpecificTerm = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, allNgramsFound, strongWords);
        booleanCondition.setTokenInvestigatedGetsMatched(!nGramsThatMatchedASpecificTerm.isEmpty());
        if (!nGramsThatMatchedASpecificTerm.isEmpty()) {
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatchedASpecificTerm);
            booleanCondition.setTextFragmentMatched(ngram);
        }
        return booleanCondition;
    }
}
