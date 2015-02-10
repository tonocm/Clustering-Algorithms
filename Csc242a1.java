import java.io.*;
import java.util.*;
import javax.swing.*;

class Csc242a1 {
  
  static Map map;
  static JFrame frame;
  
  public static void main (String[] args) throws IOException {
    
    //Variables
    File file;
    ArrayList<Coordinates> coords = new ArrayList<Coordinates>(10);
    int CLUSTERS = 3, i, n=0,k;
    double[] cx = new double[CLUSTERS];
    double[] cy = new double[CLUSTERS];
    double INF=999999;
    double x, y, minx=INF, miny=INF, maxx=0, maxy=0;
    double tempDist, mindist;
    double[][] tempClustX = new double[CLUSTERS][2];
    double[][] tempClustY = new double[CLUSTERS][2];
    int minK=-1, tempK;
    boolean flag = false;
    
    
    if (args.length == 0) {
      file = new File("A.txt");
    }
    else {
      file = new File(args[0]);
    }
    
    if(!file.exists()){
      System.out.println("Error, file not found.");
      System.exit(1);
    }
    
    Scanner scan = new Scanner(file);
    
    while(scan.hasNext()){
      x = Double.parseDouble(scan.next());
      y = Double.parseDouble(scan.next());
      
      if(x < minx)
        minx = x;
      if(y < miny)
        miny = y;
      if(x > maxx)
        maxx = x;
      if(y > maxy)
        maxy = y;
      coords.add(new Coordinates(x,y));
      n++;
    }
    
    for(i=0; i < CLUSTERS; i++){
      cx[i] = (Math.random()*(maxx-minx))+minx;
      cy[i] = (Math.random()*(maxy-miny))+miny;
    }

    //Change CONDITION!!!!!!!!!!!!!
    while(!flag){
      //Assigning Cluster Elements
      for(i=0; i < n; i++){
        mindist=INF;

        //Calculate distance for each point
        for(k=0; k < CLUSTERS; k++){
          
          tempDist = Math.sqrt(Math.pow(((coords.get(i).x)-cx[k]),2)+Math.pow(((coords.get(i).y)-cy[k]),2));

          if(tempDist < mindist){
            mindist = tempDist;
            minK = k;
          }
        }
        
        //Set point to nearest cluster
        coords.get(i).c = minK;
      }
        
      zero(tempClustX);
      zero(tempClustY);
      
      //Recalculating Centers      
      for(i=0; i < n; i++){
        tempClustX[coords.get(i).c][0] = tempClustX[coords.get(i).c][0] + coords.get(i).x;
        tempClustY[coords.get(i).c][0] = tempClustY[coords.get(i).c][0] + coords.get(i).y;
        
        tempClustX[coords.get(i).c][1]++;
        tempClustY[coords.get(i).c][1]++;
      }
      
      for(k=0; k < CLUSTERS; k++){
        tempClustX[k][0] = tempClustX[k][0]/tempClustX[k][1];
        tempClustY[k][0] = tempClustY[k][0]/tempClustY[k][1];
      }
      
      flag = true; //to stop while loop
      for(k=0; k < CLUSTERS; k++){
        if(cx[k] != tempClustX[k][0]){
          flag = false;
          cx[k] = tempClustX[k][0];
        }
        if(cy[k] != tempClustY[k][0]){
          flag = false;
          cy[k] = tempClustY[k][0];
        }
      }
    }
    
    System.out.println("Finished... Printing");
    map = new Map(coords,minx,maxx,miny,maxy);
    draw();
  }
  
  public static void zero(double[][] a){
    for(int i=0; i < a.length; i++){
      a[i][0] = 0;
      a[i][1] = 0;
    }   
  }
  
  public static void draw(){
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(map);
    frame.setSize(800,800);
    frame.setVisible(true);
  }
}