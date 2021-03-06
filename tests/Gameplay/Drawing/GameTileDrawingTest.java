package Gameplay.Drawing;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Gameplay.Model.Tile.GameTile;
import Gameplay.Model.Tile.GameTileBuilder;
import Gameplay.Model.Utility.HexConventionAle;
import Gameplay.Model.Visitors.GameTileDrawingVisitor;
import org.junit.Test;

import MapBuilder.Model.Tile.BuildTileFactory;
import MapBuilder.Model.Tile.Tile;
import MapBuilder.Views.Utility.ImageLoader;

public class GameTileDrawingTest {

    @Test
    public void testDefaultImage() throws IOException {
        BufferedImage testImg = ImageLoader.getImage("MOUNTAIN");
        byte[] testByteArray = ((DataBufferByte) testImg.getData().getDataBuffer()).getData();
        BuildTileFactory factory = new BuildTileFactory();
        Tile t = factory.createTile("MOUNTAIN", new int[]{});
        GameTileDrawingVisitor tdv = new GameTileDrawingVisitor();
        t.accept(
                tdv
        );
        BufferedImage visitorImage = tdv.getImage();
        byte[] vByteArray = ((DataBufferByte) visitorImage.getData().getDataBuffer()).getData();
        assertArrayEquals(testByteArray, vByteArray);
    }
    @Test
    public void testFunctionality(){
        GameTileBuilder factory = new GameTileBuilder(new HexConventionAle());
        ArrayList<Integer> rivers = new ArrayList<>();
        rivers.add(1);
        rivers.add(4);

        GameTile t = factory.createTile("WOODS", rivers);

        GameTileDrawingVisitor tdv = new GameTileDrawingVisitor();

        t.accept(
                tdv
        );
        try {
            ImageIO.write(tdv.getImage(), "png", new java.io.File("res/Images/Tests/testVisit.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRivers(){
        GameTileBuilder builder = new GameTileBuilder(new HexConventionAle());
        ArrayList<Integer> rivers = new ArrayList<>();
        for(int i = 1; i < 6; i++){
            rivers.add(i);
            GameTile tile = builder.createTile("WOODS", rivers);
            assertEquals(tile.getRiverType(), 1);
            assertEquals(tile.getRotationNumber(), i-1);
            rivers.clear();
        }

        for(int i = 1; i < 6; i++){
            rivers.add(i);
            rivers.add(i + 1);
            GameTile tile = builder.createTile("WOODS", rivers);
            assertEquals(tile.getRiverType(), 2);
            assertEquals(tile.getRotationNumber(), i-1);
            rivers.clear();
        }
    }

}
