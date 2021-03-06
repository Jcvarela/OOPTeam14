/**-------------------------------------------------------------------------------------
|	SetupOne Class: Created by Alejandro Chavez on 3/26/2017.
|---------------------------------------------------------------------------------------
|   Description: Sets a basic JPanel and add a listener to test camera movement, OutlineDrawer,
|   and some of the Controller functionality.
 ---------------------------------------------------------------------------------------*/

package TestSetup;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;

import MapBuilder.Controllers.KeyboardListeners.EditorKeyboardListener;
import MapBuilder.Controllers.MouseListeners.MapSubsectionMouseListener;
import MapBuilder.Model.ModelFacade;
import MapBuilder.Model.Utility.HexLocation;
import MapBuilder.Views.Drawers.TileOutlineDrawer;
import MapBuilder.Views.Utility.PixelMap;
import MapBuilder.Views.Utility.PixelPoint;


public class SetupOne {

    static HexLocation[][] simulatedMap;

    static class TestPanel extends JPanel{

        TestPanel(){
            setBackground(new Color(255,255,255));
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            for(int i=0; i<simulatedMap.length; i++){
                for(int j=0; j<simulatedMap[0].length; j++){
                    PixelPoint tileCenter = PixelMap.getTileCenter(simulatedMap[i][j]);
                    TileOutlineDrawer.drawInMap(g, tileCenter);
                    PixelPoint origin = PixelMap.getMapTileOrigin(simulatedMap[i][j]);
//                    g.drawRect(origin.getX(), origin.getY(), PixelMap.TILE_FULL_WIDTH, PixelMap.TILE_HEIGHT);
                }
            }
        }
    }

    @Test
    public static void main(String[] args) throws InterruptedException {
        ModelFacade modelFacade= ModelFacade.getInstance();
        //Initialize the map simulation
        simulatedMap = new HexLocation[21][21];
        for(int i=0; i<simulatedMap.length; i++){
            for(int j=0; j<simulatedMap[0].length; j++){
                simulatedMap[i][j] = new HexLocation(i,j);
            }
        }

        MapSubsectionMouseListener mouseListener = new MapSubsectionMouseListener(modelFacade);
        EditorKeyboardListener keyboardListener= new EditorKeyboardListener(modelFacade);

        JFrame frame = new JFrame("Drawer/Controller Test");

        frame.setSize(new Dimension(PixelMap.SCREEN_WIDTH, PixelMap.SCREEN_HEIGHT));
        frame.addMouseMotionListener(mouseListener);
        frame.addMouseListener(mouseListener);
        frame.addKeyListener(keyboardListener);

        JPanel test = new TestPanel();

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(test);

        while(true){
            test.repaint();
            Thread.sleep(10);
        }
    }
}