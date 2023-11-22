/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Emoji;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;
import net.clementlevallois.umigon.model.TextFragment;
import net.fellbaum.jemoji.EmojiManager;
import net.fellbaum.jemoji.EmojiSubGroup;

/**
 *
 * @author LEVALLOIS
 */
public class EmojisHeuristicsandResourcesLoader {

    private static Set<String> setNegativeEmojis;
    private static Set<String> setPositiveEmojis;
    private static Set<String> setHyperSatisfactionEmojis;
    private static Set<String> setNeutralEmojis;
    private static Set<String> setIntensityEmojis;
    private static Set<EmojiSubGroup> setEmojiSubGroupsPositive;
    private static Set<EmojiSubGroup> setEmojiSubGroupsNegative;
    private static Set<net.fellbaum.jemoji.Emoji> setPositiveEmojiFellbaum;
    private static Set<net.fellbaum.jemoji.Emoji> setNegativeEmojiFellbaum;

    public static void load() throws FileNotFoundException, IOException {
        if (setNegativeEmojis != null && setPositiveEmojis != null && setNeutralEmojis != null && setIntensityEmojis != null) {
            return;
        }
        setNegativeEmojis = new HashSet();
        setPositiveEmojis = new HashSet();
        setNeutralEmojis = new HashSet();
        setIntensityEmojis = new HashSet();
        setHyperSatisfactionEmojis = new HashSet();
        setEmojiSubGroupsPositive = Set.of(EmojiSubGroup.MUSIC, EmojiSubGroup.FACE_SMILING, EmojiSubGroup.CAT_FACE, EmojiSubGroup.AWARD_MEDAL);
        setEmojiSubGroupsNegative = Set.of(EmojiSubGroup.FACE_CONCERNED, EmojiSubGroup.FACE_NEGATIVE, EmojiSubGroup.FACE_NEUTRAL_SKEPTICAL, EmojiSubGroup.FACE_UNWELL, EmojiSubGroup.WARNING);
        setPositiveEmojiFellbaum = new HashSet();
        setNegativeEmojiFellbaum = new HashSet();

        for (EmojiSubGroup emojiSubGroup : setEmojiSubGroupsPositive) {
            setPositiveEmojiFellbaum.addAll(EmojiManager.getAllEmojisBySubGroup(emojiSubGroup));
        }
        for (EmojiSubGroup emojiSubGroup : setEmojiSubGroupsNegative) {
            setNegativeEmojiFellbaum.addAll(EmojiManager.getAllEmojisBySubGroup(emojiSubGroup));
        }

        String fileSeparator = FileSystems.getDefault().getSeparator();
        String PATHLOCALE = ResourcePath.returnRootResources() + "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator + "net" + fileSeparator + "clementlevallois" + fileSeparator + "umigon" + fileSeparator + "lexicons" + fileSeparator + "multilingual" + fileSeparator + "emojis.txt";
        BufferedReader br = Files.newBufferedReader(Path.of(PATHLOCALE), StandardCharsets.UTF_8);

        List<String> patternsOfInterestAsTSV = br.lines().collect(Collectors.toList());
        for (String patternOfInterestAsTSV : patternsOfInterestAsTSV) {
            String[] elements = patternOfInterestAsTSV.split("\t");
            if (elements.length < 2) {
                System.out.println("error in an emoji line, too short:");
                System.out.println(patternOfInterestAsTSV);
            }
            switch (elements[1]) {

                case "positive":
                    setPositiveEmojis.add(elements[0]);
                    break;
                case "negative":
                    setNegativeEmojis.add(elements[0]);
                    break;
                case "neutral":
                    setNeutralEmojis.add(elements[0]);
                    break;
                case "hyper-satisfaction":
                    setHyperSatisfactionEmojis.add(elements[0]);
                    break;
                case "intensity":
                    setIntensityEmojis.add(elements[0]);
                    break;
            }
        }
    }

    public static Set<String> getSetNegativeEmojis() {
        return setNegativeEmojis;
    }

    public static Set<String> getSetPositiveEmojis() {
        return setPositiveEmojis;
    }

    public static Set<String> getSetNeutralEmojis() {
        return setNeutralEmojis;
    }

    public static Set<String> getSetIntensityEmojis() {
        return setIntensityEmojis;
    }

    public static Set<String> getSetHyperSatisfactionEmojis() {
        return setHyperSatisfactionEmojis;
    }

    public static List<ResultOneHeuristics> containsAffectiveEmojis(List<TextFragment> textFragments) {
        List<ResultOneHeuristics> resultsHeuristics = new ArrayList();

        for (TextFragment textFragment : textFragments) {
            if (textFragment instanceof Emoji) {
                Emoji emoji = (Emoji) textFragment;
                String emojiAsString = emoji.getSemiColonForm();
                ResultOneHeuristics resultOneHeuristics = null;
                if (setNegativeEmojis.contains(emojiAsString)) {
                    resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._12, textFragment);
                } else if (setPositiveEmojis.contains(emojiAsString)) {
                    resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._11, textFragment);
                } else if (setHyperSatisfactionEmojis.contains(emojiAsString)) {
                    resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._17, textFragment);
                } else {
                    Optional<net.fellbaum.jemoji.Emoji> emojiOptional = EmojiManager.getByAlias(emojiAsString);
                    if (emojiOptional.isPresent()) {
                        if (setNegativeEmojiFellbaum.contains(emojiOptional.get())) {
                            resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._12, textFragment);
                        } else if (setPositiveEmojiFellbaum.contains(emojiOptional.get())) {
                            resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._11, textFragment);
                        }
                    }
                }
                if (resultOneHeuristics != null) {
                    resultsHeuristics.add(resultOneHeuristics);
                }
            }
        }
        return resultsHeuristics;
    }
}
