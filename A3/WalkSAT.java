import java.util.HashSet;
import java.util.Random;
import java.util.Arrays;

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
     *  Random object. We just make one to save time
     */
    private final Random rand = new Random();

    /**
     *  The maximum number of clauses satisfied
     */
    public int maxClausesSat = 0;

    /**
     *  Flips a symbol in the model. The symbol to flip should be
     *  the absolute value
     *
     *  @param HashSet<Integer> model the model to flip the symbol in
     *  @param int symbolToFlip       the symbol to flip in the model
     */
    private void flipSymbol(HashSet<Integer> model, int symbolToFlip) {
        // If the model contains the positive form, make negative
        // If the model contains the negative form, make positive
        if (model.contains(symbolToFlip)) {
            model.remove(symbolToFlip);
            model.add(symbolToFlip * -1);
        } else {
            model.remove(symbolToFlip * -1);
            model.add(symbolToFlip);
        }
    }

    /**
     *  Flip the best symbol in the given clause
     *
     *  @param int[][] clauses        the clauses to check for the best symbol
     *  @param int[] clause           the clause to get a symbol from to flip
     *  @param HashSet<Integer> model the model to flip the symbol in
     *  @return HashSet<Integer>      the new model with the symbol flipped
     */
    @SuppressWarnings("unchecked")
    private HashSet<Integer> flipBestSymbol(int[][] clauses, int[] clause,
        HashSet<Integer> model) {
        // Loop through the model and see flipping which symbol will
        // satisfy the most clauses
        int maxNumClausesSat = -1;
        HashSet<Integer> maxModel = model;
        for (int s : clause) {
            HashSet<Integer> cloneModel = (HashSet<Integer>) model.clone();
            flipSymbol(cloneModel, s);
            int numClausesSat = numSat(clauses, model);

            if (numClausesSat > maxNumClausesSat) {
                maxNumClausesSat = numClausesSat;
                maxModel = cloneModel;
            }
        }

        return maxModel;
    }

    /**
     *  Run the WalkSAT algorithm
     *
     *  @param int[][] clauses        the clauses to check for SAT
     *  @param int[] symbols          the symbols to assign for SAT
     *  @param float p                the probability for WalkSAT
     *  @param HashSet<Integer> model the starting model
     *  @param int maxIter            the maximum number of iterations
     *  @return boolean               whether or not the clauses are SAT
     */
    private boolean walkSAT(int[][] clauses, int[] symbols, float p,
        HashSet<Integer> model, int maxIter) {
        // Make sure we have a probability less than or equal to 100
        if (p != 100)
            p = p % (float) 100;

        int iteration = 0;
        while (iteration < maxIter) {
            // Make sure we aren't already solved
            // If we're solved, return the solution
            // If we're not solved, recurse and try again
            int numCSat = numSat(clauses, model);

            if (numCSat > maxClausesSat)
                maxClausesSat = numCSat;
            if (numCSat == clauses.length) {
                satSol = model;
                return true;
            }

            // Pick a random clause to flip from
            int clauseToFlipFrom = 0;
            int[] clause = new int[0];
            do {
                clauseToFlipFrom = rand.nextInt(clauses.length);
                clause = clauses[clauseToFlipFrom];
            } while (clauseSatisfied(clause, model) == 1);

            // First, let's get a random float to see if we should flip a random
            // symbol or flip the best symbol
            if (((rand.nextFloat() * 100) % 100) <= p) {
                // Flip a random symbol from clause
                int symbolToFlip = clause[rand.nextInt(clause.length)];
                flipSymbol(model, symbolToFlip);
                nodesExpanded++;
            } else {
                // Flip the best symbol
                model = flipBestSymbol(clauses, clause, model);
                nodesExpanded++;
            }

            iteration++;
        }

        return false;
    }

    /**
     *  Reset the maximum number of clauses
     */
    public void reset() {
        maxClausesSat = 0;
        super.reset();
    }

    /**
     *  Begins the solver. This is implemented from the SAT superclass
     *
     *  @param int[][] clauses the clauses to check for SAT
     *  @param int[] symbols   the symbols that are included in the clauses
     *  @return boolean        whether or not the solution is SAT
     */
    @Override
    protected boolean satSolver(int[][] clauses, int[] symbols) {
        // Generate the initial model
        HashSet<Integer> model = new HashSet<Integer>();
        for (int s : symbols)
            model.add(s);

        // Make sure we don't begin in a solved state
        if (numSat(clauses, model) == clauses.length) {
            satSol = model;
            return true;
        }

        // Return the result of the recursive method
        float p = (float) 50;
        int numSymbols = symbols.length;
        int maxIter = 100000;
        return walkSAT(clauses, symbols, p, model, maxIter);
    }

}
