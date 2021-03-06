package MapBuilder.Model.Terrain;

import MapBuilder.Model.Visitor.TerrainVisitor;

public class WoodsTerrain extends Terrain {
    @Override
    public void accept(TerrainVisitor v) {
        v.visitWoods(this);
    }
}
