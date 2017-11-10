package com.anishkatariya.Coursework_2;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class sigmaErrorWindow {
	JFrame errorFrame;
	String text;
	public sigmaErrorWindow(String text) {
		errorFrame = new JFrame("Error");
		this.text=text;
		init();
	}
	public void init() {
		errorFrame.setSize(200,100);
		errorFrame.getContentPane().setLayout(new FlowLayout());
		errorFrame.getContentPane().add(new JLabel(text));
		JButton ok = new JButton("ok");
		ok.addActionListener(new okListener(errorFrame));
		errorFrame.getContentPane().add(ok);
		errorFrame.setVisible(true);
	}
	
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
