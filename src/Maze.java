import java.util.*;
/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {
    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet d = new DisjointSet(w.cells());
        int[] result = new int[w.count()];
        int broken = 0;
        for (int k = 0; k < w.count(); k++) {
            if (d.union(w.a()[k], w.b()[k])) {
                result[broken] = k;
                broken++;
            }
        }
        return Arrays.copyOf(result, broken);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for (int k = 0; k < w.count(); k++) order[k] = k;

        Arrays.sort(order, (x, y) -> Integer.compare(w.cost()[x], w.cost()[y]));

        DisjointSet d = new DisjointSet(w.cells());
        int[] result = new int[w.count()];
        int broken = 0;
        for (int k : order) {
            if (d.union(w.a()[k], w.b()[k])) {
                result[broken] = k;
                broken++;
            }
        }
        return Arrays.copyOf(result, broken);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();


        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int k = 0; k < w.count(); k++) {
            adj.get(w.a()[k]).add(k);
            adj.get(w.b()[k]).add(k);
        }

        boolean[] inside = new boolean[n];
        PriorityQueue<int[]> queue = new PriorityQueue<>((p, q) -> Integer.compare(p[0], q[0]));
        long total = 0;


        queue.add(new int[]{0, 0});

        while (!queue.isEmpty()) {
            int[] top = queue.poll();
            int price = top[0];
            int cell = top[1];

            if (inside[cell]) {
                continue;
            }

            inside[cell] = true;
            total = total + price;

            List<Integer> walls = adj.get(cell);
            for (int i = 0; i < walls.size(); i++) {
                int k = walls.get(i);

                int other;
                if (w.a()[k] == cell) {
                    other = w.b()[k];
                } else {
                    other = w.a()[k];
                }

                if (!inside[other]) {
                    queue.add(new int[]{w.cost()[k], other});
                }
            }
        }
        return total;
    }
}
