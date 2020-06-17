package pohlighellman;

import java.util.Comparator;
import java.math.BigInteger;

// For Shanks algorithim, we need to sort a list of values but not forget the elements' original order.
// Indexed is essentially a pair used to remember a BigInteger and its original index in an array after
// we sort the array
public class Indexed implements Comparator<Indexed>, Comparable<Indexed> {
    private int ind;
    private BigInteger val;
    public Indexed (int i, BigInteger v) {
        ind = i;
        val = v;
    }
    public int getI () { return ind; }
    public BigInteger getV () { return val; }
    public int compare(Indexed a, Indexed b) { return (a.getV().subtract(b.getV()).signum()); }
    public int compareTo(Indexed a) { return (this.getV().subtract(a.getV()).signum());}
}
