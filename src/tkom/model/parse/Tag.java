/**
 * 
 */
package tkom.model.parse;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import tkom.model.parse.token.Token;

/**
 * Przechowuje zawartosc pojedynczego tagu 
 * np. <div atrribute = value attribute>
 * content
 * 
 * @author pk
 *
 */
public class Tag
{
	/**
	 * Jesto to numer ktory reprezentuje id tagu, ktory z kolei on jest odczytany,
	 * nie jest to id w sensie atrybutu html
	 */
	private int id;
	/**
	 * Nazwa tagu
	 */
	private String name;
	/**
	 * Zawartosc tagu
	 */
	private String content;
	/**
	 * Przechowuje rodzaj tokenu ktory otwieral ten tag
	 */
	private Token openToken;
	/**
	 * Czy ten tag moze byc zamykajacy
	 * np. </div>, ale tez moze byc </br> - ( </br> wiem ze beldnie, ale jednak)
	 */
	private boolean isCanLocking;
	/**
	 * Przechowuje token ktory ten tag zamykal
	 */
	private Token closeToken;
	/**
	 * Mowi ze tag ma zamykajacy tag
	 */
	private boolean isLocking;
	/**
	 * Przechowuje wartosc 
	 */
	private TreeMap<String, String> attributes;
	/**
	 * Tworzy tag
	 * 
	 * @param openToken jaki token otwiera ten tag
	 */
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String ret = "";
		ret += openToken.getContent();
		ret += " ";
		ret += name;
		if(isLocking)
			ret += "-";
		ret += "  ";
		for(Map.Entry<String, String> entry: attributes.entrySet())
		{	//wypisujemy atrybuty
			ret += entry.getKey();
			if(entry.getValue() != null)
			{
				ret += " = \"";
				ret += entry.getValue();
				ret += "\" ";
			}
		}
		ret += closeToken.getContent();
		ret += " ";
		if(content != null)
		{
			ret += content;
		}
		return ret;
	}
	public Tag(final Token openToken)
	{
		this.openToken = openToken;
		this.attributes = new TreeMap<String, String>(Collections.reverseOrder());
		isLocking = false;
		if(openToken.getNumber() == 3)
		{	//czy moze to byc zamykajacy tag np. </div>, ale tez moze byc </br> - ( </br> wiem ze beldnie, ale jednak)
			this.isCanLocking = true;
		}
		else
		{
			this.isCanLocking = false;
		}
	}
	
	/**
	 * Dodaje nowy atrybut do tagu
	 * 
	 * @param attribute	nazwa atrybutu
	 * @param value wartosc atrybutu, jesli null to znaczy, ze atrybut jest bez wartosci
	 */
	public void addTagAttribute(final String attribute, final String value)
	{
		attributes.put(attribute, value);
	}
	@Override
	public boolean equals(Object obj)
	{
		Tag tagObj = (Tag)obj;
		if(!(name.equals(tagObj.name)))
			return false;
		else if(!(openToken.equals(tagObj.openToken)) || !(closeToken.equals(tagObj.closeToken)))
			return false;
		else if(isCanLocking != tagObj.isCanLocking || !attributes.equals(tagObj.attributes))
			return false;
		else if(content != null && !(content.equals(tagObj.content)) || tagObj.content != null && !tagObj.content.equals(content))
			return false;
		else
			return true;
	}
	/**
	 *	Wystwietla tag na konsole 
	 */
	public void showTag()
	{
		String str = openToken.getContent();
		str += name + " ";
		for(Map.Entry<String, String> ent: attributes.entrySet())
		{
			str += ent.getKey();
			if(ent.getValue() != null)	//gdy istnieje wartosc
				str += " =\" " + ent.getValue() + "\" ";
		}
		str += closeToken.getContent();
		if(content != null)
			str += " " + content;
		System.out.println(str);
	}
	/**
	 * @return the name
	 */
	public final String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public final void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the content
	 */
	public final String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public final void setContent(String content)
	{
		this.content = content;
	}
	/**
	 * @return the openToken
	 */
	public final Token getOpenToken()
	{
		return openToken;
	}
	/**
	 * @param openToken the openToken to set
	 */
	public final void setOpenToken(Token openToken)
	{
		this.openToken = openToken;
	}
	/**
	 * @return the closeToken
	 */
	public final Token getCloseToken()
	{
		return closeToken;
	}
	/**
	 * @param closeToken the closeToken to set
	 */
	public final void setCloseToken(Token closeToken)
	{
		this.closeToken = closeToken;
	}
	/**
	 * @return the attributes
	 */
	public final TreeMap<String, String> getAttributes()
	{
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public final void setAttributes(TreeMap<String, String> attributes)
	{
		this.attributes = attributes;
	}
	/**
	 * @return the isCanLocking
	 */
	public final boolean isCanLocking()
	{
		return isCanLocking;
	}
	/**
	 * @param isCanLocking the isCanLocking to set
	 */
	public final void setCanLocking(boolean isCanLocking)
	{
		this.isCanLocking = isCanLocking;
	}
	/**
	 * @return the isLocking
	 */
	public final boolean isLocking()
	{
		return isLocking;
	}
	/**
	 * @param isLocking the isLocking to set
	 */
	public final void setLocking(boolean isLocking)
	{
		this.isLocking = isLocking;
	}
	/**
	 * @return the id
	 */
	public final int getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public final void setId(int id)
	{
		this.id = id;
	}

}
