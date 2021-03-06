package Gameplay.Model.Iterators;

import Gameplay.Model.Transporters.Transporter;
import Gameplay.Model.Visitors.Carriable;
import Gameplay.Views.Drawers.CarriableDrawingVisitor;
import Gameplay.Views.Drawers.TransporterDrawingVisitor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CarriableIterator implements StuffIterator {
    private ArrayList<Carriable> carriables;
    int cur, size;

    public CarriableIterator(ArrayList<Carriable> carriables) {
        this.carriables = carriables;
        size = carriables.size();
        cur = 0;
    }

    @Override
    public void first() {
        cur = 0;
    }

    @Override
    public void next() {
        cur++;
        cur %= carriables.size();
    }

    @Override
    public Carriable getCurrent() {
        return carriables.get(cur);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public BufferedImage getImage() {
        CarriableDrawingVisitor t = new CarriableDrawingVisitor();
        getCurrent().accept(t);
        return  t.getImage();
    }

    public Carriable getCarriableAt(int number) {
        if (isInBounds(number)) {
            return carriables.get(number);
        }
        return null;
    }

    private boolean isInBounds(int number) {
        return number >= 0 && number < carriables.size();
    }

    public void deleteAt(int index){
        try {
            carriables.remove(index);
            size--;
        }catch(Exception e){}
    }
}
