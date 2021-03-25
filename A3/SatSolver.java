import java.io.FileNotFoundException;
import java.io.File;
import java.util.Arrays;

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

        // Print the data out in CSV format
        System.out.println(
            "algo,file_name,sol,time_required," +
            "highest_num_of_clauses_sol,nodes_expanded"
        );

        // Get the files to check
        File dir = new File("A3Formulas/");
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Get the file path of the file
                String filePath = child.getAbsolutePath();
                String fileName = child.getName();

                // Try to read in the file by solving with the algorithms
                try {
                    // Start with DPLL
                    dpllsolver.solve("A3Formulas/" + fileName);
                    System.out.print("dpll," + fileName + ",");

                    if (dpllsolver.lastSat) {
                        // If SAT, print out the sol
                        System.out.print("SAT ");
                        for (int sym : dpllsolver.sortedSol)
                            System.out.print(sym + " ");
                        System.out.print("0,");
                    } else {
                        // If UNSAT, notify
                        System.out.print("UNSAT,");
                    }
                    System.out.print(dpllsolver.solveTime + ",");
                    System.out.println("NA," + dpllsolver.nodesExpanded);
                    dpllsolver.reset();

                    // Now, WalkSAT
                    // Random, so we run 10 times
                    for (int i = 0; i < 10; i++) {
                        walkSATSolver.solve("A3Formulas/" + fileName);
                        System.out.print("walksat," + fileName + ",");

                        if (walkSATSolver.lastSat) {
                            // If SAT, print the sol
                            System.out.print("SAT ");
                            for (int sym : walkSATSolver.sortedSol)
                                System.out.print(sym + " ");
                            System.out.print("0,");
                        } else {
                            // If UNSAT, notify
                            System.out.print("UNSAT,");
                        }
                        System.out.print(walkSATSolver.solveTime + ",");
                        System.out.println(walkSATSolver.maxClausesSat + "," +
                            walkSATSolver.nodesExpanded);
                        walkSATSolver.reset();
                    }
                } catch (FileNotFoundException e) {
                    // If the file could not be found, notify the user and print
                    // the stack trace in case something else is wrong
                    System.out.println("Could not open file " + args[0]);
                    e.printStackTrace();
                }
            }
        }
    }

}
