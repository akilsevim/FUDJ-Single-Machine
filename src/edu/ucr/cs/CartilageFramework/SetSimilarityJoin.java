package edu.ucr.cs.CartilageFramework;

import java.util.HashMap;
import java.util.Map;

public class SetSimilarityJoin implements FlexibleJoin<String, SetSimilarityConfig>{
    SetSimilarityConfig Configuration;
    @Override
    public Summary<String> createSummarizer1() {
        return new WordCount();
    }

    @Override
    public SetSimilarityConfig divide(Summary<String> s1, Summary<String> s2) {
        return null;
    }

    @Override
    public int[] assign1(String k1, SetSimilarityConfig setSimilarityConfig) {
        return new int[0];
    }

    @Override
    public boolean match(int b1, int b2) {
        return FlexibleJoin.super.match(b1, b2);
    }

    @Override
    public boolean verify(int b1, String k1, int b2, String k2, SetSimilarityConfig setSimilarityConfig) {
        return false;
    }

}

class WordCount implements Summary<String> {

    Map<String, Integer> WordCountMap = new HashMap<>();


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

}