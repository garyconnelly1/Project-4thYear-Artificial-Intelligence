package ie.gmit.sw.ai.observables;

import java.awt.event.KeyEvent;

/*
* An interface that provides a method signature for notifying observers based on a KeyEvent.
*/


public interface KeyObservable {
	
	public void NotifyObservers(KeyEvent keyEvent);

}
