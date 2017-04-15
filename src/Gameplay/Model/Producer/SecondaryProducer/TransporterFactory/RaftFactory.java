package Gameplay.Model.Producer.SecondaryProducer.TransporterFactory;

import Gameplay.Model.Goods.GoodsBag;
import Gameplay.Model.Goods.Trunk;
import Gameplay.Model.Producer.ProducerRequest;
import Gameplay.Model.Producer.SecondaryProducer.SecondaryProducer;
import Gameplay.Model.Producer.UserRequest;
import Gameplay.Model.Visitors.ProducerVisitor;

/**
 * Created by Willie on 4/15/2017.
 */
public class RaftFactory extends SecondaryProducer {

    private ProducerRequest input;

    public RaftFactory() {
        generateInput();
    }

    private void generateInput() {
        GoodsBag goods = new GoodsBag();
        goods.addTrunk(new Trunk());
        goods.addTrunk(new Trunk());
        input = new ProducerRequest(goods, null);
    }

    @Override
    public void accept(ProducerVisitor pv) {
        pv.visitRaftFactory(this);
    }

    @Override
    public ProducerRequest produce(UserRequest ur) {
        return null;
    }
}
