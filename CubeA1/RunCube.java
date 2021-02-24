import java.util.Scanner;
import java.util.Random;

/**
 * Provides a command line interface to move the cube
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class RunCube
 */
public class RunCube {
    /**
     * Prints instructions to the command line for how to make moves.
     */
    private static void printInstructions() {
        System.out.println();
        System.out.println("The following mappings are valid to move the faces:");
        System.out.println("1 => Front");
        System.out.println("2 => Back");
        System.out.println("3 => Left");
        System.out.println("4 => Right");
        System.out.println("5 => Up");
        System.out.println("6 => Down");
        System.out.println();
    }

    /**
     * Prompts the user to input values for the face to move and
     * number of quarter turns to make.
     *
     * @return int[] the {face value, number of turns}
     */
    private static int[] promptValues() {
        // Prompt for the face and number of quarter turns
        Scanner input = InputScanner.openScanner();
        System.out.print("Enter a face to move: ");
        int face = input.nextInt();
        System.out.print("Enter the number of clockwise quarter turns: ");
        int numTurns = input.nextInt();
        System.out.println();

        // Return the integers in an array
        return new int[] {face, numTurns};
    }

    /**
     * Moves a face on the cube using the A1Cube methods.
     *
     * @param A1Cube cube the cube to move the face on
     * @param int face    the face to turn on the cube
     * @param int k       the number of quarter turns to make
     */
    private static void moveFace(A1Cube cube, int face, int k) {
        // Switch the selected face with a move
        switch(face) {
            case 1:
                cube.front(k);
                break;
            case 2:
                cube.back(k);
                break;
            case 3:
                cube.left(k);
                break;
            case 4:
                cube.right(k);
                break;
            case 5:
                cube.up(k);
                break;
            case 6:
                cube.down(k);
                break;
        }
    }

    /**
     * Prompt the user on whether or not they want to exit the program.
     *
     * @return String the value entered by the user
     */
    private static String promptExit() {
        // Ask the user
        Scanner input = InputScanner.openScanner();
        System.out.print("Would you like to make another move (y/n)? ");
        String val = input.next();

        // Return the value
        return val;
    }

    /**
     * Prompt the user on whether or not they want to reset the cube.
     *
     * @return String the value entered by the user
     */
    private static String promptReset() {
        // Ask the user
        Scanner input = InputScanner.openScanner();
        System.out.print("Would you like to reset the cube (y/n)? ");
        String val = input.next();

        // Return the value
        return val;
    }

    /**
     * Main method that runs the program.
     *
     * @param String[] args the arguments entered when running the program
     */
    public static void main(String[] args) {
        // See if we want to randomize the cube
        boolean randomize = false;
        if (args.length == 1 && args[0].equals("r"))
            randomize = true;

        // Create the cube and show
        A1Cube cube = new Cube();
        Random rand = new Random();
        if (randomize)
            // Randomize with a random number of turns, but don't exceed 42 moves
            // and don't go any less than 7 moves so we'll be random.
            // Exceeding any relatively "small" integer is plenty of moves to
            // randomize especially considering most 2x2 cubes can be sufficiently
            // randomized with only 7 moves, but we have a computer to do the hard work...
            cube.randomize((rand.nextInt() % 36) + 7);
        cube.print();

        // Instructions
        printInstructions();

        // Get user input
        boolean finished = false;

        // Keep running until we finish
        while (! finished) {
            // Prompt for the values with validation
            int[] inputVals = promptValues();
            while (inputVals[0] < 1 || inputVals[0] > 6) {
                System.out.println("Invalid face number, try again");
                inputVals = promptValues();
            }

            // Get the face and number of quarter turns
            int face = inputVals[0];
            int numTurns = inputVals[1];

            // Move the face
            moveFace(cube, face, numTurns);

            // Reprint the cube
            cube.print();
            if (cube.isSolved())
                System.out.println("The cube is solved.");
            else
                System.out.println("The cube is not solved.");
            System.out.println();

            // Ask the user if they want to continue or quit
            String exit = promptExit();
            while (! (exit.equals("y") || exit.equals("n"))) {
                System.out.println("Invalid input");
                exit = promptExit();
            }

            // If the user wants to quit, we're finished
            if (exit.equals("n"))
                finished = true;

            // If we're not finished, ask the user if they want a reset
            if (! finished) {
                // Ask the user if they want to reset the cube
                String reset = promptReset();
                while (! (reset.equals("y") || reset.equals("n"))) {
                    System.out.println("Invalid input");
                    reset = promptReset();
                }

                System.out.println();

                // Reset the cube before continuing
                if (reset.equals("y")) {
                    cube.reset();
                    cube.print();
                }
            }
        }

        // Make sure we close the scanner
        InputScanner.closeScanner();
    }
}

/**
 * Singleton Pattern for input scanner
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class InputScanner
 */
class InputScanner {
    /**
     * The Scanner object
     */
    static Scanner inputScanner;

    /**
     * Opens the scanner if not already open and provides
     * access to the input scanner object.
     *
     * @return Scanner the open input scanner object
     */
    static Scanner openScanner() {
        // If the scanner doesn't exist, create it
        if (inputScanner == null)
            inputScanner = new Scanner(System.in);

        // Return the scanner
        return inputScanner;
    }

    /**
     * Closes the input scanner object that is open.
     * If no scanner is open, do nothing.
     */
    static void closeScanner() {
        if (inputScanner != null)
            inputScanner.close();
    }
}