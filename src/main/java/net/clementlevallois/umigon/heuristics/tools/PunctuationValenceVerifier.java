/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.clementlevallois.umigon.heuristics.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Punctuation;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;

/**
 *
 * @author LEVALLOIS
 */
public class PunctuationValenceVerifier {

    private static Set<String> punctuationSignsWithPositiveValence;
    private static Set<String> punctuationSignsWithNegativeValence;

    public static void load() {
        BufferedReader br = null;
        try {
            if (punctuationSignsWithPositiveValence != null && punctuationSignsWithNegativeValence != null) {
                return;
            }
            punctuationSignsWithPositiveValence = new HashSet();
            punctuationSignsWithNegativeValence = new HashSet();
            String PATHLOCALE = ResourcePath.returnRootResources();
            Path pathResource = Path.of(PATHLOCALE, "net/clementlevallois/umigon/lexicons/multilingual/punctuation_signs.txt");
            if (!Files.exists(pathResource)) {
                System.out.println("error the file on punctuation signs not found");
            }
            br = Files.newBufferedReader(pathResource, StandardCharsets.UTF_8);
            // we load the punctuation signs and their valence
            List<String> patternsOfInterestAsTSV = br.lines().collect(Collectors.toList());
            for (String patternOfInterestAsTSV : patternsOfInterestAsTSV) {
                String[] elements = patternOfInterestAsTSV.split("\t");
                if (elements.length < 2) {
                    System.out.println("error in an punctuatiin line, too short:");
                    System.out.println(patternOfInterestAsTSV);
                }
                switch (elements[1]) {

                    case "positive":
                        punctuationSignsWithPositiveValence.add(elements[0]);
                        break;
                    case "negative":
                        punctuationSignsWithNegativeValence.add(elements[0]);
                        break;
                }
            }
            br.close();
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public static List<ResultOneHeuristics> check(Punctuation punctuation) {
        List<ResultOneHeuristics> resultsHeuristics = new ArrayList();

        if (punctuationSignsWithPositiveValence.contains(punctuation.getOriginalForm())) {
            ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._11, punctuation);
            resultsHeuristics.add(resultOneHeuristics);
        }
        if (punctuationSignsWithNegativeValence.contains(punctuation.getOriginalForm())) {
            ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._12, punctuation);
            resultsHeuristics.add(resultOneHeuristics);
        }
        return resultsHeuristics;

    }

}
