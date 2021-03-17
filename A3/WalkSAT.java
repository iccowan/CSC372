import java.util.HashSet;

/**
 *  Implements the WalkSAT SAT Solver algorithm
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class WalkSAT
 *  @extends SAT
 */
public class WalkSAT extends SAT {

    /**
     *  Begins the solver. This is implemented from the SAT superclass.
     *  Since this is not a recursive algorithm, we may not need
     *  recursive helper functions, so we can check for the
     *  solution in this method
     *
     *  @param int[][] clauses the clauses to check for SAT
     *  @param int[]   symbols the symbols that are included in the clauses
     *  @return boolean        whether or not the solution is SAT
     */
    @Override
    protected boolean satSolver(int[][] clauses, int[] symbols) {
        /**
         *  TODO: Implement the WalkSAT algorithm. Right now, this just returns
         *        false so it'll compile
         */
        return false;
    }

}
