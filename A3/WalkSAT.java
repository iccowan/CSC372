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

    private final Random rand = new Random();

    private int numClausesSat(int[][] clauses, HashSet<Integer> model) {
        int numSat = 0;
        for (int[] c : clauses)
            if (clauseSatisfied(c, model))
                numSat++;

        return numSat;
    }

    private void flipSymbol(HashSet<Integer> model, int symbolToFlip) {
        if (model.contains(symbolToFlip)) {
            model.remove(symbolToFlip);
            model.add(symbolToFlip * -1);
        } else {
            model.remove(symbolToFlip * -1);
            model.add(symbolToFlip);
        }
    }

    @SuppressWarnings("unchecked")
    private HashSet<Integer> flipBestSymbol(int[][] clauses, HashSet<Integer> model) {
        // Loop through the model and see flipping which symbol will
        // satisfy the most clauses
        int maxNumClausesSat = -1;
        HashSet<Integer> maxModel = model;
        for (int s : model) {
            HashSet<Integer> cloneModel = (HashSet<Integer>) model.clone();
            flipSymbol(cloneModel, s);
            int numClausesSat = numClausesSat(clauses, cloneModel);

            if (numClausesSat > maxNumClausesSat) {
                maxNumClausesSat = numClausesSat;
                maxModel = cloneModel;
            }
        }

        return maxModel;
    }

    private boolean walkSAT(int[][] clauses, int[] symbols, float p,
        HashSet<Integer> model, int iteration, int maxIter) {
        // First, let's get a random float to see if we should flip a random
        // symbol or flip the best symbol
        // Make sure we have a probability less than or equal to 100
        if (p != 100)
            p = p % (float) 100;

        if (((rand.nextFloat() * 100) % 100) <= p) {
            // Flip a random symbol
            int symbolToFlip = rand.nextInt(symbols.length - 1) + 1;
            flipSymbol(model, symbolToFlip);
        } else {
            // Flip the best symbol
            model = flipBestSymbol(clauses, model);
        }

        // If we're solved, return the solution
        // If we're not solved, recurse and try again
        if (isSat(clauses, model)) {
            satSol = model;
            return true;
        } else if (iteration >= maxIter) {
            return false;
        } else {
            return walkSAT(clauses, symbols, p, model, ++iteration, maxIter);
        }
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
        if (isSat(clauses, model)) {
            satSol = model;
            return true;
        }

        // Return the result of the recursive method
        float p = (float) 50;
        int maxIter = symbols.length * symbols.length;
        return walkSAT(clauses, symbols, p, model, 1, maxIter);
    }

}
