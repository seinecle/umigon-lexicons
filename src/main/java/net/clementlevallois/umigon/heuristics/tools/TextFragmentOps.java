/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.TextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class TextFragmentOps {

    public static List<NGram> getNGramsAtRelativeOrdinalIndex(List<NGram> ngrams, NGram ngram, int relativeIndex) {
        List<NGram> ngramResults = new ArrayList();

        int indexToLookUp = ngram.getIndexOrdinalInSentence() + relativeIndex;

        // ngrams can be several terms long.
        // the search for the terms situated AFTER an ngram should start AFTER the latest of the terms contained in the ngram
        if (relativeIndex > 0) {
            indexToLookUp = indexToLookUp - 1;
        }

        if (indexToLookUp < 0 || indexToLookUp > ngrams.size() - 1) {
            return ngramResults;
        }
        ListIterator<NGram> listIterator;
        int ngramSize = 0;
        int ngramMaxSize = 5;

        if (relativeIndex < 0) {
            listIterator = ngrams.listIterator(ngrams.indexOf(ngram));
            while (listIterator.hasPrevious()) {
                NGram previous = listIterator.previous();
                if (previous.getIndexOrdinalInSentence() == indexToLookUp & (previous.getIndexOrdinalInSentence() + previous.getTerms().size()) <= ngram.getIndexOrdinalInSentence()) {
                    ngramResults.add(previous);
                }
            }
        } else {
            listIterator = ngrams.listIterator(ngrams.indexOf(ngram) + 1);
            while (listIterator.hasNext()) {
                NGram nextNGram = listIterator.next();
                for (int i = ngramSize; i < ngramMaxSize; i++) {
                    if (nextNGram.getIndexOrdinalInSentence() == (indexToLookUp + i)) {
                        ngramResults.add(nextNGram);
                    }
                }
                if (nextNGram.getIndexOrdinalInSentence() > (indexToLookUp + ngramSize)) {
                    break;
                }
            }
        }
        return ngramResults;
    }

    public static List<NGram> getNGramsAtRelativeCardinalIndex(List<NGram> ngrams, NGram ngram, int relativeIndex) {
        List<NGram> ngramResults = new ArrayList();

        int indexToLookUp = ngram.getIndexCardinalInSentence() + relativeIndex;
        if (indexToLookUp < 0 || indexToLookUp > ngrams.size() - 1) {
            return ngramResults;
        }
        ListIterator<NGram> listIterator = ngrams.listIterator(ngrams.indexOf(ngram));
        int ngramSize = 0;
        int ngramMaxSize = 5;
        if (relativeIndex < 0) {
            while (listIterator.hasPrevious() && ngramSize < ngramMaxSize) {
                NGram previous = listIterator.previous();
                if (previous.getIndexCardinalInSentence() == (indexToLookUp + ngramSize)) {
                    ngramResults.add(previous);
                }
                ngramSize++;
            }
        } else {
            while (listIterator.hasNext()) {
                NGram next = listIterator.next();
                for (int i = ngramSize; i < ngramMaxSize; i++) {
                    if (next.getIndexCardinalInSentence() == (indexToLookUp + i)) {
                        ngramResults.add(next);
                    }
                }
                if (next.getIndexCardinalInSentence() > (indexToLookUp + ngramSize)) {
                    break;
                }
            }
        }
        return ngramResults;
    }

    public static List<NGram> getNGramsAfterAnOrdinalIndex(List<NGram> ngrams, NGram ngram) {
        return ngrams.subList(ngrams.indexOf(ngram), ngrams.size());
    }

    public static List<TextFragment> getTextFragmentsAfterAnNgram(List<TextFragment> textFragments, NGram ngram) {
        List<TextFragment> textFragmentsAfterNGram = new ArrayList();
        int indexCardinalNGram = ngram.getIndexCardinalInSentence();
        for (TextFragment textFragment : textFragments) {
            if (textFragment.getIndexCardinalInSentence() > indexCardinalNGram) {
                textFragmentsAfterNGram.add(textFragment);
            }
        }
        return textFragmentsAfterNGram;
    }

    public static List<NGram> getNGramsBeforeAnOrdinalIndex(List<NGram> ngrams, NGram ngram) {
        List<NGram> results = new ArrayList();
        for (NGram ngramLoop : ngrams) {
            if (ngramLoop.getIndexOrdinalInSentence() < ngram.getIndexOrdinalInSentence()) {
                results.add(ngramLoop);
            }
        }
        return results;
    }

    public static List<NGram> checkIfListOfNgramsMatchStringsFromCollection(boolean stripped, List<NGram> ngrams, Collection<String> collection) {
        List<NGram> results = new ArrayList();
        for (NGram ngram : ngrams) {
            if (collection.contains(ngram.getCleanedAndStrippedNgramIfCondition(stripped).toLowerCase())
                    || collection.contains(ngram.getCleanedAndStrippedNgramIfCondition(!stripped).toLowerCase())) {
                results.add(ngram);
            }
        }
        return results;
    }
}
