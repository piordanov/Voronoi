/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoi;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;

import javafx.scene.paint.Color;
import be.humphreys.simplevoronoi.GraphEdge;
import java.util.List;
import javafx.scene.control.ColorPicker;
import javafx.util.Pair;
import static voronoi.VoronoiGrid.EXIT_FAILURE;
import static voronoi.VoronoiGrid.EXIT_SUCCESS;

/**
 *
 * @author Peter Iordanov
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField xInput, yInput, ptsOnFieldLabel, ptsPerPersonLabel;
    @FXML
    private Label responseLabel, player1score, player2score;
    @FXML
    private AnchorPane canvas;
    @FXML
    private Circle selector;
    
    private VoronoiGrid grid;
    private int numplacedpoints = 0;
    @FXML
    private Button coordinateButton, drawButton, newGameButton;
    
    private Color playerColor1 = Color.ORANGE, playerColor2 = Color.BLUE;
    @FXML
    private ColorPicker playerColorPicker1, playerColorPicker2;

    
    /*
    checks to see if a string is a number, works only for latin numbers i.e 0-9
    */
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    public void changePlayer()
    {
        grid.changePlayer();
        if(grid.getCurrPlayer().getPlayerId() == 0)//player one is orange
                    selector.setFill(playerColor1);
        else
            selector.setFill(playerColor2);
    }
    //
    public void drawPoint(double x, double y, boolean init)
    {
            //System.out.println(x +":" + y + "clicked.");
            Player p = grid.getCurrPlayer();
            int result = EXIT_SUCCESS;
            if(!init)
                result = p.addPoint(x,y);
            if(result != EXIT_FAILURE)
            {
                Circle c = new Circle();
                //draw the circle
                c.setCenterX(x);
                c.setCenterY(y);
                c.setRadius(3);
                c.setOpacity(1);
                if(init) //for drawing random init points
                {
                    c.setFill(Color.BLACK);
                    c.setRadius(2);
                }
                else if(grid.getCurrPlayer().getPlayerId() == 0)//player one is orange
                    c.setFill(playerColor1);
                else //player 2 is blue
                    c.setFill(playerColor2);
 
                
                canvas.getChildren().add(c);
                //System.out.println("Point added");
                numplacedpoints++;
            }
    }
    //on click
    @FXML
        private void handleMouseCreatePoint(MouseEvent event)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();
 
            drawPoint(x,y, false);
            //responseLabel.setText("Point added");
            changePlayer();
            //label.setText("");
        }
    //on Move 
    @FXML
        private void handleMouseHover(MouseEvent event)
        {
            selector.setCenterX(event.getSceneX());
            selector.setCenterY(event.getSceneY());
            xInput.setText(Double.toString(event.getSceneX()));
            yInput.setText(Double.toString(event.getSceneY()));
            if(grid.getCurrPlayer().isDone())
                selector.setVisible(false);
            
        }
        
    @FXML
        private void addInputPoint(ActionEvent event)
        {
            String xString = xInput.getText();
            String yString = yInput.getText();
            //check if inputs are valid, in terms of latin numbers i.e 0-9
            if(FXMLDocumentController.isNumeric(xString) && 
                    FXMLDocumentController.isNumeric(yString))
            {
                int x = Integer.parseInt(xString);
                int y = Integer.parseInt(yString);
                if(x <= canvas.getWidth() && y <= canvas.getHeight() && x >= 0 && y >= 0)
                {
                    drawPoint(x,y, false);
                    changePlayer();
                    
                    responseLabel.setText("Point added");
                }
                else
                {
                    responseLabel.setText("Error, input is out of bounds.");
                }
            }
            else
            {
                responseLabel.setText("Error, input is not a number.");
            }

        }
        
    @FXML
        private void drawDiagram(ActionEvent event)
        {
            //need to make sure points are drawn
            if(!grid.players[0].isDone() || !grid.players[1].isDone())
            {
                responseLabel.setText("Not all points have been placed.");
                return;
            }
            responseLabel.setText("Drawing ...");
            List<GraphEdge> list = grid.createDiagram();
            //draw the graph
            for(GraphEdge edge: list)
            {
                Line line = new Line();
                line.setStartX(edge.x1);
                line.setStartY(edge.y1);
                line.setEndX(edge.x2);
                line.setEndY(edge.y2);
                canvas.getChildren().add(line);
            }
            responseLabel.setText("Complete!");
            double[] results = grid.calculatePoints();
            player1score.setText(Double.toString(results[0]));
            player2score.setText(Double.toString(results[1]));
        }
        
        @FXML
        private void startNewGame(ActionEvent event)
        {
            canvas.getChildren().clear();
            canvas.getChildren().add(selector);
            selector.setVisible(true);
            selector.setFill(playerColorPicker1.getValue());
            String fieldPoints = ptsOnFieldLabel.getText();
            String playerPoints = ptsPerPersonLabel.getText();
            playerColor1 = playerColorPicker1.getValue();
            playerColor2 = playerColorPicker2.getValue();
            if(fieldPoints.toLowerCase().equals("random") && isNumeric(playerPoints))
            {
                grid = new VoronoiGrid( (int) Integer.parseInt(playerPoints));                 
            }
            else if(isNumeric(fieldPoints) || isNumeric(playerPoints))
            {
                int pPoints = Integer.parseInt(playerPoints);
                int fPoints = Integer.parseInt(fieldPoints);
                grid = new VoronoiGrid( (int) pPoints, (int) fPoints);
            }
            else
            {
                    responseLabel.setText("Please insert a number of points "
                            + "for each person and for the field, otherwise put \"random\"");
                    return;//exit since we have no points to draw
            }
            drawFreePoints();
        }
        
    private void drawFreePoints()
    {
        for (Pair p : grid.freePoints) {
            double x = (double) p.getKey();
            double y = (double) p.getValue();
            drawPoint(x,y, true);
        }
    }
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        grid = new VoronoiGrid();
        //we now draw the random number of points made by VoronoiGrid
        drawFreePoints();
        playerColorPicker1.setValue(Color.ORANGE);
        playerColorPicker2.setValue(Color.BLUE);
        
        
    }    
    
}
