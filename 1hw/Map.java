import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Map extends JPanel
{
  ArrayList<Coordinates> list;
  double minx,maxx,miny,maxy;
  
  
  public Map(ArrayList<Coordinates> list, double minx, double maxx, double miny, double maxy)
  {
    this.list = list;
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
    
    for(int i=0; i < list.size(); i++)
    {
      int x1 = (int) ((list.get(i).x-minx+((maxx-minx)*.025))*resize);
      int y1 = (int) ((list.get(i).y-miny+((maxy-miny)*.025))*resize);
    
    gg.drawString(""+list.get(i).c,x1,y1);
    
    }
    
  }
}