/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoi;


import java.util.ArrayList;
import javafx.util.Pair;
import be.humphreys.simplevoronoi.Voronoi;
import be.humphreys.simplevoronoi.GraphEdge;
import java.util.List;
import java.util.Random;



/**
 *
 * @author Peter Iordanov
 */
public class VoronoiGrid {
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;
    protected int currPoints = 0;
    private Player [] players;
    private int currPlayerID;
    public ArrayList<Pair> freePoints;//points to be randomly generated
    /*
    Voronoi here has a limited number of points located on a 2D plane, each represented by a Pair
    */
    
    /*
    Voronoi has two players
    @TODO: consider moving this class to a seperate file
    */
    protected class Player
    {
         int playerid;//0 for first player, 1 for second
         int maxPoints;
         boolean complete = false;
         private final ArrayList<Pair> points;//stores all points as a Pair see javafx.util.Pair
         //TODO consider a more optimal storage of points on a larger scale, everything is good when life is O(1)!
         
         public Player(int id, int max)
         {
             playerid = id;
             maxPoints = max;
             points = new ArrayList();
         }
         
         //abstraction methods to the container of the points to the FXML
         //TODO change these if the container for Pairs is changed
         //init only doesn't add this as a point for a player
         public int addPoint(double x, double y)
         {
             if(hasPoint(x,y))
             {
                 System.out.println("point taken.");
                 return EXIT_FAILURE;
             }
             
             if(points.size() >= maxPoints)
             {
                 System.out.println("reached max");
                 return EXIT_FAILURE;
             }
             points.add(new Pair(x, y));
             
             System.out.println(this.print());
             if(points.size() >= maxPoints)
                 complete = true;//indicating we are done
             return EXIT_SUCCESS;
         }
         
         
         public boolean hasPoint(double x, double y)
         {
             return points.contains(new Pair(x,y));
         }
         
        /**
         *returns a string containing information on the player itself 
         * and on every point this player has, used for debugging
         * @return the string containing information  
         */
        public String print()
         {
             String result = playerid + ", ";
             for (Pair temp : points) {
			result += temp.getKey() + ":" + temp.getValue();
                        result += " ";
		}
             return result;
         }
         
        /**
         * sets the maximum number of points a player can have. 
         * Both players MUST have the same number of points and 
         * this is used only in the constructor
         * @param n the new number of maxPoints
         */
        private void setMaxPoints(int n)
         {
             this.maxPoints = n;
         }
    }
    
    //default constructor
    public VoronoiGrid()
    {
        players = new Player[2];
        players[0] = new Player(0, 10);
        players[1] = new Player(1, 10);
        currPlayerID = 0;
        freePoints = new ArrayList();
        Random numgen = new Random();
        int n = numgen.nextInt(50)  + 10;
        for(int i = 0; i < n; i++)
        {
            int x = numgen.nextInt( 700 );// HOTFIX going to assume canvas size is fixed for now
            int y = numgen.nextInt( 430 );// HOTFIX going to assume canvas size is fixed for now
            freePoints.add(new Pair(x, y));
        }

    }
    
    public VoronoiGrid(int n)
    {
        super();
        players[0].setMaxPoints(n);
        players[1].setMaxPoints(n);
        
    }
    
    public void changePlayer()
    {
        currPlayerID = 1 - currPlayerID;//only two players so we can just alternate
        System.out.println("Now player:" + currPlayerID);
    }
    
    public Player getCurrPlayer()
    {
        return players[currPlayerID];
    }
    
    public List<GraphEdge> createDiagram()
    {
        /*
        sites to see
        http://blog.ivank.net/fortunes-algorithm-and-implementation.html#impl_cpp
        site with java implementation installation
        http://sourceforge.net/projects/simplevoronoi/?source=typ_redirect
        */
        
        Voronoi v = new Voronoi(0);
        
        //put all x values and y values in seperate arrays
        int size = players[0].points.size() + players[1].points.size();
        double [] xValues = new double[size];
        double [] yValues = new double[size];
        int curr = 0;
        //assuming both players have the same number of points, which by definition should be true
        for(int i = 0; i < players[0].points.size(); i++)
        {
                Pair p0 = players[0].points.get(i);
                Pair p1 = players[1].points.get(i);
                xValues[curr] = (double) p0.getKey();
                xValues[curr + 1] = (double) p1.getKey();
                yValues[curr] = (double) p0.getValue();
                yValues[curr + 1] = (double) p1.getValue();
                curr += 2;
                     
        }
        /*
        public List<GraphEdge> generateVoronoi(double[] xValuesIn, double[] yValuesIn,
            double minX, double maxX, double minY, double maxY)
        */
        List<GraphEdge> list = v.generateVoronoi(xValues, yValues, 0, 700, 0, 430 );
        return list;
    }
    
    
}
