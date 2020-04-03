package com.ryhma6.maven.steambeater.model;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class ObservableLoadingState {
	private static final ObservableLoadingState INSTANCE = new ObservableLoadingState();
	
	private ObservableLoadingState() {
		loadingState.setValue(LoadingState.PRELOAD);
	}
	
	public static ObservableLoadingState getInstance() {
        return INSTANCE;
    }

	

	private ObjectProperty<LoadingState> loadingState = new SimpleObjectProperty<>();

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

	public ObjectProperty<LoadingState> getLoadingStateProperty() {
		return loadingState;
	}

}
