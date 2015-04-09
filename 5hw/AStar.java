import java.io.*;
import java.util.*;
public class AStar{
  
  static int [][] goal =
  {
    {1,2,3,4,5},
    {6,-1,7,-1,8},
    {9,10,0,11,12},
    {13,-1,14,-1,15},
    {16,17,18,19,20}
  };
  static State [] open = new State[10000]; //arbitrarily high number
  static State [] closed = new State[10000]; //arbitrarily high number
  static int SIZE = 5;
  
  public static void main (String[] args) throws IOException{
    /* Initializing Variables */
    //State [][] table = makeTable();
    
    State start = makeTable();
    State n = null;
    int[][] tmp;
    
    int i=0, j=0, sigmaH, moves=0;
    boolean stop = false;
    
    /* Open or closed at 0 will indicate last index. Info stored in "value"*/
    open[0] = new State(1);
    closed[0] = new State(1);
    /* End */
    
    /* Start State and Adding h's*/
    addOpen(start);
    
    /* if open[0].value is 1, there are no elements in the heap */
    while(open[0].index > 1){
      moves++;
      n = getOpen();
      
      /* looking for start state */
      stop = false;
      for(i=0; i < n.table.length; i++){
        for(j=0; j < n.table[0].length; j++){
          if(n.table[i][j] == 0){
            stop = true;
            break;
          }
        }
        if(stop)
          break;
      }
      //System.out.println(i + " "+ j);
      //printTable(n.table);
      
      addClosed(n);
      
      if(n.h == 0){ //goal state
        stop = true;
        break;
      }
      
      /* Successor Functions */
      
      if((j-1 >= 0) && n.table[i][j-1] != -1){ //can swap left  
        tmp = null;
        tmp = copyTable(n.table); //avoiding java pointer reference.
        tmp[i][j] = n.table[i][j-1];
        tmp[i][j-1] = 0;
        
        insert(tmp, n);
      }
      
      if((i+1 < SIZE) && n.table[i+1][j] != -1){ //can swap down
        tmp = null;
        tmp = copyTable(n.table); //avoiding java pointer reference.
        tmp[i][j] = n.table[i+1][j];
        tmp[i+1][j] = 0;
        
        insert(tmp, n);
      }
      
      
      if((j+1 < SIZE) && n.table[i][j+1] != -1){ //can swap right
        tmp = null;
        tmp = copyTable(n.table); //avoiding java pointer reference.
        tmp[i][j] = n.table[i][j+1];
        tmp[i][j+1] = 0;
        
        insert(tmp, n);
      }
      
      if((i-1 >= 0) && n.table[i-1][j] != -1){ //can swap up
        tmp = null;
        tmp = copyTable(n.table); //avoiding java pointer reference.
        tmp[i][j] = n.table[i-1][j];
        tmp[i-1][j] = 0;
        
        insert(tmp, n);
      }
    }
    
    if(stop == false)
      System.out.println("Sorry. A* Failed.");
    else{
      System.out.println("win");
      while(n.parent != null){
        printTable(n.table);
        n = n.parent;
      }
      System.out.println("Solution Found in " + moves + " moves");
    }
  }
  
  public static int[][] copyTable(int [][] a){
    int[][] out = new int[a.length][a[0].length];
    
    for(int i=0; i < a.length; i++){
     for(int j=0; j < a[0].length; j++)
       out[i][j] = a[i][j];
    }
    
    return out;
  }
  
  public static void printTable(int[][] x){
    for(int i=0; i < x.length; i++){
      for(int j=0; j < x[0].length; j++){
       System.out.print(x[i][j] + "\t"); 
      }
      System.out.println();
    }
    System.out.println("-------------------------------------------------------------------------");
  }
  
  
  public static void insert(int[][] tmp, State n){
    
    int testOpen, testClosed, i;
    State temp;
    
    /* find in open = 1, find in closed = 2 */
    testClosed = -1;
    
    testOpen = found(tmp, 1);
    
    if(testOpen != 1)
      testClosed = found(tmp, 2);
    
    if((testOpen == -1) && (testClosed == -1)){ //Not found in either heap.
      addOpen(new State(tmp, sigmaH(tmp, goal), n.g+1, n));
    }
    else if((testOpen != -1) && (testClosed == -1)){ //found in open
      if(n.g+1 < open[testOpen].g){
        open[testOpen] = new State(tmp, sigmaH(tmp, goal), n.g+1, n); //THIS MIGHT BE WRONG. GOTTA MAKE SURE IT MAKES SENSE.
      }
    }
    else if((testOpen == -1) && (testClosed != -1)){ //found in closed
      if(n.g+1 < closed[testClosed].g){
        addOpen(new State(tmp, sigmaH(tmp, goal), n.g+1, n)); //add n' to open
        
        //get last child. replace. heapDown
        
        i = testClosed;
        
        closed[i] = closed[closed[0].index-1]; //Replacing current element with last element
        closed[closed[0].index-1] = null; //clear.
        closed[0].index = closed[0].index-1; //new first empty.
        
        
        /* Heap Down */
        while(closed[2*i] != null || closed[2*i+1] != null){
          if((closed[2*i] != null) && (closed[i].f > closed[2*i].f)){
            if((closed[2*i+1] != null) && (closed[i].f > closed[2*i+1].f)){
              if(closed[2*i].f > closed[2*i+1].f){
                temp = closed[i];
                closed[i] = closed[2*i+1];
                closed[2*i+1] = temp;
                i = 2*i+1;
              }
              else{
                temp = closed[i];
                closed[i] = closed[2*i];
                closed[2*i] = temp;
                i = 2*i;
              }
            }
            else{
              temp = closed[i];
              closed[i] = closed[2*i];
              closed[2*i] = temp;
              i = 2*i;
            }
          }
          else if((closed[2*i+1] != null) && (closed[i].f > closed[2*i+1].f)){
            temp = closed[i];
            closed[i] = closed[2*i+1];
            closed[2*i+1] = temp;
            i = 2*i+1;
          }
          else
            break; //top element is already smallest. 
        }
        /* End Heap Down */ 
      }
    }
    else
      System.out.println("Error in testOpen and testClosed in can swipe left"); 
  }
  
  public static int found(int[][] tmp, int which){
    int found = -1;
    int i;
    
    if(which == 1){
      for(i=1; i < open[0].index; i++){
        if(sigmaH(tmp, open[i].table) == 0){
          found = i;
          break;
        }
      }
    }
    else{
      for(i=1; i < closed[0].index; i++){
        if(sigmaH(tmp, closed[i].table) == 0){
          found = i;
          break;
        }
      }
    }
    
    return found;
  }
  
  public static void heapDown(int which){
    /* which: 1 = open, 2 = closed */
    /* making aliases for either case */
    
    State[] heap;
    State temp;
    
    if(which == 1)
      heap = open;
    else
      heap = closed;
    
    int i = 1;
    while(heap[2*i] != null || heap[2*i+1] != null){
      if((heap[2*i] != null) && (heap[i].f > heap[2*i].f)){
        if((heap[2*i+1] != null) && (heap[i].f > heap[2*i+1].f)){
          if(heap[2*i].f > heap[2*i+1].f){
            temp = heap[i];
            heap[i] = heap[2*i+1];
            heap[2*i+1] = temp;
            i = 2*i+1;
          }
          else{
            temp = heap[i];
            heap[i] = heap[2*i];
            heap[2*i] = temp;
            i = 2*i;
          }
        }
        else{
          temp = heap[i];
          heap[i] = heap[2*i];
          heap[2*i] = temp;
          i = 2*i;
        }
      }
      else if((heap[2*i+1] != null) && (heap[i].f > heap[2*i+1].f)){
        temp = heap[i];
        heap[i] = heap[2*i+1];
        heap[2*i+1] = temp;
        i = 2*i+1;
      }
      else
        break; //top element is already smallest. 
    }
  }
  
  public static void heapUp(int which){
    /* which: 1 = open, 2 = closed */
    /* making aliases for either case */
    
    State[] heap;
    State temp;
    
    if(which == 1)
      heap = open;
    else
      heap = closed;
    
    int i = heap[0].index -1; //actually ocupied element
    
    while(i > 1){
      if(heap[i].f < heap[i/2].f){
        temp = heap[i];
        heap[i] = heap[i/2];
        heap[i/2] = temp;
        i = i/2;
      }
      else
        i = i/2;
      
    }
  }
  
  public static void addOpen(State x){
    int i = open[0].index;
    
    boolean flag = true;
    while(flag){
      if(open[i] == null){
        open[i] = x;
        flag = false;
        i++;
      }
      else
        i++; //keep adding 1 until empty spot found.
    }
    
    open[0].index = i;
    heapUp(1);
  }
  
  public static State getOpen(){
    int i = open[0].index-1; //last actual element
    
    State out = open[1]; 
    
    open[1] = open[i];
    open[i] = null;
    open[0].index = i;
    
    heapDown(1);
    
    return out;
  }
  
  public static void addClosed(State x){
    int i = closed[0].index;
    boolean flag = true;
    while(flag){
      if(closed[i] == null){
        closed[i] = x;
        flag = false;
        i++;
      }
      else
        i++; //keep adding 1 until empty spot found.
    }
    
    closed[0].index = i;
    heapUp(2);
  }
  
  public static State getClosed(){
    int i = closed[0].index-1; //last actual element
    
    State out = closed[1]; 
    
    closed[1] = closed[i];
    closed[i] = null;
    closed[0].index = i;
    
    heapDown(2);
    
    return out;
  }
  
  
  /* i, j, value */
  public static int h(int x, int y, int value, int[][] cmp){
    /* Black Holes and Empty Spot*/
    if(value == -1 || value == 0)
      return 0;
    
    int i=0, j=0;
    boolean out = false;
    
    for(i=0; i < cmp.length; i++){
      for(j=0; j < cmp[0].length; j++){
        if(cmp[i][j] == value){
          out = true;
          break;
        }
      }
      if(out)
        break;
    }
    return Math.abs(i-x) + Math.abs(y-j);
  }
  
  public static int sigmaH(int[][] table, int[][] cmp){
    int sigmaH = 0;
    
    for(int i=0; i < table.length; i++){
      for(int j=0; j < table[0].length; j++){
        sigmaH = sigmaH + h(i, j, table[i][j], cmp);
      }
    }
    return sigmaH;
  }
  
  public static State makeTable() throws IOException{
    File file = new File("table.txt");
    Scanner scan = new Scanner(file).useDelimiter("\n");
    StringTokenizer token;
    
    int i=0,j=0;
    int[][] table = new int[SIZE][SIZE];
    
    while(scan.hasNext()){
      token = new StringTokenizer(scan.next(), ",");
      
      while(token.hasMoreTokens()){
        table[i][j] = Integer.parseInt(token.nextToken());
        j++;
      }
      
      i++;
      j=0;
    }
    
    return new State(table, sigmaH(table, goal));
  }
}