package Gameplay.Controller.SubControllers.TransporterCarriableControllers;

import Gameplay.Controller.MainViewController;
import Gameplay.Model.Utility.GameModelFacade;
import Gameplay.Views.MainView.MainView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by jordi on 4/17/2017.
 */
public class BuildingController implements KeyListener, MainViewController {
    UserRequestController userRequestController = new UserRequestController();
    MainView mainView;
    boolean isPSVOn = false;
    boolean isWSVOn = false;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_B && mainView!= null){
            isPSVOn = !isPSVOn;
            mainView.getProducerSelectionView().setVisible(isPSVOn);
            mainView.getProducerSelectionView().updateProducerImages(GameModelFacade.getInstance().getBuildAbilities(userRequestController.getRegion()));
        }
        if (e.getKeyCode() == KeyEvent.VK_W && mainView!= null){
            isWSVOn = !isWSVOn;
            mainView.getWallSelectionView().setVisible(isWSVOn);
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void deactivateController() {

    }

    @Override
    public void activateController(MainView mainView) {
        this.mainView= mainView;
        userRequestController.activateController(mainView);
        mainView.getInputSelectionView().setVisible(true);
        mainView.getDisplay().setFocusable(true);
        mainView.getDisplay().requestFocus();
        mainView.getDisplay().addKeyListener(this);
    }
}