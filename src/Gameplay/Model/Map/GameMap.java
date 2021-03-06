package Gameplay.Model.Map;

import Gameplay.Model.Tile.GameTile;
import Gameplay.Model.Utility.HexaVertex;
import MapBuilder.Model.Map.IViewMap;
import MapBuilder.Model.Utility.HexLocation;
import MapBuilder.Model.Utility.HexaIndex;
import MapBuilder.Model.Utility.ILocation;
import MapBuilder.Model.Visitor.MapVisitor;

import java.util.ArrayList;
import java.util.List;

public class GameMap implements IViewMap{
    private int tileCount, length, width;
    private GameTile[][] map;

    public GameMap(int maxLength, int maxWidth){
        this.length = maxLength;
        this.width = maxWidth;
        this.tileCount = 0;
        map = new GameTile[length][width];

    }

    public void initialize(GameTile[][] tiles){
        map = tiles;
    }

    public HexLocation getHexLocationOf(GameTile tile){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                if (map[i][j] == null)
                    continue;
                if (map[i][j] == tile)
                    return new HexLocation(i, j);
            }
        }
        return null;
    }

    @Override
    public GameTile getTileAt(ILocation location) {
        return map[location.getRow()][location.getCol()];
    }

    @Override
    public void accept(MapVisitor v) {
        v.visitMap(this);
    }

    @Override
    public GameTile[][] getTiles() {
        return map;
    }

    @Override
    public GameTile[][] getTiles(ILocation topLeft, ILocation bottomRight) {
        int col1 = topLeft.getCol();
        int col2 = bottomRight.getCol();
        int row1 = topLeft.getRow();
        int row2 = bottomRight.getRow();

        GameTile[][] window = new GameTile[Math.abs(col2 - col1) + 1][Math.abs(row2 - row1) + 1];

        //TODO what if map at indices has no tile
        for (int i = col1, _i = 0; i <= col2; i++, _i++){
            for (int j = row1, _j = 0; j <= row2; j++, _j++){
                window[_i][_j] = map[i][j];
            }
        }
        return window;
    }

    public List<HexaVertex> getVertices(GameTile tile, GameTile connecter) {
        List<HexaVertex> vertices = new ArrayList<HexaVertex>();
        HexLocation locationTile = getHexLocationOf(tile);
        HexLocation locationConnecter = getHexLocationOf(connecter);

        try {
            int index = locationTile.getIndexOfLocation(locationConnecter).getValue();
            vertices.add(HexaVertex.createVertex(index));
            vertices.add(HexaVertex.createVertex((index + 1) % 6));
            vertices.add(HexaVertex.createVertex(index + 6));
        } catch (Exception e) {}
        return vertices;
    }

    public int getLength(){
        return length;
    }

    public int getWidth(){
        return width;
    }
}
