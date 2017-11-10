package com.anishkatariya.Coursework_2;

import javax.swing.*;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DisplayGUI {
		
	JFrame mainFrame;
	JPanel mainPanel;
	
	public DisplayGUI() {
		init();
	}
	
	public void init() {
		
		mainFrame = new JFrame("COMP3204: Computer Vission");
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.setSize(850,300);
		
		mainPanel.setLayout(new GridLayout(1,2));
		
		JPanel sigmaPanel = new JPanel();
		sigmaPanel.setLayout(new GridLayout(5,2));
		JTextField sigma1 = new JTextField("2");
		JTextField sigma2 = new JTextField("4");
		sigmaPanel.add(new JLabel("Please Choose Sigma Values"));
		sigmaPanel.add(new JLabel(""));
		sigmaPanel.add(new JLabel("Sigma 1"));
		sigmaPanel.add(sigma1);
		sigmaPanel.add(new JLabel("Sigma 2"));
		sigmaPanel.add(sigma2);
		mainPanel.add(sigmaPanel);
		
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(6,1));
		imagePanel.add(new JLabel("Please select hybrid image pair"));
		imagePanel.add(new JLabel(""));
		ButtonGroup imageButtons = new ButtonGroup();
		 JRadioButton cycles = new JRadioButton("Bicycle-Motorcycle");
		 JRadioButton dogCat = new JRadioButton("Dog-Cat");
		 JRadioButton birdPlane = new JRadioButton("Bird-Plane");
		 JRadioButton marinstein = new JRadioButton("Marlin-Einstein");
		 JRadioButton fishmarine = new JRadioButton("Fish -Submarine");
		 
		 imageButtons.add(cycles);
		 imageButtons.add(dogCat);
		 imageButtons.add(birdPlane);
		 imageButtons.add(marinstein);
		 imageButtons.add(fishmarine);
		 cycles.setSelected(true);
		 imagePanel.add(cycles);
		 imagePanel.add(dogCat);
		 imagePanel.add(birdPlane);
		 imagePanel.add(marinstein);
		 imagePanel.add(fishmarine);
		 
		 JButton submit = new JButton("Submit");
		 submit.addActionListener(new SubmitListener(sigma1,sigma2,cycles,dogCat,birdPlane,marinstein,fishmarine));
		 imagePanel.add(submit);
		 
		 mainPanel.add(imagePanel);
		
		mainFrame.setVisible(true);
		
	}
	
	class SubmitListener implements ActionListener{
		 JRadioButton cycles;
		 JRadioButton dogCat;
		 JRadioButton birdPlane;
		 JRadioButton marinstein;
		 JRadioButton fishmarine;
		 JTextField sigma1,sigma2;
		 
		 public SubmitListener(JTextField sigma1,JTextField sigma2,JRadioButton cycles,JRadioButton dogCat,JRadioButton birdPlane,JRadioButton marinstein, JRadioButton fishmarine) {
	 
			 this.cycles=cycles;
			 this.dogCat=dogCat;
			 this.birdPlane = birdPlane;
			 this.marinstein = marinstein;
			 this.fishmarine=fishmarine;
			 this.sigma1=sigma1;
			 this.sigma2=sigma2;
					 
		 }
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int intSigma1 = 0,intSigma2=0;
			MBFImage image1 = null,image2 = null;
			try {
				intSigma1 =Integer.valueOf(sigma1.getText());
				intSigma2 = Integer.valueOf(sigma2.getText());
			}catch(NumberFormatException e) {
				sigmaErrorWindow win = new sigmaErrorWindow("Please enter valid sigma values!");
			}
			
			if(cycles.isSelected()) {
				try {
					image1 = ImageUtilities.readMBF(new File ("data/bicycle.bmp"));
					image2 = ImageUtilities.readMBF(new File ("data/motorcycle.bmp"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (dogCat.isSelected()) {
				try {
					image1 = ImageUtilities.readMBF(new File ("data/cat.bmp"));
					image2 = ImageUtilities.readMBF(new File ("data/dog.bmp"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (birdPlane.isSelected()) {
				try {
					image1 = ImageUtilities.readMBF(new File ("data/bird.bmp"));
					image2 = ImageUtilities.readMBF(new File ("data/plane.bmp"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (marinstein.isSelected()) {
				try {
					image1 = ImageUtilities.readMBF(new File ("data/einstein.bmp"));
					image2 = ImageUtilities.readMBF(new File ("data/marilyn.bmp"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (fishmarine.isSelected()) {
				try {
					image1 = ImageUtilities.readMBF(new File ("data/submarine.bmp"));
					image2 = ImageUtilities.readMBF(new File ("data/fish.bmp"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
			JFrame imageFrame = new JFrame();
			imageFrame = DisplayUtilities.createNamedWindow("","Resized Images",true);
			HybridImageCreator hb = new HybridImageCreator();
			hb.setSigma1(intSigma1);
			hb.setSigma2(intSigma2);
			hb.setImage1(image1);
			hb.setImage2(image2);
			hb.createHybridImage();
			MBFImage scaled = hb.getReSizedImages();
			DisplayUtilities.display(scaled, imageFrame);
		
		}

	}
}