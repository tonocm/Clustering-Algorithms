import java.io.*;
import java.util.*;

class SVM{
  
  public static void main(String [] args) throws IOException{ 
    File tr;
    File tu;
    File te;
    
    tr = new File("train_86_by_71.txt");
    tu = new File("tune_20_by_71.txt");
    te = new File("test_12_by_70.txt"); 
    
    readThis(tr, "train.txt", true);
    readThis(tu, "tune.txt", true);
    readThis(te, "test.txt", false);
    System.out.println("done");
  }
  
  public static void readThis(File in, String name, boolean flag) throws IOException{
    
    BufferedWriter out = new BufferedWriter(new FileWriter(new File(name), false));
    Scanner scan = new Scanner(in).useDelimiter("\n");
    StringTokenizer token;
    int i=1;
    while(scan.hasNext()){
      token = new StringTokenizer(scan.next());
      
      
      if(flag)
        out.write(token.nextToken() + " "); //madison or hamilton
      else
        out.write("0" + " ");
      
      i=1;
      while(token.hasMoreTokens()){
        
        out.write(i+":"+token.nextToken()+" ");
        i++;
      }
      out.write("\n");
    }
    out.close(); 
  }
}