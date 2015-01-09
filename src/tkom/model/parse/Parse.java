/**
 * 
 */
package tkom.model.parse;

import javax.swing.JTree;

import tkom.model.parse.Scaner.Mode;
import tkom.model.parse.token.Token;

/**
 * @author pk
 *
 */
public class Parse
{
	/**
	 * Referencja na skaner, ktory zwraca tokeny
	 */
	private Scaner scaner;
	/**
	 *	Przechowuje ostatni toke 
	 */
	private Token lastToken; 
	/**
	 * Stos ktory tworzy drzewo
	 */
	private Stack stack;
	/**
	 * Uruchamia analizacje i rozbior kodu
	 *
	 * @param urlPath adress strony do skanowania
	 */
	public Parse(final String urlPath)
	{
		scaner = new Scaner(urlPath);
		stack = new Stack();
		run();
	}
	
	/**
	 * Przetwarza nasz strumien wejsciowy
	 */
	public void run()
	{
		lastToken = scaner.getToken(Mode.CONTENT);
		while(lastToken != null)
		{
			Tag tag = getTag();
			if(tag.getName().equalsIgnoreCase("script"))
			{	//to jest specjalny rodzaj tagu, a wiec traktujemy jego szczegulnie, copiujemy jego zawartosc
				tag.setContent("");
				while(true)
				{
					if(lastToken.getNumber() == 3)
					{	//czy to jest koniec scruptu
						String tmp = lastToken.getContent();	
						lastToken = scaner.getToken(Mode.NAME);
						if(lastToken.getContent().equalsIgnoreCase("script"))
						{	//czy dany tag jest zamykajacym tagiem skryptu
							tag.setLocking(true);
							stack.addToStack(tag);
							lastToken = scaner.getToken(Mode.NAME);
							lastToken = scaner.getToken(Mode.CONTENT);
							break;
						}
						tag.setContent(tag.getContent() + tmp);	//wstawiamy content takenu, ktory byl przed obecnym 
					}
					tag.setContent(tag.getContent() + lastToken.getContent());	//wstawiamy content obecnego tokenu
					lastToken = scaner.getToken(Mode.CONTENT);
				}
			}
			if(tag != null)
			{	//razbieramy zwykly tag
				if(tag.isCanLocking())
				{
					if(!stack.trySplit(tag))
					{	//nie udalo sie znalesc tagu otwierajacego
						stack.addToStack(tag);
					}
				}
				else
				{
					stack.addToStack(tag);
				}
			}
		}
		//stack.isEnd();
	}
	
	/**
	 * Wyciaga tag
	 * 
	 * @return zwracany tag, null - jakis blad
	 */
	private Tag getTag()
	{
		Tag tag = null;
		if(lastToken.getNumber() == 0 || lastToken.getNumber() == 3)
		{	//przeprowadzamy skaner do stanu odczywywania parametrow tagu
			tag = new Tag(lastToken);
			createTag(tag);
			lastToken = scaner.getToken(Mode.CONTENT);
			if(lastToken == null)
			{	//koniec strumienia, koniec programu
				return tag;
			}
			if(lastToken.getNumber() == 7)
			{	//pobieramy tresc ktora jest poza tagiem, ale przed nastepnym tagiem
				tag.setContent(lastToken.getContent());
				lastToken = scaner.getToken(Mode.CONTENT);
			}
		}
		return tag;
	}
	
	/**
	 * tworzy tag, zwraca jego poprzez referencja, kiedy napotykamy na odpowiedni token
	 * 
	 * @param tag referencja na tag, ktory uzupelniamy
	 */
	private void createTag(Tag tag)
	{
		//tag = new Tag(lastToken); //nie dziala, bo jest zamiana referencji
		getNameTag(tag); //dodanie imie do tagu
		getAttributesTag(tag, null);	//dodajemy atrybuty
		tag.setCloseToken(lastToken);
	}
	
	/**
	 * Dodaje nazwe tagu do obiektu tag, wykonuje to przez referencje
	 * 
	 * @param tag referencja na tag, ktory uzupelniamy
	 */
	private void getNameTag(Tag tag)
	{
		lastToken = scaner.getToken(Mode.NAME);	//przeprowadzamy skaner w inny tryb pracy
		if(lastToken.getNumber() != 8)
		{	//TODO wyjatek, niepoprawna gramytka
			throw new ExceptionInInitializerError("Zamiast tag name dostalismy createTag: " + lastToken.getNumber()
					+ " content: " + lastToken.getContent());
		}
		tag.setName(lastToken.getContent());	//dodajemy imie tagu, nazwa
	}
	
	/**
	 * Dodaje wszystkie atrybuty do obiektu tag przekazanego przez referencje
	 * 
	 * @param tag referencja na tag, ktory uzupelniamy
	 * @param presentToken token ktory bedzie rozwarzala f-cja jako obecny token na wejciu, null bierze ze strumienia
	 */
	private void getAttributesTag(Tag tag, Token presentToken)
	{
		String nameAttribute;
		String valueAttribute = null;
		if(presentToken == null)	//pobieramy nowy token, ktory jest obecnie na wejciu
			lastToken = scaner.getToken(Mode.NAME);
		if(lastToken.getNumber() == 6 || lastToken.getNumber() == 1)
		{	//koniec, dalej nie ma atrybutu
			return;
		}
		if(lastToken.getNumber() != 8)
		{	//TODO wyjatek, niepoprawna gramytka
			throw new ExceptionInInitializerError("Zamiast tag name dostalismy w atrybut: " + 
					lastToken.getNumber() + " content: " + lastToken.getContent());
		}
		nameAttribute = lastToken.getContent();	//pamietamy nazwe atrybutu
		//sprawdzamy czy atrybut posiada wartosc
		lastToken = scaner.getToken(Mode.NAME); //pobieramy nastepny token
		if(lastToken.getNumber() == 2)
		{	//nazwa atrybutu posiada wartosc
			valueAttribute = getValueTag();
			getAttributesTag(tag, null); //idziemy rekurencyjnie
			tag.addTagAttribute(nameAttribute, valueAttribute); //dodajemy nowy atrybut
		}
		else if(lastToken.getNumber() == 8)
		{	//mamy ponownie nazwe
			getAttributesTag(tag, lastToken);
			tag.addTagAttribute(nameAttribute, valueAttribute); //dodajemy nowy atrybut
		}
		else if(lastToken.getNumber() == 6 || lastToken.getNumber() == 1)
		{	//koniec tego atrybutu
			tag.addTagAttribute(nameAttribute, valueAttribute); //dodajemy nowy atrybut
			return;
		}
		else
		{
			throw new ExceptionInInitializerError("Blad gramatyki, nieoczykiwany symbol w getAtrybut: "
						+ lastToken.getNumber() + " content: " + lastToken.getContent());
		}
	}
	
	/**
	 * @return zwraca wyprowadzone drzewo
	 */
	public JTree getJTree()
	{
		return stack.getTree();
	}
	/**
	 * F-cja zwracajaca wartosc atrybutu, czyli zwraca tokeny ktore sa wartosc atrybutu po ktorym zostaly wywolane
	 * 
	 * @return	zwraca wartosc atrybutu
	 */
	private String getValueTag()
	{
		boolean isQuote = false;
		lastToken = scaner.getToken(Mode.VALUE);	//pobieramy wartosc
		if(lastToken.getNumber() == 4 || lastToken.getNumber() == 5)
		{	//wartosc jest w cudzyslowie np " " lub ' ' 
			lastToken = scaner.getToken(Mode.VALUE);
			isQuote = true;
		}
		if(lastToken.getNumber() != 9)
		{	//TODO wyjatek, niepoprawna gramytka
			throw new ExceptionInInitializerError("Zamiast tag name dostalismy: " + lastToken.getNumber());
		}
		String ret = lastToken.getContent();	//pamietamy wartosc atrybutu
		if(isQuote)
		{	//gdy byl cudzyslow
			lastToken = scaner.getToken(Mode.VALUE);
		}
		return ret;		
	}
}
