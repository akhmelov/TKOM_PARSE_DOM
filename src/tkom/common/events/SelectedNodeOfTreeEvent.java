/**
 * 
 */
package tkom.common.events;

import javax.swing.tree.TreePath;

/**
 * Reprezentuje zdarzenie wybrania wezla w drzewie jtree, przechowuje sciezke do tego wezla
 * 
 * @author pk
 *
 */
public class SelectedNodeOfTreeEvent extends MyEvent
{
	/**
	 * Sciezka do wybranego wezla
	 */
	final TreePath treePath;
	/**
	 * @param treePath
	 */
	public SelectedNodeOfTreeEvent(final TreePath treePath)
	{
		this.treePath = treePath;
	}
	/**
	 * @return the treePath
	 */
	public final TreePath getTreePath()
	{
		return treePath;
	}
}
