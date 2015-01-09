/**
 * 
 */
package tkom.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.util.concurrent.BlockingQueue;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import tkom.common.events.MyEvent;
import tkom.common.events.SelectedNodeOfTreeEvent;

/**
 * Wyswietla drzewo rozbioru strumienia HTML
 * 
 * @author pk
 *
 */
class TreeOfHTML extends JPanel
{
	/**
	 * Reprezentuje te czesc okna gdzie jest wyswietlane cale drzewo
	 */
	private ScrollPane treeScrollPane;  
	/**
	 * Reprezentuje te czesc okna gdzie jest wyswietlana informajca o okreslonym wezle
	 */
	private ScrollPane contentScrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Wyswietla drzewo wyprowadzenia podanego pliku oraz zawartosc wybranego wezla
	 * 
	 * @param jTree drzewo ktore bedziemy wyswietlac
	 */
	public TreeOfHTML(final JTree jTree, DefaultMutableTreeNode nodeToShow, final BlockingQueue<MyEvent> blockingQueue)
	{
		contentScrollPane = contentScrollPane(nodeToShow);
		treeScrollPane = new ScrollPane();
		treeScrollPane.add(jTree);
		this.setLayout(new GridLayout(1, 2));
		this.add(treeScrollPane);
		this.add(contentScrollPane);
		
		//obsluga zdarzenia wybrania jakiegos wezla
		jTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e)
			{
				blockingQueue.add(new SelectedNodeOfTreeEvent(e.getPath()));
			}
		});
	}
	
	/**
	 * Zwraca panel zawierajaca zawartosc przekazanego wezla przez parametr
	 * 
	 * @param nodeToShow drzewo o ktorym bedzie informacja w zwracanej panel
	 * @return	zwraca panel, ktora zawiera informacje o przekazanym wezle
	 */
	private ScrollPane contentScrollPane(DefaultMutableTreeNode nodeToShow)
	{
		ScrollPane retScrollPane = new ScrollPane();
		if(nodeToShow == null)
		{	//nic nie pokazujemy
			return retScrollPane;
		}
		retScrollPane.add(new JLabel(nodeToShow.toString()));
		return retScrollPane;
	}
	/**
	 * Ustawia nowa zawartosc panelu, w ktorym jest wypisywana informacja na temat wybranego wezla
	 * 
	 * @param content zawartosc do umiescienia 
	 */
	public void setContentContentScrollPane(final String content)
	{
		contentScrollPane.removeAll();
		JPanel jPanel = new JPanel(new BorderLayout());	//tworzymy panel z zawartoscia
		jPanel.add(new JTextArea(content), BorderLayout.CENTER);
		contentScrollPane.add(jPanel);
		contentScrollPane.invalidate();
		contentScrollPane.validate();
		contentScrollPane.revalidate();
		contentScrollPane.repaint();
	}
}
