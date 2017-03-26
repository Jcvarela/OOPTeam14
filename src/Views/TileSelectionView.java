/**-------------------------------------------------------------------------------------
|	TileSelectionView Class: Created by Alejandro Chavez on 3/26/2017.
|---------------------------------------------------------------------------------------
|   Description: 
|
---------------------------------------------------------------------------------------*/
package Views;

import Model.Utility.PixelMap;

import javax.swing.*;
import java.awt.*;

public class TileSelectionView extends JPanel{

    TerrainSelectionView terrainSelectionView;
    RiverSelectionView riverSelectionView;
    CurrentSelectionView currentSelectionView;

    TileSelectionView(){
        setLayout(new BorderLayout());
        setBounds(50, 50, PixelMap.SCREEN_WIDTH / 5, PixelMap.SCREEN_HEIGHT - 100);
        System.out.println("Width: "+getWidth()+" -- Height: "+getHeight());

        terrainSelectionView = new TerrainSelectionView(new Dimension(getWidth() / 2, getHeight() - getWidth()));
        riverSelectionView = new RiverSelectionView(new Dimension(getWidth() / 2, getHeight() - getWidth()));
        currentSelectionView = new CurrentSelectionView(new Dimension(getWidth(), getWidth()));

        add(terrainSelectionView, BorderLayout.WEST);
        add(riverSelectionView, BorderLayout.EAST);
        add(currentSelectionView, BorderLayout.SOUTH);

        setVisible(true);
    }

//    @Override
//    public void paint(Graphics g){
//        super.paintComponent(g);
//        ((Graphics2D)g).setStroke(new BasicStroke(3));
//        g.drawRect(3,3,getWidth()-6, getHeight()-6);
//    }
}
