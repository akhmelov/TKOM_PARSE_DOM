package tkom.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import tkom.common.events.MyEvent;
import tkom.controller.Controller;
import tkom.model.Model;
import tkom.view.View;

public class Start 
{
	public static void main(String[] args)
	{
		BlockingQueue<MyEvent> blockingQueue = new LinkedBlockingQueue<MyEvent>();	//tworzenie kolejki
		Model model = new Model();
		View view = new View(blockingQueue);
		Controller controller = new Controller(view, model, blockingQueue);
		
		controller.start();	//uruchamiamy kontroler
	}
}
