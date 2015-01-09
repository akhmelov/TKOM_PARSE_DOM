/**
 * 
 */
package tkom.model.parse;

import java.awt.ScrollPane;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Kontroluje stosy
 * 
 * @author pk
 *
 */
class Stack
{
	/**
	 * Daje unikalny numer kazdemu tagowi polozonemu do na stos z tagami
	 * sluzy do pozniejszej identyfikacji kolejnosci odkladania tagow
	 */
	private static int idTag;
	/**
	 * Glowny stas gdzie sa odkaladane elementy
	 */
	LinkedList<Entry<Tag, Integer> > mainStack;
	/**
	 * Stos na ktorym jest tworzone drzewo
	 */
	LinkedList<DefaultMutableTreeNode> treeStack;
	/**
	 * Przechowuje obecna pozycje w drugim stosie, w stasie drzewa
	 */
	ListIterator<DefaultMutableTreeNode> itTreeStack;
	/**
	 * Tworzy stosy
	 */
	public Stack()
	{
		idTag = 0;
		mainStack = new LinkedList<Entry<Tag, Integer> >();
		treeStack = new LinkedList<DefaultMutableTreeNode>();
		itTreeStack = treeStack.listIterator();	//ustawiamy poczatkowa pozycje na stasie drugim
	}
	
	/**
	 * Dodaje tag do stosu pierwszego
	 * 
	 * @param tag tag ktory dodajemy do stasu
	 */
	public void addToStack(final Tag tag)
	{
		tag.setId(idTag++); //mowimy tagowi jaki z kolei zostal odczytany
		mainStack.add(new AbstractMap.SimpleEntry<Tag, Integer> (tag, treeStack.size()));
	}
	
	/**
	 * Probuje polaczyc wszystko z pierwszego stosu na drugi
	 * 
	 * @param tag tag ktory chcemy polaczyc - to musi byc zamykajacy tag np </div>, szukamy dla niego otwierajacy tag i wszystkie elementy pomiedzy laczymy
	 * @return jesli udalo sie znalesc otweirajacy tag to zwraca true, jesli nie to false
	 */
	public boolean trySplit(final Tag tag)
	{
		if(mainStack.isEmpty())	//jestesmy na poczatku, pierwszy stos jest pusty, nie ma senesu sprawdzac, tu jest koniec
			return false;
		//probujemy polaczyc glowny stos, gdy znajdziemy to dostaniemy polaczone drzewo z pierwszego stosu
		Entry<DefaultMutableTreeNode, Integer> firstStack = trySplitMainStack(tag);
		if(firstStack == null)	//nie udalo sie znalesc tagu otwierajacego
			return false;
		trySplitTreeStack(firstStack);
		return true;
	}
	
	/**
	 * Probuje polaczyc stos na ktorym odkaladane sa tagi, a nie drzewa
	 * 
	 * @param tag tag ktory zamyka
	 * @return	//zwraca drzewo z polaczonymi elementami, null - nic nie polaczylismy
	 */
	private Entry<DefaultMutableTreeNode, Integer> trySplitMainStack(final Tag tag)
	{
		//wskazuje na wieracholek stosu
		ListIterator<Entry<Tag, Integer> > itLastOnStack = mainStack.listIterator(mainStack.size());
		 //wskazuje na piewrwszy otwierajacy tag
		ListIterator<Entry<Tag, Integer> > itFirstOpenTag = mainStack.listIterator(mainStack.size());
		Entry<Tag, Integer> nodeFirstOpenTag = null; //pamieta pierwszy tag otwierajacy, z niego budowane drzewo
		while(itFirstOpenTag.hasPrevious())
		{	//szukamy pierwszy otwierajacy tag pasujacy do naszego szukanego
			//pamieta obecny element na iteratorze
			Entry<Tag, Integer> tmpFirstOpenTag = itFirstOpenTag.previous();	
			if(tmpFirstOpenTag.getKey().getName().equalsIgnoreCase(tag.getName()))
			{	//znalezlismy pierszy otwierajacy tag
				nodeFirstOpenTag = tmpFirstOpenTag; //pamietamy otwierajacy tag
				break;	//wychodzimy z petli poniewaz moze byc drugi otwierajacy tag na wyzszym poziomie
			}
		}
		if(nodeFirstOpenTag == null)	//nie ma otwierajacego tagu
			return null;
		//mowimy ze ten tag posiada tag zamykajacy
		nodeFirstOpenTag.getKey().setLocking(true);
		//robimy wezel znalezionego tagu jako korzen drzewa
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeFirstOpenTag.getKey());	
		while(itLastOnStack.hasPrevious())
		{	//zbieramy wszystkie elementy z listy dodajac je do wezla jako dzieci drzewa
			//pamieta obecny element na iteratorze
			Entry<Tag, Integer> tmpLastOnStack = itLastOnStack.previous();
			//usuwamy ostatni element ze stosu poniewaz on trafia albo do drzewa, albo to jest otwierajacy tag
			itLastOnStack.remove(); 
			if(tmpLastOnStack.equals(nodeFirstOpenTag))
			{	//jestesmy na otwierajacym tagu, wychodzimy z petli
				break;
			}
			treeNode.add(new DefaultMutableTreeNode(tmpLastOnStack.getKey()));	//dodajemy do drzewa nasz element
		}
		return new AbstractMap.SimpleEntry<DefaultMutableTreeNode, Integer>(treeNode, nodeFirstOpenTag.getValue()); 
	}
	
	/**
	 * F-cja laczaca korzenia drzewa w jedno drzewo, laczy elementy ktore znajduja sie na stosie
	 * 
	 * @param entry drzewo ktore udalo sie stworzyc z pierwszego stosu, gdzie ukladane tagi od parsera <drzewo, gdzie musimy sklejac na TreeStack>
	 */
	private void trySplitTreeStack(final Entry<DefaultMutableTreeNode, Integer> entry)
	{
		DefaultMutableTreeNode tmp = entry.getKey();	//glowny wezel
		if(entry.getValue().equals(treeStack.size()))
		{	//gdy wskaznik z pierwszego stosu wskazuje na koniec stosu z drzewem - wstawiamy 
			treeStack.push(tmp);
			return;
		}
		for(int i = treeStack.size() - 1; i >= 0; --i)
		{
			DefaultMutableTreeNode tmpNode = treeStack.get(treeStack.size() - i - 1);
			tmp.add(tmpNode);
			treeStack.remove(treeStack.size() - i - 1);
			if(i == entry.getValue())
			{	//jestesmy u celu
				tmp = orderChildren(tmp);
				treeStack.push(tmp);
				break;
			}
		}
		return;
	}

	/**
	 * @return zwraca wyprowadzone drzewo
	 */
	public JTree getTree()
	{
		return new JTree(treeStack.getLast());
	}
	/**
	 * @return	czy mamy koniec stosow
	 */
	public boolean isEnd()
	{
		for(Entry< Tag, Integer> entr: mainStack)
		{
			System.out.println("    " + entr.getKey().getName());
		}
		JFrame jFrame = new JFrame("Tree test");
		JTree jTree = new JTree(treeStack.getLast());
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(jTree);
		jFrame.add(scrollPane);
		jFrame.setSize(400, 400);
		jFrame.setVisible(true);
		//TODO
		return false;
	}
	
	/**
	 * Porzadkuje dzieci wezla tmp tak jak one byly w html, robi to za pomoca wczesniej ustawionego id
	 * 
	 * @param tmp wezl ktore dzieci chcemy uporzadkowac
	 * @return uporzadkowany wezl
	 */
	private DefaultMutableTreeNode orderChildren(DefaultMutableTreeNode tmp)
	{
		if(tmp == null)
			return tmp;
		if(tmp.getChildCount() > 0)
		{	//czy ten wezel ma dzieci
			Tag tmpTag = (Tag)tmp.getUserObject();
			DefaultMutableTreeNode ret = new DefaultMutableTreeNode(tmpTag);
			TreeMap<Integer, DefaultMutableTreeNode> sorted_map = new TreeMap<Integer, DefaultMutableTreeNode>();
			//ret.removeAllChildren(); //usuwamy dzieci bo bedziemy je nadpisywac, zeby nie powtarzaly sie
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tmp.getFirstChild();
			while(treeNode != null)
			{
				if(treeNode.getUserObject() instanceof Tag)
				{
					tmpTag = (Tag)treeNode.getUserObject();
					sorted_map.put(tmpTag.getId(), treeNode);					
				}
				treeNode = (DefaultMutableTreeNode) tmp.getChildAfter(treeNode);
			}
			for (Map.Entry<Integer, DefaultMutableTreeNode> el: sorted_map.entrySet())
			{
				ret.add(el.getValue());
			}
			return ret;
		}
		else
		{
			return tmp;
		}
	}
}
