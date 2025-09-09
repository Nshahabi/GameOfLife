import itsc2214.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;

/**
 * Tests for Project1 for size, parsing, bounds, rules, oscillators, still-life,
 * file loading, default sizing, and probability clamping.
 */
public class Project1Test {

    /** Fresh 3x3 board baseline. */
    private Project1 runner;

    /** Build a 3x3 board before each test. */
    @Before
    public void setup() {
        runner = new Project1(3, 3);
    }

    /** Constructor sets size. */
    @Test
    public void testCtorSetsSize() {
        assertEquals(3, runner.numRows());
        assertEquals(3, runner.numCols());
    }

    /** Single cell dies. */
    @Test
    public void testSingleCellDies() {
        runner.loadFromString(
            "3 3\n" +
            "O..\n" +
            "...\n" +
            "...\n"
        );
        assertTrue(runner.isAlive(0, 0));
        runner.nextGeneration();
        assertFalse(runner.isAlive(0, 0));
    }

    /** Loading from string sets size and reads pattern. */
    @Test
    public void testLoadFromStringSetsSize() {
        Project1 g = new Project1();
        g.loadFromString(
            "4 5\n" +
            ".....\n" +
            ".OOO.\n" +
            ".....\n" +
            ".....\n"
        );
        assertEquals(4, g.numRows());
        assertEquals(5, g.numCols());
    }

    /** Out of bounds returns false. */
    @Test
    public void testIsAliveBounds() {
        runner.loadFromString(
            "3 3\n" +
            "...\n" +
            ".O.\n" +
            "...\n"
        );
        assertFalse(runner.isAlive(-1, 0));
        assertFalse(runner.isAlive(0, -1));
        assertFalse(runner.isAlive(3, 0));
        assertFalse(runner.isAlive(0, 3));
    }

    /** Survival with exactly two neighbors. */
    @Test
    public void testSurvivalTwoNeighbors() {
        runner.loadFromString(
            "3 3\n" +
            ".OO\n" +
            ".O.\n" +
            "...\n"
        );
        assertTrue(runner.isAlive(1, 1));
        runner.nextGeneration();
        assertTrue(runner.isAlive(1, 1));
    }

    /** Overcrowding kills center of 3x3 full block. */
    @Test
    public void testOvercrowdingDeath() {
        runner.loadFromString(
            "3 3\n" +
            "OOO\n" +
            "OOO\n" +
            "OOO\n"
        );
        runner.nextGeneration();
        assertFalse(runner.isAlive(1, 1));
    }

    /** Blinker oscillator flips every step. */
    @Test
    public void testBlinkerOscillator() {
        Project1 g = new Project1();
        g.loadFromString(
            "5 5\n" +
            ".....\n" +
            "..O..\n" +
            "..O..\n" +
            "..O..\n" +
            ".....\n"
        );
        g.nextGeneration();
        assertTrue(g.isAlive(2, 1));
        assertTrue(g.isAlive(2, 2));
        assertTrue(g.isAlive(2, 3));
        assertFalse(g.isAlive(1, 2));
        assertFalse(g.isAlive(3, 2));
        g.nextGeneration();
        assertTrue(g.isAlive(1, 2));
        assertTrue(g.isAlive(2, 2));
        assertTrue(g.isAlive(3, 2));
    }

    /** 2x2 block is still life; detector returns true. */
    @Test
    public void testBlockStillLife() {
        Project1 g = new Project1();
        g.loadFromString(
            "4 4\n" +
            "....\n" +
            ".OO.\n" +
            ".OO.\n" +
            "....\n"
        );
        g.nextGeneration();
        assertTrue(g.isStillLife());
    }

    /** All-dead board remains dead and is still after one step. */
    @Test
    public void testAllDeadIsStillLife() {
        Project1 g = new Project1(4, 4);
        g.randomInitialize(0.0);
        g.nextGeneration();
        assertTrue(g.isStillLife());
        for (int r = 0; r < g.numRows(); r++) {
            for (int c = 0; c < g.numCols(); c++) {
                assertFalse(g.isAlive(r, c));
            }
        }
    }

    /** Default-size path and p-clamping branches in  randomInitialize. */
    @Test
    public void testRandomInitializeBranches() {
        Project1 g = new Project1();
        g.randomInitialize(-1.0);
        assertEquals(10, g.numRows());
        assertEquals(10, g.numCols());
        g.randomInitialize(2.0);
        assertEquals(10, g.numRows());
    }

    /** loadFromFile path using a temporary file. */
    @Test
    public void testLoadFromFile() throws Exception {
        File tmp = File.createTempFile("gol", ".txt");
        tmp.deleteOnExit();
        try (FileWriter fw = new FileWriter(tmp)) {
            fw.write("3 3\n");
            fw.write("O..\n");
            fw.write(".O.\n");
            fw.write("..O\n");
        }
        Project1 g = new Project1();
        g.loadFromFile(tmp.getAbsolutePath());
        assertEquals(3, g.numRows());
        assertEquals(3, g.numCols());
        assertTrue(g.isAlive(0, 0));
        assertTrue(g.isAlive(1, 1));
        assertTrue(g.isAlive(2, 2));
    }

    /** isStillLife() before any  generation: previous == null path. */
    @Test
    public void testStillLifeNoPrevious() {
        Project1 g = new Project1();
        g.loadFromString(
            "2 2\n" +
            "..\n" +
            "..\n"
        );
        assertFalse(g.isStillLife());
    }

    /** Header with one int only: cols become 0; rows set. */
    @Test
    public void testHeaderOneIntOnly() {
        Project1 g = new Project1();
        g.loadFromString(
            "3\n" +
            "...\n"
        );
        assertEquals(3, g.numRows());
        assertEquals(0, g.numCols());
    }
}
