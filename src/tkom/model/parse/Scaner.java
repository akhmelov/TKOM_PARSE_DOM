/**
 * 
 */
package tkom.model.parse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import tkom.model.parse.token.Token;

/**
 * Skanuje strumien wejsciowy i zwraca tokeny dalej do Parsera
 * 
 * @author pk
 *
 */
class Scaner
{
	/**
	 * W jakim trybie pracuje klasa, jakiego typu takiet tekstowy musi obecnie zwracac
	 * 
	 * @author pk
	 *
	 */
	enum Mode {CONTENT, VALUE, NAME}
	/**
	 * Stumien wejsciowy
	 */
	private InputStreamReader in;
	/**
	 * Pamieta ostatni symbol w strumieniu
	 */
	private String lastSymbolInStream;
	/**
	 * Czy mamy koniec strumienia
	 */
	private boolean isEndOfStream = false;
	/**
	 * Otwiera strumien url do przetworzenia i odczytuje symbol po symbolu
	 * 
	 * @param urlPath adres przetwarzanej strony
	 */
	public Scaner(final String urlPath)
	{			
		//otwieramy strumien 
        URL url;
		try
		{
			url = new URL(urlPath);
	        in = new InputStreamReader(url.openStream());
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		do	//poszukiwanie pierwszego tagu w dokumecie i pomijanie doctype
		{
			takeNextCharacter(false);	//poszukiwanie prierwszego symbolu, tokenu
		}while(!lastSymbolInStream.equals("<"));
	}
	
	/**
	 * Zwraca tokeny ze strumienia, jedno wywolanie jedyn zwrocony token
	 * 
	 * @param mode jaki tryb pracy skaner
	 * @return zwracany token, null - strumien sie skonczyl
	 */
	public Token getToken(final Mode mode)
	{
		
		if(Mode.CONTENT.equals(mode))
		{
			return getTokenModeContent();
		}
		else if(Mode.NAME.equals(mode))
		{
			return getTokenModeName();
		}
		else if(Mode.VALUE.equals(mode))
		{
			return getTokenModeValue();
		}
		else
		{
			return null;
		}
	}

	/**
	 * F-cja ktora szuka token kiedy skaner jest w trybie wyszukiwania tresci
	 * 
	 * @return zwraca znaleziony token, null gdy jest koniec buforu
	 */
	private Token getTokenModeContent()
	{
		if(isEndOfStream)
		{
			return null;
		}
		
		String content = "";
		if(lastSymbolInStream.equals("<"))
		{	//tu nie jest tresc interpretujemy dalej
			takeNextCharacter(false);	//ustawiamy nowy symbol koncowy
			if(lastSymbolInStream.equals("/"))
			{	//to jest token zlozony
				takeNextCharacter(false);	//przepuszczamy symbol / z lastSYmbol
				return new Token(3, "</");
			}
			else
			{	//to jest pierwszego rodzaju token
				return new Token(0, "<");
			}
		}
		while(!lastSymbolInStream.equals("<"))
		{	//to jest tresc
			content += lastSymbolInStream;
			takeNextCharacter(true);
		}
		return new Token(7, content);
	}
	
	/**
	 * F-cja ktora dziala kiedy skaner jest w trybie pracy wyszukiwania nazwy
	 * 
	 * @return zwraca znaleziony token, null - gdy jest koniec strumienia
	 */
	private Token getTokenModeName()
	{
		if(isEndOfStream)
		{
			return null;
		}
		
		String content = "";
		if(lastSymbolInStream.equals("/"))
		{
			takeNextCharacter(false);
			if(lastSymbolInStream.equals(">"))
			{
				takeNextCharacter(false);
				return new Token(1, "/>");
			}
			else
			{	//to nie jest token, ale czesc tresc, idziemy do tresci uzupelniajac ja
				content += "/";
			}
		}
		else if(lastSymbolInStream.equals("="))
		{
			takeNextCharacter(false);
			return new Token(2, "=");
		}
		else if(lastSymbolInStream.equals(">"))
		{
			takeNextCharacter(false);
			return new Token(6, ">");
		}
		char c = lastSymbolInStream.toCharArray()[0];
		while(!Character.isWhitespace(c))
		{	//to jest tresc
			content += lastSymbolInStream;
			takeNextCharacter(true);
			c = lastSymbolInStream.toCharArray()[0];
			if(lastSymbolInStream.equals("/") || lastSymbolInStream.equals(">") || lastSymbolInStream.equals("="))
			{	//koniec tokena name
				if(lastSymbolInStream.equals("/"))
				{
					takeNextCharacter(false);
					if(!lastSymbolInStream.equals(">"))
					{
						content += "/";
						continue;
					}
					//TODO 
				}
				return new Token(8, content);
			}
		}
		takeNextCharacter(false);	//znajdujemy nastepny symbol po spacji
		return new Token(8, content);
	}
	
	/**
	 * @return zwraca znaleziony token, null - gdy jest koniec strumienia
	 */
	private Token getTokenModeValue()
	{
		if(isEndOfStream)
		{
			return null;
		}
		
		String content = "";
		if(lastSymbolInStream.equals("/"))
		{
			takeNextCharacter(false);
			if(lastSymbolInStream.equals(">"))
			{
				return new Token(1, "/>");
			}
			else
			{	//to nie jest token, ale czesc tresc, idziemy do tresci uzupelniajac ja
				content += "/";
			}
		}
		else if(lastSymbolInStream.equals("\""))
		{
			takeNextCharacter(false);
			while(!lastSymbolInStream.equals("\""))
			{	//to jest tresc
				content += lastSymbolInStream;
				takeNextCharacter(true);
			}
			takeNextCharacter(false);	//przepuszczamy "
			return new Token(9, content);
		}
		else if(lastSymbolInStream.equals("'"))
		{
			takeNextCharacter(false);
			while(!lastSymbolInStream.equals("'"))
			{	//to jest tresc
				content += lastSymbolInStream;
				takeNextCharacter(true);
			}
			takeNextCharacter(false);	//przepuszczamy '
			return new Token(9, content);
		}
		else if(lastSymbolInStream.equals(">"))
		{
			takeNextCharacter(false);
			return new Token(6, ">");
		}
		
		char c = lastSymbolInStream.toCharArray()[0];
		while(!Character.isWhitespace(c))
		{	//to jest nazwa, tutaj jestesmy gdy wartosc byla bez cudzyslowia " lub ' 
			content += lastSymbolInStream;
			takeNextCharacter(true);
			c = lastSymbolInStream.toCharArray()[0];
			if(lastSymbolInStream.equals("/") || lastSymbolInStream.equals(">"))
			{	//koniec tokena name
				if(lastSymbolInStream.equals("/"))
				{
					takeNextCharacter(false);
					if(!lastSymbolInStream.equals(">"))
					{
						content += "/";
						continue;
					}
					//TODO 
				}
				return new Token(9, content);
			}
		}
		takeNextCharacter(false);	//znajdujemy nastepny symbol po spacji
		return new Token(9, content);
	}
	/**
	 * Odczytujemy nastepny symbol ze stumienia
	 * 
	 * @param isWhiteReturn czy zwracamy symbole biale
	 * 
	 * @return czy jest koniec strumieniu
	 */
	public boolean takeNextCharacter(final boolean isWhiteReturn)
	{
		int c;
		try
		{
			while((c = in.read()) != -1) 
			{
				String tmpStr = String.valueOf((char) c);
				if(!isWhiteReturn && Character.isWhitespace((char) c))	//pomijamy biale znaki
					continue;
				lastSymbolInStream = tmpStr;
				break;
			}
			if(c != -1)
			{	//nie nie ma konca pliku
				isEndOfStream = false;
				return false;	
			}
			else
			{	//tak koniec pliku
				isEndOfStream = true;
				return true;
			}
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}	
}
