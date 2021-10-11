package edu.ucr.cs.CartilageSetSimilarityJoin;

import edu.ucr.cs.CartilageFramework.Configuration;
import edu.ucr.cs.CartilageFramework.FlexibleJoin;
import edu.ucr.cs.CartilageFramework.Summary;

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
        String[] tokens = k1.split(" ");
        int length = tokens.length;
        int[] ranks = new int[length];
        for(int i = 0; i < length; i++) {
            ranks[i] = setSimilarityConfig.S.indexOf(tokens[i]);
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
        JaccardSimilarity js = new JaccardSimilarity();
        Double s = js.apply(k1, k2);
        return js.apply(k1, k2) >= SimilarityThreshold;
    }

}

class WordCount implements Summary<String>{

    public Map<String, Integer> WordCountMap = new HashMap<>();

    @Override
    public void add(String k) {
        String[] tokens = k.split(" ");
        for(String token:tokens) {
            WordCountMap.merge(token, 1, Integer::sum);
        }
    }

    @Override
    public void add(Summary<String> s) {

    }


}

class SetSimilarityConfig implements Configuration {
    ArrayList<String> S = new ArrayList<>();
    SetSimilarityConfig(String[] S) {
        Collections.addAll(this.S, S);
    }
}
