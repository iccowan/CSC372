import java.io.File;

public class FindAlphadokuSoln {

    private static boolean inRowColSquare(char[][][][] puzzle,
        int i, int j, int k, int l, char c) {
        for (int j2 = 0; j2 < 5; j2++) {
            for (int l2 = 0; l2 < 5; l2++) {
                if (puzzle[i][j2][k][l2] == c)
                    return true;
            }
        }

        for (int k2 = 0; k2 < 5; k2++) {
            for (int l2 = 0; l2 < 5; l2++) {
                if (puzzle[i][j][k2][l2] == c)
                    return true;
            }
        }

        for (int i2 = 0; i2 < 5; i2++) {
            for (int j2 = 0; j2 < 5; j2++) {
                if (puzzle[i2][j2][k][l] == c)
                    return true;
            }
        }

        return false;
    }

    private static boolean checkForUniqueness(char[][][][] puzzle,
        char[][][][] originalPuzzle) {
        // We loop through the original puzzle to find all of the places that
        // we can try another character. We then run the solver on this new
        // puzzle with every letter in the alphabet. If we get a new solution,
        // the solution is not unique. If we do not get a new solution, the
        // solution is unique
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        if (originalPuzzle[i][j][k][l] == ' ') {
                            // Check for another solution
                            for (char c : AlphadokuSolver.letters) {
                                if (c == puzzle[i][j][k][l] || inRowColSquare(originalPuzzle, i, j, k, l, c))
                                    continue;
                                char[][][][] copy = deepCopyPuzzle(originalPuzzle);
                                copy[i][j][k][l] = c;
                                AlphadokuSolver solver = new AlphadokuSolver(copy);
                                if (solver.solve()) {
                                    // Print the solution
                                    solver.printPuzzle();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private static char[][][][] deepCopyPuzzle(char[][][][] puzzle) {
        char[][][][] copy = new char[5][5][5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        copy[i][j][k][l] = puzzle[i][j][k][l];
                    }
                }
            }
        }

        return copy;
    }

    public static void main(String[] args) {
        File dir = new File("puzzles/");
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Get the file path of the file
                String filePath = child.getAbsolutePath();
                String fileName = child.getName();

                // Solve the puzzle
                AlphadokuSolver solver = new AlphadokuSolver(filePath);
                solver.printSpacer();
                System.out.println("Results for " + fileName + ":");
                solver.printSpacer();
                //solver.printPuzzle();
                boolean solveable = solver.solve();
                solver.printSpacer();

                if (solveable) {
                    solver.printPuzzle();
                    solver.printSpacer();

                    // Check if the solutions are unique or not
                    if (! checkForUniqueness(solver.puzzle, solver.puzzleImmutable))
                        System.out.println("Solution is unique");
                } else {
                    System.out.println("No solution");
                }
            }
        }
    }

}
