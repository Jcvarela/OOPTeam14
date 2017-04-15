package Gameplay.Model.Region;

import Gameplay.Model.Visitors.ConnectionGenerator;
import Gameplay.Model.Visitors.LandConnectionGenerator;
import Gameplay.Model.Visitors.RegionVisitor;

/**
 * Created by zrgam_000 on 4/14/2017.
 */
public class LandRegion extends Region{
    @Override
    public void accept(RegionVisitor rv) {
        rv.visitLandRegion(this);
    }

    @Override
    public ConnectionGenerator getConnectionGenerator() {
        return new LandConnectionGenerator(getRegionSet());
    }
}