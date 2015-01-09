/**
 * 
 */
package tkom.common.events;

/**
 * @author pk
 *
 */
public class InputedURLEvent extends MyEvent
{
	private String url;
	
	public InputedURLEvent(final String url)
	{	
		this.url = url;
	}
	
	/**
	 * @return the url
	 */
	public final String getUrl()
	{
		return url;
	}
}
