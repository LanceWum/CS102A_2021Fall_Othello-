package view;

import java.io.File;
import javafx.scene.media.*;

public class MusicPlay {

    static AudioClip bgMusic = new AudioClip(new File("src\\resource\\music\\gaoshanliushui.mp3").toURI().toString());
    static AudioClip stepMusic = new AudioClip(new File("src\\resource\\music\\step.mp3").toURI().toString());

    public static void playBGMusic()
    {
        bgMusic.play();   //开始播放
        //ac.setCycleCount(1000);  //设置循环次数
    }

    public static void stopBGMusic()
    {
        bgMusic.stop();
    }
    public static void playStepMusic()
    {
        //ac = new AudioClip(new File("src\\data\\gaoshanliushui.mp3").toURI().toString());
        //ac.setVolume(50);
        stepMusic.play();   //开始播放
        //ac.setCycleCount(1000);  //设置循环次数
    }
}
