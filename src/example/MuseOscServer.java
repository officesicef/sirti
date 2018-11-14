package example;

import model.EEG;
import oscP5.OscMessage;
import oscP5.OscP5;
import view.DisplayData;

public class MuseOscServer {

	static MuseOscServer museOscServer;

	OscP5 museServer;
	static int recvPort = 7000;
	DisplayData displayData = new DisplayData();
	int waveCounter = 0;
	EEG[] waves = new EEG[4];

	public static void main(String[] args) {
		System.out.println("start");
		museOscServer = new MuseOscServer();
		museOscServer.museServer = new OscP5(museOscServer, recvPort);
	}

	void oscEvent(OscMessage msg) {
		sendData(msg);
	}

	void sendData(OscMessage msg) {
		displayWaves(msg);
		displayFacialGestures(msg);
		displayEEG(msg);
	}

	private void displayEEG(OscMessage msg) {
		EEG[] eeg = new EEG[4];
		if (msg.checkAddrPattern("Person0/eeg") == true) {
			for (int i = 0; i < 4; i++) {
				double tmp = msg.get(i).doubleValue() / 50;
				eeg[i] = new EEG(i, tmp);
			}
			
			displayData.displayEEGData(eeg);
		}
	}

	private void displayFacialGestures(OscMessage msg) {
		if (msg.checkAddrPattern("Person0/elements/blink") == true) {
			if (msg.get(0).intValue() == 1) {
				displayData.displayBlink();
			}
		} else if (msg.checkAddrPattern("Person0/elements/jaw_clench") == true) {
			if (msg.get(0).intValue() == 1) {
				displayData.displayClench();
			}
		}

	}

	private void displayWaves(OscMessage msg) {
		if (msg.checkAddrPattern("Person0/elements/alpha_relative") == true) {
			waves[0] = waveMedium(msg);
		} else if (msg.checkAddrPattern("Person0/elements/beta_relative") == true) {
			waves[1] = waveMedium(msg);
		} else if (msg.checkAddrPattern("Person0/elements/gamma_relative") == true) {
			waves[2] = waveMedium(msg);
		} else if (msg.checkAddrPattern("Person0/elements/delta_relative") == true) {
			waves[3] = waveMedium(msg);
		}
		if (waveCounter == 4) {
			displayData.displayWavesData(waves);
			waveCounter = 0;
		}
	}

	private EEG waveMedium(OscMessage msg) {
		double sum = 0;
		for (int i = 0; i < 4; i++) {
			sum += msg.get(i).doubleValue() * 100;
		}
		waveCounter++;
		return new EEG(-1, sum / 4);
	}

	void nod(OscMessage msg) {
		if (msg.checkAddrPattern("Person0/gyro") == true) {
			if (Math.abs(msg.get(1).doubleValue()) > 50) {
				System.out.println("YES");
			}
		}
	}

	void shakeHead(OscMessage msg) {
		if (msg.checkAddrPattern("Person0/gyro") == true) {
			if (Math.abs(msg.get(2).doubleValue()) > 50) {
				System.out.println("NO");
			}
		}
	}

	void blink(OscMessage msg) {
		if (msg.checkAddrPattern("Person0/elements/blink") == true) {
			if (msg.get(0).intValue() == 1) {
				System.out.println(";-)");
			}
		}
	}

	void clench(OscMessage msg) {
		if (msg.checkAddrPattern("Person0/elements/jaw_clench") == true) {
			if (msg.get(0).intValue() == 1) {
				System.out.println(":-]");
			}
		}
	}
}