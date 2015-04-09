import java.io.*;
import java.util.*;
class KNN {
  public static void main (String [] args) throws IOException{
    File tr;
    File tu;
    File te;
    
    /** Loading Training Set
      * First token represents author (1 Hamilton, 2 Madison)
      * Next, 70 floats represent relative frequencies of words used by every author
      **/
    tr = new File("train_86_by_71.txt");
    tu = new File("tune_20_by_71.txt");
    te = new File("test_12_by_70.txt");
    
    ArrayList<Element> train = new ArrayList<Element>();
    ArrayList<Element> tune1 = new ArrayList<Element>();
    ArrayList<Element> tune2 = new ArrayList<Element>();
    ArrayList<Element> test = new ArrayList<Element>();
    
    train = readFile(tr, true, false); //author's id is known.
    tune1 = readFile(tu, true, false); //author's id is known.
    tune2 = readFile(tu, false, true); //author's id not known.
    test = readFile(te, false, false); //author's id is not known.
    
    int k = bestK(train, tune1, tune2); //finding best K.
    
    System.out.println("k = "+k);
    
    guessID(train, test, k);
    print(test, test, true);
  }
  
  public static int bestK(ArrayList<Element> train, ArrayList<Element> tuneKnown, ArrayList<Element> tuneUnknown){
    final int INF = 999999; //big number...
    
    int error, errorTemp=INF;
    
    int k=1; //base case.
    boolean flag = false;
    
    guessID(train, tuneUnknown, k); //guesses id for tuneUnknown (base case, k=1)

    //print(tuneUnknown, tuneKnown);
    
    error = findError(tuneUnknown, tuneKnown);
    
    while(!flag){
      
        guessID(train, tuneUnknown, k+1); //get distance for k=+1;
        errorTemp = findError(tuneUnknown, tuneKnown); //tests for the new errror.
        
        if(errorTemp <= error){
          error = errorTemp; //saves smaller error
          k++; //the new K is better than the old K
          //System.out.println("k= " + k);
          //System.out.println(error);
          //print(tuneUnknown, tuneKnown);
        }
        else
          flag = true; //stops looping. Drawback: can be a local minima, must test other implementations...
    }
    System.out.println("Number of Errors: " + error);
    return k; //returns k
  }
  
  public static int findError(ArrayList<Element> u, ArrayList<Element> k){
    int error=0;
    for(int i=0; i < u.size(); i++){
      if(k.get(i).id != u.get(i).id)
        error++;
    }
    return error;
  }
  
  public static void guessID(ArrayList<Element> train, ArrayList<Element> tune, int k){
    
    ArrayList<Double> distances1 = new ArrayList<Double>();
    ArrayList<Double> distances2 = new ArrayList<Double>();
    int hamilton, madison;
    
    for(int i=0; i < tune.size(); i++){
      
      for(int j=0; j < train.size(); j++){
        if(train.get(j).id == 1)
          distances1.add(distance(tune.get(i).set, train.get(j).set)); //hamilton
        else
          distances2.add(distance(tune.get(i).set, train.get(j).set)); //madison
      }
      
      double d1[] = makeArray(distances1);
      double d2[] = makeArray(distances2);
      
      distances1.clear();
      distances2.clear();
      
      Arrays.sort(d1);
      Arrays.sort(d2);
      
      hamilton=0;
      madison=0;
      for(int z=0; z < k; z++){
        
        if(d1[hamilton] < d2[madison])
          hamilton++;
        else if(d1[hamilton] > d2[madison])
          madison++;
        else{ //they're equal
          if(((int)(Math.random()*10)) < 5)
            hamilton++;
          else
            madison++;
        }
      }
      
      /*Assigning id based on prediction */
      if(hamilton > madison)
        tune.get(i).id = 1;
      else if(madison > hamilton)
        tune.get(i).id = 2;
      else{ //they're equal
          if(((int)(Math.random()*10)) < 5)
            tune.get(i).id = 1;
          else
            tune.get(i).id = 2;
        }
    }
  }
  
  public static double[] makeArray(ArrayList<Double> a){
   
    double[] out = new double[a.size()];
    
    for(int i=0; i < a.size(); i++)
      out[i] = a.get(i);
    
    return out;
  }
  
  public static double distance(ArrayList<Double> x, ArrayList<Double> y){ //x = tune, y = train
    double sum=0;

    for(int i=0; i < x.size(); i++)
      sum = sum + Math.pow(x.get(i)-y.get(i) ,2);
    
    return Math.sqrt(sum);
  }
  
  public static ArrayList<Element> readFile(File name, boolean id,boolean hack) throws FileNotFoundException{
    ArrayList<Element> out = new ArrayList<Element>();
    Scanner scan = new Scanner(name).useDelimiter("\n");
    StringTokenizer token;
    double min=9999999.0, max=0.0, store=0.0;
    
    Element temp;
    
    while(scan.hasNext()){
      min=9999999.0;
      max=0.0;
      
      token = new StringTokenizer(scan.next());
      
      if(id == true)
        temp = new Element((int)Double.parseDouble(token.nextToken())); //guaranteed that the first element is the author's id
      else{
        if(hack == true)
          token.nextToken(); //tossing out the ID
        
        temp = new Element(-1); //guaranteed that the first element is the author's id
      }
      
      while(token.hasMoreTokens()){
        store = Double.parseDouble(token.nextToken());
        
        if(store < min)
          min = store;
        if(store > max)
          max = store;
        
        temp.set.add(store);
      }
      
      //normalize(temp, min, max);
      
      out.add(temp);
    }
    normalize(out);
    return out;
  }
  
  /*public static void normalize(Element out, double min, double max){
  
    //switch scaling method. Gotta use columns instead of rows
    
    for(int i=0; i < out.set.size(); i++)
      out.set.set(i, out.set.get(i)/max);
    
  }*/
  
  public static void normalize(Element out){
  
    int i=0, j=0;
    
    for(j=0; j < out.get(j).set.size(); j++){
     
      for(i=0; i < out.get(j).set.size(); i++){
        
        
      }
      
    }
    
  }
  
  public static void print(ArrayList<Element> u, ArrayList<Element> k, boolean hack){
    for(int i=0; i < k.size(); i++){
      if(hack)
        System.out.println("Guess: " + u.get(i).id);
      else
        System.out.println("U: " + u.get(i).id + " K: " + k.get(i).id);
    }
  }
}