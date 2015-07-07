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
    public Player [] players;
    private int currPlayerID;
    public ArrayList<Pair> freePoints;//points to be randomly generated
    /*
    Voronoi here has a limited number of points located on a 2D plane, each represented by a Pair
    */
 
    //default constructor
    public VoronoiGrid()
    {
        players = new Player[2];
        players[0] = new Player(0, 10);
        players[1] = new Player(1, 10);
        currPlayerID = 0;
        freePoints = new ArrayList();
        Random numgen = new Random();
        int n = numgen.nextInt(100)  + 10;
        for(int i = 0; i < n; i++)
        {
            double x = numgen.nextInt( 700 );// HOTFIX going to assume canvas size is fixed for now
            double y = numgen.nextInt( 430 );// HOTFIX going to assume canvas size is fixed for now
            freePoints.add(new Pair(x, y));
        }

    }
    
    public VoronoiGrid(int playerPoints)
    {
        players = new Player[2];
        players[0] = new Player(0, playerPoints);
        players[1] = new Player(1, playerPoints);
        currPlayerID = 0;
        freePoints = new ArrayList();
        Random numgen = new Random();
        int n = numgen.nextInt(100)  + 10;
        for(int i = 0; i < n; i++)
        {
            double x = numgen.nextInt( 700 );// HOTFIX going to assume canvas size is fixed for now
            double y = numgen.nextInt( 430 );// HOTFIX going to assume canvas size is fixed for now
            freePoints.add(new Pair(x, y));
        }
        
    }
    
    public VoronoiGrid(int playerPoints, int freePoints)
    {
        players = new Player[2];
        players[0] = new Player(0, playerPoints);
        players[1] = new Player(1, playerPoints);
        currPlayerID = 0;
        this.freePoints = new ArrayList();
        Random numgen = new Random();
        int n = freePoints;
        for(int i = 0; i < n; i++)
        {
            double x = numgen.nextInt( 700 );// HOTFIX going to assume canvas size is fixed for now
            double y = numgen.nextInt( 430 );// HOTFIX going to assume canvas size is fixed for now
            this.freePoints.add(new Pair(x, y));
        }
    }
    
    public void changePlayer()
    {
        currPlayerID = 1 - currPlayerID;//only two players so we can just alternate
        //System.out.println("Now player:" + currPlayerID);
    }
    
    public Player getCurrPlayer()
    {
        return players[currPlayerID];
    }
    
    public List<GraphEdge> createDiagram()
    {
        /*
        site with voronoi game java implementation installation
        http://sourceforge.net/projects/simplevoronoi/?source=typ_redirect
        */
        //put all x values and y values in seperate arrays
        ArrayList<Pair> p0Points = players[0].getPoints();
        ArrayList<Pair> p1Points = players[1].getPoints();
        int size = p0Points.size() + p1Points.size();
        double [] xValues = new double[size];
        double [] yValues = new double[size];
        int curr = 0;
        //assuming both players have the same number of points, which by definition should be true
        for(int i = 0; i < p0Points.size(); i++)
        {
                Pair p0 = p0Points.get(i);
                Pair p1 = p1Points.get(i);
                xValues[curr] = (double) p0.getKey();
                xValues[curr + 1] = (double) p1.getKey();
                yValues[curr] = (double) p0.getValue();
                yValues[curr + 1] = (double) p1.getValue();
                curr += 2;
                     
        }
        /*
        public List<GraphEdge> generateVoronoi(double[] xValuesIn, double[] yValuesIn,
            double minX, double maxX, double minY, double maxY)
        assumes the size of the space
        */
        Voronoi v = new Voronoi(0);
        List<GraphEdge> list = v.generateVoronoi(xValues, yValues, 0, 700, 0, 430 );
        return list;
    }
    
    /*
    * calculate the number of points each Player recieves of freePoints
    * TODO find the nearest set of points and in cases of two or three nearest neighbors distribute
    * points evenly (for example1/2 1/2 or 1/3 2/3)
    *@return double[] result, an array of 2 values, the number of points awarded to player 1 and 2 respectively
    */
    public double[] calculatePoints()
    {
        double[] result = new double[2];
        ArrayList<Pair> pointsp0 = players[0].getPoints();
        ArrayList<Pair> pointsp1 = players[1].getPoints();
        for(Pair fpoint : freePoints)
        {
            double bestdistancep0 = Double.MAX_VALUE;//at most we will have three points equidistant
            double bestdistancep1 = Double.MAX_VALUE;//stores the actual distance
            ArrayList<Pair> bestpointsp0 = new ArrayList<>();//stores the actual points
            ArrayList<Pair> bestpointsp1 = new ArrayList<>();
            for(Pair p0point : pointsp0)
            {
                double xd = (double) fpoint.getKey() - (double) p0point.getKey();//error here
                double yd = (double) fpoint.getValue() - (double) p0point.getValue();
                double dist = (xd * xd) + (yd * yd);
                if(bestpointsp0.isEmpty() || dist < bestdistancep0)
                {
                    bestpointsp0.clear();
                    bestpointsp0.add(p0point);
                    bestdistancep0 = dist;
                }
                else if(dist == bestdistancep0)// a little leeway
                {
                    bestpointsp0.add(p0point);
                }
            }                
            for(Pair p1point : pointsp1)
            {
                double xd = (Double) fpoint.getKey() - (Double) p1point.getKey();
                double yd = (Double) fpoint.getValue() - (Double) p1point.getValue();
                double dist = (xd * xd) + (yd * yd);                
                if(bestpointsp1.isEmpty() || dist < bestdistancep1)
                {
                    bestpointsp1.clear();
                    bestpointsp1.add(p1point);
                    bestdistancep1 = dist;
                }
                else if(Math.abs(dist - bestdistancep1) < 2)
                {
                    bestpointsp1.add(p1point);
                }
            }            
            if(Math.abs(bestdistancep0 - bestdistancep1) < 2)
            {
                double total = (double) (bestpointsp0.size() + bestpointsp1.size());
                result[0] += bestpointsp0.size() / total;
                result[1] += bestpointsp1.size() / total;
            }
            else if(bestdistancep0 < bestdistancep1)
            {
                result[0] += 1;
            }
            else
            {
                result[1] += 1;
            }
            
        }
        
        
        return result;
    }
}
