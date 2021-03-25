import java.util.Arrays;
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
     *  Sorted array (by absolute value) of satSol
     */
    public int[] sortedSol = null;

    /**
     *  Keeps track of whether or not the SAT solver found a solution.
     */
    public boolean lastSat = false;

    /**
     *  Keeps track of the time required to find a solution.
     */
    public long solveTime = 0;

    /**
     *  Keeps track of the number of nodes expanded to find a solution.
     */
    public long nodesExpanded = 0;

    /**
     *  Checks to see whether or not a clause is satisfied
     *
     *  @param int[] clause           the clause to be checked for SAT
     *  @param HashSet<Integer> model the model to check the clause against
     *  @return boolean               whether or not the clause is SAT
     */
    protected int clauseSatisfied(int[] clause, HashSet<Integer> model) {
        // Loop through the clause and compare the value to the
        // value that we have in the model
        for (int val : clause) {
            // If the actual value matches the value in the model, we're sat
            if (model.contains(val))
                return 1;
        }

        return 0;
    }

    /**
     *  Checks to see how many of the clauses are SAT
     *
     *  @param int[][] clauses        the clauses to check
     *  @param HashSet<Integer> model the model to check the clauses against
     *  @return int                   number of SAT clauses
     */
    protected int numSat(int[][] clauses, HashSet<Integer> model) {
        // If the model is empty, return false
        if (model.isEmpty())
            return 0;

        // Check and see if all clauses can be satisfied with the current model
        int numSat = 0;
        for (int[] clause : clauses) {
            if (clauseSatisfied(clause, model) == 1)
                numSat++;
        }

        return numSat;
    }

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
        solveTime = 0;
        nodesExpanded = 0;
    }

    /**
     *  Takes care of reading the DIMACS formatted file for deciding whether
     *  or not we are SAT. This will call the subclass implementation of
     *  the abstract satSolver() method and will use the returned boolean
     *  value to set the lastSat attribute
     *
     *  @param String filePath the path of the DIMACS file
     */
    @SuppressWarnings("unchecked")
    public void solve(String filePath) throws FileNotFoundException {
        // Read in the DIMACS file
        ReadDIMACS clausesAndSymbols = ReadDIMACS.read(filePath);

        // Reset ourselves
        reset();

        // Extract the clauses and symbols
        int[][] clauses = clausesAndSymbols.clauses;
        int[] symbols = clausesAndSymbols.symbols;

        // Set lastSat attribute to the value returned by the solver
        long startTime = System.currentTimeMillis();
        lastSat = satSolver(clauses, symbols);
        long endTime = System.currentTimeMillis();
        solveTime = endTime - startTime;

        // If the satSol is not null, let's convert it to a sorted array
        // and make sure all of the values exist
        if (satSol != null) {
            for (int s : symbols)
                if (! (satSol.contains(s) || satSol.contains(s * -1)))
                    satSol.add(s);

            int[] sol = new int[satSol.size()];
            int i = 0;
            for (int sym : satSol) {
                sol[i] = sym;
                i++;
            }
            sortedSol = SatMergeSortIntArray.sort(sol);
        }
    }

}
