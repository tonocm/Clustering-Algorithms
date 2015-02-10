import java.util.*;

class Cluster{
 
  public ArrayList<Coordinates> elements = new ArrayList<Coordinates>();
  public int number;
  
  public Cluster(Coordinates element, int number){
   
   this.number = number;
   elements.add(element);
    
  }
  
}