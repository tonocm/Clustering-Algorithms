/**
 * Hector Cardenas
 * 28288961
 * November 14, 2013
 * TAs: Mohan and Feroz
 * Lab: Max Flow
 **/
public class Graph
{ 
  public int vCnt, eCnt; // number of verticies and edges
  public boolean dirgraph, w = true; // false for undirected graphs 
  public Adjacent adj[][]; 
  public Vertex[] verticies;
  final int INFINITY = 1000000;
  public Graph()
  {
  }
  public Graph(int numVerticies, boolean isDirgraph)
  {
    dirgraph = isDirgraph;
    vCnt = numVerticies;
    eCnt = 0;
    adj = new Adjacent[vCnt][vCnt];
    verticies = new Vertex[vCnt];
  }
  public boolean directed()
  {
    return dirgraph;
  }
  public int getVerticies() // return number of verticies
  {
    return vCnt;
  }
  public int getEdges() // return number of edges
  {
    return eCnt;
  }
  public void insert(Edge e, int c)
  { 
    if(dirgraph == false)
    {
      if(adj[e.v][e.w].exists == false) //checking if the edge exists
      {
        adj[e.v][e.w].exists = true;
        adj[e.w][e.v].exists = true;
        adj[e.w][e.v].cost = c;
        adj[e.w][e.v].res = c;
        adj[e.v][e.w].cost = c;
        adj[e.v][e.w].res = c;
        verticies[e.v] = new Vertex(e.v);
        verticies[e.w] = new Vertex(e.w);
        ++eCnt;
      }
      else
        System.out.println("Edge " + e.v + " " + e.w + " already Exists");
    }
    else
    {
      if(adj[e.v][e.w].exists == false) //checking if edge exist
      {
        adj[e.v][e.w].exists = true;
        adj[e.v][e.w].cost = c;
        adj[e.v][e.w].res = c;
        verticies[e.v] = new Vertex(e.v);
        verticies[e.w] = new Vertex(e.w);
        ++eCnt;
      }
      else
        System.out.println("Edge " + e.v + " " + e.w + " already Exists");
    }
  }
  public void delete(Edge e)
  {
    if(dirgraph == false)
    {
      if(adj[e.v][e.w].exists == true) //checking if the edge exist
      {
        adj[e.v][e.w].exists = false;
        adj[e.w][e.v].exists = false;
        verticies[e.v] = null;
        verticies[e.w] = null;
        --eCnt;
      }
      else
        System.out.println("Edge " + e.v + " " + e.w + " does not Exists");
    }
    else
    {
      if(adj[e.v][e.w].exists == true)
      {
        adj[e.v][e.w].exists = false;
        verticies[e.v] = null;
        verticies[e.w] = null;
        --eCnt;
      }
      System.out.println("Edge " + e.v + " " + e.w + " does not Exists");
    }
  }
  public boolean edge(int node1, int node2) //are they connected?
  {
    if(adj[node1][node2].exists == true)
      return true;
    else
      return false;
  }
  public AdjList getAdjList(int vertex)
  {
    return new AdjArray(vertex);
  }
  public void setMatrix()
  {
    adj = new Adjacent[vCnt][vCnt];
    
    for(int i=0; i < vCnt; i++)
    {
      for(int j=0; j < vCnt; j++)
        adj[i][j] = new Adjacent();
    }
    
  }
  public void setVerticies()
  {
    verticies = new Vertex[vCnt];
  }
  public void shortestPath(int x)
  {
    if(w == false)
      unweighted(verticies[x]);
    else
      dijkstra(verticies[x]);
  }
  public int getFlow(int x, int y)
  {
    int out;
    out = maxFlow(verticies[x], verticies[y]);
    return out;
  }
  public void unweighted(Vertex s)  
  {
    int k=0, i=0;
    boolean flag = false;
    
    for(i=0; i < vCnt; i++) 
    {
      if(verticies[i] != null)
      {
        verticies[i].distance = INFINITY; 
        verticies[i].known = false; 
      }
    } 
    s.distance = 0;
    for (int currdist = 0 ; currdist < vCnt; currdist++)  
    { 
      for(i = 0; i < vCnt; i++)
      {
        if(verticies[i] != null)
        {
          if (!verticies[i].known && verticies[i].distance == currdist)  
          { 
            verticies[i].known = true;
            AdjList a = getAdjList(i);
            for(int t = a.beg(); !a.end(); t = a.next())
            {
              k=0;
              while(flag == false)
              {
                if(verticies[k] != null)
                {
                  if(verticies[k].data == t)
                    flag = true;
                  else
                    k++;
                }
                else
                  k++;
              }
              flag = false; //resetting!
              if(verticies[k].distance == INFINITY) 
              {
                verticies[k].distance = currdist + 1; 
                verticies[k].path = verticies[i];
              }
            }
          }
        }
      }
    } 
  }
  public int maxFlow(Vertex s, Vertex e)
  {
    int min = INFINITY;
    Vertex v = e;
    int maxFlow = 0;
    
    while(e.distance != INFINITY)
    {
      dijkstra(s);
      
      //Finding Min Flow in Dijkstra's Augmentation Path
      while(v.path != null)
      {
        //System.out.println("V: " + v.data + '\n' + "V Path: " + v.path.data + '\n' + "Residual: " + adj[v.data][v.path.data].res);
        if(min > adj[v.path.data][v.data].res)
          min = adj[v.path.data][v.data].res;
        v = v.path;
      }
      
      v = e; //resetting End Vertex
      
      //Subtracting the Residual Graph's paths to the min found above
      while(v.path != null)
      {
        adj[v.path.data][v.data].res = adj[v.path.data][v.data].res - min;
        adj[v.path.data][v.data].flow = adj[v.path.data][v.data].flow + min;
        
        //Removing the possibility of testing the same path again when the residual is 0, aka, the flow is max in the edge
        if(adj[v.path.data][v.data].res == 0)
        {
          adj[v.path.data][v.data].exists = false;
          adj[v.path.data][v.data].cost = INFINITY;
        }
        
        v = v.path;
      }
      maxFlow = maxFlow + min;
      
      v = e; //resetting End Vertex
    }
    return maxFlow;
  }
  public void dijkstra(Vertex s)  
  {
    boolean flag = false;
    int i, small = INFINITY, k=0, smallT = 0;
    for(i=0; i < vCnt; i++)
    {
      if(verticies[i] != null)
      {
        verticies[i].distance = INFINITY; 
        verticies[i].known = false; 
      }
    }
    s.distance = 0;
    for ( ; ; )  
    {
      small = INFINITY; //setting small value to infinity at the beginning of the loop
      smallT = INFINITY;
      for(i=0; i< vCnt; i++)
      {
        if(verticies[i] != null)
        {
          if(verticies[i].known == false)
          {
            if(smallT > verticies[i].distance)
            {
              small = i;
              smallT = verticies[i].distance;
            }
          }
        }
      }
      
      if(small == INFINITY)
        break; //all verticies have been analyzed
      
      verticies[small].known = true;
      
      AdjList a = getAdjList(small);
      
      k = 0; //resetting!
      for(int t = a.beg(); !a.end(); t = a.next())
      {
        while(flag == false)
        {
          if(verticies[k] != null)
          {
            if(verticies[k].data == t)
              flag = true;
            else
              k++;
          }
          else
            k++;
        }
        flag = false; //resetting!
        
        if(!verticies[k].known)
        {
          if(verticies[small].distance + adj[small][k].cost < verticies[k].distance)
          {
            verticies[k].distance = verticies[small].distance + adj[small][k].cost;
            verticies[k].path = verticies[small]; 
          }
        }
      }
    } 
  }
  /**public void dijkstra(Vertex s)  
  {
    boolean flag = false;
    int i, small = INFINITY, k=0;
    for(i=0; i < vCnt; i++)
    {
      if(verticies[i] != null)
      {
        verticies[i].distance = INFINITY; 
        verticies[i].known = false; 
      }
    }
    s.distance = 0;
    for ( ; ; )  
    {
      small = INFINITY; //setting small value to infinity at the beginning of the loop
      for(i=0; i< vCnt; i++)
      {
        if(verticies[i] != null)
        {
          if(verticies[i].known == false)
          {
            if(small > verticies[i].distance)
              small = i;
          }
        }
      }
      
      if(small == INFINITY)
        break; //all verticies have been analyzed
      
      verticies[small].known = true;
      
      AdjList a = getAdjList(small);
      
      k = 0; //resetting!
      for(int t = a.beg(); !a.end(); t = a.next())
      {
        while(flag == false)
        {
          if(verticies[k] != null)
          {
            if(verticies[k].data == t)
              flag = true;
            else
              k++;
          }
          else
            k++;
        }
        flag = false; //resetting!
        
        if(!verticies[k].known)
        {
          if(verticies[small].distance + adj[small][k].cost < verticies[k].distance)
          {
            verticies[k].distance = verticies[small].distance + adj[small][k].cost;
            verticies[k].path = verticies[small]; 
          }
        }
      }
    } 
  }**/ 
  private class AdjArray implements AdjList
  { 
    private int v; // what vertex we are interested in 
    private int i; // so we can keep track of where
    
    public AdjArray(int v)
    { 
      this.v = v;
      i = -1;
    }
    public int next()
    {
      for (i++; i < adj.length; i++)
      { 
        if(edge(v,i) == true)
          return i;
      }
      return -1;
    } 
    public int beg()
    {
      i= -1;
      return next();
    } 
    public boolean end()
    {
      if(i < vCnt)
        return false;
      else
        return true;
    }
  }
}