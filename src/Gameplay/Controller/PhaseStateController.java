package Gameplay.Controller;

import Gameplay.Model.Actions.Action;
import Gameplay.Views.MainView.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jordi on 4/13/2017.
 */
public abstract class PhaseStateController  {

    protected void clearPanels(MainView mainView){
        mainView.hideAllPhaseSubViews();
        mainView.dettachAllControllers();
    }

    public void activateController(MainView mainView){
        clearPanels(mainView);
        activateState(mainView);
    }

    public abstract void activateState(MainView mainView);

}
