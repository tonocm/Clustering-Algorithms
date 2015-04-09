public class State{
  
  int index, h, g, f;
  int [][] table;
  State parent;
  
  public State(int[][] table, int h){
    this.table = table;
    this.h = h;
    parent = null;
    g = 0;
    f = g+h;
  }
  
  public State(int[][] table, int h, int g, State parent){
    this.table = table;
    this.h = h;
    this.parent = parent;
    this.g = g;
    f = g+h;
  }
  
  public State(int index){
    this.index = index;
  }
  
}