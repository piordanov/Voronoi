/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoi;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import voronoi.VoronoiGrid.*;
import javafx.scene.shape.*;

import javafx.scene.paint.Color;
import be.humphreys.simplevoronoi.GraphEdge;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Peter Iordanov
 */
public class FXMLDocumentController implements Initializable {
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;
    @FXML
    private TextField xInput, yInput;
    @FXML
    private Label label;
    @FXML
    private AnchorPane canvas;
    
    private VoronoiGrid grid;
    private int numplacedpoints = 0;
    @FXML
    private Button coordinateButton;
    @FXML
    private Button drawButton;
    /*
    @FXML
    private Button button2;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        Stage stage = (Stage) label.getScene().getWindow();
        // Swap screen
        stage.setScene(new Scene(new Pane()));
    }
    
    @FXML
       private void handleButtonAction2(ActionEvent event)
       {
           System.out.println("Ha!");
          label.setText("This is a test.");
       }
       */
    
    /*
    checks to see if a string is a number, works only for latin numbers i.e 0-9
    */
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    //
    public void drawPoint(int x, int y, boolean init)
    {
            System.out.println(x +":" + y + "clicked.");
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
                else if(grid.getCurrPlayer().playerid == 0)//player one is orange
                    c.setFill(Color.ORANGE);
                else //player 2 is blue
                    c.setFill(Color.BLUE);
 
                
                canvas.getChildren().add(c);
                System.out.println("Point added");
                numplacedpoints++;
            }
            else
            {
                System.out.println("No more points to be placed");
                //TODO stop this controller, begin Fortune's algorithm
            }
    }

    @FXML
        private void handleMouseCreatePoint(MouseEvent event)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();
 
            drawPoint(x,y, false);
            label.setText("Point added");
            grid.changePlayer();
            //label.setText("");
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
                    grid.changePlayer();
                    label.setText("Point added");
                }
                else
                {
                    label.setText("Error, input is out of bounds.");
                }
            }
            else
            {
                label.setText("Error, input is not a number.");
            }

        }
        
    @FXML
        private void drawDiagram(ActionEvent event)
        {
            label.setText("Drawing ...");
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
            label.setText("Complete");
        }
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        grid = new VoronoiGrid();
        //we now draw a random number of points, between 10 - 60
        for (Pair p : grid.freePoints) {
            int x = (int) p.getKey();
            int y = (int) p.getValue();
            drawPoint(x,y, true);
        }
    }    
    
}
