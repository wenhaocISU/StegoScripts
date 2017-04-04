package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.P;
import common.Settings;
import script.PixelKnot;

public class MainPanel extends JPanel{

	
	private static final long serialVersionUID = 1L;
	
	private JPanel configPanel, displayPanel;
	private JFileChooser fc;
	private File apk;

	public static void main(String[] args)
	{
		initAndShow();
	}

	public static void initAndShow()
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				JFrame frame = new JFrame("Scripts");
				frame.add(new MainPanel());
				frame.setMinimumSize(new Dimension(1200, 800));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
	
	private JButton b_startEmulators, b_loadApp, b_run;
	
	public MainPanel()
	{
		fc = new JFileChooser();
		
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		
		
		configPanel = new JPanel();
		configPanel.setBorder(BorderFactory.createEtchedBorder());
		b_startEmulators = new JButton("Start Emulators");
		b_startEmulators.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				File emFolder = new File(Settings.avdDir);
				for (File em : emFolder.listFiles())
				{
					if (em.isFile() && em.getName().endsWith(".ini"))
					{
						String name = em.getName().substring(0, em.getName().indexOf(".ini"));
						P.exec(Settings.emulatorPath+ " -avd " + name);
						try {
							Thread.sleep(300);
						}
						catch (Exception exc)
						{
							exc.printStackTrace();
						}
					}
				}
			}
		});
		configPanel.add(b_startEmulators);
		b_loadApp = new JButton("Load APK");
		b_loadApp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int rv = fc.showOpenDialog(MainPanel.this);
				if (rv == JFileChooser.APPROVE_OPTION)
				{
					apk = fc.getSelectedFile();
					System.out.println(apk.getAbsolutePath());
				}
			}
		});
		configPanel.add(b_loadApp);
		b_run = new JButton("Run PixelKnot Script");
		b_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PixelKnot.run();
			}
		});
		configPanel.add(b_run);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0.2;
		add(configPanel, c);
		
		
		displayPanel = new JPanel();
		displayPanel.setBorder(BorderFactory.createEtchedBorder());
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.8;
		add(displayPanel, c);
	}
	
}
