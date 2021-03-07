import java.util.ArrayList;

/**
 * Runs the test for the solver
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class A2
 */
public class A2 {
    /**
     * Switches the integer for the direction with a string for printing
     *
     * @param int direction the integer direction to move
     * @return String the string representation of the direction
     */
    private static String moveDirectionString(int direction) {
        // C -> Clockwise
        // CC-> Counter-Clockwise
        switch (direction) {
            case SolveCube.CLOCKWISE:
                return "C";
            case SolveCube.COUNTERCLOCKWISE:
                return "CC";
        }

        // Should never get here, but java requires this
        return "Other";
    }

    /**
     * Turns an integer move into the string representation
     *
     * @param int move the integer representation of the move
     * @return String the string representation of the move
     */
    private static String moveString(int move) {
        switch (move) {
            case SolveCube.NO_MOVE:
                return "None";
            case SolveCube.FRONT:
                return "Front";
            case SolveCube.BACK:
                return "Back";
            case SolveCube.LEFT:
                return "Left";
            case SolveCube.RIGHT:
                return "Right";
            case SolveCube.UP:
                return "Up";
            case SolveCube.DOWN:
                return "Down";
        }

        // Should never get here, but java requires this
        return "Other";
    }

    /**
     * Main
     *
     * @param String[] args
     */
    public static void main(String[] args) {
        // Number of moves to randomize
        int i = 0;

        // Let's keep running until we decide we should stop and terminate the
        // program
        // This will print the data in CSV format, so it can be put into a file
        // the solve sequence will print the name of the face to turn followed by a "C" or "CC"
        // to designate which direction the face was turned

        // CSV header
        System.out.println("num_moves_to_randomize,num_moves_to_solve,num_nodes_created_to_solve,num_nodes_visited_to_solve,wall_time_to_solve,solve_sequence");
        while (true) {
            // Run for 10 cubes
            for (int j = 0; j < 10; j++) {
                // Reset the solver
                SolveCube.reset();

                // Create a new cube and randomize
                Cube cube = new Cube();
                cube.randomize(i);

                // Now, let's solve the cube and record some information
                long startTime = System.currentTimeMillis();
                Node soln = SolveCube.solveCube(cube);
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;

                // Create a list of all of the moves to a solution
                ArrayList<int[]> moves = new ArrayList<int[]>();
                int numMovesToSolve = 0;
                while (soln.parent != null) {
                    moves.add(0, new int[] {soln.move, soln.direction});
                    numMovesToSolve++;
                    soln = soln.parent;
                }

                // Print in a CSV format for easy analysis later on
                System.out.print(i + "," + numMovesToSolve + "," + SolveCube.numNodesCreated.toString() + "," + SolveCube.numNodesVisited.toString() + "," + elapsedTime + ",");
                for (int[] moveArray : moves) {
                    System.out.print(moveString(moveArray[0]));
                    System.out.print("-");
                    System.out.print(moveDirectionString(moveArray[1]));
                    System.out.print(";");
                }
                System.out.print("\n");
            }

            // Increment i
            i++;
        }
    }
}
