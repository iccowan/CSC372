/*
  CSC 372 S21
  A1
  Interface to use for your Cube
 */
interface A1Cube
{
  void front(int k); // rotate front clockwise k quarter turns
  // Note that k here could be any valid Java integer (use mod!)
  void back(int k); // rotate back face, etc.
  void left(int k); 
  void right(int k);
  void up(int k);
  void down(int k);
  boolean isSolved(); // returns true if the cube is solved
  // Note that if the whole cube is rotated, it is still solved!
  void randomize(int k);
  /*
    for i := 1 to k do:
      choose a random side (front, back, etc.) and direction
          (counterclockwise = 1 turn, counterclockwise = 3 turns or -1 turns)
          that do not undo the last turn
      apply the turn
    end for
   */
  void reset();
  void print();
  A1Cube clone(); // returns a deep copy of the cube object
}
