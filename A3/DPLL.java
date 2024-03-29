import java.util.HashSet;

/**
 *  Implements the DPLL SAT Solver algorithm
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class DPLL
 *  @extends SAT
 */
public class DPLL extends SAT {

    /**
     *  Checks to see if a clause can ever be satisfied with the given model
     *
     *  @param int[] clause     the clause to check for SAT
     *  @param HashSet<Integer> model the model to check against
     *  @return boolean         whether or not the clause can be satisfied
     */
    private boolean canBeSatisfied(int[] clause, HashSet<Integer> model) {
        // Check and see if can be sat
        // If just one symbols makes it SAT, we can be SAT
        for (int val : clause)
            if (! model.contains(val * -1))
                return true;

        return false;
    }

    /**
     *  Checks to see if clauses can ever be satisfied with the given model
     *
     *  @param int[][] clauses  the clauses to check for SAT
     *  @param HashSet<Integer> model the model to check against
     *  @return boolean         whether or not the clauses can be satisfied
     */
    private boolean canBeSatisfied(int[][] clauses, HashSet<Integer> model) {
        // Loop through and see if we can be SAT or not
        // This is different than the single clause, because all clauses have
        // to be satisfied
        for (int[] clause : clauses)
            if (! canBeSatisfied(clause, model))
                return false;

        return true;
    }

    /**
     *  Main DPLL algorithm recursive method. This is the main implementation
     *  of the DPLL algorithm
     *
     *  @param int[][] clauses        the clauses to check for SAT
     *  @param int[] symbols          the symbols that are included in the
     *                                clauses
     *  @param HashSet<Integer> model the model to check against for SAT
     */
    @SuppressWarnings("unchecked")
    private boolean DPLL(int[][] clauses, int[] symbols,
                         HashSet<Integer> model, int lastNumSat) {
        // New node expaned
        nodesExpanded++;

        // If we're already sat, return
        // If not, we'll continue
        // If we are not sat and we are out of symbols, return unsat
        int numSat = numSat(clauses, model);
        if (numSat == clauses.length) {
            satSol = model;
            return true;
        } else if (numSat < lastNumSat || (! canBeSatisfied(clauses, model)) ||
            symbols.length == 0)
            return false;

        // Now, we continue by taking the next symbol to check
        // and get the array of the remaining symbols
        int symbol = symbols[0];
        int[] restOfSymbols = new int[symbols.length - 1];
        for (int i = 0; i < symbols.length; i++)
            if (i != 0)
                restOfSymbols[i - 1] = symbols[i];

        // Set the next symbol we'll check to true and false
        // so we can recursively check both states
        HashSet<Integer> modelSymTrue = (HashSet<Integer>) model.clone();
        modelSymTrue.add(symbol);

        HashSet<Integer> modelSymFalse = (HashSet<Integer>) model.clone();
        modelSymFalse.add(-1 * symbol);

        // Finally, we'll return whether or not we're SAT with either of
        // these values. We are just looking for ONE solution, so if
        // either of these is true, we have a solution
        return DPLL(clauses, restOfSymbols, modelSymTrue, numSat) ||
               DPLL(clauses, restOfSymbols, modelSymFalse, numSat);
    }

    /**
     *  Finds all of the unit clauses and pure symbols in the clauses.
     *  This will give us a set of the union of these unit clauses and pure
     *  symbols. This will be our base model because these must be the
     *  appropriate values or we will definitely be UNSAT
     *
     *  @param int[][] clauses   the clauses to loop through and check
     *  @return HashSet<Integer> the model of the units and pure symbols
     */
    private HashSet<Integer> findUnitClausesAndPureSymbols(int[][] clauses) {
        // Create new HashSets to store the values
        HashSet<Integer> units = new HashSet<Integer>();
        HashSet<Integer> pureSymbols = new HashSet<Integer>();
        HashSet<Integer> notPureSymbols = new HashSet<Integer>();

        // We loop through all of the clauses
        for (int[] clause : clauses) {
            // If the clause length is 1, we'll add that as a unit clause
            if (clause.length == 1)
                units.add(clause[0]);

            // For all of the values in the clause, we will check to see
            // if it is a pure symbol. We do this by:
            //  - Adding the symbol the first time we see it to the set
            //  - If we see the symbol again in a negated form, we know
            //    it is not pure so we remember that
            //  - Finally, when we see the symbol in its pure form we don't
            //    have to do anything else, but if we see a symbol that we
            //    already know isn't pure, we don't do anything with that either
            for (int val : clause) {
                // Negated value exists, so not pure
                if (pureSymbols.contains(-1 * val)) {
                    pureSymbols.remove(-1 * val);
                    notPureSymbols.add(val);
                } else if ((! notPureSymbols.contains(val)) &&
                           (! notPureSymbols.contains(-1 * val)) &&
                           (! pureSymbols.contains(val)) &&
                           (! pureSymbols.contains(-1 * val))) {
                    // Possibly pure? If it's still here at the end, it is pure
                    pureSymbols.add(val);
                }
            }
        }

        // Union of the units and pure symbols
        units.addAll(pureSymbols);

        return units;
    }

    /**
     *  Begins the solver. This is implemented from the SAT superclass
     *
     *  @param int[][] clauses the clauses to check for SAT
     *  @param int[]   symbols the symbols that are included in the clauses
     *  @return boolean        whether or not the solution is SAT
     */
    @Override
    protected boolean satSolver(int[][] clauses, int[] symbols) {
        // No clauses means we need to return true because
        // they're all satisfied
        if (clauses.length == 0)
            return true;

        // If we have any empty clauses, we need to return false because
        // there is no way to satisfy an empty clause
        for (int[] clause : clauses) {
            if (clause.length == 0)
                return false;
        }

        // Let's find all of the unit clauses
        HashSet<Integer> unitClausesAndPureSymbols =
            findUnitClausesAndPureSymbols(clauses);

        // Remove all of the symbols that are unit clauses or pure symbols
        // from the symbols array
        int[] newSymbols = symbols;
        for (int val : unitClausesAndPureSymbols) {
            if (val < 0) val = -1 * val;

            int[] oldSymbols = newSymbols;
            newSymbols = new int[oldSymbols.length - 1];
            int i = 0;
            for (int sym : oldSymbols) {
                if (sym != val) {
                    newSymbols[i] = sym;
                    i++;
                }
            }
        }

        // Now, we return the value of the recursion...
        return DPLL(clauses, newSymbols, unitClausesAndPureSymbols, 0);
    }

}
