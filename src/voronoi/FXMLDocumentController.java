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
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import voronoi.VoronoiGrid.*;
import javafx.scene.shape.*;

import javafx.scene.paint.Color;

/**
 *
 * @author Peter Iordanov
 */
public class FXMLDocumentController implements Initializable {
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;
    @FXML
    private Label label;
    @FXML
    private AnchorPane canvas;
    private VoronoiGrid grid;
    private int numplacedpoints = 0;
    @FXML
    private Button button;
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
       
    @FXML
        private void handleMouseCreatePoint(MouseEvent event)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();
            System.out.println(x +":" + y + "clicked.");
            Player p = grid.getCurrPlayer();
            int result = p.addPoint(x,y);
            if(result != EXIT_FAILURE)
            {               
                Circle c = new Circle();
                //draw the circle
                c.setCenterX(x);
                c.setCenterY(y);
                c.setRadius(3);
                c.setOpacity(1);
                if(grid.getCurrPlayer().playerid == 0)//player one is orange
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
            
            grid.changePlayer();
        }
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        grid = new VoronoiGrid();
        
    }    
    
}
