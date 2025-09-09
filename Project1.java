import itsc2214.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * Conway's Game of Life on a 2D boolean grid.
 * Implements GameOfLife interface used in this class.
 */
public class Project1 implements GameOfLife {

    /** Current generation. */
    private boolean[][] current;
    /** Previous generation. */
    private boolean[][] previous;
    /** Grid size. */
    private int rows, cols;

    /** Builds an empty board. size set later by loaders. */
    public Project1() {
        rows = 0;
        cols = 0;
        current = new boolean[0][0];
        previous = null;
    }

    /**
     * Builds a board with fixed size  all cells dead.
     * @param row rows (negatives become 0)
     * @param col cols (negatives become 0)
     */
    public Project1(int row, int col) {
        int r = row;
        if (r < 0) { r = 0; }
        int c = col;
        if (c < 0) { c = 0; }

        rows = r;
        cols = c;
        current = new boolean[rows][cols];
        previous = null;
    }

    /**
     * Randomly sets each cell alive with probability p in [0,1].
     * If size is not set a 10x10 board is created.
     * @param aliveProbability probability a cell starts alive
     */
    @Override
    public void randomInitialize(double aliveProbability) {
        if (rows == 0 || cols == 0) {
            rows = 10;
            cols = 10;
            current = new boolean[rows][cols];
        }
        double p = aliveProbability;
        if (p < 0.0) { p = 0.0; }
        if (p > 1.0) { p = 1.0; }

        Random rng = new Random();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                current[r][c] = rng.nextDouble() < p;
            }
        }
        previous = null;
    }

    /**
     * Loads a grid from a string.
     * First line "rows cols". Then {@code rows} lines of '.' or 'O'.
     * Extra chars ignored. short lines assumed dead.
     * @param data textual grid
     */
    @Override
    public void loadFromString(String data) {
        Scanner sc = new Scanner(data);
        int newR = 0;
        int newC = 0;
        if (sc.hasNextInt()) { newR = sc.nextInt(); }
        if (sc.hasNextInt()) { newC = sc.nextInt(); }
        if (sc.hasNextLine()) { sc.nextLine(); }

        boolean[][] grid = new boolean[newR][newC];
        for (int r = 0; r < newR && sc.hasNextLine(); r++) {
            String line = sc.nextLine();
            for (int c = 0; c < newC && c < line.length(); c++) {
                grid[r][c] = line.charAt(c) == 'O';
            }
        }
        sc.close();

        rows = newR;
        cols = newC;
        previous = null;
        current = grid;
    }

    /**
     * Loads a grid from file. delegates to {@link #loadFromString(String)}.
     * @param filename path to text file
     * @throws FileNotFoundException if file cannot be opened
     */
    @Override
    public void loadFromFile(String filename) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append('\n');
        }
        sc.close();
        loadFromString(sb.toString());
    }

    /**
     * Counts live neighbors of (r,c) in the 8-cell neighborhood.
     * @param r row
     * @param c col
     * @return number of live neighbors
     */
    @Override
    public int countLiveNeighbors(int r, int c) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) { continue; }
                int rr = r + dr;
                int cc = c + dc;
                boolean in = rr >= 0 && rr < rows && cc >= 0 && cc < cols;
                if (in && current[rr][cc]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Advances one generation by Conway's rules.
     * Alive stays alive with 2 or 3 neighbors. Dead births with 3.
     */
    @Override
    public void nextGeneration() {
        if (rows == 0 || cols == 0) { return; }

        boolean[][] next = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int n = countLiveNeighbors(r, c);
                if (current[r][c]) {
                    next[r][c] = n == 2 || n == 3;
                } else {
                    next[r][c] = n == 3;
                }
            }
        }
        previous = current;
        current = next;
    }

    /**
     * True if (r,c) is alive; false for out-of-bounds.
     * @param r row
     * @param c col
     * @return cell state
     */
    @Override
    public boolean isAlive(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) { return false; }
        return current[r][c];
    }

    /** @return number of rows */
    @Override
    public int numRows() {
        return rows;
    }

    /** @return number of columns */
    @Override
    public int numCols() {
        return cols;
    }

    /**
     * True if current grid equals previous grid =  no change.
     * False when there is no previous generation.
     * @return still-life flag
     */
    @Override
    public boolean isStillLife() {
        boolean shapeMismatch =
            previous == null
            || previous.length != rows
            || (rows > 0 && previous[0].length != cols);

        if (shapeMismatch) {
            return false;
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (current[r][c] != previous[r][c]) { return false; }
            }
        }
        return true;
    }
}
