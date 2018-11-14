package view;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.jfree.ui.RefineryUtilities;

import model.EEG;

public class DisplayData extends JFrame {

	private Timer timer;
	MainView mainView;

	public DisplayData() {
		mainView = new MainView("Safe&Sound");
		mainView.pack();
		RefineryUtilities.centerFrameOnScreen(mainView);
		mainView.setVisible(true);
	}

	public void start() {
		timer.start();
	}

	public void displayWavesData(EEG[] eeg) {
		mainView.appendWavesData(eeg);
	}
	
	public void displayEEGData(EEG[] eeg) {
		mainView.appendEEGData(eeg);
	}

	public void displayBlink() {
		mainView.activateFacialGesture("blink");
	}

	public void displayClench() {
		mainView.activateFacialGesture("clench");
	}
}
