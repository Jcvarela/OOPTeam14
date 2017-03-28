package Views.MapEditor.TileSelection;

import Model.Tile.Tile;
import Model.Utility.TerrainIterator;
import Model.Utility.TileIterator;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CurrentSelectionView extends JPanel {
    
    private BufferedImage currSelectionImage = null;
    private TileIterator terrainIterator = new TerrainIterator();
    private TileIterator riverIterator = terrainIterator.getRiverIterator();
    private RiverSelectionView riverSelectionView = null;

    public CurrentSelectionView(Dimension size, RiverSelectionView riverSelectionView) {
        this.riverSelectionView = riverSelectionView;
        setPreferredSize(size);
        setVisible(true);
        setOpaque(false);
        drawCurrentSelection();
    }

    public void update( int tileIndex ) {

        this.riverIterator = riverSelectionView.getIterator();
        riverIterator.setSelectedTile( tileIndex );
        currSelectionImage = riverIterator.getSelectedTileImage();

    }

    public void drawCurrentSelection() {

        currSelectionImage = riverIterator.getSelectedTileImage();
        repaint();
    }

    public void rotate() {

        riverIterator.rotate();
        drawCurrentSelection();
    }

    @Override
    protected void paintComponent(Graphics g) {
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g.setColor( new Color(0xffCABD80)  );
        //g.fillRect(0, 0, getWidth(), getHeight());
        int width = (int)( getWidth() * 0.90 );
        g.drawImage(currSelectionImage, (int)(width * .05) - 3, (int)(width * .05) + 10, width, (int)(width * 0.9), null);
    }
    //Gives global access to currently selected image without breaking encapsulation
    // Note: It spits out a copy of the currently selected image
    public BufferedImage getSelectedTileImage(){
        return riverIterator.getSelectedTileImage();
    }

    public Tile getSelectedTile(){
        return riverIterator.getSelectedTile();
    }
}
