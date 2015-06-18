/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoi;

import java.util.ArrayList;
import javafx.util.Pair;



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
         private ArrayList<Pair> points;//stores all points as a Pair see javafx.util.Pair
         //TODO consider a more optimal storage of points on a larger scale, everything is good when life is O(1)!
         
         public Player(int id, int max)
         {
             playerid = id;
             maxPoints = max;
             points = new ArrayList();
         }
         
         //abstraction methods to the container of the points to the FXML
         //TODO change these if the container for Pairs is changed
         public int addPoint(int x, int y)
         {
             if(hasPoint(x,y) || points.size() >= maxPoints)
                 return EXIT_FAILURE;
             points.add(new Pair(x, y));
             maxPoints++;
             
             System.out.println(this.print());
             if(points.size() == maxPoints)
                 complete = true;//indicating we are done
             return EXIT_SUCCESS;
         }
         
         
         public boolean hasPoint(int x, int y)
         {
             return points.contains(new Pair(x,y));
         }
         
         public String print()
         {
             String result = playerid + ", ";
             for (Pair temp : points) {
			result += temp.getKey() + ":" + temp.getValue();
                        result += " ";
		}
             return result;
         }
    }
    
    //default constructor
    public VoronoiGrid()
    {
        players = new Player[2];
        players[0] = new Player(0, 10);
        players[1] = new Player(1, 10);
        currPlayerID = 0;

    }
    
    public VoronoiGrid(int n)
    {
        super();
        //we have maxPoints to be double the maximum for each Player
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
    
    
    
    
}
