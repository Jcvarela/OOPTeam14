package MapBuilder.Model.Visitor;

import java.awt.image.BufferedImage;

import MapBuilder.Model.Map.IViewMap;
import MapBuilder.Model.Tile.Tile;
import MapBuilder.Model.Utility.ILocation;
import MapBuilder.Views.Utility.ImageLoader;

public class MapDrawingVisitor implements MapVisitor {
    private BufferedImage[][] imageArray;

    public MapDrawingVisitor(){
    }

    public MapDrawingVisitor(ILocation location){
        //TODO implement
    }

    @Override
    public void visitMap(IViewMap map) {

        Tile[][] tiles = map.getTiles();
        int height = tiles.length;
        int width = tiles[0].length;

        imageArray = new BufferedImage[width][height];
        /*
        Will return array of tiles in the camera window. There will
        be null tiles.
        */
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if(tiles[row][col] == null){

                    imageArray[row][col] = ImageLoader.getDefaultImage();
//                  System.out.println("i: "+col+" -- j: "+row);
//                  System.out.println(imageArray[row][col]);
                }
                else{
                    TileDrawingVisitor tdv = new TileDrawingVisitor();
                    tiles[row][col].accept(tdv);
                    imageArray[row][col] = tdv.getImage();
                }
            }
        }
    }

    public BufferedImage[][] getImageArray(){
        return this.imageArray;
    }
}
