import java.util.*;
/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */
public class Zones {

    /**
     * Mission 4. Kosaraju. Returns zoneOf: zoneOf[cell] = number of the zone, numbers 0 .. zones-1.
     * Number the zones in the order in which the second pass finds them. That order is a topological order
     * of the zone graph: every slide between two zones goes from a smaller number to a larger number.
     *
     * Plan: 1) DFS on the slides, write the cells in the order in which they are finished (use a stack, not recursion);
     * 2) go through the cells from the last finished to the first; if a cell has no zone yet, DFS from it on the reversed
     * slides (slidesInto) and give every cell it reaches the next zone number.
     * Is DAG in Main is 1 when the number of zones equals the number of cells (no cycle), so you do not need a separate check.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] out = Graphs.slidesFrom(s);
        int[][] in = Graphs.slidesInto(s);
        int[] stack = new int[n];


        boolean[] seen = new boolean[n];
        int[] pos = new int[n];
        int[] order = new int[n];
        int finished = 0;

        for (int start = 0; start < n; start++) {
            if (seen[start]) {
                continue;
            }
            int top = 0;
            stack[top] = start;
            top++;
            seen[start] = true;

            while (top > 0) {
                int cell = stack[top - 1];
                if (pos[cell] < out[cell].length) {
                    int slide = out[cell][pos[cell]];
                    pos[cell]++;
                    int next = s.to()[slide];
                    if (!seen[next]) {
                        seen[next] = true;
                        stack[top] = next;
                        top++;
                    }
                } else {
                    order[finished] = cell;
                    finished++;
                    top--;
                }
            }
        }


        int[] zoneOf = new int[n];
        for (int i = 0; i < n; i++) {
            zoneOf[i] = -1;
        }
        int zones = 0;

        for (int i = n - 1; i >= 0; i--) {
            int start = order[i];
            if (zoneOf[start] != -1) {
                continue;
            }
            int top = 0;
            stack[top] = start;
            top++;
            zoneOf[start] = zones;

            while (top > 0) {
                top--;
                int cell = stack[top];
                for (int j = 0; j < in[cell].length; j++) {
                    int prev = s.from()[in[cell][j]];
                    if (zoneOf[prev] == -1) {
                        zoneOf[prev] = zones;
                        stack[top] = prev;
                        top++;
                    }
                }
            }
            zones++;
        }
        return zoneOf;
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Go through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        List<List<Integer>> byZone = new ArrayList<>();
        for (int z = 0; z < zoneCount; z++) {
            byZone.add(new ArrayList<>());
        }
        for (int i = 0; i < s.count(); i++) {
            int fromZone = zoneOf[s.from()[i]];
            byZone.get(fromZone).add(i);
        }

        long INF = Long.MAX_VALUE;
        long[] dist = new long[zoneCount];
        int[] stages = new int[zoneCount];
        for (int z = 0; z < zoneCount; z++) {
            dist[z] = INF;
            stages[z] = 1;
        }
        int startZone = zoneOf[0];
        int exitZone = zoneOf[s.n() - 1];
        dist[startZone] = 0;


        for (int x = 0; x < zoneCount; x++) {
            List<Integer> slides = byZone.get(x);
            for (int j = 0; j < slides.size(); j++) {
                int i = slides.get(j);
                int y = zoneOf[s.to()[i]];
                if (y == x) {
                    continue;
                }

                if (stages[x] + 1 > stages[y]) {
                    stages[y] = stages[x] + 1;
                }

                if (dist[x] != INF) {
                    long cost = dist[x] + s.time()[i];
                    if (cost < dist[y]) {
                        dist[y] = cost;
                    }
                }
            }
        }

        int longest = 0;
        for (int z = 0; z < zoneCount; z++) {
            if (stages[z] > longest) {
                longest = stages[z];
            }
        }
        return new long[]{longest, dist[exitZone]};
    }
}
