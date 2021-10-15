package edu.ucr.cs.CartilageFramework;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        switch (args[0]) {
            case "fuzzy": {

                SetSimilarityJoin joiner = new SetSimilarityJoin(0.5);
                Cartilage c = new Cartilage();
                List<String> r = new ArrayList<>();
                List<String> s = new ArrayList<>();
                try (Stream<String> stream = Files.lines(Paths.get("ARSummary4.csv"), StandardCharsets.UTF_8)) {
                    stream.forEach(r::add);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (Stream<String> stream = Files.lines(Paths.get("ARSummary3.csv"), StandardCharsets.UTF_8)) {
                    stream.forEach(s::add);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<Pair<String, String>> results = c.flexibleJoin(r, s, joiner);
                System.out.println(results.size());
                HashSet<String> uniques = new HashSet<>();
                float totalsim = 0.0f;
                for (Pair<String, String> p : results) {
                    System.out.println(p.k + " / " + p.v + "("+Utilities.calculateJaccardSimilarityS(p.k,p.v )+")");
                    uniques.add(p.k + " / " + p.v);
                    totalsim += Utilities.calculateJaccardSimilarityS(p.k,p.v );

                }
                System.out.println(uniques.size());

                break;
            }
            case "spatial": {
                SpatialJoin joiner = new SpatialJoin();
                Cartilage c = new Cartilage();

                List<Rectangle> r = new ArrayList<>();
                List<Rectangle> s = new ArrayList<>();

                try (BufferedReader br = new BufferedReader(new FileReader("Rectangles1.csv"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] lineA = line.split(",");
                        Rectangle rectangle = new Rectangle();
                        rectangle.x1 = Double.parseDouble(lineA[0]);
                        rectangle.y1 = Double.parseDouble(lineA[1]);
                        rectangle.x2 = Double.parseDouble(lineA[2]);
                        rectangle.y2 = Double.parseDouble(lineA[3]);
                        r.add(rectangle);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try (BufferedReader br = new BufferedReader(new FileReader("Rectangles2.csv"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] lineA = line.split(",");
                        Rectangle rectangle = new Rectangle();
                        rectangle.x1 = Double.parseDouble(lineA[0]);
                        rectangle.y1 = Double.parseDouble(lineA[1]);
                        rectangle.x2 = Double.parseDouble(lineA[2]);
                        rectangle.y2 = Double.parseDouble(lineA[3]);
                        s.add(rectangle);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                List<Pair<Rectangle, Rectangle>> results = c.flexibleJoin(r, s, joiner);
                System.out.println(results.size());
                for (Pair<Rectangle, Rectangle> p : results) {
                    System.out.println(p.k.x1 +","+p.k.y1 +" " + p.k.y2 +","+p.k.y2 + "|" + p.v.x1 +","+p.v.y1 +" " + p.v.y2 +","+p.v.y2);
                }
                break;
            }
            default: break;

        }

    }
}
