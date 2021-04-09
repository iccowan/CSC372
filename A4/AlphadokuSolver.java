import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.Character;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *  Loads and solves alphadoku puzzles
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class AlphadokuSolver
 */
public class AlphadokuSolver {

    /**
     *  Map from the letters and their locations to the integers
     */
    private HashMap<Quintet, Integer> lettersToNum =
        new HashMap<Quintet, Integer>();

    /**
     *  Map from the integers to the letters and their locations
     */
    private HashMap<Integer, Quintet> numToLetters =
        new HashMap<Integer, Quintet>();

    /**
     *  Array of every character A-Y
     */
    static final char[] letters = new char[] {
                                               'A', 'B', 'C', 'D', 'E',
                                               'F', 'G', 'H', 'I', 'J',
                                               'K', 'L', 'M', 'N', 'O',
                                               'P', 'Q', 'R', 'S', 'T',
                                               'U', 'V', 'W', 'X', 'Y',
                                             };

    /**
     *  Puzzle format: [Big rows][Big columns][Small rows][Small columns]
     *  We keep a version that we will modify and then an "immutable" puzzle
     *  that we will agree to not change
     */
    char[][][][] puzzle = new char[5][5][5][5];
    char[][][][] puzzleImmutable = new char[5][5][5][5];

    /**
     *  Constructor
     *
     *  @param String fileName the name of the file containing the puzzle
     */
    public AlphadokuSolver(String fileName) {
        // Initialize the maps
        initMaps();

        // Load the puzzle from the file
        loadPuzzle(fileName);
    }

    /**
     *  Constructor
     *
     *  @param char[][][][] puzzle the puzzle
     */
    public AlphadokuSolver(char[][][][] puzzle) {
        // Initialize the maps
        initMaps();

        // Save the puzzle
        this.puzzle = puzzle;
    }

    /**
     *  Initializes the maps
     */
    private void initMaps() {
        // Begin with symbol 1
        int x = 1;

        // Loop through all of the locations and letters and map
        // to the integers
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        for (char letter : AlphadokuSolver.letters) {
                            // Create a quintet with the location and the letter
                            Quintet trip = new Quintet(i, j, k, l, letter);

                            // Put into the maps
                            lettersToNum.put(trip, x);
                            numToLetters.put(x, trip);

                            // Move onto the next symbol
                            x++;
                        }
                    }
                }
            }
        }
    }

    /**
     *  Loads the puzzle from a file into the class
     *
     *  @param String fileName the file containing the puzzle
     */
    private void loadPuzzle(String fileName) {
        // Try to open the file
        try {
            // Open the file
            File f = new File(fileName);
            Scanner reader = new Scanner(f);

            // Loop through and put into the puzzle array
            int i = 0;
            int j = 0;

            // Loop until the file is empty
            while (reader.hasNextLine()) {
                // Get the next line
                String data = reader.nextLine();

                // Trim and split the line into the sections
                data.trim();
                String[] splitLine = data.split("  ");

                // Make sure there's stuff to add
                if (splitLine.length > 1) {
                    int k = 0;

                    // Loop through each part of the line
                    for (String line : splitLine) {
                        // Split into the letters and add the letters to
                        // the array
                        String[] splitInside = line.split(" ");
                        int l = 0;
                        for (String s : splitInside) {
                            char c = s.charAt(0);

                            // If it's an underscore, store as a space
                            if (c == '_')
                                c = ' ';

                            puzzle[i][j][k][l] = c;
                            puzzleImmutable[i][j][k][l] = c;
                            l++;
                        }
                        k++;
                    }

                    // Iterate through all of the locations
                    j = (j + 1) % 5;
                    if (j == 0)
                        i++;
                }
            }

            // Close
            reader.close();
        } catch (FileNotFoundException e) {
            // If there was an error, print it
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     *  Prints the puzzle to the console
     */
    public void printPuzzle() {
        // Loop through each location and print the character
        // Print the spaces where they belong and replace all of the
        // stored spaces with an underscore
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        char c = puzzle[i][j][k][l];
                        if (c == ' ')
                            System.out.print("_");
                        else
                            System.out.print(c);
                        System.out.print(" ");
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
            if (i != 4)
                System.out.println();
        }
    }

    /**
     *  Prints a spacer...simply a helper
     */
    public void printSpacer() {
        // Print a pretty spacer
        System.out.println();
        for (int i = 0; i < 53; i++)
            System.out.print("=");
        System.out.println();
        System.out.println();
    }

    /**
     *  Adds a clause to the ArrayList of clauses... this is unnecessary
     *  but originally the logic to add a clause was more complicated
     *  and just changing this slightly made more sense
     *
     *  @param ArrayList<int[]> clauses the list of clauses
     *  @param int[] clause             the clause to add to the list
     */
    private void addClause(ArrayList<int[]> clauses, int[] clause) {
        // Add the clause to the list
        clauses.add(clause);
    }

    /**
     *  Generates all of the clauses for the puzzle
     *
     *  @return ArrayList<int[]> the list of all of the clauses
     */
    private ArrayList<int[]> generateClauses() {
        // Each spot has exactly one letter
        ArrayList<int[]> clauses = new ArrayList<int[]>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        int[] clause1 = new int[25];
                        int index = 0;
                        for (int letterIndex = 0;
                            letterIndex < 25; letterIndex++) {
                            char letter = AlphadokuSolver.letters[letterIndex];
                            for (int letterIndex2 = letterIndex + 1;
                                letterIndex2 < 25; letterIndex2++) {
                                char letter2 =
                                    AlphadokuSolver.letters[letterIndex2];
                                int[] clause2 = new int[2];
                                clause2[0] = -1 * lettersToNum.get(
                                    new Quintet(i, j, k, l, letter)
                                );
                                clause2[1] = -1 * lettersToNum.get(
                                    new Quintet(i, j, k, l, letter2)
                                );
                                addClause(clauses, clause2);
                            }
                            clause1[index++] = lettersToNum.get(
                                new Quintet(i, j, k, l, letter)
                            );
                        }
                        addClause(clauses, clause1);
                    }
                }
            }
        }

        // Each letter can appear exactly once in each small square
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        for (int j2 = j; j2 < 5; j2++) {
                            int initL2 = 0;
                            if (j2 == j)
                                initL2 = l + 1;
                            for (int l2 = initL2; l2 < 5; l2++) {
                                for (char letter : AlphadokuSolver.letters) {
                                    int[] clause = new int[2];
                                    clause[0] = -1 * lettersToNum.get(
                                        new Quintet(i, j, k, l, letter)
                                    );
                                    clause[1] = -1 * lettersToNum.get(
                                        new Quintet(i, j2, k, l2, letter)
                                    );
                                    addClause(clauses, clause);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Each letter can appear exactly once in each row
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        for (int k2 = k; k2 < 5; k2++) {
                            int initL2 = 0;
                            if (k2 == k)
                                initL2 = l + 1;
                            for (int l2 = initL2; l2 < 5; l2++) {
                                for (char letter : AlphadokuSolver.letters) {
                                    int[] clause = new int[2];
                                    clause[0] = -1 * lettersToNum.get(
                                        new Quintet(i, j, k, l, letter)
                                    );
                                    clause[1] = -1 * lettersToNum.get(
                                        new Quintet(i, j, k2, l2, letter)
                                    );
                                    addClause(clauses, clause);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Each letter can appear exactly once in each column
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        for (int i2 = i; i2 < 5; i2++) {
                            int initJ2 = 0;
                            if (i2 == i)
                                initJ2 = j + 1;
                            for (int j2 = initJ2; j2 < 5; j2++) {
                                for (char letter : AlphadokuSolver.letters) {
                                    int[] clause = new int[2];
                                    clause[0] = -1 * lettersToNum.get(
                                        new Quintet(i, j, k, l, letter)
                                    );
                                    clause[1] = -1 * lettersToNum.get(
                                        new Quintet(i2, j2, k, l, letter)
                                    );
                                    addClause(clauses, clause);
                                }
                            }
                        }
                    }
                }
            }
        }

        // If we already have a soln for a spot, it's a unit clause
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        char val = puzzle[i][j][k][l];
                        if (val != ' ') {
                            int[] clause = new int[]{
                                lettersToNum.get(
                                    new Quintet(i, j, k, l, val)
                                ),
                            };
                            addClause(clauses, clause);
                        }
                    }
                }
            }
        }

        return clauses;
    }

    /**
     *  Parse the solution that we get back from the SAT solver
     *
     *  @param int[] soln the array of solution integers
     */
    private void parseSoln(int[] soln) {
        // Loop through each part of the solution
        for (int sol : soln) {
            // If true, that is the letter for that location
            // Ignore if false
            if (sol > 0) {
                Quintet quin = numToLetters.get(sol);
                puzzle[quin.getI()][quin.getJ()][quin.getK()][quin.getL()] =
                    quin.getLetter();
            }
        }
    }

    /**
     *  Generates the CNF file for the SAT solver
     *
     *  @param ArrayList<int[]> clauses list of clauses
     *  @param int numSym               the number of symbols
     *  @return String                  the CNF file name
     */
    private String generateCNF(ArrayList<int[]> clauses, int numSym) {
        // We'll create a temp file for the CNF
        String fileName = "temp/out.temp";

        // Try to open and write to the file
        try {
            // Open the file
            File outFile = new File(fileName);
            outFile.createNewFile();

            // Write to the file
            FileWriter writer = new FileWriter(fileName);
            writer.write("p cnf " + numSym + " " + clauses.size() + "\n");

            // Loop through each clause and add all of the clauses to the file
            for (int[] clause : clauses) {
                for (int var : clause)
                    writer.write(var + " ");
                writer.write("0\n");
            }

            // Close the writer
            writer.close();
        } catch (Exception e) {
            // If there's an exception, announce it
            e.printStackTrace();
        }

        return fileName;
    }

    /**
     *  Run the Sat4j SAT solver.
     *
     *  We use this competition grade SAT solver for efficiency. No need to
     *  reinvent the wheel if it is not part of the assignment.
     *
     *  Find more information about Sat4j here: https://www.sat4j.org/
     *
     *  @param String fileName the name of the CNF file
     */
    private boolean runSAT4J(String fileName) {
        // Try to run the command
        try {
            // Create a process and run the SAT solver
            Process process = Runtime.getRuntime()
                .exec("java -jar sat4j-1.7.jar " + fileName);

            // Get the output of the command
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );

            // Iterate through all of the lines from the response,
            // and parse as appropriate
            String line = "";
            while ((line = reader.readLine()) != null) {
                // Split by a space
                String[] splitLine = line.split(" ");

                if (splitLine[0].equals("v")) {
                    // Create an array and store the solution
                    int[] soln = new int[splitLine.length - 2];
                    int i = 0;
                    for (String val : splitLine) {
                        if (val.equals("v") || val.equals("0"))
                            continue;

                        soln[i] = Integer.parseInt(val);
                        i++;
                    }

                    // Parse the solution
                    parseSoln(soln);

                    // Solution found
                    return true;
                } else if (splitLine[0] == "s") {
                    if (splitLine[1].equals("UNSATISFIABLE"))
                        // Solution not found
                        return false;
                }

            }
        } catch (Exception e) {
            // If an exception is caught, announce it
            e.printStackTrace();
        }

        // If there was an issue, the puzzle was not solved
        return false;
    }

    /**
     *  Calls all of the helpers to solve the puzzle
     *
     *  @return boolean whether or not the puzzle is solveable
     */
    public boolean solve() {
        ArrayList<int[]> clauses = generateClauses();
        int numSym = 15625;

        // Generate the CNF file
        String fileName = generateCNF(clauses, numSym);

        // Run SAT4J
        boolean isSolveable = runSAT4J(fileName);

        // Do some cleanup and delete the CNF file
        File f = new File("temp/out.temp");
        f.delete();

        return isSolveable;
    }

}

/**
 *  Allows easy access for all of the locations and letters.
 *  These are the symbols in a meaningful form.
 *
 *  @class Quintet
 */
class Quintet {

    /**
     *  Location identifier indices
     */
    private int i;
    private int j;
    private int k;
    private int l;

    /**
     *  Character in the location
     */
    private char letter;

    /**
     *  Constructor
     *
     *  @param int i       index i
     *  @param int j       index j
     *  @param int k       index k
     *  @param int l       index l
     *  @param char letter the letter
     */
    Quintet(int i, int j, int k, int l, char letter) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
        this.letter = letter;
    }

    /**
     *  We don't want anything to be able to change,
     *  so we'll make all of the attributes private and create getters
     */
    int getI() { return this.i; }
    int getJ() { return this.j; }
    int getK() { return this.k; }
    int getL() { return this.l; }
    char getLetter() { return this.letter; }

    /**
     *  Check for equality
     *
     *  @param Object obj the Quintet to check for equality
     *  @return boolean   whether or not the Quintets are equal
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        Quintet that = (Quintet)obj;

        // If all of the indices and the letter are the same, they're equal
        return this.i == that.i &&
               this.j == that.j &&
               this.k == that.k &&
               this.l == that.l &&
               this.letter == that.letter;
    }

    /**
     *  For equality checking, we need to have hashcodes that are based
     *  off of the values and not the memory locations
     *
     *  @return int the hash code that we calculated
     */
    @Override
    public int hashCode() {
        return i + j + k + l + Character.getNumericValue(letter);
    }

    /**
     *  "Stringifies" the Quintet
     *
     *  @return String the string version
     */
    @Override
    public String toString() {
        return "Quintet(" + i + ", " + j + ", " +
                        k + ", " + l + ", " + letter + ")";
    }

}
