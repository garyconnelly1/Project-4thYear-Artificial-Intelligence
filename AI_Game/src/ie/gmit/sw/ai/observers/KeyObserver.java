package ie.gmit.sw.ai.observers;

import java.awt.event.KeyEvent;

/*
* An interface that provides a method signature for updating key observer objects whenever a key is pressed.
*/

public interface KeyObserver {

	public void update(KeyEvent keyEvent);
	
}
