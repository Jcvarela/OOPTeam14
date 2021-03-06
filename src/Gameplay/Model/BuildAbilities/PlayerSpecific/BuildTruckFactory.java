package Gameplay.Model.BuildAbilities.PlayerSpecific;

import Gameplay.Model.BuildAbilities.BuildAbility;
import Gameplay.Model.Goods.Board;
import Gameplay.Model.Goods.GoodsBag;
import Gameplay.Model.Goods.Stone;
import Gameplay.Model.Map.SecondaryProducerHandler;
import Gameplay.Model.Producer.ProducerRequest;
import Gameplay.Model.Producer.SecondaryProducer.TransporterProducer.LandTransporterProducers.TruckProducer;
import Gameplay.Model.Producer.UserRequest;
import Gameplay.Model.Region.Region;
import Gameplay.Model.Utility.PlayerID;
import Gameplay.Model.Visitors.BuildAbilityVisitor;

/**
 * Created by zrgam_000 on 4/16/2017.
 */
public class BuildTruckFactory extends BuildAbility{
    SecondaryProducerHandler transporterProducerHandler;

    public BuildTruckFactory(PlayerID id) {

        super(id);

        transporterProducerHandler = SecondaryProducerHandler.getInstance();

        GoodsBag goods = new GoodsBag();
        goods.addBoard(new Board());
        goods.addBoard(new Board());
        goods.addStone(new Stone());
        goods.addStone(new Stone());

        input = new ProducerRequest(goods, null);
    }


    @Override
    public void build(UserRequest ur, Region region) {
        if(!ur.contains(getInput())){
            return;
        }
        ur.removeUsed(input);
        ur.reset();

        transporterProducerHandler.placeTransporterProducer(new TruckProducer(region), region);
    }

    @Override
    public void accept(BuildAbilityVisitor bav) {
        bav.visitBuildTruckFactory(this);
    }
}
