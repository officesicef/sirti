package service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.Timer;

public class Analyzer {

	Queue<Double> data = new LinkedList<Double>();
	int abnormalityCounter = 0;
	int disconnectedCounter = 0;
	boolean isInitialized = false;
	public static final double CRITICAL_VALUE = 2.0;
	public static final int TIMER_COUNT_TIME = 5000;
	public static final float ALARMING_PERCENTAGE = 0.5f;
	public static final double START = 17.0;
	Timer timer;
	
	public Analyzer()
	{
		ActionListener taskPerformer = new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	isInitialized = true;
	        }
		};

		timer = new Timer(TIMER_COUNT_TIME ,taskPerformer);
		timer.setRepeats(false);
		timer.start();
	}
	
	public void addData(double value)
	{
		data.add(value);
		
		if(value == 0)
		{
			disconnectedCounter++;
		}
		else
		{
			disconnectedCounter--;
		}
		
		if(value >= CRITICAL_VALUE + START || value <= START - CRITICAL_VALUE)
		{
			abnormalityCounter++;
		}
		
		if(isInitialized)
		{
	    	if(abnormalityCounter >= data.size() * ALARMING_PERCENTAGE)
	    	{
	    		RaiseAlarm();
	    	}
	    	
			RemoveData();
		}
	}
	
	private void RemoveData()
	{
		if(disconnectedCounter == data.size())
		{
			RaiseAlarm();
		}
		
		double tmp = data.peek();
		if(tmp >= CRITICAL_VALUE + START || tmp <= START - CRITICAL_VALUE)
		{
			abnormalityCounter--;
		}
		data.poll();
	}
	
	private void RaiseAlarm()
	{
		MusicPlayer.play();
		
	}
}
