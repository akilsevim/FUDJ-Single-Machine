package edu.ucr.cs.CartilageFramework;

import java.util.*;
import java.util.stream.Collectors;

public class SetSimilarityJoin implements FlexibleJoin<String, SetSimilarityConfig> {
    Double SimilarityThreshold = 0.0;
    SetSimilarityJoin(Double SimilarityThreshold) {
        this.SimilarityThreshold = SimilarityThreshold;
    }
    @Override
    public Summary<String> createSummarizer1() {
        return new WordCount();
    }

    @Override
    public SetSimilarityConfig divide(Summary<String> s1, Summary<String> s2) {
        WordCount s1wc = (WordCount) s1;
        WordCount s2wc = (WordCount) s2;
        for(String token: s1wc.WordCountMap.keySet()) {
            s2wc.WordCountMap.merge(token, s1wc.WordCountMap.get(token), Integer::sum);
        }

        LinkedHashMap<String,Integer> SortedWordCountMap = s2wc.WordCountMap.entrySet().stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new SetSimilarityConfig(SortedWordCountMap.keySet().toArray(String[]::new));

    }

    @Override
    public int[] assign1(String k1, SetSimilarityConfig setSimilarityConfig) {
        ArrayList<String> tokens = Utilities.tokenizer(k1);
        int length = tokens.size();
        int[] ranks = new int[length];
        for(int i = 0; i < length; i++) {
            ranks[i] = setSimilarityConfig.S.get(tokens.get(i));
        }
        Arrays.sort(ranks);
        Double PrefixLength = (length - Math.ceil(SimilarityThreshold * length) + 1);
        return Arrays.copyOfRange(ranks, 0, PrefixLength.intValue());
    }

    @Override
    public boolean match(int b1, int b2) {
        return FlexibleJoin.super.match(b1, b2);
    }

    @Override
    public boolean verify(int b1, String k1, int b2, String k2, SetSimilarityConfig setSimilarityConfig) {
        return Utilities.calculateJaccardSimilarityS(k1, k2) >= SimilarityThreshold;
    }

}

class WordCount implements Summary<String>{

    public Map<String, Integer> WordCountMap = new HashMap<>();

    @Override
    public void add(String k) {
        ArrayList<String> tokens = Utilities.tokenizer(k);
        for(String token:tokens) {
            WordCountMap.merge(token, 1, Integer::sum);
        }
    }

    @Override
    public void add(Summary<String> s) {
        WordCount wc = (WordCount) s;
        for(String token: WordCountMap.keySet()) {
            WordCountMap.merge(token, wc.WordCountMap.get(token), Integer::sum);
        }
    }
}

class SetSimilarityConfig implements Configuration {
    HashMap<String, Integer> S = new HashMap<>();
    SetSimilarityConfig(String[] OrderedTokens) {
        for(int i = 0; i < OrderedTokens.length; i++) {
            this.S.put(OrderedTokens[i], i);
        }
    }
}

abstract class Utilities {
    public static Double calculateJaccardSimilarity(CharSequence left, CharSequence right) {
        Set<String> intersectionSet = new HashSet<String>();
        Set<String> unionSet = new HashSet<String>();
        boolean unionFilled = false;
        int leftLength = left.length();
        int rightLength = right.length();
        if (leftLength == 0 || rightLength == 0) {
            return 0d;
        }

        for (int leftIndex = 0; leftIndex < leftLength; leftIndex++) {
            unionSet.add(String.valueOf(left.charAt(leftIndex)));
            for (int rightIndex = 0; rightIndex < rightLength; rightIndex++) {
                if (!unionFilled) {
                    unionSet.add(String.valueOf(right.charAt(rightIndex)));
                }
                if (left.charAt(leftIndex) == right.charAt(rightIndex)) {
                    intersectionSet.add(String.valueOf(left.charAt(leftIndex)));
                }
            }
            unionFilled = true;
        }
        return (Double.valueOf(intersectionSet.size()) / Double.valueOf(unionSet.size()));
    }

    public static Float calculateJaccardSimilarityS(String left, String right) {
        Set<String> intersectionSet = new HashSet<String>();
        Set<String> unionSet = new HashSet<String>();
        boolean unionFilled = false;

        ArrayList<String> leftTokens = tokenizer(left);
        ArrayList<String> rightTokens = tokenizer(right);



        int leftLength = leftTokens.size();
        int rightLength = rightTokens.size();
        if (leftLength == 0 || rightLength == 0) {
            return 0f;
        }

        for (int leftIndex = 0; leftIndex < leftLength; leftIndex++) {
            unionSet.add(leftTokens.get(leftIndex));
            for (int rightIndex = 0; rightIndex < rightLength; rightIndex++) {
                if (!unionFilled) {
                    unionSet.add(rightTokens.get(rightIndex));
                }
                if (leftTokens.get(leftIndex).equals(rightTokens.get(rightIndex))) {
                    intersectionSet.add(leftTokens.get(leftIndex));
                }
            }
            unionFilled = true;
        }
        return (Float.valueOf(intersectionSet.size()) / ((leftLength + rightLength) - Float.valueOf(intersectionSet.size())));
    }

    public static ArrayList<String> tokenizer(String text) {
        ArrayList<String> tokens = new ArrayList<>();
        String lowerCaseText = text.toLowerCase();
        int startIx = 0;


        while (startIx < lowerCaseText.length()) {
            while (startIx < lowerCaseText.length() && isSeparator(lowerCaseText.charAt(startIx))) {
                startIx++;
            }
            int tokenStart = startIx;

            while (startIx < lowerCaseText.length() && !isSeparator(lowerCaseText.charAt(startIx))) {
                startIx++;
            }
            int tokenEnd = startIx;

            // Emit token.
            String token = lowerCaseText.substring(tokenStart, tokenEnd);

            if(!token.isEmpty()) tokens.add(token);
        }
        return tokens;
    }

    private static boolean isSeparator(char c) {
        return !(Character.isLetterOrDigit(c) || Character.getType(c) == Character.OTHER_LETTER
                || Character.getType(c) == Character.OTHER_NUMBER);
    }
}