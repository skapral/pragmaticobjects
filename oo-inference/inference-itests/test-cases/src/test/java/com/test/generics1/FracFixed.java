package com.test.generics1;


public class FracFixed implements Fraction {
    private final int num;
    private final int denum;

    public FracFixed(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Override
    public final int numerator() {
        return num;
    }

    @Override
    public final int denumenator() {
        return denum;
    }
}
