package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import model.EEG;
import service.Analyzer;
import service.MusicPlayer;

public class MainView extends ApplicationFrame {

	private String TITLE = "Waves";
	private static final float MINMAX = 100;
	private static final int COUNT = 2 * 60;
	DynamicTimeSeriesCollection wavesDataset;
	DynamicTimeSeriesCollection eegDataset;
	private Timer timer;
	JLabel blinkLabel;
	JLabel warningLabel;
	JLabel clenchLabel;
	JPanel warningPanel;
	JButton resetButton;
	JTextArea logArea;
	private int eegAcceptCounter = 0;
	Analyzer[] analyzer = new Analyzer[4];
	public static Timer warningTimer;

	public MainView(final String title) {
		super(title);

		analyzer[0] = new Analyzer();
		analyzer[1] = new Analyzer();
		analyzer[2] = new Analyzer();
		analyzer[3] = new Analyzer();

		init();
		initWarning();
		addWaveChannels();
		addEegChannels();
		JFreeChart wavesChart = createChart(wavesDataset);
		JFreeChart eegChart = createChart(eegDataset);

		this.setLayout(new GridLayout(2, 1));

		JPanel southPanel = secondaryEvents();

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JPanel panelEegChart = new JPanel(new BorderLayout());
		panelEegChart.add(new ChartPanel(eegChart), BorderLayout.CENTER);
		JPanel panelWavesChart = new JPanel(new BorderLayout());
		panelWavesChart.add(new ChartPanel(wavesChart), BorderLayout.CENTER);

		addTab(tabbedPane, "Waves", panelWavesChart);
		addTab(tabbedPane, "EEG", panelEegChart);

		this.add(tabbedPane);
		this.add(southPanel);
	}

	void initWarning() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				warningPanel.setVisible(true);
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				Date date = new Date();
				addWarningLog(formatter.format(date).toString() + " SEIZURE DETECTION");
			}
		};
		warningTimer = new Timer(200, taskPerformer);
		warningTimer.setRepeats(false);
	}

	void addTab(JTabbedPane tabbedPane, String label, JPanel panel) {
		tabbedPane.addTab(label, null, panel, label);
	}

	private JPanel secondaryEvents() {
		blinkLabel = new JLabel();
		javax.swing.ImageIcon imgBlink = new javax.swing.ImageIcon(getClass().getResource("/open_eyes.jpg"));
		blinkLabel.setIcon(imgBlink);
		blinkLabel.setVerticalAlignment(JLabel.CENTER);
		blinkLabel.setHorizontalAlignment(JLabel.CENTER);

		warningLabel = new JLabel();
		javax.swing.ImageIcon imgWarning = new javax.swing.ImageIcon(getClass().getResource("/warning.png"));
		warningLabel.setIcon(imgWarning);
		warningLabel.setVerticalAlignment(JLabel.CENTER);
		warningLabel.setHorizontalAlignment(JLabel.CENTER);

		warningPanel = new JPanel(new BorderLayout());
		warningPanel.add(warningLabel, BorderLayout.CENTER);
		warningPanel.setVisible(false);

		clenchLabel = new JLabel();
		javax.swing.ImageIcon imgClench = new javax.swing.ImageIcon(getClass().getResource("/not_clench.jpg"));
		clenchLabel.setIcon(imgClench);
		clenchLabel.setVerticalAlignment(JLabel.CENTER);
		clenchLabel.setHorizontalAlignment(JLabel.CENTER);

		JPanel southPanel = new JPanel(new GridLayout(1, 3));
		southPanel.setBackground(Color.WHITE);
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//		JPanel leftPanel = new JPanel();
		JPanel facialEventPanel = new JPanel(new GridLayout(2, 1));
		
//		leftPanel.add(tabbedPane);
//		leftPanel.add(southPanel);
		
		
		facialEventPanel.add(blinkLabel);
		facialEventPanel.add(clenchLabel);
		JPanel logPanel = new JPanel(new BorderLayout());
		logArea = new JTextArea("Warning Log:");
		logArea.setEditable(false);
		logArea.setFont(logArea.getFont().deriveFont(25f));
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logPanel.add(scrollPane, BorderLayout.CENTER);
		JPanel resetPanel = new JPanel(new GridLayout(2, 3));
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				MusicPlayer.reset();
				warningPanel.setVisible(false);
			}
		});
		resetPanel.add(new JPanel());
		resetPanel.add(resetButton);
		resetPanel.add(new JPanel());
		resetPanel.add(new JPanel());
		resetPanel.add(new JPanel());
		resetPanel.add(new JPanel());
		logPanel.add(resetPanel, BorderLayout.SOUTH);
		southPanel.add(facialEventPanel);
		southPanel.add(warningPanel);
		southPanel.add(logPanel);
		return southPanel;
	}

	private void addWarningLog(String warning) {
		logArea.append("\n" + warning);
	}

	private void addWaveChannels() {
		// create flat line on start
		wavesDataset.addSeries(new float[0], 0, "Alpha");
		wavesDataset.addSeries(new float[0], 1, "Beta");
		wavesDataset.addSeries(new float[0], 2, "Gamma");
		wavesDataset.addSeries(new float[0], 3, "Delta");
	}

	private void addEegChannels() {
		eegDataset.addSeries(new float[0], 0, "EEG_1");
		eegDataset.addSeries(new float[0], 1, "EEG_2");
		eegDataset.addSeries(new float[0], 2, "EEG_3");
		eegDataset.addSeries(new float[0], 3, "EEG_4");
	}

	private void init() {
		wavesDataset = new DynamicTimeSeriesCollection(4, COUNT, new Second());
		wavesDataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));

		eegDataset = new DynamicTimeSeriesCollection(4, COUNT, new Second());
		eegDataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
	}

	public void appendEEGData(EEG[] eeg) {
		eegAcceptCounter = (eegAcceptCounter + 1) % 15;
		if (eegAcceptCounter != 0)
			return;

		for (int i = 0; i < eeg.length; i++) {
			analyzer[i].addData(eeg[i].getData());
		}

		float[] newData = new float[eeg.length];

		newData[0] = (float) eeg[0].getData() + 50;
		newData[1] = (float) eeg[1].getData();
		newData[2] = (float) eeg[2].getData() - 50;
		newData[3] = (float) eeg[3].getData() - 100;

		eegDataset.advanceTime();
		eegDataset.appendData(newData);
	}

	public void activateFacialGesture(String event) {
		if (event.equals("blink")) {
			highlightBlink(blinkLabel);
		} else if (event.equals("clench")) {
			highlightClench(clenchLabel);
		}
	}

	private void highlightBlink(final JLabel label) {
		javax.swing.ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/closed_eyes.jpg"));
		label.setIcon(img);
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				javax.swing.ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/open_eyes.jpg"));
				label.setIcon(img);
			}
		};
		Timer timer = new Timer(200, taskPerformer);
		timer.setRepeats(false);
		timer.start();
	}

	private void highlightClench(final JLabel label) {
		javax.swing.ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/clench.jpg"));
		label.setIcon(img);
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				javax.swing.ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/not_clench.jpg"));
				label.setIcon(img);
			}
		};
		Timer timer = new Timer(200, taskPerformer);
		timer.setRepeats(false);
		timer.start();
	}

	public void appendWavesData(EEG[] eeg) {
		float[] newData = new float[eeg.length];

		newData[0] = (float) eeg[0].getData() + 50;
		newData[1] = (float) eeg[1].getData();
		newData[2] = (float) eeg[2].getData() - 50;
		newData[3] = (float) eeg[3].getData() - 100;

		wavesDataset.advanceTime();
		wavesDataset.appendData(newData);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(TITLE, "", "", dataset, true, true, false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		domain.setVisible(false);
		ValueAxis range = plot.getRangeAxis();
		range.setRange(-MINMAX, MINMAX);
		TITLE = "EEG";
		return result;
	}

	public void start() {
		timer.start();
	}
}