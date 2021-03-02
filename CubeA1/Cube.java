import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;

/**
 * Models a 2x2 Rubik's Cube
 * Ian Cowan
 * CSC 372
 * Spring 2021
 *
 * @class Cube
 * @implements A1Cube
 */
public class Cube implements A1Cube {
    /**
     * Keep track of the faces in a 3D array
     * The following track what is stored in each depth of the array:
     *     [Faces][Face Top and Bottom][Top/Bottom]
    */
    int[][][] faces = {
            { // front - white
                {0,0},
                {0,0}
            },
            { // back - yellow
                {1,1},
                {1,1}
            },
            { // top - blue
                {2,2},
                {2,2}
            },
            { // bottom - green
                {3,3},
                {3,3}
            },
            { // left - orange
                {4,4},
                {4,4}
            },
            { // right - red
                {5,5},
                {5,5}
            },
        };

    /**
     * Keep track of the indices for each
     * face for consistency
     */
    final static int FRONT = 0; // 0's
    final static int BACK = 1; // 1's
    final static int TOP = 2; // 4's
    final static int BOTTOM = 3; // 5's
    final static int LEFT = 4; // 3's
    final static int RIGHT = 5; // 2's

    /**
     * Keep track of the opposite faces and adjacent faces for solving
     * Index is the current face... the value in the array is the opposite face
     */
    final static int[] OPPOSITE_FACES = {1, 0, 3, 2, 5, 4};
    final static int[] TOP_FACES = {2, 2, 1, 0, 2, 2};
    final static int[] BOTTOM_FACES = {3, 3, 0, 1, 3, 3};
    final static int[] LEFT_FACES = {4, 5, 4, 4, 1, 0};
    final static int[] RIGHT_FACES = {5, 4, 5, 5, 0, 1};

    /**
     * Clockwise and Counterclockwise integer representations for consistency
     */
    private final static int CLOCKWISE = 0;
    private final static int COUNTERCLOCKWISE = 1;

    /**
     * Rotates the specified face one quarter turn in the direction
     * specified
     *
     * Modifies the face aray for the particular face indicated
     *
     * @param int faceIndex the face to move
     * @param int direction the direction to move the face (clockwise or counterclockwise)
     */
    private void rotateFace(int faceIndex, int direction) {
        // get the face and all the positions
        int[][] face = faces[faceIndex];
        int pos1 = face[0][0]; // (0,0)
        int pos2 = face[0][1]; // (0,1)
        int pos3 = face[1][0]; // (1,0)
        int pos4 = face[1][1]; // (1,1)

        if (direction == CLOCKWISE) {
            // rotate clockwise
            // (0,0) -> (0,1)
            face[0][1] = pos1;
            // (0,1) -> (1,1)
            face[1][1] = pos2;
            // (1,0) -> (0,0)
            face[0][0] = pos3;
            // (1,1) -> (1,0)
            face[1][0] = pos4;
        } else if (direction == COUNTERCLOCKWISE) {
            // rotate counterclockwise
            // (0,1) -> (0,0)
            face[0][0] = pos2;
            // (1,1) -> (0,1)
            face[0][1] = pos4;
            // (0,0) -> (1,0)
            face[1][0] = pos1;
            // (1,0) -> (1,1)
            face[1][1] = pos3;
        }
    }

    /**
     * Update the faces adjacent to the right face for a quarter turn on
     * the front face in the clockwise or counterclockwise direction.
     *
     * Modifies the passed face arrays to represent the new permutation
     *
     * @param int direction  clockwise or counterclockwise direction
     * @param int[][] top    the face int array that references the top face
     * @param int[][] right  the face int array that references the right face
     * @param int[][] bottom the face int array that references the bottom face
     * @param int[][] left   the face int array that references the left face
     *
     * @implNote The faces referenced as parameters are in reference to the front
     *           when rotating the cube such that the front face is now the front face
     */
    private void rotateAdjacentFacesFront(int direction, int[][] top, int[][] right, int[][] bottom, int[][] left) {
        // Get the values that will move
        int top3 = top[1][0];
        int top4 = top[1][1];
        int right1 = right[0][0];
        int right3 = right[1][0];
        int bottom1 = bottom[0][0];
        int bottom2 = bottom[0][1];
        int left2 = left[0][1];
        int left4 = left[1][1];

        // now, let's rotate the appropriate sides
        if (direction == CLOCKWISE) {
            // TOP(1,0) -> RIGHT(0,0)
            right[0][0] = top3;
            // TOP(1,1) -> RIGHT(1,0)
            right[1][0] = top4;

            // RIGHT(0,0) -> BOTTOM(0,1)
            bottom[0][1] = right1;
            // RIGHT(1,0) -> BOTTOM(0,0)
            bottom[0][0] = right3;

            // BOTTOM(0,0) -> LEFT(0,1)
            left[0][1] = bottom1;
            // BOTTOM(0,1) -> LEFT(1,1)
            left[1][1] = bottom2;

            // LEFT(0,1) -> TOP(1,1)
            top[1][1] = left2;
            // LEFT(1,1) -> TOP(1,0)
            top[1][0] = left4;
        } else if (direction == COUNTERCLOCKWISE) {
            // TOP(1,0) -> LEFT(1,1)
            left[1][1] = top3;
            // TOP(1,1) -> LEFT(0,1)
            left[0][1] = top4;

            // LEFT(1,1) -> BOTTOM(0,1)
            bottom[0][1] = left4;
            // LEFT(0,1) -> BOTTOM(0,0)
            bottom[0][0] = left2;

            // BOTTOM(0,1) -> RIGHT(0,0)
            right[0][0] = bottom2;
            // BOTTOM(0,0) -> RIGHT(1,0)
            right[1][0] = bottom1;

            // RIGHT(0,0) -> TOP(1,0)
            top[1][0] = right1;
            // RIGHT(1,0) -> TOP(1,1)
            top[1][1] = right3;
        }
    }

    /**
     * Rotate the front face one quarter turn clockwise or counterclockwise.
     *
     * Modifies the front face array to represent the new permutation
     *
     * @param int direction clockwise or counterclockwise direction to turn the face
     */
    private void rotateFront(int direction) {
        // first, rotate just the front face
        rotateFace(FRONT, direction);

        // Get all the faces that we're going to be moving
        // These names are RELATIVE to the current face
        int[][] top = faces[TOP];
        int[][] right = faces[RIGHT];
        int[][] bottom = faces[BOTTOM];
        int[][] left = faces[LEFT];

        rotateAdjacentFacesFront(direction, top, right, bottom, left);
    }

    /**
     * Update the faces adjacent to the right face for a quarter turn on
     * the back face in the clockwise or counterclockwise direction.
     *
     * Modifies the passed face arrays to represent the new permutation
     *
     * @param int direction  clockwise or counterclockwise direction
     * @param int[][] top    the face int array that references the top face
     * @param int[][] right  the face int array that references the right face
     * @param int[][] bottom the face int array that references the bottom face
     * @param int[][] left   the face int array that references the left face
     *
     * @implNote The faces referenced as parameters are in reference to the back
     *           when rotating the cube such that the back face is now the front face
     */
    private void rotateAdjacentFacesBack(int direction, int[][] top, int[][] right, int[][] bottom, int[][] left) {
        // Get the values that will move
        int top1 = top[0][0];
        int top2 = top[0][1];
        int right1 = right[0][0];
        int right3 = right[1][0];
        int bottom3 = bottom[1][0];
        int bottom4 = bottom[1][1];
        int left2 = left[0][1];
        int left4 = left[1][1];

        // now, let's rotate the appropriate sides
        if (direction == CLOCKWISE) {
            // TOP(0,1) -> RIGHT(0,0)
            right[0][0] = top2;
            // TOP(0,0) -> RIGHT(1,0)
            right[1][0] = top1;

            // RIGHT(0,0) -> BOTTOM(1,0)
            bottom[1][0] = right1;
            // RIGHT(1,0) -> BOTTOM(1,1)
            bottom[1][1] = right3;

            // BOTTOM(1,1) -> LEFT(0,1)
            left[0][1] = bottom4;
            // BOTTOM(1,0) -> LEFT(1,1)
            left[1][1] = bottom3;

            // LEFT(0,1) -> TOP(0,0)
            top[0][0] = left2;
            // LEFT(1,1) -> TOP(0,1)
            top[0][1] = left4;
        } else if (direction == COUNTERCLOCKWISE) {
            // TOP(0,1) -> LEFT(1,1)
            left[1][1] = top2;
            // TOP(0,0) -> LEFT(0,1)
            left[0][1] = top1;

            // LEFT(1,1) -> BOTTOM(1,0)
            bottom[1][0] = left4;
            // LEFT(0,1) -> BOTTOM(1,1)
            bottom[1][1] = left2;

            // BOTTOM(1,0) -> RIGHT(0,0)
            right[0][0] = bottom3;
            // BOTTOM(1,1) -> RIGHT(1,0)
            right[1][0] = bottom4;

            // RIGHT(0,0) -> TOP(0,1)
            top[0][1] = right1;
            // RIGHT(1,0) -> TOP(0,0)
            top[0][0] = right3;
        }
    }

    /**
     * Rotate the back face one quarter turn clockwise or counterclockwise.
     *
     * Modifies the back face array to represent the new permutation
     *
     * @param int direction clockwise or counterclockwise direction to turn the face
     */
    private void rotateBack(int direction) {
        // first, rotate just the top face
        rotateFace(BACK, direction);

        // Get all the faces that we're going to be moving
        // These names are RELATIVE to the current face
        int[][] top = faces[TOP];
        int[][] right = faces[LEFT];
        int[][] bottom = faces[BOTTOM];
        int[][] left = faces[RIGHT];

        rotateAdjacentFacesBack(direction, top, right, bottom, left);
    }

    /**
     * Update the faces adjacent to the right face for a quarter turn on
     * the top face in the clockwise or counterclockwise direction.
     *
     * Modifies the passed face arrays to represent the new permutation
     *
     * @param int direction  clockwise or counterclockwise direction
     * @param int[][] top    the face int array that references the top face
     * @param int[][] right  the face int array that references the right face
     * @param int[][] bottom the face int array that references the bottom face
     * @param int[][] left   the face int array that references the left face
     *
     * @implNote The faces referenced as parameters are in reference to the top
     *           when rotating the cube such that the top face is now the front face
     */
    private void rotateAdjacentFacesTop(int direction, int[][] top, int[][] right, int[][] bottom, int[][] left) {
        // Get the values that will move
        int top2 = top[0][1];
        int top1 = top[0][0];
        int right1 = right[0][0];
        int right2 = right[0][1];
        int bottom1 = bottom[0][0];
        int bottom2 = bottom[0][1];
        int left1 = left[0][0];
        int left2 = left[0][1];

        // now, let's rotate the appropriate sides
        if (direction == CLOCKWISE) {
            // TOP(0,1) -> RIGHT(0,1)
            right[0][1] = top2;
            // TOP(0,0) -> RIGHT(0,0)
            right[0][0] = top1;

            // RIGHT(0,1) -> BOTTOM(0,1)
            bottom[0][1] = right2;
            // RIGHT(0,0) -> BOTTOM(0,0)
            bottom[0][0] = right1;

            // BOTTOM(0,0) -> LEFT(0,0)
            left[0][0] = bottom1;
            // BOTTOM(0,1) -> LEFT(0,1)
            left[0][1] = bottom2;

            // LEFT(0,1) -> TOP(0,1)
            top[0][1] = left2;
            // LEFT(0,0) -> TOP(0,0)
            top[0][0] = left1;
        } else if (direction == COUNTERCLOCKWISE) {
            // TOP(1,0) -> LEFT(0,1)
            left[0][1] = top2;
            // TOP(0,0) -> LEFT(0,0)
            left[0][0] = top1;

            // LEFT(0,1) -> BOTTOM(0,1)
            bottom[0][1] = left2;
            // LEFT(0,0) -> BOTTOM(0,0)
            bottom[0][0] = left1;

            // BOTTOM(0,1) -> RIGHT(0,1)
            right[0][1] = bottom2;
            // BOTTOM(0,0) -> RIGHT(0,0)
            right[0][0] = bottom1;

            // RIGHT(0,0) -> TOP(0,0)
            top[0][0] = right1;
            // RIGHT(0,1) -> TOP(0,1)
            top[0][1] = right2;
        }
    }

    /**
     * Rotate the top face one quarter turn clockwise or counterclockwise.
     *
     * Modifies the top face array to represent the new permutation
     *
     * @param int direction clockwise or counterclockwise direction to turn the face
     */
    private void rotateTop(int direction) {
        // first, rotate just the top face
        rotateFace(TOP, direction);

        // Get all the faces that we're going to be moving
        // These names are RELATIVE to the current face
        int[][] top = faces[BACK];
        int[][] right = faces[RIGHT];
        int[][] bottom = faces[FRONT];
        int[][] left = faces[LEFT];

        rotateAdjacentFacesTop(direction, top, right, bottom, left);
    }

    /**
     * Update the faces adjacent to the right face for a quarter turn on
     * the bottom face in the clockwise or counterclockwise direction.
     *
     * Modifies the passed face arrays to represent the new permutation
     *
     * @param int direction  clockwise or counterclockwise direction
     * @param int[][] top    the face int array that references the top face
     * @param int[][] right  the face int array that references the right face
     * @param int[][] bottom the face int array that references the bottom face
     * @param int[][] left   the face int array that references the left face
     *
     * @implNote The faces referenced as parameters are in reference to the bottom
     *           when rotating the cube such that the bottom face is now the front face
     */
    private void rotateAdjacentFacesBottom(int direction, int[][] top, int[][] right, int[][] bottom, int[][] left) {
        // Get the values that will move
        int top3 = top[1][0];
        int top4 = top[1][1];
        int right3 = right[1][0];
        int right4 = right[1][1];
        int bottom3 = bottom[1][0];
        int bottom4 = bottom[1][1];
        int left3 = left[1][0];
        int left4 = left[1][1];

        // now, let's rotate the appropriate sides
        if (direction == CLOCKWISE) {
            // TOP(1,0) -> RIGHT(1,0)
            right[1][0] = top3;
            // TOP(1,1) -> RIGHT(1,1)
            right[1][1] = top4;

            // RIGHT(1,1) -> BOTTOM(1,1)
            bottom[1][1] = right4;
            // RIGHT(1,0) -> BOTTOM(1,0)
            bottom[1][0] = right3;

            // BOTTOM(1,1) -> LEFT(1,1)
            left[1][1] = bottom4;
            // BOTTOM(1,0) -> LEFT(1,0)
            left[1][0] = bottom3;

            // LEFT(1,1) -> TOP(1,1)
            top[1][1] = left4;
            // LEFT(1,0) -> TOP(1,0)
            top[1][0] = left3;
        } else if (direction == COUNTERCLOCKWISE) {
            // TOP(1,1) -> LEFT(1,1)
            left[1][1] = top4;
            // TOP(1,0) -> LEFT(1,0)
            left[1][0] = top3;

            // LEFT(1,1) -> BOTTOM(1,1)
            bottom[1][1] = left4;
            // LEFT(1,0) -> BOTTOM(1,0)
            bottom[1][0] = left3;

            // BOTTOM(1,1) -> RIGHT(1,1)
            right[1][1] = bottom4;
            // BOTTOM(1,0) -> RIGHT(1,0)
            right[1][0] = bottom3;

            // RIGHT(1,1) -> TOP(1,1)
            top[1][1] = right4;
            // RIGHT(1,0) -> TOP(1,0)
            top[1][0] = right3;
        }
    }

    /**
     * Rotate the bottom face one quarter turn clockwise or counterclockwise.
     *
     * Modifies the bottom face array to represent the new permutation
     *
     * @param int direction clockwise or counterclockwise direction to turn the face
     */
    private void rotateBottom(int direction) {
        // first, rotate just the top face
        rotateFace(BOTTOM, direction);

        // Get all the faces that we're going to be moving
        // These names are RELATIVE to the current face
        int[][] top = faces[FRONT];
        int[][] right = faces[RIGHT];
        int[][] bottom = faces[BACK];
        int[][] left = faces[LEFT];

        rotateAdjacentFacesBottom(direction, top, right, bottom, left);
    }

    /**
     * Update the faces adjacent to the left face for a quarter turn on
     * the right face in the clockwise or counterclockwise direction.
     *
     * Modifies the passed face arrays to represent the new permutation
     *
     * @param int direction  clockwise or counterclockwise direction
     * @param int[][] top    the face int array that references the top face
     * @param int[][] right  the face int array that references the right face
     * @param int[][] bottom the face int array that references the bottom face
     * @param int[][] left   the face int array that references the left face
     *
     * @implNote The faces referenced as parameters are in reference to the left
     *           when rotating the cube such that the left face is now the front face
     */
    private void rotateAdjacentFacesLeft(int direction, int[][] top, int[][] right, int[][] bottom, int[][] left) {
        // Get the values that will move
        int top1 = top[0][0];
        int top3 = top[1][0];
        int right1 = right[0][0];
        int right3 = right[1][0];
        int bottom1 = bottom[0][0];
        int bottom3 = bottom[1][0];
        int left2 = left[0][1];
        int left4 = left[1][1];

        // now, let's rotate the appropriate sides
        if (direction == CLOCKWISE) {
            // TOP(0,0) -> RIGHT(0,0)
            right[0][0] = top1;
            // TOP(1,0) -> RIGHT(1,0)
            right[1][0] = top3;

            // RIGHT(0,0) -> BOTTOM(0,0)
            bottom[0][0] = right1;
            // RIGHT(0,1) -> BOTTOM(1,0)
            bottom[1][0] = right3;

            // BOTTOM(0,0) -> LEFT(1,1)
            left[1][1] = bottom1;
            // BOTTOM(1,0) -> LEFT(0,1)
            left[0][1] = bottom3;

            // LEFT(1,1) -> TOP(0,0)
            top[0][0] = left4;
            // LEFT(0,1) -> TOP(1,0)
            top[1][0] = left2;
        } else if (direction == COUNTERCLOCKWISE) {
            // TOP(0,0) -> LEFT(1,1)
            left[1][1] = top1;
            // TOP(1,0) -> LEFT(0,1)
            left[0][1] = top3;

            // LEFT(0,1) -> BOTTOM(1,0)
            bottom[1][0] = left2;
            // LEFT(1,1) -> BOTTOM(0,0)
            bottom[0][0] = left4;

            // BOTTOM(1,0) -> RIGHT(1,0)
            right[1][0] = bottom3;
            // BOTTOM(0,0) -> RIGHT(0,0)
            right[0][0] = bottom1;

            // RIGHT(1,0) -> TOP(1,0)
            top[1][0] = right3;
            // RIGHT(0,0) -> TOP(0,0)
            top[0][0] = right1;
        }
    }

    /**
     * Rotate the left face one quarter turn clockwise or counterclockwise.
     *
     * Modifies the left face array to represent the new permutation
     *
     * @param int direction clockwise or counterclockwise direction to turn the face
     */
    private void rotateLeft(int direction) {
        // first, rotate just the top face
        rotateFace(LEFT, direction);

        // Get all the faces that we're going to be moving
        // These names are RELATIVE to the current face
        int[][] top = faces[TOP];
        int[][] right = faces[FRONT];
        int[][] bottom = faces[BOTTOM];
        int[][] left = faces[BACK];

        rotateAdjacentFacesLeft(direction, top, right, bottom, left);
    }

    /**
     * Update the faces adjacent to the right face for a quarter turn on
     * the right face in the clockwise or counterclockwise direction.
     *
     * Modifies the passed face arrays to represent the new permutation
     *
     * @param int direction  clockwise or counterclockwise direction
     * @param int[][] top    the face int array that references the top face
     * @param int[][] right  the face int array that references the right face
     * @param int[][] bottom the face int array that references the bottom face
     * @param int[][] left   the face int array that references the left face
     *
     * @implNote The faces referenced as parameters are in reference to the right
     *           when rotating the cube such that the right face is now the front face
     */
    private void rotateAdjacentFacesRight(int direction, int[][] top, int[][] right, int[][] bottom, int[][] left) {
        // Get the values that will move
        int top2 = top[0][1];
        int top4 = top[1][1];
        int right1 = right[0][0];
        int right3 = right[1][0];
        int bottom2 = bottom[0][1];
        int bottom4 = bottom[1][1];
        int left2 = left[0][1];
        int left4 = left[1][1];

        // now, let's rotate the appropriate sides
        if (direction == CLOCKWISE) {
            // TOP(1,1) -> RIGHT(0,0)
            right[0][0] = top4;
            // TOP(0,1) -> RIGHT(1,0)
            right[1][0] = top2;

            // RIGHT(0,0) -> BOTTOM(1,1)
            bottom[1][1] = right1;
            // RIGHT(1,0) -> BOTTOM(0,1)
            bottom[0][1] = right3;

            // BOTTOM(1,1) -> LEFT(1,1)
            left[1][1] = bottom4;
            // BOTTOM(0,1) -> LEFT(0,1)
            left[0][1] = bottom2;

            // LEFT(1,1) -> TOP(1,1)
            top[1][1] = left4;
            // LEFT(0,1) -> TOP(0,1)
            top[0][1] = left2;
        } else if (direction == COUNTERCLOCKWISE) {
            // TOP(1,1) -> LEFT(1,1)
            left[1][1] = top4;
            // TOP(0,1) -> LEFT(0,1)
            left[0][1] = top2;

            // LEFT(0,1) -> BOTTOM(0,1)
            bottom[0][1] = left2;
            // LEFT(1,1) -> BOTTOM(1,1)
            bottom[1][1] = left4;

            // BOTTOM(1,1) -> RIGHT(0,0)
            right[0][0] = bottom4;
            // BOTTOM(0,1) -> RIGHT(1,0)
            right[1][0] = bottom2;

            // RIGHT(0,0) -> TOP(1,1)
            top[1][1] = right1;
            // RIGHT(1,0) -> TOP(0,1)
            top[0][1] = right3;
        }
    }

    /**
     * Rotate the right face one quarter turn clockwise or counterclockwise.
     *
     * Modifies the right face array to represent the new permutation
     *
     * @param int direction clockwise or counterclockwise direction to turn the face
     */
    private void rotateRight(int direction) {
        // first, rotate just the top face
        rotateFace(RIGHT, direction);

        // Get all the faces that we're going to be moving
        // These names are RELATIVE to the current face
        int[][] top = faces[TOP];
        int[][] right = faces[BACK];
        int[][] bottom = faces[BOTTOM];
        int[][] left = faces[FRONT];

        rotateAdjacentFacesRight(direction, top, right, bottom, left);
    }

    /**
     * Checks to see if a particular face is in a solved state
     *
     * @param int faceIndex face to check for a solved state
     * @return boolean      whether or not the face is solved
     */
    private boolean isFaceSolved(int faceIndex) {
        // Get the face
        int[][] face = faces[faceIndex];

        // Make sure the face is correct
        // Get the first value of the face and let's compare
        // This should work because if the first number is same
        // as all of the others, then the face is solved. But if the
        // first number is different from any of the other numbers,
        // the face is not solved
        int faceVal = face[0][0];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                if (face[i][j] != faceVal)
                    return false; // Not solved

        // Solved
        return true;
    }

    /**
     * Checks to see if the cube is in a solved state or not
     *
     * @return boolean whether or not the cube is solved
     */
    public boolean isSolved() {
        // Look at each of the 6 sides and determine if the cube is solved
        boolean isSolved = true;
        for (int i = 0; i < 6; i++)
            if (! isFaceSolved(i))
                return false; // one face not solved

        // Everything solved
        return true;
    }

    /**
     * Handles moving a particular face k quarter turns clockwise.
     *
     * @param int face the face to move
     * @param int k    the number of clockwise quarter turns
     */
    private void moveFace(int face, int k) {
        // Move the face k times clockwise
        int rotationNum = k % 4;

        // If we have 1 or 2, we will rotate clockwise
        // If we have 3, we will rotate counterclockwise
        int direction = rotationNum == 3 ? COUNTERCLOCKWISE : CLOCKWISE;
        if (rotationNum == 3)
            rotationNum = 1;

        // Make the move the particular number of times
        for (int i = 0; i < rotationNum; i++) {
            switch (face) {
                case FRONT:
                    rotateFront(direction);
                    break;
                case BACK:
                    rotateBack(direction);
                    break;
                case TOP:
                    rotateTop(direction);
                    break;
                case BOTTOM:
                    rotateBottom(direction);
                    break;
                case LEFT:
                    rotateLeft(direction);
                    break;
                case RIGHT:
                    rotateRight(direction);
                    break;
            }
        }
    }

    /**
     * Moves the front face of the cube k quarter clockwise turns.
     *
     * @param int k the number of turns to make
     */
    @Override
    public void front(int k) {
        moveFace(FRONT, k);
    }

    /**
     * Moves the back face of the cube k quarter clockwise turns.
     *
     * @param int k the number of turns to make
     */
    @Override
    public void back(int k) {
        moveFace(BACK, k);
    }

    /**
     * Moves the top (up) face of the cube k quarter clockwise turns.
     *
     * @param int k the number of turns to make
     */
    @Override
    public void up(int k) {
        moveFace(TOP, k);
    }

    /**
     * Moves the bottom (down) face of the cube k quarter clockwise turns.
     *
     * @param int k the number of turns to make
     */
    @Override
    public void down(int k) {
        moveFace(BOTTOM, k);
    }

    /**
     * Moves the left face of the cube k quarter clockwise turns.
     *
     * @param int k the number of turns to make
     */
    @Override
    public void left(int k) {
        moveFace(LEFT, k);
    }

    /**
     * Moves the right face of the cube k quarter clockwise turns.
     *
     * @param int k the number of turns to make
     */
    @Override
    public void right(int k) {
        moveFace(RIGHT, k);
    }

    /**
     * Randomizes the cube with k random quarter turns.
     *
     * Will change the cube's faces to a random permutation
     *
     * @param int k the number of random quarter turns
     */
    @Override
    public void randomize(int k) {
        // Make k moves to randomize the cube
        Random rand = new Random();
        int[] prevMove = new int[2];
        for (int i = 0; i < k; i++) {
            // Random face
            int face = rand.nextInt(6);

            // Random direction
            int direction = rand.nextInt(2);

            // Make sure we don't have an undo of the previous move
            while (i == 0 && face == prevMove[0] && direction != prevMove[1]) {
                // Random face
                face = rand.nextInt(6);

                // Random direction
                direction = rand.nextInt(2);
            }

            // Move the cube
            int numTurns;
            if (direction == CLOCKWISE)
                numTurns = 1;
            else
                numTurns = 3;

            moveFace(face, numTurns);
            prevMove = new int[] {face, direction};
        }
    }

    /**
     * Resets the cube to its original, solved state.
     *
     * Will completely reset the faces array to the solved state
     */
    @Override
    public void reset() {
        // Loop through all of the faces and all of the
        // values within the faces and reset the faces
        // to the values that denote the correct face values.
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 2; k++)
                    faces[i][j][k] = i;
    }

    /**
     * Creates a deep copy of the cube.
     *
     * @return A1Cube the deep copy of the cube
     */
    @Override
    public A1Cube clone() {
        // Create the new cube to copy
        Cube newCube = new Cube();

        // Copy all of the values for the faces
        // from this cube to the new cube
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 2; k++)
                    newCube.faces[i][j][k] = faces[i][j][k];

        // Return the new cube and caste into an A1Cube
        return (A1Cube) newCube;
    }

    /**
     * Prints a particular face to the console
     *
     * @param int faceIndex the face to print
     */
    private void printFace(int faceIndex) {
        int[][] face = faces[faceIndex];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(face[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints the cube to the console
     */
    @Override
    public void print() {
        // Print out each face
        System.out.println("FRONT:");
        printFace(FRONT);
        System.out.println("BACK:");
        printFace(BACK);
        System.out.println("RIGHT:");
        printFace(RIGHT);
        System.out.println("LEFT:");
        printFace(LEFT);
        System.out.println("TOP:");
        printFace(TOP);
        System.out.println("BOTTOM:");
        printFace(BOTTOM);
    }
}