import java.io.FileNotFoundException;

/**
 *  Static class to run the SAT solver for the different SAT algorithms we
 *  implement
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class SatSolver
 */
public class SatSolver {

    /**
     *  Main
     *
     *  @param String[] args
     *      0 => The file path of the DIMACS file to read
     */
    public static void main(String[] args) {
        // Create our DPLL and WalkSAT objects
        DPLL dpllsolver = new DPLL();
        WalkSAT walkSATSolver = new WalkSAT();

        // Get the file path
        String filePath = args[0];

        // Try to read in the file by solving with the algorithms
        try {
            // Start with DPLL
            dpllsolver.solve(filePath);

            if (dpllsolver.lastSat) {
                // If SAT, print out the sol
                System.out.print("DPLL: SAT ");
                for (int sym : dpllsolver.satSol)
                    System.out.print(sym + " ");
                System.out.println("0");
            } else {
                // If UNSAT, notify
                System.out.println("DPLL: UNSAT");
            }

            // Now, WalkSAT
            walkSATSolver.solve(filePath);

            if (walkSATSolver.lastSat) {
                // If SAT, print the sol
                System.out.print("WalkSAT: SAT ");
                for (int sym : walkSATSolver.satSol)
                    System.out.print(sym + " ");
                System.out.println("0");
            } else {
                // If UNSAT, notify
                System.out.println("WalkSAT: UNSAT");
            }
        } catch (FileNotFoundException e) {
            // If the file could not be found, notify the user and print
            // the stack trace in case something else is wrong
            System.out.println("Could not open file " + args[0]);
            e.printStackTrace();
        }
    }

}
