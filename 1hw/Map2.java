import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Map2 extends JPanel
{
  ArrayList<Coordinates> listA, listB;
  double minx,maxx,miny,maxy;
  
  
  public Map2(ArrayList<Coordinates> listA, ArrayList<Coordinates> listB, double minx, double maxx, double miny, double maxy)
  {
    this.listA = listA;
    this.listB = listB;
    this.minx = minx;
    this.miny = miny;
    this.maxx = maxx;
    this.maxy = maxy;
  }
  
  @Override
  public void paintComponent(Graphics gg)
  {
    super.paintComponent(gg);
    this.setBackground(Color.WHITE); //can remove
    Graphics2D g2 = (Graphics2D) gg; //can remove
    gg.setColor(Color.BLACK); //can remove Maybe?
    
    double resize;
    double resizex = (getWidth())/(1.05*(maxx-minx));
    double resizey = (getHeight())/(1.05*(maxy-miny));
    
    if(resizex<resizey)
      resize=resizex;
    else
      resize=resizey;
    
    int i;
    
    for(i=0; i < listA.size(); i++)
    {
      int x1 = (int) ((listA.get(i).x-minx+((maxx-minx)*.025))*resize);
      int y1 = (int) ((listA.get(i).y-miny+((maxy-miny)*.025))*resize);
    
    gg.drawString("+",x1,y1);
    
    }
    
    for(i=0; i < listB.size(); i++)
    {
      int x1 = (int) ((listB.get(i).x-minx+((maxx-minx)*.025))*resize);
      int y1 = (int) ((listB.get(i).y-miny+((maxy-miny)*.025))*resize);
    
    gg.drawString("*",x1,y1);
    
    }
    
  }
}