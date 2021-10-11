package edu.ucr.cs.CartilageSetSimilarityJoin;

import edu.ucr.cs.CartilageFramework.Cartilage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        SetSimilarityJoin joiner = new SetSimilarityJoin(0.5);
        Cartilage c = new Cartilage();
        List<String> r = new ArrayList<>();
        List<String> s = new ArrayList<>();
        try (Stream<String> stream = Files.lines( Paths.get("ARSummary4.csv"), StandardCharsets.UTF_8))
        {
            stream.forEach(r::add);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try (Stream<String> stream = Files.lines( Paths.get("ARSummary3.csv"), StandardCharsets.UTF_8))
        {
            stream.forEach(s::add);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        List<Pair<String, String>> results = c.flexibleJoin(r, s, joiner);
        System.out.println(results.size());
        for(Pair<String, String> p: results) {
            System.out.println(p.k + " / " + p.v);
        }
    }
}
