/**
 * 
 */
package tkom.model.parse.token;

/**
 * Tokeny
 * 			0 - <
 * 			1 - />
 * 			2 -	=
 * 			3 - </
 * 			4 - "
 *			5 - '
 *			6 - >
 *			7 - Tresc (transl eng Content)
 *			8 - Name
 *			9 - Value
 * 
 * @author pk
 *
 */
public class Token
{
	/**
	 * Numer z tablicy tokenu
	 */
	private int number;
	/**
	 * Zawartosc tokenu
	 */
	private final String content;
	
	public Token(final int number, final String content)
	{
		this.number = number;
		this.content = content;
	}

	@Override
	public boolean equals(Object obj)
	{
		Token tokenObj = (Token) obj;
		if(number != tokenObj.number)
			return false;
		else if(!(content.equals(tokenObj.content)))
			return false;
		else
			return true;
	}
	/**
	 * @return the number
	 */
	public final int getNumber()
	{
		return number;
	}

	/**
	 * @return the content
	 */
	public final String getContent()
	{
		return content;
	}
	
}
