import java.util.HashMap;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Arrays;
import java.lang.Character;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class AlphadokuSolver {

    private HashMap<Quintet, Integer> lettersToNum = new HashMap<Quintet, Integer>();
    private HashMap<Integer, Quintet> numToLetters = new HashMap<Integer, Quintet>();
    static final char[] letters = new char[] {
                                                       'A', 'B', 'C', 'D', 'E',
                                                       'F', 'G', 'H', 'I', 'J',
                                                       'K', 'L', 'M', 'N', 'O',
                                                       'P', 'Q', 'R', 'S', 'T',
                                                       'U', 'V', 'W', 'X', 'Y',
                                                     };

    /**
     *  Puzzle format: [Big rows][Big columns][Small rows][Small columns]
     */
    char[][][][] puzzle = new char[5][5][5][5];
    char[][][][] puzzleImmutable = new char[5][5][5][5];

    public AlphadokuSolver(String fileName) {
        initMaps();
        loadPuzzle(fileName);
    }

    public AlphadokuSolver(char[][][][] puzzle) {
        initMaps();
        this.puzzle = puzzle;
    }

    private void initMaps() {
        int x = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        for (char letter : AlphadokuSolver.letters) {
                            Quintet trip = new Quintet(i, j, k, l, letter);
                            lettersToNum.put(trip, x);
                            numToLetters.put(x, trip);
                            x++;
                        }
                    }
                }
            }
        }
    }

    private void loadPuzzle(String fileName) {
        try {
            File f = new File(fileName);
            Scanner reader = new Scanner(f);

            int i = 0;
            int j = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                data.trim();
                String[] splitLine = data.split("  ");
                if (splitLine.length > 1) {
                    int k = 0;
                    for (String line : splitLine) {
                        String[] splitInside = line.split(" ");
                        int l = 0;
                        for (String s : splitInside) {
                            char c = s.charAt(0);
                            if (c == '_')
                                c = ' ';

                            puzzle[i][j][k][l] = c;
                            puzzleImmutable[i][j][k][l] = c;
                            l++;
                        }
                        k++;
                    }

                    j = (j + 1) % 5;
                    if (j == 0)
                        i++;
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void printPuzzle() {
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

    public void printSpacer() {
        System.out.println();
        for (int i = 0; i < 53; i++)
            System.out.print("=");
        System.out.println();
        System.out.println();
    }

    private void addClause(ArrayList<int[]> clauses, int[] clause) {
        clauses.add(clause);
    }

    private ArrayList<int[]> generateClauses() {
        // Each spot has exactly one letter
        ArrayList<int[]> clauses = new ArrayList<int[]>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        int[] clause1 = new int[25];
                        int index = 0;
                        for (int letterIndex = 0; letterIndex < 25; letterIndex++) {
                            char letter = AlphadokuSolver.letters[letterIndex];
                            for (int letterIndex2 = letterIndex + 1; letterIndex2 < 25; letterIndex2++) {
                                char letter2 = AlphadokuSolver.letters[letterIndex2];
                                int[] clause2 = new int[2];
                                clause2[0] = -1 * lettersToNum.get(new Quintet(i, j, k, l, letter));
                                clause2[1] = -1 * lettersToNum.get(new Quintet(i, j, k, l, letter2));
                                addClause(clauses, clause2);
                            }
                            clause1[index++] = lettersToNum.get(new Quintet(i, j, k, l, letter));
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
                                    clause[0] = -1 * lettersToNum.get(new Quintet(i, j, k, l, letter));
                                    clause[1] = -1 * lettersToNum.get(new Quintet(i, j2, k, l2, letter));
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
                                    clause[0] = -1 * lettersToNum.get(new Quintet(i, j, k, l, letter));
                                    clause[1] = -1 * lettersToNum.get(new Quintet(i, j, k2, l2, letter));
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
                                    clause[0] = -1 * lettersToNum.get(new Quintet(i, j, k, l, letter));
                                    clause[1] = -1 * lettersToNum.get(new Quintet(i2, j2, k, l, letter));
                                    addClause(clauses, clause);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Each letter can appear exactly once in each column

        // If we already have a soln for a spot, it's a unit clause
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {
                        char val = puzzle[i][j][k][l];
                        if (val != ' ') {
                            int[] clause = new int[] { lettersToNum.get(new Quintet(i, j, k, l, val)), };
                            addClause(clauses, clause);
                        }
                    }
                }
            }
        }

        return clauses;
    }

    private void parseSoln(int[] soln) {
        for (int sol : soln) {
            if (sol > 0) {
                Quintet quin = numToLetters.get(sol);
                puzzle[quin.getI()][quin.getJ()][quin.getK()][quin.getL()] =
                    quin.getLetter();
            }
        }
    }

    private String generateCNF(ArrayList<int[]> clauses, int numSym) {
        String fileName = "temp/out.temp";
        try {
            File outFile = new File(fileName);
            outFile.createNewFile();

            FileWriter writer = new FileWriter(fileName);
            writer.write("p cnf " + numSym + " " + clauses.size() + "\n");
            for (int[] clause : clauses) {
                for (int var : clause)
                    writer.write(var + " ");
                writer.write("0\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private boolean runSAT4J(String fileName) {
        try {
            Process process = Runtime.getRuntime().exec("java -jar sat4j-1.7.jar " + fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(" ");
                if (splitLine[0].equals("v")) {
                    int[] soln = new int[splitLine.length - 2];
                    int i = 0;
                    for (String val : splitLine) {
                        if (val.equals("v") || val.equals("0"))
                            continue;

                        soln[i] = Integer.parseInt(val);
                        i++;
                    }

                    parseSoln(soln);

                    return true;
                } else if (splitLine[0] == "s") {
                    if (splitLine[1].equals("UNSATISFIABLE"))
                        return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

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

class Quintet {

    private int i;
    private int j;
    private int k;
    private int l;
    private char letter;

    Quintet(int i, int j, int k, int l, char letter) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
        this.letter = letter;
    }

    int getI() { return this.i; }
    int getJ() { return this.j; }
    int getK() { return this.k; }
    int getL() { return this.l; }
    char getLetter() { return this.letter; }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        Quintet that = (Quintet)obj;

        return this.i == that.i &&
               this.j == that.j &&
               this.k == that.k &&
               this.l == that.l &&
               this.letter == that.letter;
    }

    @Override
    public int hashCode() {
        return i + j + k + l + Character.getNumericValue(letter);
    }

    @Override
    public String toString() {
        return "Quintet(" + i + ", " + j + ", " +
                        k + ", " + l + ", " + letter + ")";
    }

}
