package tkom.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tkom.common.events.MyEvent;

class WaitingPerforming extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WaitingPerforming(final BlockingQueue<MyEvent> blockingQueue, final String status)
	{	
		JPanel mainJPanel = new JPanel();	//towrzymy glowna panel
			JLabel inputURL = new JLabel(status);	//Komunikat co sie dzieje 
			
			mainJPanel.add(inputURL);
		this.add(mainJPanel);	
		
		this.setVisible(true); 	//robimy okno widoczne
	}
}
