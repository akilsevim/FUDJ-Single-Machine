package edu.ucr.cs.CartilageFramework;

class Rectangle {
    double x1, x2, y1, y2;
}
class SpatialJoinConfiguration implements Configuration {
    Rectangle Grid;
}
class computeMBR implements Summary<Rectangle> {

    @Override
    public void add(Rectangle k) {

    }

    @Override
    public void add(Summary<Rectangle> s) {

    }
}

public class SpatialJoin implements FlexibleJoin<Rectangle, SpatialJoinConfiguration>{
    @Override
    public Summary<Rectangle> createSummarizer1() {
        return new computeMBR();
    }

    @Override
    public SpatialJoinConfiguration divide(Summary<Rectangle> s1, Summary<Rectangle> s2) {
        return null;
    }

    @Override
    public int[] assign1(Rectangle k1, SpatialJoinConfiguration spatialJoinConfiguration) {
        return new int[0];
    }

    @Override
    public boolean verify(int b1, Rectangle k1, int b2, Rectangle k2, SpatialJoinConfiguration spatialJoinConfiguration) {
        return false;
    }
}
