/**
 * 
 */
package tkom.controller;

import tkom.common.events.MyEvent;

/**
 * @author pk
 *
 */
abstract class Strategy
{
	public abstract void perform(MyEvent event);
}
