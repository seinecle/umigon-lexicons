/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.heuristics.tools;

import net.clementlevallois.umigon.model.classification.LanguageSpecificLexicons;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.classification.TermWithConditionalExpressions;
import java.io.BufferedReader;
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
import net.clementlevallois.utils.TextCleaningOps;

/**
 *
 * @author LEVALLOIS
 */
public class LoaderOfLexiconsAndConditionalExpressions {

    private final String lang;
    private BufferedReader br;
    private String string;
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
    private Set<String> setModerators;
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
        lexiconsWithoutTheirConditionalExpressions = new HashSet();
        setNegations = new HashSet();
        setTimeTokens = new HashSet();
        setFalsePositiveOpinions = new HashSet();
        setIronicallyPositive = new HashSet();
        setPositivePriorAssociations = new HashSet();
        setNegativePriorAssociations = new HashSet();
        setModerators = new HashSet();
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

                br = Files.newBufferedReader(pathResource, StandardCharsets.UTF_8);

                String numberPrefixInFilename = fileName.substring(0, fileName.indexOf("_"));
                int map = Integer.parseInt(numberPrefixInFilename);
                if (map == 0 || map == 14) {
                    continue;
                }

                String term = null;
                String featureString;
                String rule = null;
                String[] fields;
                String[] parametersArray;
                String[] featuresArray;
                List<String> featuresList;
                Iterator<String> featuresListIterator;
                String field0 = "";
                String field1;
                String field2;
                String field3;
                String hashtagRelevant;
                //mapFeatures:
                //key: a feature
                //value: a set of parameters for the given feature
                while ((string = br.readLine()) != null) {
                    fields = string.split("\t");
                    if (fields.length == 0) {
                        System.out.println("empty line or something in file " + fileName + "for lang" + lang);
                        continue;
                    }
                    if (!lang.equals("zh")) {
                        field0 = TextCleaningOps.flattenToAsciiAndRemoveApostrophs(fields[0].trim());
                    }
                    if (field0.isEmpty()) {
                        continue;
                    }
                    field1 = (fields.length < 2) ? null : fields[1];
                    field2 = (fields.length < 3) ? "" : fields[2].trim();
                    field3 = (fields.length < 4) ? "" : fields[3].trim();

                    term = field0;

                    featureString = field1;
                    if (map == 3 || map == 6 || map == 10 || map == 11 || map == 12 || map == 14 || map == 15 || map == 16 || map == 18 || map == 19) {
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
                        //set of moderators
                        if (map == 16) {
                            setModerators.add(term);
                            continue;
                        }

                        System.out.println("error:");
                        System.out.println(string);
                        System.out.println(Arrays.toString(fields));
                        continue;
                    }
                    rule = field2;

                    hashtagRelevant = field3;
                    //parse the "feature" field to disentangle the feature from the parameters
                    //this parsing rule will be extended to allow for multiple features
                    if (featureString == null) {
                        System.out.println("error reading lexicon line: \"" + string + "\" in language " + lang);
                        System.out.println("item in the lexicon not imported");
                        continue;
                    }
                    if (featureString.contains("12") | featureString.contains("11") | featureString.contains("10")) {
                        System.out.println("error in feature, probably a missing tab:");
                        System.out.println(string);
                        System.out.println(Arrays.toString(fields));
                    }
                    featuresArray = featureString.split("\\+\\+\\+");
                    featuresList = Arrays.asList(featuresArray);
                    featuresListIterator = featuresList.iterator();
                    lexiconsAndConditionalExpressions = new TermWithConditionalExpressions();
                    while (featuresListIterator.hasNext()) {
                        BooleanCondition booleanExpression = new BooleanCondition();
                        String condition;

                        featureString = featuresListIterator.next();
                        if (featureString == null || featureString.isEmpty() || featureString.equals("null")) {
                            continue;
                        }
                        if (featureString.contains("///")) {
                            parametersArray = featureString.substring(featureString.indexOf("///") + 3, featureString.length()).split("\\|");
                            condition = featureString.substring(0, featureString.indexOf("///"));
                            if (condition != null & !condition.isEmpty()) {
                                if (condition.startsWith("!")) {
                                    booleanExpression.setCondition(condition.substring(1), true);
                                } else {
                                    booleanExpression.setCondition(condition, false);
                                }
                                booleanExpression.setAssociatedKeywords(new HashSet(Arrays.asList(parametersArray)));
                            }
                        } else {
                            if (featureString.startsWith("!")) {
                                booleanExpression.setCondition(featureString.substring(1), true);
                                booleanExpression.setFlipped(Boolean.TRUE);
                            } else {
                                booleanExpression.setCondition(featureString, false);
                            }
                        }
                        if (booleanExpression.getBooleanConditionEnum() == null) {
                            System.out.println("problem with conditional expression for line: ");
                            System.out.println(string);
                        }
                        lexiconsAndConditionalExpressions.addFeature(booleanExpression);
                    }
                    if (hashtagRelevant.equalsIgnoreCase("x")) {
                        lexiconsAndConditionalExpressions.setHashtagRelevant(false);
                    }
                    lexiconsAndConditionalExpressions.generateNewHeuristic(term, rule);

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
                br.close();
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
        lex.setSetModerators(setModerators);
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

    public Set<String> getSetModerators() {
        return multilingualLexicons.get(lang).getSetModerators();
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
