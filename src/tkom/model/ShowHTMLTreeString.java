package tkom.model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import tkom.model.parse.Tag;

/**
 * Slurzy do przetwarzania wezla drzewa do postaci string, ze wszystkimi jego dzieczmi
 * 
 * @author pk
 *
 */
class ShowHTMLTreeString
{
	/**
	 * Wezel ktory bedziemy przetwarzac
	 */
	private DefaultMutableTreeNode node;
	/**
	 * @param treePath sciezka do wezla, ktory bedzie przetwarzany
	 */
	public ShowHTMLTreeString(final TreePath treePath)
	{
		node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
	}
	
	/**
	 * Uruchamia przetwarzanie wezla do postaci tekstowej
	 * 
	 * @return wezel wraz z dzieczmi w postaci tekstowej
	 */
	public String run()
	{
		String ret = "";
		try
		{
			ret = compileChildren(node, 0);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return ret;
		
		//Tag tag = (Tag)node.getUserObject();
		//node.getChildAfter(node);
		//return tag.toString();
	}
	
	/**
	 * Slurzy do przetwarzania podanego node w postac tekstowa oraz w rekurencji wywuluje dla jego dzieci
	 * 
	 * @param defaultMutableTreeNode przetwarza dany wezel
	 * @param level mowi na ktorym poziome zagniezdzenia jest dane wywolanie, zeby ladnie wyswietlic tekst
	 * 
	 * @return zwraca wynik przetwarzania danego wezla oraz jego dzieci
	 */
	private String compileChildren(DefaultMutableTreeNode defaultMutableTreeNode, int level)
	{
		String ret = "";
		String pad = new String(new char[level * 2]).replace('\0', ' '); //odstep od lewej strony
 		Tag tag = (Tag)defaultMutableTreeNode.getUserObject();
		ret += pad + tag.toString() + "\n";
		if(tag.getContent() != null)
			ret += pad + "    " + tag.getContent() + "\n";
		if(defaultMutableTreeNode.getChildCount() > 0)
		{	//czy ten wezel ma dzieci
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getFirstChild();
			while(treeNode != null)
			{
				ret += compileChildren(treeNode, level + 1);
				treeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getChildAfter(treeNode);
			}
		}
		if(tag.isLocking())
		{	//dodajemy do wyswietlenia tag zamykajacy
			ret += pad + "</ ";
			ret += tag.getName();
			ret += " > \n";
		}
		return ret;
	}
}
