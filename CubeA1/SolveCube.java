import java.math.BigInteger;

/**
 * Solves a 2x2 Rubik's Cube
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class SolveCube
 */
public class SolveCube {

    /**
     * Keeps track of the number of nodes created
     */
    public static BigInteger numNodesCreated = new BigInteger("0");
    public static BigInteger numNodesVisited = new BigInteger("0");
    public static BigInteger bigInt1 = new BigInteger("1");

    /**
     * Moves for the solving nodes
     */
    final static int NO_MOVE = 0;
    final static int FRONT = 1;
    final static int BACK = 2;
    final static int LEFT = 3;
    final static int RIGHT = 4;
    final static int UP = 5;
    final static int DOWN = 6;

    final static int CLOCKWISE = 1;
    final static int COUNTERCLOCKWISE = 2;

    /**
     * The heuristic for estimating the number of moves to finish solving the cube.
     * This works by assuming one cublet is fixed, and then checking each sticker
     * on the cube and checking to see how many moves it will take to get that sticker
     * onto the correct face (this turns out to be fairly simple, and we can create
     * a basic mapping (see below)). All of these "distances" are then added up and
     * divided by 8, since we move 8 stickers with each quarter turn. This will occasionally
     * underestimate the number of moves to solve, but will never overestimate and is thus
     * an admissible heuristic.
     *
     * The idea for this heuristic was inspired by the following article on
     * StackOverflow discussing heuristics for a 3x3 cube.
     * https://stackoverflow.com/questions/60130124/heuristic-function-for-rubiks-cube-in-a-algorithm-artificial-intelligence
     *
     * Heuristic Mapping:
     * 0 <= On the correct face
     * 1 <= On the opposite of the correct face (relative back)
     * 2 <= On a face adjacent to the correct face (on the top, bottom, and sides)
     * If it was to take more than 2 moves, we could just make a move in the opposite direction
     * to turn that number of moves into moves(mod 2)
     *
     * @param Cube cube the cube to estimate the number of moves to a solved state
     * @return int the estimated number of moves to a solved state
     */
    static int heuristic(Cube cube) {
        // First, let's get the cublet that we are fixing in place
        // Front, top, left cublet is what we will fix
        int frontStickerFixed = cube.faces[0][0][0];
        int topStickerFixed = cube.faces[Cube.TOP][1][0];
        int leftStickerFixed = cube.faces[Cube.LEFT][0][1];

        // Now, using that color let's get all of the colors that the
        // relative faces should be. We have to do this because there
        // is not only one solved state... there are many different
        // orientations that could be correct
        int[] correctFaceColors = new int[6];
        correctFaceColors[Cube.FRONT] = frontStickerFixed;
        correctFaceColors[Cube.BACK] = Cube.OPPOSITE_FACES[frontStickerFixed];
        correctFaceColors[Cube.TOP] = topStickerFixed;
        correctFaceColors[Cube.BOTTOM] = Cube.OPPOSITE_FACES[topStickerFixed];
        correctFaceColors[Cube.LEFT] = leftStickerFixed;
        correctFaceColors[Cube.RIGHT] = Cube.OPPOSITE_FACES[leftStickerFixed];

        // Now, we need to loop through each face and see how many moves it would take
        // to get that sticker to the correct face
        // Assume the heuristic will be 0, i.e. the cube is solved
        int h = 0;

        // Loop through each face
        for (int i = 0; i < 6; i++) {
            // Get the color that this face should be
            int frontColor = correctFaceColors[i];

            // Get the color that the back face should be
            int oppositeI = 0;
            if (i % 2 == 0)
                oppositeI = i + 1;
            else
                oppositeI = i - 1;
            int backColor = correctFaceColors[oppositeI];

            // Loop through all 4 of the stickers on this face
            // If the sticker is supposed to be here, we add 0
            // If the sticker is supposed to be on the opposite side, we add 2
            // If the sticker is neither supposed to be here or on the opposite side,
            // we add 1 since it must need to be on some adjacent face by process of elimination
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    if (frontColor == cube.faces[i][j][k])
                        // We realisticly would do nothing
                        h = h + 0;
                    else if (backColor == cube.faces[i][j][k])
                        // Requires 2 moves to get the sticker on the correct side (on the back)
                        h = h + 2;
                    else
                        // On the adjacent side, requires 1 move to get the sticker on the correct side
                        h = h + 1;
                }
            }
        }

        // Now, we divide by 12 because we are moving 8 stickers at a time and 12
        // will ensure that this h is admissible...
        // We just use integer division to make life easy with pretty integers :)
        // Integer division truncates so this will be the same as taking the floor function
        return h / 8;
    }

    /**
     * This is the recursive method for the cube solving. This will continue exploring its children
     * until either memory runs out, the program is terminated, the maximum function value of f is
     * reached, or a solution is found. Once a solution is found, the last Node in the solution
     * will be returned so the solution can be retrieved. If no solution was found, this will
     * return null.
     *
     * @param Node n the current node to explore
     * @param int maxF the maximum allowable value of the function, f
     * @return Node the final Node in the solution
     */
    private static Node aDepthFirstSearch(Node n, int maxF) {
        // We just visited a new node
        SolveCube.numNodesVisited = SolveCube.numNodesVisited.add(bigInt1);

        // Not solved and at the end of where we're going
        if (n.f >= maxF)
            return null;
        // Check and see if the node is solved
        else if (n.cubeState.isSolved()) {
            return n;
        }

        // Recursive step
        Node[] children = n.children();
        for (Node child : children) {
            Node returnNode = aDepthFirstSearch(child, maxF);

            // If we find a solution, let's stop and return
            if (returnNode != null)
                return returnNode;
        }

        // No solution found, return null
        return null;
    }

    /**
     * We use IDA* as proposed by Dr. Korf...
     * We will do a DFS but instead of looking at nodes,
     * we are going to look at path costs. Once the path is
     * greater than the admissible g + h, we will begin on
     * another node. Continue this with iteratively increasing
     * maximum f.
     *
     * Korf, Richard E. “Depth-First Iterative-Deepening: An Optimal Admissible
     * Tree Search.” Artificial Intelligence, vol. 28, no. 1, 1986, pp. 97–109.
     *
     * @param Cube cube the cube that we want to solve
     * @return Node the Node of the final solved state
     */
    public static Node solveCube(Cube cube) {
        // Start at the initial state
        int maxF = 0;

        // Begin the search until we find a solution
        Node soln = null;
        while (soln == null) {
            // Try to find a solution
            SolveCube.numNodesCreated = SolveCube.numNodesCreated.add(bigInt1);
            soln = aDepthFirstSearch(new Node(null, NO_MOVE, NO_MOVE, (Cube)cube.clone(), 0), maxF);

            // If no solution is found, increment the maxF
            if (soln == null) maxF++;
        }

        // Return the number of moves to solve the cube
        return soln;
    }

    /**
     * This will reset the counters within this static class since we cannot create
     * a new object.
     */
    public static void reset() {
        numNodesCreated = new BigInteger("0");
        numNodesVisited = new BigInteger("0");
    }

}

/**
 * Node for the IDA* solving algorithm
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class Node
 */
class Node {

    /**
     * The curent state of the cube in the Node
     */
    Cube cubeState;

    /**
     * The parent of this current Node
     */
    Node parent;

    /**
     * The move that is made (references the SolveCube move constants)
     */
    int move;

    /**
     * The direction of the move that is made (references the SolveCube move constants)
     */
    int direction;

    /**
     * Value of f = g + h where g is the depth of the Node and h is the value of the heuristic
     */
    int f;

    /**
     * Current depth (g) for calculating the frontier depths
     */
    int depth;

    /**
     * Node Constructor
     *
     * @param Node parent the parent Node of the current node
     * @param int move the move to get this Node
     * @param int direction the direction of move to get this Node
     * @param Cube cubeState current state of the cube in this current Node
     * @param int depth current depth of the current Node
     */
    Node(Node parent, int move, int direction, Cube cubeState, int depth) {
        this.cubeState = cubeState;
        this.parent = parent;
        this.move = move;
        this.direction = direction;
        this.f = depth + SolveCube.heuristic(cubeState);
        this.depth = depth;
    }

    /**
     * Makes a move to create the children. This is used to generate the
     * children nodes and will return a copy of the current state to prevent
     * any sort of shallow issues.
     *
     * @param int move the move to make
     * @param int direction the direction to make the move
     * @return Cube a clone of the current state cube with the move made
     */
    Cube makeMove(int move, int direction) {
        Cube newState = (Cube)cubeState.clone();

        // Get the number of turns to make
        int turnNum = 0;
        if (direction == SolveCube.CLOCKWISE)
            turnNum = 1;
        else if (direction == SolveCube.COUNTERCLOCKWISE)
            turnNum = 3;

        // Switch the integer move value with an actual move
        switch (move) {
            case SolveCube.NO_MOVE:
                break;
            case SolveCube.FRONT:
                newState.front(turnNum);
                break;
            case SolveCube.BACK:
                newState.back(turnNum);
                break;
            case SolveCube.LEFT:
                newState.left(turnNum);
                break;
            case SolveCube.RIGHT:
                newState.right(turnNum);
                break;
            case SolveCube.UP:
                newState.up(turnNum);
                break;
            case SolveCube.DOWN:
                newState.down(turnNum);
                break;
        }

        // Return this new state
        return newState;
    }

    /**
     * Generates an array of all of the children of the current node
     *
     * @return Node[] the array of each child of the current node
     */
    Node[] children() {
        // Generate a child for every possible move from this state
        Node[] children = new Node[12];

        // Make each move
        for (int i = 0; i < 12; i++) {
            int move = (i % 6) + 1;
            int direction = (i % 2) + 1;

            // Add the new node to the array of children
            children[i] = new Node(this, move, direction, makeMove(move, direction), depth + 1);

            // Just created a new node
            SolveCube.numNodesCreated = SolveCube.numNodesCreated.add(SolveCube.bigInt1);
        }

        // Return all children
        return children;
    }
}
