package MapBuilder.Model.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import MapBuilder.Model.Tile.BuildTile;
import MapBuilder.Model.Tile.BuildTileFactory;
import MapBuilder.Model.Utility.HexLocation;
import MapBuilder.Model.Utility.HexaIndex;

/**
 * Created by zrgam_000 on 3/27/2017.
 */
public class PlacementManagerTest {

    PlacementManager placementManager;
    BuildMap buildMap;
    BuildTileFactory btf;
    HexaIndex index1;
    HexaIndex index2;
    HexaIndex index3;
    HexaIndex index4;
    HexaIndex index5;
    HexaIndex index6;

    @Before
    public void TestSetup(){
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();
        btf = new BuildTileFactory();

        try {
            index1 = HexaIndex.createIndex(1);
            index2 = HexaIndex.createIndex(2);
            index3 = HexaIndex.createIndex(3);
            index4 = HexaIndex.createIndex(4);
            index5 = HexaIndex.createIndex(5);
            index6 = HexaIndex.createIndex(6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ValidateAfterRemove() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK",  new int[]{1, 4});
        placementManager.placeTileAt(tile, new HexLocation(5, 5));
        tile = btf.createTile("SEA",  new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(4, 5));
        tile = btf.createTile("WOODS",  new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        BuildTile RiverTile;

        RiverTile = btf.createTile("WOODS",  new int[]{3, 4});

        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 5)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(4, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(5, 6)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(6, 5)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(6, 4)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(6, 3)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(5, 3)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(4, 4)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 4)));

        RiverTile = btf.createTile("WOODS",  new int[]{1, 3, 5});

        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 5)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 6)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(4, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(5, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(6, 5)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(6, 4)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(6, 3)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(5, 3)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(4, 4)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 4)));

        placementManager.removeTileAt(new HexLocation(5,5));

        tile = btf.createTile("ROCK",  new int[]{1, 4});

        assertFalse(placementManager.validate(RiverTile, new HexLocation(5, 5)));
        assertTrue(placementManager.validate(tile, new HexLocation(5, 5)));
    }

    @Test
    public void SeaValidateTest() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK", new int[]{1, 4});
        placementManager.placeTileAt(tile, new HexLocation(5, 5));
        tile = btf.createTile("SEA", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(4, 5));
        tile = btf.createTile("WOODS", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        BuildTile SeaTile = btf.createTile("SEA", new int[]{});

        assertTrue(placementManager.validate(SeaTile, new HexLocation(3, 5)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(3, 6)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(4, 6)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(5, 6)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(6, 5)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(6, 4)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(6, 3)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(5, 3)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(4, 4)));
        assertTrue(placementManager.validate(SeaTile, new HexLocation(3, 4)));

        assertFalse(placementManager.validate(SeaTile, new HexLocation(4, 5)));
        assertFalse(placementManager.validate(SeaTile, new HexLocation(5, 4)));
        assertFalse(placementManager.validate(SeaTile, new HexLocation(5, 5)));

        assertFalse(placementManager.validate(SeaTile, new HexLocation(20, 20)));
    }

    @Test
    public void LandValidate() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK", new int[]{1, 4});
        placementManager.placeTileAt(tile, new HexLocation(5, 5));
        tile = btf.createTile("SEA", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(4, 5));
        tile = btf.createTile("WOODS", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        BuildTile LandTile = btf.createTile("DESERT", new int[]{});

        assertTrue(placementManager.validate(LandTile, new HexLocation(3, 5)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(3, 6)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(4, 6)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(5, 6)));
        assertFalse(placementManager.validate(LandTile, new HexLocation(6, 5)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(6, 4)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(6, 3)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(5, 3)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(4, 4)));
        assertTrue(placementManager.validate(LandTile, new HexLocation(3, 4)));

        assertFalse(placementManager.validate(LandTile, new HexLocation(4, 5)));
        assertFalse(placementManager.validate(LandTile, new HexLocation(5, 4)));
        assertFalse(placementManager.validate(LandTile, new HexLocation(5, 5)));

        assertFalse(placementManager.validate(LandTile, new HexLocation(20, 20)));
    }

    @Test
    public void RiverValidate() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK", new int[]{1, 4});
        placementManager.placeTileAt(tile, new HexLocation(5, 5));
        tile = btf.createTile("SEA", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(4, 5));
        tile = btf.createTile("WOODS", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        BuildTile RiverTile = btf.createTile("ROCK", new int[]{1, 3});

        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 5)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(4, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(5, 6)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(6, 5)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(6, 4)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(6, 3)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(5, 3)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(4, 4)));
        assertTrue(placementManager.validate(RiverTile, new HexLocation(3, 4)));

        assertFalse(placementManager.validate(RiverTile, new HexLocation(4, 5)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(5, 4)));
        assertFalse(placementManager.validate(RiverTile, new HexLocation(5, 5)));

        assertFalse(placementManager.validate(RiverTile, new HexLocation(20, 20)));

    }

    @Test
    public void placeRiverTest(){
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK",  new int[]{2, 4, 6});
        BuildTile tile2 = btf.createTile("ROCK",  new int[]{3, 5});

        placementManager.placeTileAt(tile, new HexLocation(2, 2));

        assertTrue(placementManager.validate(tile2, new HexLocation(2, 3)));
    }

    @Test
    public void placeTileAt() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK",  new int[]{1, 4});

        assertEquals(0,placementManager.getNumSlots());

        placementManager.placeTileAt(tile, new HexLocation(5, 5));

        assertEquals(6,placementManager.getNumSlots());

        tile = btf.createTile("SEA",  new int[]{});

        placementManager.placeTileAt(tile, new HexLocation(4, 5));

        assertEquals(8,placementManager.getNumSlots());

        tile = btf.createTile("WOODS",  new int[]{});

        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        assertEquals(10, placementManager.getNumSlots());
    }

    @Test
    public void removeTileAt() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK",  new int[]{1, 4});

        assertEquals(0,placementManager.getNumSlots());

        placementManager.placeTileAt(tile, new HexLocation(5, 5));

        assertEquals(6,placementManager.getNumSlots());

        tile = btf.createTile("SEA",  new int[]{});

        placementManager.placeTileAt(tile, new HexLocation(4, 5));

        assertEquals(8,placementManager.getNumSlots());

        tile = btf.createTile("WOODS",  new int[]{});

        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        assertEquals(10,placementManager.getNumSlots());

        placementManager.removeTileAt(new HexLocation(5, 5));

        assertEquals(10,placementManager.getNumSlots());

        tile = btf.createTile("SEA",  new int[]{});

        placementManager.removeTileAt(new HexLocation(4, 5));

        assertEquals(6,placementManager.getNumSlots());

        tile = btf.createTile("WOODS",  new int[]{});

        placementManager.removeTileAt(new HexLocation(5, 4));

        assertEquals(0, placementManager.getNumSlots());
    }

    @Test
    public void validateRivers(){
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK",  new int[]{1, 4});
        placementManager.placeTileAt(tile, new HexLocation(5, 5));
        tile = btf.createTile("SEA",  new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(4, 5));
        tile = btf.createTile("WOODS",  new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        BuildTile RiverTile = btf.createTile("DESERT",  new int[]{1});

        assertFalse(placementManager.validateRivers());

        placementManager.placeTileAt(RiverTile, new HexLocation(6, 5));

        assertTrue(placementManager.validateRivers());

        placementManager.removeTileAt(new HexLocation(5, 5));

        assertFalse(placementManager.validateRivers());

        tile = btf.createTile("ROCK",  new int[]{1, 4});
        placementManager.placeTileAt(tile, new HexLocation(5, 5));

        assertTrue(placementManager.validateRivers());

        placementManager.removeTileAt(new HexLocation(4, 5));

        assertFalse(placementManager.validateRivers());
    }

    @Test
    public void validateContiguous() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK", new int[]{});
        assertTrue(placementManager.validate(tile, new HexLocation(5, 5)));
        placementManager.placeTileAt(tile, new HexLocation(5, 5));
        tile = btf.createTile("SEA", new int[]{});
        assertTrue(placementManager.validate(tile, new HexLocation(4, 5)));
        placementManager.placeTileAt(tile, new HexLocation(4, 5));
        tile = btf.createTile("WOODS", new int[]{});
        assertTrue(placementManager.validate(tile, new HexLocation(5, 4)));
        placementManager.placeTileAt(tile, new HexLocation(5, 4));

        assertTrue(placementManager.validateMap());

        tile = btf.createTile("SEA", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(10, 5));

        assertFalse(placementManager.validateMap());

        placementManager.removeTileAt(new HexLocation(10, 5));

        assertTrue(placementManager.validateMap());

        placementManager.removeTileAt(new HexLocation(5,5));

        assertFalse(placementManager.validateMap());
    }

    @Test
    public void validateContiguousWithRivers() {
        BuildMap.reset();
        buildMap = BuildMap.getInstance();
        placementManager = new PlacementManager();

        BuildTile tile = btf.createTile("ROCK", new int[]{1, 3, 5});
        placementManager.placeTileAt(tile, new HexLocation(5,5));

        assertFalse(placementManager.validateMap());

        tile = btf.createTile("ROCK", new int[]{4});
        placementManager.placeTileAt(tile, new HexLocation(4,5));

        assertFalse(placementManager.validateMap());

        tile = btf.createTile("ROCK", new int[]{2});
        placementManager.placeTileAt(tile, new HexLocation(5,4));

        assertFalse(placementManager.validateMap());

        tile = btf.createTile("ROCK", new int[]{6});
        placementManager.placeTileAt(tile, new HexLocation(5,6));

        assertTrue(placementManager.validateMap());

        tile = btf.createTile("ROCK", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(3,5));

        assertTrue(placementManager.validateMap());

        tile = btf.createTile("ROCK", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(2,5));

        assertTrue(placementManager.validateMap());

        placementManager.removeTileAt(new HexLocation(3, 5));

        assertFalse(placementManager.validateMap());

        placementManager.removeTileAt(new HexLocation(2, 5));

        assertTrue(placementManager.validateMap());

        tile = btf.createTile("ROCK", new int[]{});
        placementManager.placeTileAt(tile, new HexLocation(4,6));

        assertTrue(placementManager.validateMap());
    }

}