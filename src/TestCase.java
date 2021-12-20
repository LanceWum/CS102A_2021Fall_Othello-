import model.GameSystem;
import view.GameFrame;
import view.MusicPlay;

import javax.swing.*;

public class TestCase {
    public static void main(String[] args) {

        GameSystem gs = new GameSystem();
        int result = gs.loadPlayerList();
        System.out.printf("load player list result: %d\n",result);

        result = gs.loadGameList();
        System.out.printf("load game list result: %d\n",result);

        result = gs.loadStepWeights();
        System.out.printf("loadStepWeights result: %d\n",result);


    }
}
