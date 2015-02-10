import java.io.*;
import java.util.*;
import javax.swing.*;

class Hierarchical {
  
  static Map2 map;
  static JFrame frame;
  
  public static void main (String [] args) throws IOException{
    
    File file;
    double INF=999999;
    double x, y, minx=INF, miny=INF, maxx=0, maxy=0, tempDist, minDist=INF, absMinDist=INF;
    int n=0, i,j, index=0,absIndexJ=0,absIndexI=0;
    ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    
    if (args.length == 0)
      file = new File("B.txt");
    else
      file = new File(args[0]);
    
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
      
      clusters.add(new Cluster(new Coordinates(x,y), n));
      n++; // incrementing cluster's "name"
    }
    
    //change condition
    while(clusters.size() > 2){
      absMinDist=INF;
      
      for(i=0; i < clusters.size(); i++){
        
        minDist = INF;
        
        for(j=0; j < clusters.size();j++){
          if(clusters.get(i).number != clusters.get(j).number){ //avoiding testing same cluster v. self
            tempDist = minDist(clusters.get(i).elements, clusters.get(j).elements); //minimum distances between any element of two clusters
            
            //Shortest Distance, excluding same cluster. Setting element to its new cluster
            if(tempDist < minDist){
              minDist = tempDist;
              index = j; //cluster position in ArrayList that's closer to the element.
            }
          }
        }
        
        if(minDist < absMinDist){
          absMinDist = minDist;
          absIndexJ = index;
          absIndexI = i;
        }
      }
      
      //Now we know the closest clusters i and j
      //Next step is to reduce custers
      reduce(absIndexI,absIndexJ,clusters);
    }
    
    System.out.println("Loading Display...");

    //Map stuff
    map = new Map2(clusters.get(0).elements,clusters.get(1).elements,minx,maxx,miny,maxy);
    draw();
  }
  
  public static double minDist(ArrayList<Coordinates> a, ArrayList<Coordinates> b){
    double INF=999999;
    double tempDist, minDist,out=INF;
    
    for(int i=0; i < a.size(); i++){ 
     minDist=INF;
     
      for(int j=0; j < b.size(); j++){
        tempDist = Math.sqrt(Math.pow((a.get(i).x-b.get(j).x) ,2) + Math.pow((a.get(i).y-b.get(j).y) ,2));
        
        if(tempDist < minDist)
          minDist = tempDist;
      }
      
      if(minDist < out)
       out = minDist;
    }
    
    return out;
  }
  
  //by convention, j always goes into i
  public static void reduce(int i, int j, ArrayList<Cluster> clusters){
    
    for(int z=0; z < clusters.get(j).elements.size(); z++)
      clusters.get(i).elements.add(clusters.get(j).elements.get(z));
    
    clusters.remove(j);
    // Java takes care of its memory usage with its automatic garbage collection mechanism
  }
  
  public static void draw(){
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(map);
    frame.setSize(800,800);
    frame.setVisible(true);
  }
}