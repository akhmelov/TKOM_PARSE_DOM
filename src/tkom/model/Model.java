package tkom.model;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import tkom.model.parse.Parse;
import tkom.model.parse.Tag;

/**
 * @author pk
 *
 *Model MVC
 *
 */
public class Model
{ 
	private Parse parse;
	
	public Model()
	{
		
	}
	
	/**
	 * Uruchamiamy analizator tekstu
	 * 
	 * @param url adress strony ktora skanujemy
	 * 
	 * @return zwraca wyprowadzone drzewo rozbioru struktury DOM
	 */
	public JTree startParse(final String url)
	{ 
		parse = new Parse(url);
		return parse.getJTree();
	}
	/**
	 * 
	 * 
	 * @param treePath sciezka do wezla ktory chcemy uzyskac
	 * @return to co zamiera ten element
	 */
	public String getNodeTree(final TreePath treePath)
	{
		ShowHTMLTreeString showHTMLTreeString = new ShowHTMLTreeString(treePath);
		return showHTMLTreeString.run();
	}
}
