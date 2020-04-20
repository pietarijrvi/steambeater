package com.ryhma6.maven.steambeater.model;

import java.util.ArrayList;
import java.util.List;

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
	private boolean databaseFailure = false;

	/**
	 * Sets a flag that shows that the api data was not loaded successfully so final
	 * status will be failure.
	 */
	public void setApiLoadFailure() {
		apiLoadFailure = true;
	}

	/**
	 * Sets a flag that shows that the database data was not loaded successfully so
	 * final status will be failure.
	 */
	public void setDatabaseFailure() {
		databaseFailure = true;
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
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// if loading data has failed, set failure status instead of completion
						if (newStatus == LoadingStatus.COMPLETED) {
							if (apiLoadFailure) {
								loadingStatus.setValue(LoadingStatus.FAILURE);
							} else {
								loadingStatus.setValue(newStatus);
							}
							// reset failure flag
							apiLoadFailure = false;
							databaseFailure = false;
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

	/**
	 * Returns a list that contains all current errors in separate strings
	 * 
	 * @return list of error messages
	 */
	public List<String> getErrorMessages() {
		List<String> msg = new ArrayList<String>();
		if (apiLoadFailure)
			msg.add(LoadingStatus.getDescription(LoadingStatus.API_FAILURE));
		if (databaseFailure)
			msg.add(LoadingStatus.getDescription(LoadingStatus.DATABASE_FAILURE));
		return msg;
	}

}
