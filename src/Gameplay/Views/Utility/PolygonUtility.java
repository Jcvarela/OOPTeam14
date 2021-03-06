/**-------------------------------------------------------------------------------------
|	PolygonUtility Class: Created by Alejandro Chavez on 4/15/2017.
|---------------------------------------------------------------------------------------
|   Description: Takes care of the Generation of all relevant Polygons in the game.
|   Hexatiles, Regions, Rivers, and Menu elements are generated here dynamically while
|   taking into account Pixel inputs.
---------------------------------------------------------------------------------------*/

package Gameplay.Views.Utility;
import Gameplay.Views.Utility.PolygonProportions.*;
import MapBuilder.Views.Utility.PixelPoint;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PolygonUtility {

    static class RegionCode{
        public RegionCode(String code, PolygonPointSet polygon) {
            this.code = code;
            this.polygon = polygon;
        }
        public String code;
        public PolygonPointSet polygon;
    }

    public static List<PolygonPointSet> type0Regions = new ArrayList<>();
    public static List<PolygonPointSet> type1Regions = new ArrayList<>();
    public static List<PolygonPointSet> type2Regions = new ArrayList<>();
    public static List<PolygonPointSet> type3Regions = new ArrayList<>();
    public static List<PolygonPointSet> type4Regions = new ArrayList<>();
    public static List<PolygonPointSet> type5Regions = new ArrayList<>();
    private static Map<Integer,List<PolygonPointSet>> typeMap = new HashMap<>();

    static {

        //TYPE #0
        //-------------------------------------------------------------
        //Region
        List<Point> pointSet = new ArrayList<>();
        pointSet.add(PixelMap.TOP_LEFT);
        pointSet.add(PixelMap.TOP_RIGHT);
        pointSet.add(PixelMap.RIGHT);
        pointSet.add(PixelMap.BOTTOM_RIGHT);
        pointSet.add(PixelMap.BOTTOM_LEFT);
        pointSet.add(PixelMap.LEFT);
        PolygonPointSet type0region = new PolygonPointSet(pointSet);
        type0Regions.add(type0region);


        //TYPE #1
        //-------------------------------------------------------------
        //River
        PolygonPointSet type1river = new PolygonPointSet(River1Proportions.river);
        type1Regions.add(type1river);

        //Region
        PolygonPointSet type1region = new PolygonPointSet(River1Proportions.region);
        type1Regions.add(type1region);


        //TYPE #2
        //-------------------------------------------------------------
        //River
        PolygonPointSet type2river = new PolygonPointSet(River2Proportions.river);
        type2Regions.add(type2river);

        //Regions
        PolygonPointSet type2region1 = new PolygonPointSet(River2Proportions.region1);
        PolygonPointSet type2region2 = new PolygonPointSet(River2Proportions.region2);
        type2Regions.add(type2region1);
        type2Regions.add(type2region2);


        //TYPE #3
        //-------------------------------------------------------------
        //River
        PolygonPointSet type3river = new PolygonPointSet(River3Proportions.river);
        type3Regions.add(type3river);

        //Regions
        PolygonPointSet type3region1 = new PolygonPointSet(River3Proportions.region1);
        PolygonPointSet type3region2 = new PolygonPointSet(River3Proportions.region2);
        type3Regions.add(type3region1);
        type3Regions.add(type3region2);


        //TYPE #4
        //-------------------------------------------------------------
        //River
        PolygonPointSet type4river = new PolygonPointSet(River4Proportions.river);
        type4Regions.add(type4river);

        //Regions
        PolygonPointSet type4region1 = new PolygonPointSet(River4Proportions.region1);
        PolygonPointSet type4region2 = new PolygonPointSet(River4Proportions.region2);
        type4Regions.add(type4region1);
        type4Regions.add(type4region2);


        //TYPE #5
        //-------------------------------------------------------------
        //River
        PolygonPointSet type5river = new PolygonPointSet(River5Proportions.river);
        type5Regions.add(type5river);

        //Regions
        PolygonPointSet type5region1 = new PolygonPointSet(River5Proportions.region1);
        PolygonPointSet type5region2 = new PolygonPointSet(River5Proportions.region2);
        PolygonPointSet type5region3 = new PolygonPointSet(River5Proportions.region3);
        type5Regions.add(type5region1);
        type5Regions.add(type5region2);
        type5Regions.add(type5region3);



        //Add the maps
        typeMap.put(new Integer(0), type0Regions);
        typeMap.put(new Integer(1), type1Regions);
        typeMap.put(new Integer(2), type2Regions);
        typeMap.put(new Integer(3), type3Regions);
        typeMap.put(new Integer(4), type4Regions);
        typeMap.put(new Integer(5), type5Regions);
    }

    //Returns the Hexagon formed by the joining the corner points and a ZERO offset
    public static Polygon getHexagon(PixelPoint center){
        Polygon polygon = new Polygon();
        for(PixelPoint point: getHexatilePoints(center, 0, 0)){
            polygon.addPoint(point.getX(), point.getY());
        }
        return polygon;
    }

    //Returns the Hexagon formed by the joining the corner points taking camera offset into account
    public static Polygon getHexagon(PixelPoint center, int xOffset, int yOffset){
        Polygon polygon = new Polygon();
        for(PixelPoint point: getHexatilePoints(center, xOffset, yOffset)){
            polygon.addPoint(point.getX(), point.getY());
        }
        return polygon;
    }

    //Gets corner points for a Hexatile
    private static List<PixelPoint> getHexatilePoints(PixelPoint center, int xOffset, int yOffset){
        java.util.List<PixelPoint> points = new ArrayList<>();
        //Top-Left point
        points.add(new PixelPoint(center.getX() - (PixelMap.TILE_WIDTH/2) - xOffset, center.getY() - (PixelMap.TILE_HEIGHT/2) - yOffset));
        //Left point
        points.add(new PixelPoint(center.getX() - (PixelMap.TILE_WIDTH) - xOffset, center.getY() - yOffset));
        //Bottom-Left point
        points.add(new PixelPoint(center.getX() - (PixelMap.TILE_WIDTH/2) - xOffset, center.getY() + (PixelMap.TILE_HEIGHT/2) - yOffset));
        //Bottom-Right point
        points.add(new PixelPoint(center.getX() + (PixelMap.TILE_WIDTH/2) - xOffset, center.getY() + ((PixelMap.TILE_HEIGHT/2) - yOffset)));
        //Right point
        points.add(new PixelPoint(center.getX() + (PixelMap.TILE_WIDTH) - xOffset, center.getY() - yOffset));
        //Top-Right point
        points.add(new PixelPoint(center.getX() + (PixelMap.TILE_WIDTH/2) - xOffset, center.getY() - (PixelMap.TILE_HEIGHT/2) - yOffset));
        return points;
    }

    //generates Squares based on set center
    public static Rectangle generateSquare(PixelPoint origin, Integer side){
        Rectangle square = new Rectangle();
        square.setBounds(origin.getX(), origin.getY(), side, side);
        return square;
    }


    //Generates a Polygon from a Rectangle
    public static Polygon rectangleToPolygon(Rectangle rect) {
        int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
        int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};
        return new Polygon(xpoints, ypoints, 4);
    }


    public static List<PolygonPointSet> getRegionsByType(Integer riverIndex){
        return typeMap.get(riverIndex);
    }


}
