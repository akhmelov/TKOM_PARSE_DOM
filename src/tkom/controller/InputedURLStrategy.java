/**
 * 
 */
package tkom.controller;

import javax.swing.JTree;

import tkom.common.events.InputedURLEvent;
import tkom.common.events.MyEvent;
import tkom.model.Model;
import tkom.view.View;

/**
 * @author pk
 *
 *Obsluguje zdarzenie kiedy zostal wprowadzony adress strony internetowej, przekazuje jego do modelu do obslugi
 *
 */
class InputedURLStrategy extends Strategy
{
	private Model model;
	private View view;
	
	/**
	 * Konstruktor inicjalizujacy
	 * 
	 * @param model referencja na model
	 * @param view	referencja na widok
	 */
	public InputedURLStrategy(final Model model, final View view)
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
		view.setWaitingPerformingWindow("Przekazywanie do modelu");
		JTree jTree = model.startParse(((InputedURLEvent)event).getUrl()); 
		view.setTreeOfHTML(jTree, null);
	}

}
