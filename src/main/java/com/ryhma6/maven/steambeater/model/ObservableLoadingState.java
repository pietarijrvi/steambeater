package com.ryhma6.maven.steambeater.model;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Contains the current loading state that can be observed in UI controls. Singleton.
 *
 */
public class ObservableLoadingState {
	private static final ObservableLoadingState INSTANCE = new ObservableLoadingState();

	private ObservableLoadingState() {
		loadingState.setValue(LoadingState.PRELOAD);
	}

	public static ObservableLoadingState getInstance() {
		return INSTANCE;
	}

	/**
	 * Contains the current loading state
	 */
	private ObjectProperty<LoadingState> loadingState = new SimpleObjectProperty<>();
	
	private long lastCompletionMillis; 
	
	/**
	 * Returns the time stamp of the last loading completion (system time in milliseconds).
	 * @return time stamp
	 */
	public long getLastCompletionMillis() {
		return lastCompletionMillis;
	}

	/**
	 * Set the current loading state. Can be set from any thread, events (notifying observers) are posted to event queue.
	 * @param currentState
	 */
	public void setLoadingState(LoadingState currentState) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(currentState==LoadingState.COMPLETED) {
							lastCompletionMillis = System.currentTimeMillis();
						}
						loadingState.setValue(currentState);
					}
				});
			}
		}).start();
	}

	/**
	 * Get load state property that can be bound to observers
	 * @return
	 */
	public ObjectProperty<LoadingState> getLoadingStateProperty() {
		return loadingState;
	}

}
