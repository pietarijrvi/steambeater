package com.ryhma6.maven.steambeater.model;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Contains the current loading status that can be observed in UI controls.
 * Singleton.
 *
 */
public class ObservableLoadingStatus {
	private static final ObservableLoadingStatus INSTANCE = new ObservableLoadingStatus();

	/**
	 * Contains the current loading status
	 */
	private ObjectProperty<LoadingStatus> loadingStatus = new SimpleObjectProperty<>();

	/**
	 * Last successful api data load completion time
	 */
	private long lastCompletionMillis;
	private boolean apiLoadFailure = false;

	/**
	 * Sets a flag that shows that the api data was not loaded successfully so final
	 * status will be failure.
	 */
	public void setApiLoadFailure() {
		apiLoadFailure = true;
	}

	private ObservableLoadingStatus() {
		loadingStatus.setValue(LoadingStatus.PRELOAD);
	}

	public static ObservableLoadingStatus getInstance() {
		return INSTANCE;
	}

	/**
	 * Returns the time stamp of the last loading completion (system time in
	 * milliseconds).
	 * 
	 * @return time stamp
	 */
	public long getLastCompletionMillis() {
		return lastCompletionMillis;
	}

	/**
	 * Set the current loading state. Can be set from any thread, events (notifying
	 * observers) are posted to event queue.
	 * 
	 * @param currentState
	 */
	public void setLoadingStatus(LoadingStatus newStatus) {
		if (newStatus == LoadingStatus.COMPLETED) {
			lastCompletionMillis = System.currentTimeMillis();
		} else if (newStatus == LoadingStatus.PRELOAD) {
			// reset failure flag
			apiLoadFailure = false;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// if loading data has failed, set failure status instead of completion
						if (apiLoadFailure && newStatus == LoadingStatus.COMPLETED) {
							loadingStatus.setValue(LoadingStatus.FAILURE);
						} else {
							loadingStatus.setValue(newStatus);
						}
					}
				});
			}
		}).start();
	}

	/**
	 * Get load state property that can be bound to observers
	 * 
	 * @return
	 */
	public ObjectProperty<LoadingStatus> getLoadingStateProperty() {
		return loadingStatus;
	}

}
