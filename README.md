# Conway's Game of Life — `Project1` (Java)

A minimal, test-driven implementation of Conway’s Game of Life for a 2D boolean grid.  
The core class **`Project1`** implements a course-provided **`itsc2214.GameOfLife`** interface and is covered by JUnit tests in **`Project1Test.java`**.

> Built for quick grading and clear diffs: small API surface, deterministic tests, and no external frameworks beyond JUnit 4.

---

## Features

- **Two constructors**: default size and explicit `rows × cols`.
- **Board loading** from a compact string header + rows (e.g., `"3 3\n.O.\nOOO\n...\n"`).
- **Random initialization** with probability clamps (staying in `[0,1]`).
- **Core rules**: next generation via classic Life rules (B3/S23).
- **Utilities**: neighbor counting, bounds-safe `isAlive`, still-life detection.
- **JUnit test suite** covering parsing, bounds, rules, oscillators, and edge cases.

---

## Project Layout

```
Project1.java          # Implementation (implements itsc2214.GameOfLife)
Project1Test.java      # JUnit 4 tests
```

> Note: The interface `itsc2214.GameOfLife` must be available on your classpath (usually provided by your course).

---

## Quick Start (no build tool)

1) **Get JUnit jars** (4.13.2 + Hamcrest 1.3) into a `lib/` folder:

- `junit-4.13.2.jar`
- `hamcrest-core-1.3.jar`

2) **Compile** (Mac/Linux):
```bash
javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar Project1.java Project1Test.java
```

On Windows (PowerShell/CMD), use `;` instead of `:`:
```bat
javac -cp .;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar Project1.java Project1Test.java
```

> If your course provides an `itsc2214` jar (e.g., `itsc2214.jar`), add it to the classpath the same way.

3) **Run tests** (Mac/Linux):
```bash
java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore Project1Test
```

Windows:
```bat
java -cp .;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar org.junit.runner.JUnitCore Project1Test
```

---

## Public API (selected)

```java
// Constructors
public Project1();
public Project1(int row, int col);

// Board setup
public void randomInitialize(double liveProbability);
public void loadFromString(String spec);
public void loadFromFile(File f) throws FileNotFoundException;

// Simulation
public void nextGeneration();
public int  countLiveNeighbors(int r, int c);

// Introspection
public boolean isAlive(int r, int c);
public int  numRows();
public int  numCols();
public boolean isStillLife();
```

**String format for `loadFromString`:**
- Header: `"<rows> <cols>\n"`  (one or two integers accepted by tests)
- Then `rows` lines of `'.'` (dead) and `'O'` (alive).  
Example:
```text
3 3
.O.
OOO
...
```

---

## Example: tiny driver

If you want to visualize a few generations without JUnit, create a `Main.java` like:

```java
public class Main {
    public static void main(String[] args) {
        Project1 g = new Project1(5, 5);
        g.loadFromString(
            "5 5\n" +
            ".....\n" +
            "..O..\n" +
            "..O..\n" +
            "..O..\n" +
            ".....\n"
        );
        for (int step = 0; step < 4; step++) {
            print(g, step);
            g.nextGeneration();
        }
    }
    static void print(Project1 g, int gen) {
        System.out.println("Generation " + gen);
        for (int r = 0; r < g.numRows(); r++) {
            for (int c = 0; c < g.numCols(); c++) {
                System.out.print(g.isAlive(r, c) ? 'O' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
}
```

Compile & run:
```bash
javac Main.java Project1.java
java Main
```

---

## Notes & Assumptions

- **Interface dependency**: `itsc2214.GameOfLife` must be provided by your environment; add it to the classpath if it’s packaged as a jar.
- **Determinism**: tests avoid randomness by using `loadFromString`; use `randomInitialize` only for manual experiments.
- **I/O**: `loadFromFile(File)` expects the same text format as `loadFromString`.

---

## Contributing

- Keep PRs small and focused.
- Add/extend JUnit tests when you modify behavior.
- Prefer pure Java & standard libs (no framework creep).

---

## License

Add your preferred license (e.g., MIT) here.
