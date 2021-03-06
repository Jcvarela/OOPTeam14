package Gameplay.Model.Producer;

import Gameplay.Model.Goods.GoodsBag;
import Gameplay.Model.Transporters.Transporter;

public class ProducerRequest extends Request {

    private GoodsBag goods;
    private Transporter transporter;

    public ProducerRequest(GoodsBag goods, Transporter transporter) {
        this.goods = goods;
        this.transporter = transporter;
    }

    public GoodsBag getGoodsBag() {
        GoodsBag ret = new GoodsBag();
        ret.add(goods);
        return ret;
    }

    public Transporter getTransporter() {
        return transporter;
    }
}
