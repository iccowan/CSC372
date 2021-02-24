import java.util.ArrayList;

/**
 * Heuristic to determine the least number of moves the cube can be solved in
 * Uses the A* algorithm
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class A1CubeNumMoves
 */
public class A1CubeNumMoves {
    /**
     * Keep track of our frontier using an ArrayList.
     * These are nodes that we can explore next, and we will choose the
     * one with the least weight.
     */
    private ArrayList<CubeStateNode> frontier = new ArrayList<CubeStateNode>();

    /**
     * "Recursive" method to search for the solution using A*
     *
     * @return int the number of moves to a solution
     *
     * @implNote Implemented using a while loop to prevent a StackOverflow, but the
     *           same concept applies as using recursion
     */
    private int findSolutionMoves() {
        // No solution has been found
        boolean solution = false;

        // Keep looping while there is no solution
        // Once we find a solution, we will return from within
        // this loop so as a paradox, we will never update solution
        while (! solution) {
            // Let's loop through the frontier and see where we want to search next
            int minDepth = Integer.MAX_VALUE;
            CubeStateNode exploreNode = frontier.get(0);
            for (CubeStateNode node : frontier) {
                // We have a node with a lesser depth
                if (node.depth < minDepth) {
                    minDepth = node.depth;
                    exploreNode = node;
                }
            }

            // Now we have the node we want to explore, so let's iterate over it's children
            for (CubeStateNode childNode : exploreNode.childNodes()) {
                // If we find a child that is solved, we return the depth we're at
                if (childNode.cubeState.isSolved()) {
                    return exploreNode.depth + 1;
                }

                // If the child is not solved, we add to the frontier to further explore
                frontier.add(childNode);
            }

            // Remove the explored node so we don't continue looking at the same node
            frontier.remove(exploreNode);
        }

        // If we ever get here, something went wrong because we return
        // from within the loop
        return Integer.MAX_VALUE;
    }

    /**
     * Public method to begin determining the number of moves
     * required to solve the cube. If the cube is already solved,
     * this will return the correct answer and not get to any iteration.
     *
     * @param A1Cube cube the cube to determine the number of moves required to solve
     * @return int        the least number of moves required to solve the cube
     */
    public int numMovesToSolveCube(A1Cube cube) {
        // If the cube is solved, we're finished
        if (cube.isSolved())
            return 0;

        // Get a cube to work with
        A1Cube workingCube = cube.clone();

        // First, let's create a node for this cube and add to frontier
        CubeStateNode parentNode = new CubeStateNode(0, workingCube);
        frontier.add(parentNode);

        // Return the number of moves it takes to get to a solution
        return findSolutionMoves();
    }

    /**
     * Reset the object. This can be used if testing repetitively instead
     * of creating a new object. Creating a new object has the same effect.
     */
    public void reset() {
        // Reset the frontier
        frontier = new ArrayList<CubeStateNode>();
    }
}

/**
 * Nodes for our exploration
 *
 * @class CubeStateNode
 */
class CubeStateNode {
    /**
     * Depth in the tree of the current Node
     */
    int depth;

    /**
     * Current state of the cube at this point in the tree
     */
    A1Cube cubeState;

    /**
     * We use this to simplify creating all of the children for this node
     */
    private static final int FRONT = 0;
    private static final int BACK = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int DOWN = 4;
    private static final int UP = 5;

    /**
     * Constructor
     *
     * @param int depth        Current depth of this node in the tree
     * @param A1Cube cubeState A1Cube for the current state
     */
    CubeStateNode(int depth, A1Cube cubeState) {
        this.depth = depth;
        this.cubeState = cubeState;
    }

    /**
     * Generates a new child based on the move and number of turns specified.
     *
     * @param int move     the face to move on the cube
     * @param int numTurns the number of turns to make on the specified face
     * @return A1Cube      the child cube
     */
    private A1Cube createChild(int move, int numTurns) {
        // Create a clone of the cube
        A1Cube child = cubeState.clone();

        // Switch the move with the proper move
        // and make the move for the number of turns
        switch (move) {
            case FRONT:
                child.front(numTurns);
                break;
            case BACK:
                child.back(numTurns);
                break;
            case LEFT:
                child.left(numTurns);
                break;
            case RIGHT:
                child.right(numTurns);
                break;
            case DOWN:
                child.down(numTurns);
                break;
            case UP:
                child.up(numTurns);
                break;
        }

        // Return the child
        return child;
    }

    /**
     * Creates an array list of all of the children for the specified node.
     *
     * @return ArrayList<CubeStateNode> all of the children states from this state
     */
    ArrayList<CubeStateNode> childNodes() {
        // Create a list to put the children in
        ArrayList<CubeStateNode> children = new ArrayList<CubeStateNode>();

        // Create a new cube for each possible child state and add to children
        // Make each move 3 times (a 4th time would reset to original state)
        for (int i = 0; i < 6; i++)
            for (int j = 1; j <= 3; j++)
                children.add(new CubeStateNode(depth + 1, createChild(i, j)));

        // Return the children
        return children;
    }
}