package MapBuilder.Views.MapEditor.TileSelection;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import MapBuilder.Model.Utility.TerrainIterator;
import MapBuilder.Model.Utility.TileIterator;
import MapBuilder.Views.Utility.ImageLoader;

public class RiverSelectionView extends JPanel {

    private ArrayList<BufferedImage> riverImages = new ArrayList<>();
    private TileIterator terrainIterator = new TerrainIterator();
    private TileIterator riverIterator = terrainIterator.getRiverIterator();
    private BufferedImage shadow;
    private int numTiles = 6;
    
    public RiverSelectionView(Dimension size) {
        setPreferredSize(size);
        setVisible(true);
        setOpaque(false);
        drawRiverTiles();
        shadow = ImageLoader.getImage("TILE_SHADOW");
    }

    public void update(int index) {
        riverImages.clear();
        terrainIterator.first();
        for(int i = 0; i < index; i++) {
            terrainIterator.next();
        }
        riverIterator = terrainIterator.getRiverIterator();
        numTiles = riverIterator.getSize();
        drawRiverTiles();
    }

    public void drawRiverTiles() {

        riverIterator.first();
        for(int i = 0; i < riverIterator.getSize(); i++) {
            riverImages.add( riverIterator.getImage() );
            riverIterator.next();
        }

        repaint();
    }

    public TileIterator getIterator() {
        return riverIterator;
    }


    @Override
    protected void paintComponent(Graphics g) {
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int width = (int)( getWidth() * 0.90 );
        while(getHeight() / width < 6) {
            width -= 5;
        }

        //g.setColor( new Color(0xffCABD80)  );
        //g.fillRect(0, 0, getWidth(), getHeight());

        int i = 0;
        for(BufferedImage img : riverImages ) {
        	g.drawImage(shadow, (int)(width * .05) + 1, (int)(width * .05)  + i * width + 3, width, (int)(width * 0.9), null);
            g.drawImage(img, (int)(width * .05), (int)(width * .05)  + i * width, width, (int)(width * 0.9), null);
            i++;
        }
    }

}
