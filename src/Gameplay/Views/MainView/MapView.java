package Gameplay.Views.MainView;

import Gameplay.Controller.CameraController;
import Gameplay.Controller.CameraKeysController;
import Gameplay.Model.Visitors.GameMapDrawingVisitor;
import Gameplay.Views.Drawers.*;
import Gameplay.Views.Utility.*;
import MapBuilder.Model.Map.IViewMap;
import MapBuilder.Model.Utility.HexLocation;
import MapBuilder.Views.Utility.ImageLoader;
import MapBuilder.Views.Utility.PixelPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MapView extends JPanel {

    private RenderingThread renderingThread;
    private BufferedImage[][] tileImages;
    private List<ImageWithLocation> transporterImages;
    private List<ImageWithLocation> producerImages;
    private List<ImageWithLocation> goodsImages;
    private List<Line> roads;
    private List<Line> bridges;
    private List<RotatedImageWithLocation> wallImages;
    private CursorState cursorState = CursorState.getInstance();

    public void updateTileImages(IViewMap map) {
        GameMapDrawingVisitor drawingVisitor = new GameMapDrawingVisitor();
        map.accept(drawingVisitor);
        tileImages = drawingVisitor.getImageArray();
    }

    public void updateTransporterImages() {
        AllTransporterDrawer atd = new AllTransporterDrawer();
        transporterImages = atd.getAllTransporterImages();
    }

    public void updateProducerImages() {
        AllProducerDrawer apd = new AllProducerDrawer();
        producerImages = apd.getAllProducerImages();
    }

    public void updateGoodsImages() {
        AllGoodDrawer agd = new AllGoodDrawer();
        goodsImages = agd.getAllGoodImages();
    }

    public void updateRoadImages() {
        RoadDrawer rd = new RoadDrawer();
        roads = rd.getAllRoads();
    }

    public void updateBridgeImages() {
        BridgeDrawer bd = new BridgeDrawer();
        bridges = bd.getAllBridges();
    }

    public void updateWallImages() {
        WallDrawer wd = new WallDrawer();
        wallImages = wd.getAllWalls();
    }

    public MapView(){
        setLayout(null);
        setBounds(0,0,PixelMap.SCREEN_WIDTH, PixelMap.SCREEN_HEIGHT);
        setVisible(true);

        //Add Camera Movement Controller
        CameraController cameraController = new CameraController();
        addMouseMotionListener(cameraController);
        addMouseListener(cameraController);

        renderingThread = new RenderingThread(this, 30);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ImageLoader.getImage("GAME_BACKGROUND"), 0, 0, getWidth(), getHeight(), null);
        if (tileImages == null)
            return;
        for (int i = 0; i < tileImages.length; i++) {
            for (int j = 0; j < tileImages[i].length; j++) {
                if (PixelMap.isTileVisible(new HexLocation(i, j))) {
                    PixelPoint origin = PixelMap.getMapTileOrigin(new HexLocation(i,j));
                    TileInternalDrawer.drawInMap(g, tileImages[i][j], origin);
                }
            }
        }

        //Region Center Test
        PixelPoint c = cursorState.getCursor();
        g.drawRect(c.getX(), c.getY(), 5, 5);

        //Tile Marker
//        GridDrawer.drawActiveTile(g, CursorState.getInstance().getActiveTile());

        //Region Marker Test
        if(!cursorState.isDrawingRoad()) {
            Polygon region = cursorState.getRegionArea();
            if (region != null && cursorState.isMarkerActive())
                GridDrawer.drawActiveRegion(g, region);
        }
        else{
            if(!cursorState.isIsBridge()) {
                Color color = new Color(198, 82, 45);
                paintRoadMarker(g, color, 5);
                GridDrawer.drawActiveDestination(g, cursorState.getRegionArea(), color, 3);
            }else{
                Color color = new Color(31, 44, 198);
                paintRoadMarker(g, color, 10);
                GridDrawer.drawActiveDestination(g, cursorState.getRegionArea(), color, 6);
            }
        }


        updateRoadImages();
        for (Line road : roads)
            road.draw(g);

        updateBridgeImages();
        for (Line bridge : bridges)
            bridge.draw(g);

        updateTransporterImages();
        for (ImageWithLocation image : transporterImages)
            image.draw(g);

        updateGoodsImages();
        for (ImageWithLocation image : goodsImages)
            image.draw(g);

        updateProducerImages();
        for (ImageWithLocation image : producerImages)
            image.draw(g);

        updateWallImages();
        for (RotatedImageWithLocation image : wallImages)
            image.draw(g);
    }

    public void startRendering(int frameRate){
        renderingThread.setFrameRate(frameRate);
        renderingThread.start();
    }

    public void paintRoadMarker(Graphics g, Color color, int stroke){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(stroke));
        g2.setColor(color);
        PixelPoint[] points = cursorState.getDrawingPoints();
        if(points.length > 0){
            g.drawLine(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY());
        }
    }

    public void stopRendering(){
        renderingThread.interrupt();
    }
}
