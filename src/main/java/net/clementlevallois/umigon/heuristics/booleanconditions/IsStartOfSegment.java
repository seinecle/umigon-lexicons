/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.List;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.NGram;
import static net.clementlevallois.umigon.model.classification.BooleanCondition.BooleanConditionEnum.isStartOfSegment;

/**
 *
 * @author LEVALLOIS
 */
public class IsStartOfSegment {

    public static BooleanCondition check(List<NGram> nGramsInSentence, NGram ngram) {
        BooleanCondition booleanCondition = new BooleanCondition(isStartOfSegment);
        if (nGramsInSentence.isEmpty()) {
            booleanCondition.setTokenInvestigatedGetsMatched(false);
            return booleanCondition;
        } else {
            boolean isStart = nGramsInSentence.get(0).getCleanedAndStrippedNgram().equalsIgnoreCase(ngram.getCleanedAndStrippedNgram());
            if (isStart) {
                booleanCondition.setTokenInvestigatedGetsMatched(true);
                booleanCondition.setTextFragmentMatched(ngram);
                return booleanCondition;
            } else {
                booleanCondition.setTokenInvestigatedGetsMatched(false);
                return booleanCondition;
            }
        }
    }

}
