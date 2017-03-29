package Model.Utility.MapParsers;

import Model.Map.BuildMap;
import Model.ModelFacade;
import Model.Tile.BuildTile;
import Model.Tile.BuildTileFactory;
import Model.Utility.FileIO;
import Model.Utility.HexLocation;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi on 3/27/2017.
 */
public class DaveBuilder implements MapParser {
    private FileIO fileIO = new FileIO();
    private BuildTileFactory tileFactory = new BuildTileFactory();
    final JFileChooser fc=  new JFileChooser();
    final static String pathname = "./res/SavedMaps/";

    public void buildMap(String path) {

        if (!path.equals(null)) {
            try {
                String fileInfo = fileIO.loadFileText(path);
                parseData(fileInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void parseData(String fileInfo) {

        fileInfo = cleanString(fileInfo);

        String[] tilesStringRepresentation = separateTiles(fileInfo);

        List<DaveBuilderTile> tiles = extractTilesInfo(tilesStringRepresentation);

        createMap(tiles);

    }

    /**
     * removes parenthesis from the whole string
     *
     * @param fileInfo
     * @return
     */
    private String cleanString(String fileInfo) {
        fileInfo = fileInfo.replace("(", "");
        fileInfo = fileInfo.replace(")", "");

        return fileInfo;
    }

    /**
     * separates each line of tiles
     *
     * @param fileInfo
     * @return
     */
    private String[] separateTiles(String fileInfo) {
        String[] lines = fileInfo.split("\\|");

        return lines;
    }

    /**
     * extracts the information from the arrays of strings and places it on a DaveBuilderTile list so that
     * the information for all the tiles is easily accessible
     *
     * @param tilesStringRepresentation
     * @return
     */
    private List<DaveBuilderTile> extractTilesInfo(String[] tilesStringRepresentation) {
        List<DaveBuilderTile> tiles = new ArrayList<>();

        for (int i = 1; i < tilesStringRepresentation.length; i++) {
            String tempTile = tilesStringRepresentation[i];

            DaveBuilderTile daveBuilderTile = null;
            try {
                daveBuilderTile = separateTileParameters(tempTile);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            if (daveBuilderTile != null) {
                tiles.add(daveBuilderTile);
            }
        }

        return tiles;
    }

    /**
     * separates the parameters needed to create a tile
     * makes a DaveBuilderTile class that is used like a bag of information, just to be organized
     *
     * @param tileStringRepresentation
     * @return
     */
    private DaveBuilderTile separateTileParameters(String tileStringRepresentation) throws RuntimeException {
        String[] tile = tileStringRepresentation.split(" ");
        DaveBuilderTile daveBuilderTile = new DaveBuilderTile(tile);
        return daveBuilderTile;

    }

    /**
     * gets a DaveBuilderTile and with this information creates a BuildTile
     *
     * @param tile
     * @return
     */
    private BuildTile createTile(DaveBuilderTile tile) {
        //TODO:ask if this should go through the facade
        String terrain = tile.getTerrain();
        int[] riverIndices = tile.getRivers();
        return tileFactory.createTile(terrain, riverIndices);
    }

    /**
     * converts the tiles received into BuildTiles calling the createTile function
     * converts the location received in cube coordinates into cartesian coordinates
     * and then places these tiles on the map calling the placeTile function
     *
     * @param tiles
     */
    private void createMap(List<DaveBuilderTile> tiles) {
        List<TilePlacement> placements= new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            DaveBuilderTile tempDaveTile = tiles.get(i);

            HexLocation location = convertToOddQOffset(tempDaveTile.getCubeLocation());
            BuildTile buildTile = createTile(tempDaveTile);
            TilePlacement tempPlacement=buildTilePlacement(buildTile,location);
            placements.add(tempPlacement);
        }
        placeTiles(placements);
    }

    private void placeTiles(List<TilePlacement>placements) {
        ModelFacade modelFacade=ModelFacade.getInstance();
        modelFacade.placeFromFile(placements);
    }

    private TilePlacement buildTilePlacement(BuildTile tile, HexLocation location){
        return new TilePlacement(tile,location);
    }


    private HexLocation convertToOddQOffset(CubeLocation cubeLocation) {
        ModelFacade modelFacade=ModelFacade.getInstance();
        int x = cubeLocation.getX();
        int y = cubeLocation.getY();
        int z = cubeLocation.getZ();


        int col = x;
        int row = z + (x - (x & 1)) / 2;

        row += modelFacade.getMapLength() / 2;
        col += modelFacade.getMapWidth() / 2;


        return new HexLocation(row, col);
    }

    private CubeLocation convertToCube(int row, int col) {
        ModelFacade modelFacade=ModelFacade.getInstance();
        int x, y, z;
        row -= modelFacade.getMapLength() / 2;
        col -= modelFacade.getMapWidth() / 2;

        x = col;
        z = row - (col - (col & 1)) / 2;
        y = -x - z;
        return new CubeLocation(x, y, z);
    }

    @Override
    public void saveMap(BuildMap map, String path) {

        if (!path.equals(null)) {

            List<DaveBuilderTile> tiles = getFormattedTiles(map);
            String output = formatTiles(tiles);
            saveFile(path, output);
        }
    }

    private String findSavePath() {
        String path = "";
        String txt = ".txt";
        fc.setCurrentDirectory(new java.io.File(pathname));
        fc.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
        fc.setDialogTitle("Test1");

        int result = fc.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getAbsolutePath();

        }

        if (!path.contains(txt)) {
            path += txt;
        }

        return path;
    }

    /**
     * gets a two dimensional array of tiles
     * visits every tile to extract the necessary information
     * uses that information to create formatted tiles
     * returns an array of formatted tiles in DaveBuilderTile format
     *
     * @param map
     * @return
     */
    private List<DaveBuilderTile> getFormattedTiles(BuildMap map) {
        List<DaveBuilderTile> formattedTiles = new ArrayList<>();
        int lastRow = map.getHEIGHT() - 1;
        int lastCol = map.getWIDTH() - 1;

        HexLocation topLeft = new HexLocation(0, 0);
        HexLocation bottomRight = new HexLocation(lastRow, lastCol);

        BuildTile[][] tiles = map.getTiles(topLeft, bottomRight);

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                BuildTile tempTile = tiles[row][col];
                if (isValidTile(tempTile)) {
                    MapSavingVisitor visitor = new MapSavingVisitor();
                    tempTile.accept(visitor);
                    DaveBuilderTile tempFormattedTile = convertToTileFormat(row, col, visitor);
                    formattedTiles.add(tempFormattedTile);
                }
            }
        }
        return formattedTiles;
    }

    /**
     * checks that a tile is not null
     *
     * @param tile
     * @return
     */
    private boolean isValidTile(BuildTile tile) {
        return tile != null;
    }


    private DaveBuilderTile convertToTileFormat(int row, int col, MapSavingVisitor visitor) {
        CubeLocation cubeLocation = convertToCube(row, col);
        ArrayList<Integer> riverIndices = visitor.getRiverIndices();
        String terrain = visitor.getTerrain().toString();

        return new DaveBuilderTile(cubeLocation, terrain, riverIndices);
    }

    /**
     * creates the output string
     * <p>
     * this class might seem to break LoD but in reality all this information is located in this class
     * the reason for using classes such as CubeLocation and DaveBuilderTile is so that the code is more organized
     * and so that there are less bugs
     *
     * @param tiles
     * @return
     */
    private String formatTiles(List<DaveBuilderTile> tiles) {
        String output = tiles.size() + "\n";

        for (DaveBuilderTile tile : tiles) {
            String location = formatLocation(tile);
            String terrain = formatTerrain(tile);
            String riverEdges = formatRiverEdges(tile);

            output += location + terrain + riverEdges + "\n";
        }
        return output;
    }

    private String formatLocation(DaveBuilderTile tile) {
        CubeLocation cubeLocation = tile.getCubeLocation();
        return "(" + cubeLocation.getX() + " " + cubeLocation.getY() + " " + cubeLocation.getZ() + ")";
    }

    private String formatTerrain(DaveBuilderTile tile) {
        return " " + tile.getTerrain();
    }

    private String formatRiverEdges(DaveBuilderTile tile) {
        int[] rivers = tile.getRivers();
        String riverEdges = "";

        if (areValidRivers(rivers)) {
            riverEdges = " (";
            for (int i = 0; i < rivers.length; i++) {
                riverEdges += rivers[i] + " ";
            }
            riverEdges += ")";
        }

        return riverEdges;
    }

    private boolean areValidRivers(int[] rivers) {
        return rivers!=null;
    }

    private void saveFile(String path, String output) {
        fileIO.saveFileText(path, output);
    }


}