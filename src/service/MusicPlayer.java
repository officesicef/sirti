package service;

import view.MainView;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {

	public static boolean isPlaying = false;
	static Clip clip;

	public static void play() {
		if (isPlaying)
			return;
		isPlaying = true;
		MainView.warningTimer.start();
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(MusicPlayer.class.getResource("/sound.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static void reset() {
		isPlaying = false;
		clip.stop();
	}
}
