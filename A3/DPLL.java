import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.Arrays;

public class DPLL {

    public HashSet<Integer> satSol = null;
    public boolean lastSat = false;

    private boolean clauseSatisfied(int[] clause, HashSet<Integer> model) {
        // Loop through the clause and compare the value to the
        // value that we have in the model
        for (int val : clause)
            // If the actual value matches the value in the model, we're sat
            if (model.contains(val))
                return true;

        // If we get here, then no value to sat the clause was found, so unsat
        return false;
    }

    private boolean isSat(int[][] clauses, HashSet<Integer> model) {
        // If the model is empty, return false
        if (model.isEmpty())
            return false;

        // Assume not sat
        boolean sat = true;

        // Check and see if all clauses can be satisfied with the current model
        for (int[] clause : clauses)
            if (! clauseSatisfied(clause, model))
                sat = false;

        return sat;
    }

    @SuppressWarnings("unchecked")
    private boolean DPLL(int[][] clauses, int[] symbols, HashSet<Integer> model) {
        // If we're already sat, return
        // If not, we'll continue
        // If we are not sat and we are out of symbols, return unsat
        if (isSat(clauses, model)) {
            satSol = model;
            return true;
        } else if (symbols.length == 0)
            return false;

        // Now, we continue by taking the next symbol to check
        int symbol = symbols[0];
        int[] restOfSymbols = new int[symbols.length - 1];
        for (int i = 0; i < symbols.length; i++)
            if (i != 0)
                restOfSymbols[i - 1] = symbols[i];

        HashSet<Integer> modelSymTrue = (HashSet<Integer>) model.clone();
        modelSymTrue.add(symbol);

        HashSet<Integer> modelSymFalse = (HashSet<Integer>) model.clone();
        modelSymFalse.add(-1 * symbol);

        return DPLL(clauses, restOfSymbols, modelSymTrue) ||
               DPLL(clauses, restOfSymbols, modelSymFalse);
    }

    private HashSet<Integer> findUnitClausesAndPureSymbols(int[][] clauses) {
        HashSet<Integer> units = new HashSet<Integer>();
        HashSet<Integer> pureSymbols = new HashSet<Integer>();
        HashSet<Integer> notPureSymbols = new HashSet<Integer>();
        for (int[] clause : clauses) {
            if (clause.length == 1)
                units.add(clause[0]);

            for (int val : clause) {
                if (pureSymbols.contains(-1 * val)) {
                    pureSymbols.remove(-1 * val);
                    notPureSymbols.add(val);
                } else if ((! notPureSymbols.contains(val)) && (! notPureSymbols.contains(-1 * val)) &&
                           (! pureSymbols.contains(val)) && (! pureSymbols.contains(-1 * val))) {
                    pureSymbols.add(val);
                }
            }
        }

        // Union... but if it's a unit, that is more important
        units.addAll(pureSymbols);

        return units;
    }

    private boolean DPLL(int[][] clauses, int[] symbols) {
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
        HashSet<Integer> unitClausesAndPureSymbols = findUnitClausesAndPureSymbols(clauses);

        // Remove all of the symbols that are unit clauses or pure symbols
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
        return DPLL(clauses, newSymbols, unitClausesAndPureSymbols);
    }

    public void reset() {
        satSol = null;
        lastSat = false;
    }

    public void solve(String filePath) throws FileNotFoundException {
        // We need to get the predicates and the clauses
        int[] symbols = new int[1];
        int[][] clauses = new int[1][1];

        // Read the file and solve based off of the format
        File myObj = new File(filePath);
        Scanner myReader = new Scanner(myObj);
        boolean begin = false;
        int line = 0;
        int clauseLines = 0;
        int totalLines = 1;
        while (myReader.hasNextLine() && clauseLines < totalLines) {
            // Split the line by spaces
            String[] lineArray = myReader.nextLine().strip().split(" ");

            // If we're at the first line, read in the total number of lines
            if (lineArray.length > 0 && lineArray[0].equals("p")) {
                totalLines = Integer.valueOf(lineArray[3]);
                clauses = new int[totalLines][];
                int numPred = Integer.valueOf(lineArray[2]);
                symbols = new int[numPred];
                for (int i = 1; i <= numPred; i++)
                    symbols[i - 1] = i;

                begin = true;
            } else if(begin) {
                // Not on the first line, so let's add the clauses
                int i = 0;
                int nextVal = Integer.valueOf(lineArray[i]);
                int[] clause = new int[lineArray.length - 1];
                while (nextVal != 0) {
                    clause[i] = nextVal;
                    nextVal = Integer.valueOf(lineArray[++i]);
                }

                clauses[clauseLines] = clause;
                clauseLines++;
            }

            line++;
        }
        myReader.close();

        lastSat = DPLL(clauses, symbols);
    }

}
