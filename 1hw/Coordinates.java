public class Coordinates{
  
  public double x; // x-coordinate
  public double y; // y-coordinate
  public int c; //cluster number
  public Coordinates parent, leftChild, rightChild;
  
  //for use in k-means
  public Coordinates(double x, double y){
    this.x = x;
    this.y = y;
    c = -1;
    parent = null;
    leftChild = null;
    rightChild = null;
  }
  
  //for use in heirarchical
 /* public Coordinates(double x, double y, int c){
    this.x = x;
    this.y = y;
    this.c = c;
    parent = null;
    leftChild = null;
    rightChild = null;
  }
  
  //for use in building BST
  public Coordinates(Coordinates l, Coordinates r){
    x = 0;
    y = 0;
    c = 0;
    parent = null;
    leftChild = l;
    rightChild = r;
  }*/
  
  public String out(){
    return "X Coord: " + x + " Y Coord: " + y;
  }
}