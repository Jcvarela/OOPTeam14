package MapBuilder.Views;

import java.awt.Dimension;

import javax.swing.JFrame;

import Gameplay.Views.Display;
import MapBuilder.Controllers.KeyboardListeners.EditorKeyboardListener;
import MapBuilder.Model.ModelFacade;
import MapBuilder.Views.MapEditor.MapEditorView;
import MapBuilder.Views.MapEditor.MapView.MapSubsectionView;
import MapBuilder.Views.MapEditor.TileSelection.TileSelectionView;
import MapBuilder.Views.Utility.PixelMap;

public class MapEditorDisplay extends JFrame {
    private MapEditorView mapEditorView;
    private MapSubsectionView  mapSubsectionView;
    private Display displayFrame;

    public MapEditorDisplay(Display displayFrame) {

        this.displayFrame = displayFrame;
        setupFrame();
        mapEditorView = new MapEditorView( displayFrame, this );
        addKeyListener(new EditorKeyboardListener(ModelFacade.getInstance()));
        add(mapEditorView);
        mapSubsectionView = mapEditorView.getMapSubsectionView();

        EditorKeyboardListener listener = new EditorKeyboardListener(ModelFacade.getInstance());
        listener.setComponent(this);
        addKeyListener(listener);
    }

    private void setupFrame(){
        getContentPane().setPreferredSize(new Dimension(PixelMap.SCREEN_WIDTH, PixelMap.SCREEN_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public TileSelectionView getTileSelectionView() {
        return mapEditorView.getTileSelectionView();
    }
    public void updateMap(){
        mapSubsectionView.repaint();
    }

    public MapSubsectionView getMapSubsectionView() {
        return mapSubsectionView;
    }
}
