/**-------------------------------------------------------------------------------------
|	TileSelectionView Class: Created by Alejandro Chavez on 3/26/2017.
|---------------------------------------------------------------------------------------
|   Description: 
|
---------------------------------------------------------------------------------------*/
package MapBuilder.Views.MapEditor.TileSelection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import MapBuilder.Controllers.MouseListeners.TileSelectionMouseListener;
import MapBuilder.Views.MapEditor.MapEditorView;
import MapBuilder.Views.MapEditor.MapView.MapSubsectionView;
import MapBuilder.Views.Utility.ImageLoader;
import MapBuilder.Views.Utility.PixelMap;

public class TileSelectionView extends JPanel{
    private TerrainSelectionView terrainSelectionView;
    private RiverSelectionView riverSelectionView;
    private CurrentSelectionView currentSelectionView;
    private BufferedImage panelBackground;
    
    public TileSelectionView(MapSubsectionView mapSubsectionView, MapEditorView editorView){
        setLayout(new BorderLayout());
        setBounds((int)(PixelMap.SCREEN_WIDTH * 0.0125), (int)(PixelMap.SCREEN_HEIGHT * 0.025), (int)(PixelMap.SCREEN_WIDTH * (0.0125 + 0.155)), (int)(PixelMap.SCREEN_HEIGHT * 0.95));
        setOpaque(false);
        
        terrainSelectionView = new TerrainSelectionView(new Dimension(getWidth() / 2 + 1, getHeight() - getWidth()));
        riverSelectionView = new RiverSelectionView(new Dimension(getWidth() / 2 + 1, getHeight() - getWidth()));
        currentSelectionView = new CurrentSelectionView(new Dimension(getWidth(), getWidth()), riverSelectionView);
        panelBackground = ImageLoader.getImage("PANEL_BACKGROUND");

        add(terrainSelectionView, BorderLayout.WEST);
        add(riverSelectionView, BorderLayout.EAST);
        add(currentSelectionView, BorderLayout.SOUTH);

        TileSelectionMouseListener listener = new TileSelectionMouseListener(this, currentSelectionView, mapSubsectionView, editorView);

        addMouseListener(listener);
        addMouseMotionListener(listener);

        setVisible(true);
    }


    public TerrainSelectionView getTerrainSelectionView() {
        return terrainSelectionView;
    }
    public RiverSelectionView getRiverSelectionView() {
        return riverSelectionView;
    }
    public CurrentSelectionView getCurrentSelectionView() {
        return currentSelectionView;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.drawImage(panelBackground, 0, 0, (int)(getWidth() * 1.145), (int)(getHeight() * 1.032), null);
    	int recWidth = (int)(getWidth() * 0.97);
    	int recHeight = (int)(getHeight() * 0.71);
    	g.drawRect(0, 0, recWidth, (int)(getHeight() * 0.99));
    }
}
