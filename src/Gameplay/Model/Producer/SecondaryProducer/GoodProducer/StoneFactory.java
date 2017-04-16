package Gameplay.Model.Producer.SecondaryProducer.GoodProducer;

import Gameplay.Model.Goods.Clay;
import Gameplay.Model.Goods.GoodsBag;
import Gameplay.Model.Goods.Stone;
import Gameplay.Model.Producer.ProducerRequest;
import Gameplay.Model.Producer.UserRequest;
import Gameplay.Model.Visitors.ProducerVisitor;

/**
 * Created by Willie on 4/15/2017.
 */
public class StoneFactory extends SecondaryGoodFactory {

    private ProducerRequest input;

    public StoneFactory() {
        generateInput();
    }

    private void generateInput() {
        GoodsBag goods = new GoodsBag();
        goods.addClay(new Clay());
        input = new ProducerRequest(goods, null);
    }

    private GoodsBag generateOutputs() {
        GoodsBag goods = new GoodsBag();
        goods.addStone(new Stone());
        goods.addStone(new Stone());
        return goods;
    }

    @Override
    public void accept(ProducerVisitor pv) {
        pv.visitStoneFactory(this);
    }

    @Override
    public GoodsBag produce(UserRequest ur) {
        if (!ur.contains(input))
            return new GoodsBag();
        else {
            ur.removeUsed(input);
            ur.reset();
            return generateOutputs();
        }
    }
}
