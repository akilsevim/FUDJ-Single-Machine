package edu.ucr.cs.CartilageFramework;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SetSimilarityJoin joiner = new SetSimilarityJoin();
        Cartilage c = new Cartilage();
        List<String> r = new ArrayList<>();
        r.add("test is a good test to test something bad");
        r.add("test is a bad test to test something good");

        List<String> s = new ArrayList<>();

        s.add("test is a good test to test something bad");
        s.add("test is a bad test to test something good");

        c.flexibleJoin(r, s, joiner);
    }
}
