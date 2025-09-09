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



