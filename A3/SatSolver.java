import java.io.FileNotFoundException;

public class SatSolver {

    public static void main(String[] args) {
        DPLL solver = new DPLL();
        try {
            solver.solve(args[0]);
            if (solver.lastSat) {
                System.out.print("SAT ");
                for (int sym : solver.satSol)
                    System.out.print(sym + " ");
                System.out.println("0");
            } else {
                System.out.println("UNSAT");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
