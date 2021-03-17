import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  Reads the DIMACS file information... This is in a separate class
 *  to prevent code duplication
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class ReadDIMACS
 */
public class ReadDIMACS {

    /**
     *  Keeps track of all of the clauses that we are importing to check
     */
    int[][] clauses;

    /**
     *  Keeps track of all of the symbols that are used in the clauses that
     *  we are checking
     */
    int[] symbols;

    /**
     *  Private constructor
     *
     *  @param int[][] clauses
     *  @param int[] symbols
     */
    private ReadDIMACS(int[][] clauses, int[] symbols) {
        this.clauses = clauses;
        this.symbols = symbols;
    }

    /**
     *  Handles reading in the DIMACS file. This is a static function that
     *  returns an object with the clauses and symbols so that we ensure
     *  that all of the data gets back to the user appropriately
     *
     *  @param String filePath the path of the file to read
     *  @return ReadDIMACS     the object containing the clauses and symbols
     *
     *  @throws FileNotFoundException
     */
    public static ReadDIMACS read(String filePath)
        throws FileNotFoundException {
        // We need to get the predicates and the clauses
        int[] symbols = new int[1];
        int[][] clauses = new int[1][1];

        // Read the file and save the clauses and symbols
        File myObj = new File(filePath);
        Scanner myReader = new Scanner(myObj);

        // We setup a few different variables to use while we read the file
        // to keep track of where we're at
        boolean begin = false;
        int line = 0;
        int clauseLines = 0;
        int totalLines = 1;

        // Loop while we have another line and the lines that we have read
        // of the clauses are less than the total number of clause lines that
        // we're taking in
        while (myReader.hasNextLine() && clauseLines < totalLines) {
            // Split the line by spaces
            String[] lineArray = myReader.nextLine().strip().split(" ");

            // If we're at the first line, read in the total number of lines
            // and some other information that doesn't count as a clause
            if (lineArray.length > 0 && lineArray[0].equals("p")) {
                totalLines = Integer.valueOf(lineArray[3]);
                clauses = new int[totalLines][];
                int numPred = Integer.valueOf(lineArray[2]);
                symbols = new int[numPred];
                for (int i = 1; i <= numPred; i++)
                    symbols[i - 1] = i;

                begin = true;
            } else if(begin) {
                // Not on the first line, so let's add the clause
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

        // Close the file
        myReader.close();
        
        return new ReadDIMACS(clauses, symbols);
    }

}
