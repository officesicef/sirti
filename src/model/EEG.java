package model;

public class EEG {
	int channel;
	double data;
	
	
	
	public EEG(int channel, double data) {
		this.channel = channel;
		this.data = data;
	}

	public EEG() {
		
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}
	
	

}
