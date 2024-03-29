/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.heuristics.tools;

import net.clementlevallois.umigon.model.classification.LanguageSpecificLexicons;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.classification.TermWithConditionalExpressions;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author LEVALLOIS
 */
public class LoaderOfLexiconsAndConditionalExpressions {

    private final String lang;
    private TermWithConditionalExpressions lexiconsAndConditionalExpressions;
    private Set<String> lexiconsWithoutTheirConditionalExpressions;
    private Map<String, TermWithConditionalExpressions> mapH1;
    private Map<String, TermWithConditionalExpressions> mapH2;
    private Map<String, TermWithConditionalExpressions> mapH3;
    private Map<String, TermWithConditionalExpressions> mapH4;
    private Map<String, TermWithConditionalExpressions> mapH5;
    private Map<String, TermWithConditionalExpressions> mapH6;
    private Map<String, TermWithConditionalExpressions> mapH7;
    private Map<String, TermWithConditionalExpressions> mapH8;
    private Map<String, TermWithConditionalExpressions> mapH9;
    private Map<String, TermWithConditionalExpressions> mapH10;
    private Map<String, TermWithConditionalExpressions> mapH11;
    private Map<String, TermWithConditionalExpressions> mapH12;
    private Map<String, TermWithConditionalExpressions> mapH13;
    private Map<String, TermWithConditionalExpressions> mapH17;
    private Set<String> setNegations;
    private Set<String> setTimeTokens;
    private Set<String> setSubjective;
    private Set<String> setPositivePriorAssociations;
    private Set<String> setNegativePriorAssociations;
    private Set<String> setHashTags;
    private Set<String> setModeratorsForward;
    private Set<String> setModeratorsBackward;
    private Set<String> setStrong;
    private Set<String> setFalsePositiveOpinions;
    private Set<String> setIronicallyPositive;

    private Map<String, LanguageSpecificLexicons> multilingualLexicons = new HashMap();

    public LoaderOfLexiconsAndConditionalExpressions(String lang) {
        this.lang = lang;
    }

    public void load() {
        if (lang == null || lang.isBlank()) {
            System.out.println("lang is null or empty in the heuristics loading class. Exiting");
            System.exit(1);
        }
        List<String> fileNames = new ArrayList();
        fileNames.add("10_negations.txt");
        fileNames.add("13_hashtags.txt");
        fileNames.add("16_moderators.txt");
        fileNames.add("1_positive tone.txt");
        fileNames.add("2_negative tone.txt");
        fileNames.add("17_hypersatisfaction.txt");
        fileNames.add("3_strength of opinion.txt");
        fileNames.add("15_isIronicallyPositive.txt");
        fileNames.add("9_commercial tone.txt");
        fileNames.add("7_call_to_action.txt");
        fileNames.add("5_question.txt");
        fileNames.add("6_subjective.txt");
        fileNames.add("4_time.txt");
        fileNames.add("8_humor or light.txt");
        fileNames.add("14_false positive opinions.txt");
        fileNames.add("12_time indications.txt");
        fileNames.add("11_hints difficulty.txt");
        fileNames.add("18_positive_prior_assocations.txt");
        fileNames.add("19_negative_prior_assocations.txt");
        fileNames.add("20_moderators_backward.txt");
        lexiconsWithoutTheirConditionalExpressions = new HashSet();
        setNegations = new HashSet();
        setTimeTokens = new HashSet();
        setFalsePositiveOpinions = new HashSet();
        setIronicallyPositive = new HashSet();
        setPositivePriorAssociations = new HashSet();
        setNegativePriorAssociations = new HashSet();
        setModeratorsForward = new HashSet();
        setModeratorsBackward = new HashSet();
        setStrong = new HashSet();
        setSubjective = new HashSet();
        mapH1 = new HashMap();
        mapH2 = new HashMap();
        mapH4 = new HashMap();
        mapH3 = new HashMap();
        mapH5 = new HashMap();
        mapH7 = new HashMap();
        mapH8 = new HashMap();
        mapH9 = new HashMap();
        mapH10 = new HashMap();
        mapH11 = new HashMap();
        mapH12 = new HashMap();
        mapH13 = new HashMap();
        mapH17 = new HashMap();
        for (String fileName : fileNames) {
            try {
                String PATHLOCALE = ResourcePath.returnRootResources();

                Path pathResource = Path.of(PATHLOCALE, "src/main/resources/net/clementlevallois/umigon/lexicons/" + lang + "/" + fileName);
                if (!Files.exists(pathResource)) {
                    continue;
                }

                List<String> lines = Files.readAllLines(pathResource, StandardCharsets.UTF_8);

                String numberPrefixInFilename = fileName.substring(0, fileName.indexOf("_"));
                int map = Integer.parseInt(numberPrefixInFilename);
                if (map == 0 || map == 14) {
                    continue;
                }
                String term = null;
                String[] fields;
                String[] parametersArray;
                String[] arrayOfBooleanConditioons;
                List<String> listOfBooleanConditions;
                Iterator<String> booleanConditionsIterator;
                String booleanConditionsAttachedToTheTerm;
                String booleanExpressionForEvaluation;
                String field3;
                String hashtagRelevant;
                //mapFeatures:
                //key: a feature
                //value: a set of parameters for the given feature
                for (String line : lines) {
                    fields = line.split("\t");
                    if (fields.length == 0) {
                        System.out.println("empty line or something in file " + fileName + "for lang" + lang);
                        continue;
                    }
//                    if (!lang.equals("zh")) {
//                        field0 = TextCleaningOps.flattenToAsciiAndRemoveApostrophs(fields[0].trim());
//                    }
                    if (fields[0] == null || fields[0].isEmpty()) {
                        continue;
                    }
//                    if (fields[0].equals("best")){
//                        System.out.println("stop");
//                    }
                    term = fields[0].trim();
                    booleanConditionsAttachedToTheTerm = (fields.length < 2) ? null : fields[1];
                    booleanExpressionForEvaluation = (fields.length < 3) ? "" : fields[2].trim();
                    hashtagRelevant = (fields.length < 4) ? "" : fields[3].trim();
                    if (map == 3 || map == 6 || map == 10 || map == 11 || map == 12 || map == 14 || map == 15 || map == 16 || map == 18 || map == 19 || map == 20) {
                        //negations
                        if (map == 10) {
                            setNegations.add(term);
                            continue;
                        }
                        //strong
                        if (map == 3) {
                            lexiconsAndConditionalExpressions = new TermWithConditionalExpressions();
                            lexiconsAndConditionalExpressions.setHashtagRelevant(false);
                            lexiconsAndConditionalExpressions.generateNewHeuristic(term, "");
                            mapH3.put(term, lexiconsAndConditionalExpressions);
                            setStrong.add(term);
                            continue;
                        }

                        //subjective
                        if (map == 6) {
                            setSubjective.add(term);
                            continue;
                        }
                        //hints difficulty
                        if (map == 11) {
                            lexiconsAndConditionalExpressions = new TermWithConditionalExpressions();
                            lexiconsAndConditionalExpressions.setHashtagRelevant(false);
                            lexiconsAndConditionalExpressions.generateNewHeuristic(term, "");
                            mapH11.put(term, lexiconsAndConditionalExpressions);
                            continue;
                        }

                        //time indications
                        if (map == 12) {
                            setTimeTokens.add(term);
                            continue;
                        }

                        //words with positive prior associations
                        if (map == 18) {
                            setPositivePriorAssociations.add(term);
                            continue;
                        }

                        //words with negative prior associations
                        if (map == 19) {
                            setNegativePriorAssociations.add(term);
                            continue;
                        }

                        //set of terms which look like opinions but are false postives
                        if (map == 14) {
                            setFalsePositiveOpinions.add(term);
                            continue;
                        }
                        //set of terms which look like opinions but are false postives
                        if (map == 15) {
                            setIronicallyPositive.add(term);
                            continue;
                        }
                        //set of moderators forward
                        if (map == 16) {
                            setModeratorsForward.add(term);
                            continue;
                        }

                        //set of moderators backward
                        if (map == 20) {
                            setModeratorsBackward.add(term);
                            continue;
                        }

                        System.out.println("error:");
                        System.out.println(line);
                        System.out.println(Arrays.toString(fields));
                        continue;
                    }

                    //parse the "feature" field to disentangle the feature from the parameters
                    //this parsing rule will be extended to allow for multiple features
                    if (booleanConditionsAttachedToTheTerm == null) {
                        System.out.println("error reading lexicon line: \"" + line + "\" in language " + lang);
                        System.out.println("item in the lexicon not imported");
                        continue;
                    }
                    if (booleanConditionsAttachedToTheTerm.contains("12") | booleanConditionsAttachedToTheTerm.contains("11") | booleanConditionsAttachedToTheTerm.contains(":10")| booleanConditionsAttachedToTheTerm.contains("10}")) {
                        System.out.println("probable error in list of boolean conditions because we detected 10, 11 or 12 in it. Probably a missing tab:");
                        System.out.println(line);
                        System.out.println(Arrays.toString(fields));
                    }
                    arrayOfBooleanConditioons = booleanConditionsAttachedToTheTerm.split("\\+\\+\\+");
                    listOfBooleanConditions = Arrays.asList(arrayOfBooleanConditioons);
                    booleanConditionsIterator = listOfBooleanConditions.iterator();
                    lexiconsAndConditionalExpressions = new TermWithConditionalExpressions();
                    while (booleanConditionsIterator.hasNext()) {
                        BooleanCondition booleanExpression = new BooleanCondition();
                        String condition;

                        String oneBooleanCondition = booleanConditionsIterator.next();
                        if (oneBooleanCondition == null || oneBooleanCondition.isEmpty() || oneBooleanCondition.equals("null")) {
                            continue;
                        }
                        if (oneBooleanCondition.contains("///")) {
                            parametersArray = oneBooleanCondition.substring(oneBooleanCondition.indexOf("///") + 3, oneBooleanCondition.length()).split("\\|");
                            condition = oneBooleanCondition.substring(0, oneBooleanCondition.indexOf("///"));
                            if (condition != null & !condition.isEmpty()) {
                                if (condition.startsWith("!")) {
                                    booleanExpression.setCondition(condition.substring(1), true);
                                } else {
                                    booleanExpression.setCondition(condition, false);
                                }
                                booleanExpression.setAssociatedKeywords(new HashSet(Arrays.asList(parametersArray)));
                            }
                        } else {
                            if (oneBooleanCondition.startsWith("!")) {
                                booleanExpression.setCondition(oneBooleanCondition.substring(1), true);
                                booleanExpression.setFlipped(Boolean.TRUE);
                            } else {
                                booleanExpression.setCondition(oneBooleanCondition, false);
                            }
                        }
                        if (booleanExpression.getBooleanConditionEnum() == null) {
                            System.out.println("problem with conditional expression for line: ");
                            System.out.println(line);
                        }
                        lexiconsAndConditionalExpressions.addFeature(booleanExpression);
                    }
                    if (hashtagRelevant.equalsIgnoreCase("x")) {
                        lexiconsAndConditionalExpressions.setHashtagRelevant(false);
                    }
                    lexiconsAndConditionalExpressions.generateNewHeuristic(term, booleanExpressionForEvaluation);

                    lexiconsWithoutTheirConditionalExpressions.add(term);
                    //positive
                    if (map == 1) {
                        mapH1.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //negative
                    if (map == 2) {
                        mapH2.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //hypersatisfaction
                    if (map == 17) {
                        mapH17.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //time
                    if (map == 4) {
                        mapH4.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //question
                    if (map == 5) {
                        mapH5.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //address
                    if (map == 7) {
                        mapH7.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //humor
                    if (map == 8) {
                        mapH8.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //commercial offer
                    if (map == 9) {
                        mapH9.put(term, lexiconsAndConditionalExpressions);
                        continue;
                    }
                    //hashtag specific terms !! WE NEGLECT THE CASE
                    if (map == 13) {
                        mapH13.put(term.toLowerCase(), lexiconsAndConditionalExpressions);
                    }
                }
            } catch (IOException ex) {
                System.out.println("IO Exception in heuristics loader!");
                System.out.println("probably encoding issue with file " + fileName + " of lang " + lang);
            }
        }
        LanguageSpecificLexicons lex = new LanguageSpecificLexicons();
        lex.setLanguage(lang);
        lex.setLexiconsWithoutTheirConditionalExpressions(lexiconsWithoutTheirConditionalExpressions);
        lex.setMapH1(mapH1);
        lex.setMapH2(mapH2);
        lex.setMapH3(mapH3);
        lex.setMapH4(mapH4);
        lex.setMapH5(mapH5);
        lex.setMapH7(mapH7);
        lex.setMapH8(mapH8);
        lex.setMapH9(mapH9);
        lex.setMapH10(mapH10);
        lex.setMapH11(mapH11);
        lex.setMapH11(mapH11);
        lex.setMapH12(mapH12);
        lex.setMapH13(mapH13);
        lex.setMapH17(mapH17);
        lex.setSetHashTags(setHashTags);
        lex.setSetFalsePositiveOpinions(setFalsePositiveOpinions);
        lex.setSetIronicallyPositive(setIronicallyPositive);
        lex.setSetModeratorsForward(setModeratorsForward);
        lex.setSetModeratorsBackward(setModeratorsBackward);
        lex.setSetStrong(setStrong);
        lex.setSetNegations(setNegations);
        lex.setSetTimeTokens(setTimeTokens);
        lex.setSetSubjective(setSubjective);
        lex.setSetSubjective(setSubjective);
        multilingualLexicons.put(lang, lex);
    }

    public Map<String, TermWithConditionalExpressions> getMapH1() {
        return multilingualLexicons.get(lang).getMapH1();
    }

    public Map<String, TermWithConditionalExpressions> getMapH2() {
        return multilingualLexicons.get(lang).getMapH2();
    }

    public Map<String, TermWithConditionalExpressions> getMapH3() {
        return multilingualLexicons.get(lang).getMapH3();
    }

    public Map<String, TermWithConditionalExpressions> getMapH13() {
        return multilingualLexicons.get(lang).getMapH13();
    }

    public Map<String, TermWithConditionalExpressions> getMapH17() {
        return multilingualLexicons.get(lang).getMapH17();
    }

    public Map<String, TermWithConditionalExpressions> getMapH9() {
        return multilingualLexicons.get(lang).getMapH9();
    }

    public Set<String> getSetNegations() {
        return multilingualLexicons.get(lang).getSetNegations();
    }

    public Set<String> getSetModeratorsForward() {
        return multilingualLexicons.get(lang).getSetModeratorsForward();
    }

    public Set<String> getSetModeratorsBackward() {
        return multilingualLexicons.get(lang).getSetModeratorsBackward();
    }

    public Set<String> getSetStrong() {
        return multilingualLexicons.get(lang).getSetStrong();
    }

    public Set<String> getTimeIndications() {
        return multilingualLexicons.get(lang).getSetTimeTokens();
    }

    public Set<String> getSetIronicallyPositive() {
        return multilingualLexicons.get(lang).getSetIronicallyPositive();
    }

    public Set<String> getLexiconsWithoutTheirConditionalExpressions() {
        return multilingualLexicons.get(lang).getLexiconsWithoutTheirConditionalExpressions();
    }

    public Set<String> getSetSubjective() {
        return multilingualLexicons.get(lang).getSetSubjective();
    }

    public Set<String> getSetPositivePriorAssociations() {
        return setPositivePriorAssociations;
    }

    public Set<String> getSetNegativePriorAssociations() {
        return setNegativePriorAssociations;
    }
}
