/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoi;

import java.util.ArrayList;
import javafx.util.Pair;
import static voronoi.VoronoiGrid.EXIT_FAILURE;
import static voronoi.VoronoiGrid.EXIT_SUCCESS;

/**
 *
 * @author Peter Iordanov
 */
    public class Player
    {
         private int playerid;//0 for first player, 1 for second
         private int maxPoints;
         private boolean complete = false;
         private final ArrayList<Pair> points;//stores all points as a Pair see javafx.util.Pair
         //TODO consider a more optimal storage of points on a larger scale, everything is good when life is O(1)!
         
         public Player(int id, int max)
         {
             playerid = id;
             maxPoints = max;
             points = new ArrayList();
         }
         
         //abstraction methods to the container of the points to the FXML
         //change these if the container for Pairs is changed
         //init only doesn't add this as a point for a player
         public int addPoint(double x, double y)
         {
             if(hasPoint(x,y))
             {
                 //System.out.println("point taken.");
                 return EXIT_FAILURE;
             }
             
             if(points.size() >= maxPoints)
             {
                 //System.out.println("reached max");
                 return EXIT_FAILURE;
             }
             points.add(new Pair(x, y));
             
             //System.out.println(this.print());
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
 this is used only in the constructor
         * @param n the new number of maxPoints
         */
        public void setMaxPoints(int n)
         {
             this.maxPoints = n;
         }

        public int getPlayerId() {
            return playerid;
        }
        
        public ArrayList<Pair> getPoints()
        {
            return points;
        }
        
        public boolean isDone()
        {
            return complete;
        }
    }
