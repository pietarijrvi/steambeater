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

	/**
	 * Set the current loading state. Can be set from non-UI thread.
	 * @param currentState
	 */
	public void setLoadingState(LoadingState currentState) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
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
