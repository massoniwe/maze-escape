/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        // TODO: add your own checks here
        // itself
        DisjointSet d2 = new DisjointSet(5);
        check(!d2.union(2,2), "union(2;2) returns false");
        check(d2.count()==5, "count stays 5 after union(2;2)");

        DisjointSet d3 = new DisjointSet(6);
        d3.union(0, 1); d3.union(2, 3); d3.union(0, 2);
        check(!d3.union(1, 3), "union(1;3) returns false");        // допиши описание
        check(d3.count() == 3, "count stays 3 after the failed union(1, 3)");        // допиши описание

        DisjointSet d1 = new DisjointSet(100000);
        for(int i=0;i<99999;i++){
            d1.union(i,i+1);
        }
        check(d1.count()==1, "chain of 100000 cells is one set");
        check(d1.connected(0,99999), "first and last cell of the chain are connected");


        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
