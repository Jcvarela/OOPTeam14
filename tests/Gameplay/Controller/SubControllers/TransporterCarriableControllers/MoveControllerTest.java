package Gameplay.Controller.SubControllers.TransporterCarriableControllers;

import Gameplay.Controller.MainController;
import Gameplay.Model.Iterators.TransporterIterator;
import Gameplay.Model.Map.GameMap;
import Gameplay.Model.Phases.PhaseManager;
import Gameplay.Model.Region.Region;
import Gameplay.Model.Tile.GameTile;
import Gameplay.Model.Tile.RegionMap;
import Gameplay.Model.TransporterFactory.DonkeyFactory;
import Gameplay.Model.TransporterFactory.SteamerFactory;
import Gameplay.Model.TransporterFactory.TransporterFactory;
import Gameplay.Model.Transporters.Transporter;
import Gameplay.Model.Utility.GameMapDaveBuilder;
import Gameplay.Model.Utility.GameModelFacade;
import Gameplay.Model.Utility.HexaVertex;
import Gameplay.Views.Display;
import Gameplay.Views.Utility.CursorState;
import MapBuilder.Model.Utility.HexLocation;
import MapBuilder.Model.Utility.MapParsers.BuildMapDaveBuilder;
import MapBuilder.Model.Utility.MapParsers.DaveBuilder;

import static org.junit.Assert.*;

/**
 * Created by jordi on 4/16/2017.
 */
public class MoveControllerTest {

    public static final String workingDir = System.getProperty("user.dir");
    public static final String mapsDir = workingDir + "\\res\\SavedMaps\\full board map.dave";

    public static void main(String[] args) {

        Display display = new Display();
        DaveBuilder builder = new GameMapDaveBuilder();
        builder.buildMap(mapsDir);
        GameModelFacade modelFacade =  GameModelFacade.getInstance();

//        TradingController tradingController = new TradingController();
//        tradingController.activateController(display.getMainView());

        //Creating and linking the Move Controller
        MoveController moveController = new MoveController();
        moveController.activateController(display.getMainView());

        //Creating and linking the Main Controller
        MainController mainController = new MainController(display.getMainView());
        PhaseManager phaseManager = new PhaseManager(mainController);
        modelFacade.setPhaseManager(phaseManager);

        //Starts the Game and generates Transporters
        modelFacade.startGame();
        moveController.addTransporters(generateCarriableIter(modelFacade));

        modelFacade.startGame();
//        moveController.addTransporters(generateCarriableIter(modelFacade));
    }


    public static TransporterIterator generateCarriableIter(GameModelFacade modelFacade) {


        if(modelFacade == null)
            return null;

         GameMap map = modelFacade.debugGetMap();


        HexLocation loc = CursorState.getInstance().getActiveTile();

        GameTile tile = map.getTileAt( loc );

        System.out.println(tile);


        RegionMap rm = tile.getRegionMap();



        Region r = rm.getRegionAt( new HexaVertex(2, -1) );



        System.out.println(r);
        return modelFacade.getTransporters(r);
    }



}