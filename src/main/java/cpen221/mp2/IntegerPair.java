package cpen221.mp2;

public class IntegerPair {

    /*
        Abstraction Function:
            key = K(int), all integers valid.
            value = V(int), all integers valid.
        Rep invariant:
            IntegerPair is not null.
            IntegerPair is immutable.

        notes:  checkRep unnecessary because IntegerPair only
                deals with primitive integers,there will be an
                error if null is passed in.
     */

    private int key;
    private int value;

    public IntegerPair(int key, int value) {
        this.key = key;
        this.value = value;
    }

    protected int getKey() {
        return this.key;
    }

    protected int getValue() {
        return this.value;
    }
}
