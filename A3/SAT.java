import java.util.HashSet;
import java.io.FileNotFoundException;

/**
 *  Abstract class to prevent duplicating code between the SAT solvers
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @abstract class SAT
 */
abstract class SAT {

    /**
     *  Tracks the solution for the SAT solver. Will be null if no solution.
     */
    public HashSet<Integer> satSol = null;

    /**
     *  Keeps track of whether or not the SAT solver found a solution.
     */
    public boolean lastSat = false;

    /**
     *  Takes care of solving the SAT clauses and finding whether or not
     *  a solution exists. This will should the final solution before returning
     *  as the satSol attribute so it can be accessed after returning
     *
     *  @param int[][] clauses the clauses to check for SAT
     *  @param int[]   symbols the symbols that are included in the clauses
     *  @return boolean        whether or not the solution is SAT
     */
    abstract protected boolean satSolver(int[][] clauses, int[] symbols);

    /**
     *  Resets the solver so the object can be used again and a new object
     *  does not have to be created. Will set satSol to null and lastSat to
     *  false which are their default values. We call this at the beginning
     *  of each solve() method call, but can be called by the user
     *
     * @implNote THIS MUST BE CALLED BETWEEN ANY 2 SOLVERS THAT EXTEND THIS
     *           CLASS WHETHER OR NOT THEY ARE THE SAME SUBCLASS...
     */
    public void reset() {
        satSol = null;
        lastSat = false;
    }

    /**
     *  Takes care of reading the DIMACS formatted file for deciding whether
     *  or not we are SAT. This will call the subclass implementation of
     *  the abstract satSolver() method and will use the returned boolean
     *  value to set the lastSat attribute
     *
     *  @param String filePath the path of the DIMACS file
     */
    public void solve(String filePath) throws FileNotFoundException {
        // Read in the DIMACS file
        ReadDIMACS clausesAndSymbols = ReadDIMACS.read(filePath);

        // Reset ourselves
        reset();

        // Extract the clauses and symbols
        int[][] clauses = clausesAndSymbols.clauses;
        int[] symbols = clausesAndSymbols.symbols;

        // Set lastSat attribute to the value returned by the solver
        lastSat = satSolver(clauses, symbols);
    }

}
