import java.io.File;

/**
 *  Handles finding the Alphadoku solution
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class FindAlphadokuSoln
 */
public class FindAlphadokuSoln {

    /**
     *  Checks to see if the given character is in the row, column, or square
     *  of the current location we're checking
     *
     *  @param char[][][][] puzzle the puzzle
     *  @param int i               index i
     *  @param int j               index j
     *  @param int k               index k
     *  @param int l               index l
     *  @param char c              the character to check for
     *  @return boolean            whether or not the character is there
     */
    private static boolean inRowColSquare(char[][][][] puzzle,
        int i, int j, int k, int l, char c) {
        // Loop through j and l to check the mini square
        for (int j2 = 0; j2 < 5; j2++) {
            for (int l2 = 0; l2 < 5; l2++) {
                // If we find the character, return true
                if (puzzle[i][j2][k][l2] == c)
                    return true;
            }
        }

        // Loop through k and l to check the row
        for (int k2 = 0; k2 < 5; k2++) {
            for (int l2 = 0; l2 < 5; l2++) {
                // If we find the character, return true
                if (puzzle[i][j][k2][l2] == c)
                    return true;
            }
        }

        // Loop through i and j to check the column
        for (int i2 = 0; i2 < 5; i2++) {
            for (int j2 = 0; j2 < 5; j2++) {
                // If we find the character, return true
                if (puzzle[i2][j2][k][l] == c)
                    return true;
            }
        }

        // Not found
        return false;
    }

    /**
     *  Checks for uniqueness of a puzzle's solution
     *
     *  @param char[][][][] puzzle         the puzzle solution to check
     *  @param char[][][][] originalPuzzle the original puzzle
     */
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

    /**
     *  Deep copies a puzzle
     *
     *  @param char[][][][] puzzle the puzzle to copy
     *  @return char[][][][]       the copy of the puzzle
     */
    private static char[][][][] deepCopyPuzzle(char[][][][] puzzle) {
        // Create a deep copy
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

    /**
     *  Main
     *
     *  @param String[] args 0 => File name of the puzzle
     */
    public static void main(String[] args) {
        // Solve the puzzle
        AlphadokuSolver solver = new AlphadokuSolver(args[0]);
        boolean solveable = solver.solve();

        // If the puzzle is solveable, print it
        // and check for uniqueness
        if (solveable) {
            solver.printPuzzle();
            solver.printSpacer();

            // Check if the solutions are unique or not
            if (! checkForUniqueness(solver.puzzle, solver.puzzleImmutable))
                System.out.println("Solution is unique");
        } else {
            // No solution found
            System.out.println("No solution");
        }
    }

}
