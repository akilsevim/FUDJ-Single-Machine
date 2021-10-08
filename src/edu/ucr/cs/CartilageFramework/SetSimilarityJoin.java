package edu.ucr.cs.CartilageFramework;

import java.util.HashMap;
import java.util.Map;

public class SetSimilarityJoin implements FlexibleJoin{
    @Override
    public Summary<String> createSummarizer1() {
        return new WordCount();
    }

    @Override
    public Object divide(Summary s1, Summary s2) {

    }

    @Override
    public int[] assign1(Object k1, Object o) {
        return new int[0];
    }

    @Override
    public boolean match(int b1, int b2) {
        return FlexibleJoin.super.match(b1, b2);
    }

    @Override
    public boolean verify(int b1, Object k1, int b2, Object k2, Object o) {
        return false;
    }

}

class WordCount implements Summary<String> {

    Map<String, Integer> WordCountMap = new HashMap<>();

    @Override
    public void add(Summary<String> s) {

    }

    @Override
    public void add(String k) {
        String[] tokens = k.split(" ");
        for(String token:tokens) {
            WordCountMap.merge(token, 1, Integer::sum);
        }

    }
}
