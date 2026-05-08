package smarthome.controller;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayerInGUI {

    private Clip clip;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private int pausePosition = 0;

    public void load(String path) {

        try {

            File soundFile = new File(path);

            if (!soundFile.exists()) {
                System.out.println("Sound file not found.");
                return;
            }

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(soundFile);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {

        if (clip == null) return;

        // resume from pause
        if (isPaused) {
            clip.setMicrosecondPosition(pausePosition);
        }

        clip.start();
        isPlaying = true;
        isPaused = false;
    }

    public void loop() {

        if (clip == null) return;

        if (isPaused) {
            clip.setMicrosecondPosition(pausePosition);
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        isPlaying = true;
        isPaused = false;
    }

    public void pause() {

        if (clip != null && isPlaying) {

            pausePosition = (int) clip.getMicrosecondPosition();

            clip.stop();

            isPaused = true;
            isPlaying = false;
        }
    }

    public void stop() {

        if (clip != null) {

            clip.stop();
            clip.setMicrosecondPosition(0);

            isPlaying = false;
            isPaused = false;
            pausePosition = 0;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }
}