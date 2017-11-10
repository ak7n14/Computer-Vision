package com.anishkatariya.Coursework_2;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
/*
 * Defining a window to display when there is an error with the sigma values entered
 */
public class sigmaErrorWindow {
	JFrame errorFrame;
	String text;
	public sigmaErrorWindow(String text) {
		errorFrame = new JFrame("Error");
		this.text=text;//text to display
		init();
	}
	
	//Initialising the error window
	public void init() {
		errorFrame.setSize(200,100);
		errorFrame.getContentPane().setLayout(new FlowLayout());
		errorFrame.getContentPane().add(new JLabel(text));
		JButton ok = new JButton("ok");
		ok.addActionListener(new okListener(errorFrame));
		errorFrame.getContentPane().add(ok);
		errorFrame.setVisible(true);
	}
	//Adding action listener to ok button to close window.
	class okListener implements ActionListener{
		JFrame frame;
		public okListener(JFrame frame) {
			this.frame=frame;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			
		}
		
	}
}
