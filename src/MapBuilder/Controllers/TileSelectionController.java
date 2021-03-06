package MapBuilder.Controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import MapBuilder.Views.MapEditor.TileSelection.CurrentSelectionView;
import MapBuilder.Views.MapEditor.TileSelection.RiverSelectionView;
import MapBuilder.Views.MapEditor.TileSelection.TerrainSelectionView;
import MapBuilder.Views.MapEditor.TileSelection.TileSelectionView;

public class TileSelectionController implements MouseListener{

    private TileSelectionView tileSelectionView = null;
    private TerrainSelectionView terrainSelectionView = null;
    private RiverSelectionView riverSelectionView = null;
    private CurrentSelectionView currentSelectionView = null;

    public TileSelectionController(TileSelectionView tileSelectionView) {

        this.tileSelectionView = tileSelectionView;
        this.terrainSelectionView = tileSelectionView.getTerrainSelectionView();
        this.riverSelectionView = tileSelectionView.getRiverSelectionView();
        this.currentSelectionView = tileSelectionView.getCurrentSelectionView();

        tileSelectionView.addMouseListener(this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double x = (double)(e.getPoint().getX() / tileSelectionView.getWidth());
        double y = (double)(e.getPoint().getY() / tileSelectionView.getHeight());

        // 6 is the number of tiles on display
        int tileIndex = (int)( y / (0.7 / 6) );
        
        if (tileIndex >= 0 && tileIndex < 6) {
            
            if( y < 0.75 && x < 0.5) {      // click in terrain panel
                riverSelectionView.update( tileIndex );
                currentSelectionView.update( 0 );
            } else if( y < 0.75 && x > 0.5 && x < 1) { // click in river panel
                currentSelectionView.update( tileIndex );
            }
        } else if( y > 0.75 ) { // click in current selection panel
                currentSelectionView.rotate();
            }
     }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
}
