package ie.gmit.sw.ai.observables;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import ie.gmit.sw.ai.observers.KeyObserver;

public class KeyPressed implements KeyListener, KeyObservable {

	private static KeyPressed instance = null;

	private ArrayList<KeyObserver> objList;

	private KeyPressed() {
		objList = new ArrayList<KeyObserver>();
	}

	@Override
	public void NotifyObservers(KeyEvent keyEvent) {
		for (KeyObserver obs : objList) {
			obs.update(keyEvent);
		}
	}

	public void AddObserver(KeyObserver obs) {
		if (obs != null)
			objList.add(obs);
	}

	public void DelObserver(KeyObserver obs) {
		if (obs != null)
			objList.remove(obs);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		NotifyObservers(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public static KeyPressed getInstance() {
		if (instance == null) {
			instance = new KeyPressed();
		}

		return instance;

	}

}
