package tkom.view;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import tkom.common.events.MyEvent;

public class View extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BlockingQueue<MyEvent>	blockingQueue;
	/**
	 * Referencja do obecnego okna
	 */
	private JFrame window;
	/**
	 * Panel ktora jest wyswietlana w glownym oknie, zazwyczaj jest to jakis obiekt dziedziczony po JPanel
	 */
	private JPanel addToWindowJPanel;
	/**
	 * rozmiar okna wyswietlanego
	 */
	private Dimension dimensionWindow; 
	public View(BlockingQueue<MyEvent> blockingQueue)
	{
		this.blockingQueue = blockingQueue;
		window = this;	//kod zostal zmodyfikowany i nie w dokumentacji jest uzyto slowo window, wiec zostaje nadal
		
		dimensionWindow = new Dimension(400, 400);
		this.setTitle("Wprowadz nazwe pliku");	//tworzymy kontener typu jFrame
		this.setSize(dimensionWindow);	//ustawiamy rozmiar
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //co robimy z programem po zamjnieciu okna
		this.setVisible(true);
		//this.setLayout(new BorderLayout());
		setInputWindow();
	}
	
	/**
	 * Ustawia okna w ktorym wprowadzamy adres strony ktora chemy przetworzyc
	 */
	public void setInputWindow()
	{
		JPanel tmpJPanel = new InputURL(blockingQueue);
		setNewPanel(tmpJPanel, "Start");
	}
	
	/**
	 *	wyswietla okno kiedy model przetwarza i twodzy drzewo DOM
	 *
	 *@param status jaki jest obecnie status przetwarzania
	 */
	public void setWaitingPerformingWindow(final String status)
	{
		JPanel tmpJPanel =  new WaitingPerforming(blockingQueue, status);
		setNewPanel(tmpJPanel, status);
	}
	/**
	 * ustawia okno ktore pokazuje wyprowadzone drzewo 
	 * 
	 * @param jTree drzewo ktore zostanie wyswietlone uzytkownikowi
	 * @param defaultMutableTreeNode 
	 */
	public void setTreeOfHTML(final JTree jTree, final DefaultMutableTreeNode defaultMutableTreeNode)
	{
		addToWindowJPanel = new TreeOfHTML(jTree, defaultMutableTreeNode, blockingQueue);
		addToWindowJPanel.setPreferredSize(dimensionWindow);
		setNewPanel(addToWindowJPanel, "Struktura DOM");
	}
	/**
	 * Wypisuje w oknie zawartosc wybranego wezla, zawwartosc jest opisywana przez podany parametr
	 * 
	 * @param content to co zostanie wypisane jako zawartosc danego wezla 
	 */
	public void showNodeContentOnTree(final String content)
	{
		TreeOfHTML treeOfHTML = (TreeOfHTML)addToWindowJPanel;
		treeOfHTML.setContentContentScrollPane(content);
	}
	/**
	 * Ustawia nowy widok w oknie na taki jaki zawiera jpanel
	 * 
	 * @param jPanel widok ktory zastapi obecny
	 * @param title	naglowek ktory bedzie wyswietlany na oknie
	 */
	private void setNewPanel(final JPanel jPanel, final String title)
	{
		window.getContentPane().removeAll();
		window.add(jPanel);
		window.setTitle(title);
		window.getContentPane().invalidate();
		window.getContentPane().validate();
		window.getContentPane().revalidate();
		//this.pack(); //TODO dopasowanie rozmiaru panelu do iframe
		window.repaint();
	}
}
