package model;

import java.util.ArrayList;

public class DataTransfer {
	
	ArrayList<EEG> eegData = new ArrayList<EEG>();
	
	public void add(EEG eeg) {
		eegData.add(eeg);
	}
	
	public void remove() {
		eegData.clear();
	}

}
