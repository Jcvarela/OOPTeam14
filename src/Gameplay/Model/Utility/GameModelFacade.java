package Gameplay.Model.Utility;

import Gameplay.Model.BuildAbilities.BuildAbility;
import Gameplay.Model.Goods.*;
import Gameplay.Model.Iterators.CarriableIterator;
import Gameplay.Model.Iterators.StuffIterator;
import Gameplay.Model.Iterators.TransporterIterator;
import Gameplay.Model.Map.*;
import Gameplay.Model.Phases.PhaseManager;
import Gameplay.Model.Phases.PhaseState;
import Gameplay.Model.Producer.Producer;
import Gameplay.Model.Producer.ProducerRequest;
import Gameplay.Model.Producer.SecondaryProducer.GoodProducer.CoalBurner;
import Gameplay.Model.Producer.SecondaryProducer.GoodProducer.Sawmill;
import Gameplay.Model.Producer.SecondaryProducer.GoodProducer.SecondaryGoodProducer;
import Gameplay.Model.Producer.SecondaryProducer.GoodProducer.StockMarket;
import Gameplay.Model.Producer.SecondaryProducer.TransporterProducer.SecondaryTransporterProducer;
import Gameplay.Model.Producer.UserRequest;
import Gameplay.Model.Region.Region;
import Gameplay.Model.Tile.GameTile;
import Gameplay.Model.Tile.RegionMap;
import Gameplay.Model.TransporterFactory.*;
import Gameplay.Model.Transporters.Transporter;
import Gameplay.Model.Visitors.Carriable;
import Gameplay.Model.Visitors.DropOffExchangeHandler;
import Gameplay.Model.Visitors.PickUpExchangeHandler;
import Gameplay.Model.Visitors.RegionPlacableVisitor;
import MapBuilder.Model.Utility.HexLocation;
import MapBuilder.Model.Utility.HexaIndex;

import java.util.*;

public class GameModelFacade { //TODO make an abstract facade
    private static GameModelFacade gameModelFacade;
    private GameMap gameMap;
    private static int maxMapLength, maxMapWidth;
    public TransporterHandler transporterHandler;
    private GoodsHandler goodsHandler;
    private PrimaryProducerHandler primaryProducerHandler;
    private SecondaryProducerHandler secondaryProducerHandler;
    private MovementManager movementManager;
    private WallHandler wallHandler;

    private PhaseManager phaseManager;
    private PlayerID currentPlayer;

    private UserRequestHandler userRequestHandler;


    private GameModelFacade(GameMap map) {
        this.gameMap = map;
        maxMapLength = map.getLength();
        maxMapWidth = map.getWidth();
    }

    public void setCurrentPlayer(PlayerID player){
        currentPlayer = player;
        System.out.println(currentPlayer.getID());
    }

    //Controlling the Phase Manager
    public void setPhaseManager(PhaseManager phaseManager){ this.phaseManager = phaseManager; }
    public void nextTurn(){ phaseManager.nextTurn(); }
    public PhaseState getCurrentPhase(){ return phaseManager.getCurrentState(); }

    public static GameModelFacade getInstance(){
        if (isInitialized()) {
            return gameModelFacade;
        }
        return null;
    }

    public static void initialize( GameMap map ){
        if (!isInitialized()){
            gameModelFacade = new GameModelFacade(map);
        }
    }

    private static boolean isInitialized(){
        return gameModelFacade != null;
    }

    public static int getMaxMapLength(){
        return maxMapLength;
    }

    public static int getMaxMapWidth(){
        return maxMapWidth;
    }

    public GameMap getMap(){
        return gameMap;
    }

    public void startGame() {
        wallHandler = WallHandler.getInstance();
        userRequestHandler = new UserRequestHandler();

        movementManager = new MovementManager(transporterHandler, wallHandler, goodsHandler);

        try {
            gameMap.getTiles()[10][10].getRegionMap().getRegionAt(HexaVertex.createVertex(4)).getRegionSet().addRoadRegion(
                    gameMap.getTiles()[10][11].getRegionMap().getRegionAt(HexaVertex.createVertex(5))
            );
            gameMap.getTiles()[10][11].getRegionMap().getRegionAt(HexaVertex.createVertex(5)).getRegionSet().addRoadRegion(
                    gameMap.getTiles()[10][10].getRegionMap().getRegionAt(HexaVertex.createVertex(4))
            );
            gameMap.getTiles()[10][10].getRegionMap().getRegionAt(HexaVertex.createVertex(1)).getRegionSet().addBridgeRegion(
                    gameMap.getTiles()[10][10].getRegionMap().getRegionAt(HexaVertex.createVertex(3))
            );
            gameMap.getTiles()[10][10].getRegionMap().getRegionAt(HexaVertex.createVertex(3)).getRegionSet().addBridgeRegion(
                    gameMap.getTiles()[10][10].getRegionMap().getRegionAt(HexaVertex.createVertex(1))
            );
        } catch(Exception e) {}

        GameTile tile1 = gameMap.getTiles()[10][10];
        GameTile tile2 = gameMap.getTiles()[9][10];

        try {
            Wall wall = new Wall();
            wall.strengthen();
            wall.setPlayerID(PlayerID.getNeutralPlayerID());
            wallHandler.addWall(tile1.getRegionAtHexaVertex(HexaVertex.createVertex(4)), tile2.getRegionAtHexaVertex(HexaVertex.createVertex(6)), wall);
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }

        primaryProducerHandler = PrimaryProducerHandler.getInstance();
        secondaryProducerHandler = SecondaryProducerHandler.getInstance();

        setUpGoodsHandler();
    }

    private void setUpGoodsHandler() {

        goodsHandler = GoodsHandler.getInstance();
        GameTile[][] tiles = gameMap.getTiles();
        RegionPlacableVisitor pcv = new RegionPlacableVisitor();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == null)
                    continue;
                RegionMap rm = tiles[i][j].getRegionMap();
                Iterator<Region> regionIterator = rm.getMyRegions();
                while (regionIterator.hasNext()) {
                    Region r = regionIterator.next();
                    r.accept(pcv);
                    if (pcv.getPlacable()) {
                        GoodsBag gb = new GoodsBag();
                        goodsHandler.place(gb, r);
                    }
                }
            }
        }

        transporterHandler = TransporterHandler.getInstance();



        //TODO delete
        TransporterFactory factory = new DonkeyFactory();
        Transporter tr = factory.create();
        factory = new SteamerFactory();
        Transporter t = factory.create();
        factory = new WagonFactory();
        Transporter truck = factory.create();
        tr.setPlayerID(PlayerID.getPlayer1ID());
        t.setPlayerID(PlayerID.getPlayer1ID());
        truck.setPlayerID(PlayerID.getPlayer1ID());

        try {
            Region r = gameMap.getTileAt(new HexLocation(10,10)).getRegionAtHexaVertex(HexaVertex.createVertex(1));
            Region r2 = gameMap.getTileAt(new HexLocation(10,9)).getRegionAtHexaVertex(HexaVertex.createVertex(1));
            Region r3 = gameMap.getTileAt(new HexLocation(10,11)).getRegionAtHexaVertex(HexaVertex.createVertex(4));
            GoodsBag goodsBag = new GoodsBag();
            goodsBag.addStone(new Stone());
            goodsHandler.place(goodsBag, r);
            secondaryProducerHandler.placeGoodsProducer(new Sawmill(), r);
            secondaryProducerHandler.placeGoodsProducer(new CoalBurner(), r2);
            secondaryProducerHandler.placeGoodsProducer(new StockMarket(), r3);
            tr.pickUpGood(new Trunk());
            truck.pickUpGood(new Coins());
            truck.pickUpGood(new Coins());
            truck.pickUpGood(new Paper());
            t.pickUpGood(new Board());
            t.pickUpGood(new Stock());
            r.enterRegion(tr);
            r.enterRegion(truck);
            transporterHandler.place(tr, r);
            transporterHandler.place(truck, r);
            r = gameMap.getTileAt(new HexLocation(10,10)).getRegionAtHexaVertex(HexaVertex.createVertex(8));
            transporterHandler.place(t, r);//steamer
            r.enterRegion(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO delete


    }

    /**
     * TODO: this is to be implemented differently on different phases so that the view
     * can display different carriables
     * @return
     */
    public CarriableIterator testGetCarriablesThisShouldBeDeleted(){
        ArrayList<Carriable> transporters = new ArrayList<>();
        TransporterFactory tf = new DonkeyFactory();
        transporters.add(tf.create());
        transporters.add(tf.create());
        transporters.add(tf.create());
        tf = new TruckFactory();
        transporters.add(tf.create());
        transporters.add(tf.create());
        transporters.add(new Paper());
        transporters.add(new Gold());
        transporters.add(new Coins());
        transporters.add(new Stock());

        return new CarriableIterator(transporters);

    }

    public StuffIterator testGetTransporterIteratorDELETETHIS(){
        ArrayList<Transporter> transporters = new ArrayList<>();
        TransporterFactory tf = new DonkeyFactory();
        transporters.add(tf.create());
        transporters.add(tf.create());
        transporters.add(tf.create());
        tf = new TruckFactory();
        transporters.add(tf.create());
        transporters.add(tf.create());
        return new TransporterIterator(transporters);
    }

    public void move(Region region, Transporter transporter){
        System.out.println("MOOOOOOOOOOOOOOOOOVVVVVVVVVVVVVVVVVVEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        movementManager.move(transporter, region, false);
    }


    /**
     * TODO: to be implemented, made for when a transporter needs to drop a carriable on a certain tile
     * @param region
     */
    public void dropCarriable(Region region, Transporter target, Carriable carriable) {
        TransporterOccupancy transporterOccupancy = transporterHandler.getOccupancyAt(region);
        GoodsBag goodsBag = goodsHandler.getGoodsBagAt(region);
        carriable.accept(new DropOffExchangeHandler(transporterOccupancy, goodsBag, target));
    }

    /**
     * TODO: to be implemented,
     * given the set of parameters pickup the transporter
     * @param region
     * @param transporter
     * @param carriable
     */
    public void pickUpCarriable(Region region, Transporter transporter, Carriable carriable){
        TransporterOccupancy transporterOccupancy = transporterHandler.getOccupancyAt(region);
        GoodsBag goodsBag = goodsHandler.getGoodsBagAt(region);
        carriable.accept(new PickUpExchangeHandler(transporterOccupancy, goodsBag, transporter));
    }

    /**
     * TODO: to be implemented, pass a list of transporters or ITERATOR owned by the player given a region
     * @param region
     * @return
     */
    public TransporterIterator getTransporters(Region region){
        return new TransporterIterator(transporterHandler.getTransportersAt(region));
    }

    public List<Region> getAllRegionsWithTransporter() {
        return transporterHandler.getAllRegions();
    }

    public Producer getProducer(Region region) {
        Producer producer = primaryProducerHandler.getPrimaryProducerAt(region);
        if (producer != null)
            return producer;
        producer = secondaryProducerHandler.getSecondaryProducerAt(region);
        if (producer != null)
            return producer;
        producer = secondaryProducerHandler.getTransporterProducerAt(region);
        if (producer != null)
            return producer;
        return null;
    }

    public List<Region> getAllRegionsWithProducer() {
        List<Region> regions = new ArrayList<>();
        regions.addAll(primaryProducerHandler.getBuiltRegions());
        regions.addAll(secondaryProducerHandler.getBuiltRegions());
        return regions;
    }

    public GoodsBag getGoodsBag(Region region) {
        return goodsHandler.getGoodsBagAt(region);
    }

    public List<Region> getAllRegionsWithGoodsBag() {
        return goodsHandler.getAllRegions();
    }

    public Map<Region, Region> getAllRoads() {
        Map<Region, Region> roads = new HashMap<Region, Region>();
        GameTile[][] tiles = gameMap.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                Iterator<Region> regions = tiles[i][j].getMyRegions();
                while (regions.hasNext()) {
                    Region region1 = regions.next();
                    for (Region region2 : region1.getRegionSet().getRoadRegions()) {
                        roads.put(region1, region2);
                        roads.put(region2, region1);
                    }
                }
            }
        }
        return roads;
    }

    public Map<Region, Region> getAllBridges() {
        Map<Region, Region> bridges = new HashMap<Region, Region>();
        GameTile[][] tiles = gameMap.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                Iterator<Region> regions = tiles[i][j].getMyRegions();
                while (regions.hasNext()) {
                    Region region1 = regions.next();
                    for (Region region2 : region1.getRegionSet().getBridgeRegions()) {
                        bridges.put(region1, region2);
                        bridges.put(region2, region1);
                    }
                }
            }
        }
        return bridges;
    }

    public Map<GameTile, Map<GameTile, Wall>> getAllWalls() {
        Map<GameTile, Map<GameTile, Wall>> walls = new HashMap<GameTile, Map<GameTile, Wall>>();
        Map<Region, Map<Region, Wall>> regionWalls = wallHandler.getAllWalls();
        for (Region region1 : regionWalls.keySet()) {
            for (Region region2 : regionWalls.get(region1).keySet()) {
                Map<GameTile, Wall> wallMap = new HashMap<GameTile, Wall>();
                wallMap.put(region2.getParentTile(), regionWalls.get(region1).get(region2));
                walls.put(region1.getParentTile(), wallMap);
            }
        }
        return walls;
    }

    /**
     * TODO: to be implemented, given a tile and a list of vertices return a region
     * @return
     */
    public Region getRegion(GameTile tile, HexaVertex vertex){
        return  tile.getRegionMap().getRegionAt(vertex);
    }

    /**
     * given a transporter return its carriable depending on the phase it is in
     * @param transporter
     * @return
     */
    public CarriableIterator getTransporterCarriable(Transporter transporter){
        ArrayList<Carriable> myShit = transporter.getCarriables();

        return new CarriableIterator(myShit);
    }

    /**
     * given a region return its carriable depending on the phase
     * @param region
     * @return
     */
    public CarriableIterator getRegionCarriable(Region region){
        ArrayList<Carriable> myShit = new ArrayList<>();

        myShit.addAll(transporterHandler.getTransportersAt(region));
        myShit.addAll(goodsHandler.getGoodsBagAt(region).getGoods());

        return new CarriableIterator(myShit);
    }

    public void addCarriableToUserRequest(Transporter t, Carriable c) {
        userRequestHandler.addCarriable(t.getGoodsBag(), c);
    }

    
    public void addCarriableToUserRequest(Region r, Carriable c) {
        userRequestHandler.addCarriable(goodsHandler.getGoodsBagAt(r), c);
    }

    public void resetUserRequest() {
        userRequestHandler.reset();
    }


    public void generateBridge(Region start, Region end){
        //Implementation goes Here
        System.out.println("Create Bridge");
    }
    public void generateRoad(Region start, Region end) {
        GoodsBag cost = new GoodsBag();
        cost.addStone(new Stone());
        ProducerRequest requiredInputs = new ProducerRequest(cost, null);
        UserRequest userInputs = userRequestHandler.getUserRequest();
        if (userInputs.contains(requiredInputs)) {
            if (start.getRegionSet().getLandRegions().contains(end)) {
                if (end.getRegionSet().getLandRegions().contains(start)) {
                    start.getRegionSet().removeLandRegion(end);
                    start.getRegionSet().addRoadRegion(end);
                    end.getRegionSet().removeLandRegion(start);
                    end.getRegionSet().addRoadRegion(start);
                }
            }
        }
    }

    public List<Carriable> getUserRequestCarriables() {
        return userRequestHandler.getCarriables();
    }

    public void produce(Region r) {
        SecondaryTransporterProducer stp = secondaryProducerHandler.getTransporterProducerAt(r);
        SecondaryGoodProducer sgp = secondaryProducerHandler.getSecondaryProducerAt(r);
        if (stp != null) {
            stp.produce(userRequestHandler.getUserRequest());
        }
        else if (sgp != null) {
            sgp.produce(userRequestHandler.getUserRequest());
        }
    }

    public List<BuildAbility> getBuildAbilities(Region r) {
        //TODO add current player
        return r.getBuildAbilities(null);
    }

    public void activateBuildAbility(Region r, BuildAbility ba) {
        ba.build(userRequestHandler.getUserRequest(), r);
    }

    public void buildWall(Region r, HexaIndex direction) {
        System.out.println(r + " " + direction);
    }

    public void init() {
        startGame();
    }
}
