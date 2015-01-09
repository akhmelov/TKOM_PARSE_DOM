/**
 * 
 */
package tkom.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tkom.common.events.InputedURLEvent;
import tkom.common.events.MyEvent;

/**
 * @author pk
 *
 */
class InputURL extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InputURL(final BlockingQueue<MyEvent> blockingQueue)
	{	
		JPanel mainJPanel = new JPanel();	//towrzymy glowna panel
		this.setLayout(new BorderLayout());
			JLabel inputURL = new JLabel("Wprowadz adress strony internetowej URL");	//Komunikat co trzeba wpisac 
			final JTextField inputURLField = new JTextField(25);	//tworzymy pole do wprowadzenia nazwy strony
			JButton okJButton = new JButton("OK");
			
			okJButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					InputedURLEvent event = new InputedURLEvent(inputURLField.getText());
					blockingQueue.add(event);
					inputURLField.setText("ok");
				}
			});
			
			mainJPanel.add(inputURL);
			mainJPanel.add(inputURLField);
			mainJPanel.add(okJButton);
		this.add(mainJPanel);	
		
		this.setVisible(true); 	//robimy okno widoczne
	}

}
