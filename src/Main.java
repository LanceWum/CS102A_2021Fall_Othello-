import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import view.GameFrame;
import view.MusicPlay;
import view.StartFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame(600);
            mainFrame.setVisible(false);
            MusicPlay.playBGMusic();
        });
    }
}
