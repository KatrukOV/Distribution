# Distribution

## Problem Description
The problem is to solve the 'steady state' heat equation on a rectangular region, representing a plate heated along three edges to 100°C, and cooled to 0°C along the fourth edge.

### Grid and Nodes
The region is covered with a grid of M by N nodes. An M by N array `W` is used to record the temperature.

### Steady State Condition
The steady state solution to the discrete heat equation satisfies the following condition at an interior grid point:
`W[Central] = (1/4) * (W[North] + W[South] + W[East] + W[West])`
where:
- `Central` is the index of the grid point.
- `North`, `South`, `East`, `West` are the indices of its immediate neighbors.

### Iterative Solution
A 'better' solution is obtained by replacing each interior point by the average of its 4 neighbors. This process is repeated until the difference between successive estimates of the solution goes towards zero (within a user-specified tolerance).

## Task Requirements
1. **Methodology**: State two possible methods that could be used to distribute this processing over multiple processors.
    - **Row/Column Striping (1D Partitioning)**: The grid is divided into horizontal or vertical strips. Each processor handles a contiguous set of rows or columns. This method is simple to implement and requires communication only with two neighbors (above and below or left and right).
    - **Block/Checkerboard Partitioning (2D Partitioning)**: The grid is divided into smaller rectangular blocks. Each processor handles a sub-grid. This method is more scalable for a large number of processors as it reduces the amount of data transferred relative to the amount of computation, although it requires communication with up to four neighbors.

2. **Implementation**: Write a program to implement a chosen method to solve the heat diffusion problem in a distributed manner.
    - **Implementation Description**:
        - The chosen method is **Row Striping**.
        - The implementation uses Java's `Thread` and `CyclicBarrier` to simulate a distributed system with 4 processors.
        - The grid is partitioned into 4 equal strips of rows. Each thread is responsible for calculating the new temperatures for its assigned strip.
        - A `CyclicBarrier` is used to synchronize threads at the end of each iteration. The barrier's action calculates the global maximum difference and checks for convergence, then swaps the "old" and "new" grids for the next iteration.
        - Boundary row data is naturally "exchanged" through the shared memory, where each thread reads the necessary boundary rows from the shared "old" grid.
    - **Testing and Verification**:
        - The system was tested by comparing the output of the parallel implementation (`PlateParallel`) with a sequential implementation (`PlateSimple`).
        - For a 20x30 grid with a tolerance of 0.0005, both versions were run until convergence.
        - The results were saved to `Temperatures_Single.json` and `Temperatures_Parallel.json`.
        - Temperature distribution images were generated as `Temperatures_Single.png` and `Temperatures_Parallel.png`.
        - The standard deviation of differences between the two solutions was calculated to be 0.0, confirming that the parallel implementation correctly reproduces the sequential results.

    - **Calculated Results**:
        - (a) Single processor: See `Temperatures_Single.json` and `Temperatures_Single.png`.
        - (b) Four processors: See `Temperatures_Parallel.json` and `Temperatures_Parallel.png`.
    - **Standard Deviation of Differences**: 0.0000000000.

