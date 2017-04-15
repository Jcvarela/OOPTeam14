package Gameplay.Model.Producer.SecondaryProducer.GoodProducer;

import Gameplay.Model.Producer.ProducerRequest;
import Gameplay.Model.Producer.SecondaryProducer.SecondaryProducer;
import Gameplay.Model.Producer.UserRequest;
import Gameplay.Model.Visitors.ProducerVisitor;

/**
 * Created by Willie on 4/15/2017.
 */
public class Mint extends SecondaryProducer {
    @Override
    public void accept(ProducerVisitor pv) {
        pv.visitMint(this);
    }

    @Override
    public ProducerRequest produce(UserRequest ur) {
        return null;
    }
}
