/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int cols = w.cols();
        int n = w.cells();
        int exit = n - 1;
        int[][] g = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (g[i].length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] next = new int[n];
        int[] stack = new int[n];
        int top = 0;
        int count = 0;

        stack[top] = 0;
        top++;
        visited[0] = true;
        parent[0] = -1;
        count = 1;

        while (top > 0 && !visited[exit]) {
            int cell = stack[top - 1];

            if (next[cell] == 4) {
                top--;
                continue;
            }

            int dir = next[cell];
            next[cell]++;

            int nb = neighbor(cell, dir, cols, n);
            if (nb == -1) continue;
            if (!isOpen(g, cell, nb)) continue;
            if (visited[nb]) continue;

            visited[nb] = true;
            count++;
            parent[nb] = cell;
            stack[top] = nb;
            top++;
        }


        int length = 0;
        int c = exit;
        while (c != -1) {
            length++;
            c = parent[c];
        }
        int[] path = new int[length];
        c = exit;
        for (int i = length - 1; i >= 0; i--) {
            path[i] = c;
            c = parent[c];
        }

        return new Result(path, count, deadEnds);
    }

    private static int neighbor(int cell, int dir, int cols, int n) {
        if (dir == 0) {
            if (cell - cols >= 0) return cell - cols;
        } else if (dir == 1) {
            if (cell % cols != cols - 1) return cell + 1;
        } else if (dir == 2) {
            if (cell + cols < n) return cell + cols;
        } else {
            if (cell % cols != 0) return cell - 1;
        }
        return -1;
    }

    private static boolean isOpen(int[][] g, int cell, int nb) {
        for (int i = 0; i < g[cell].length; i++) {
            if (g[cell][i] == nb) {
                return true;
            }
        }
        return false;
    }
}
