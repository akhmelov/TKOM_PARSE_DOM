/**
 * 
 */
package tkom.controller;

import tkom.common.events.MyEvent;
import tkom.common.events.SelectedNodeOfTreeEvent;
import tkom.model.Model;
import tkom.view.View;

/**
 * @author pk
 *
 */
public class SelectedNodeOfTreeStrategy extends Strategy
{
	private final Model model;
	private final View view;
	/**
	 * 
	 */
	public SelectedNodeOfTreeStrategy(final Model model, final View view)
	{
		this.model = model;
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see tkom.controller.Strategy#perform(tkom.common.events.MyEvent)
	 */
	@Override
	public void perform(MyEvent event)
	{
		SelectedNodeOfTreeEvent selectedNodeOfTreeEvent = (SelectedNodeOfTreeEvent)event;
		view.showNodeContentOnTree(model.getNodeTree(selectedNodeOfTreeEvent.getTreePath()));
	}

}
