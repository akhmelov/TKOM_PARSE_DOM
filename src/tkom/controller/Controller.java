package tkom.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import tkom.common.events.InputedURLEvent;
import tkom.common.events.MyEvent;
import tkom.common.events.SelectedNodeOfTreeEvent;
import tkom.model.Model;
import tkom.view.View;

public class Controller	extends Thread
{
	private BlockingQueue<MyEvent>	blockingQueue;
	private Model model;
	private View view;
	
	private Map<Class<? extends MyEvent>, Strategy> strategyMap;	//mapa strategij
	
	public Controller(View view, Model model, BlockingQueue<MyEvent> blockingQueue)
	{
		this.view = view;
		this.model = model;
		this.blockingQueue = blockingQueue;
		
		strategyMap = new HashMap<Class<? extends MyEvent>,	Strategy>();	//tworzymy hash mape
		strategyMap.put(InputedURLEvent.class, new InputedURLStrategy(model, view));
		strategyMap.put(SelectedNodeOfTreeEvent.class, new SelectedNodeOfTreeStrategy(model, view));
		//strategyMap.put(KeyPressedMyEvent.class,new KeyPressedStrategy(view,model)); //tworzymy dzilanie dla przyciskow klawisz
		
		///TEST/////////TEST///////WYWALIC PONIZEJ///TEST
		//model.startParse("http://www.oracle.com/"); //https://translate.google.ru/#pl/en/cudzyslow
		//model.startParse("http://www.december.com/html/demo/hello.html");
		//model.startParse("http://www.google.com");
		//WYWALIC DOTAD///TEST///TEST////////////////////
	}
	
	@Override
	public void run()
	{	//uruchamiamy watek i czekamy na strategie
		try
		{
			while(true)	//czekamy na strategie
			{
				MyEvent event = blockingQueue.take();	//pobieramy zdarzenie
				strategyMap.get(event.getClass()).perform(event);	//wykanujemy odpowiadajaca strategie
			}
		}
		catch(Exception e)
		{
			System.out.print("Zgloszony wyjatek nie strategii do eventa w controller.run: " + e);
		}
		super.run();
	}
}
