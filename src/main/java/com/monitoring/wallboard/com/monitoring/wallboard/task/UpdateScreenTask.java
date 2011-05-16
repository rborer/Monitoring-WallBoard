package com.monitoring.wallboard.com.monitoring.wallboard.task;

import com.monitoring.wallboard.WallboardWindow;
import com.monitoring.wallboard.com.monitoring.wallboard.json.WallboardDefinition;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.StopWatch;
import org.apache.pivot.wtk.CardPane;
import org.apache.pivot.wtk.GridPane;
import org.apache.pivot.wtk.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author reynald
 */
public class UpdateScreenTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateScreenTask.class);

	private WallboardWindow window;

	private GridPane gridPane;

	private WallboardDefinition wallboardDefinition;

	private int currentPosition = 0;

	public UpdateScreenTask(final WallboardWindow window, final WallboardDefinition wallboardDefinition) {
		// TODO: replace by Guava stuff
		Validate.notNull(window, "the 'window' parameter cannot be null!");
		Validate.notNull(wallboardDefinition, "the 'wallboardDefinition' parameter cannot be null!");

		this.window = window;
		this.gridPane = window.getGrid();
		this.wallboardDefinition = wallboardDefinition;

		window.setGridColumnCount(wallboardDefinition.getNbColumns());
	}

	@Override
	public void run() {
		while (true) {
			try {
				refresh();

				TimeUnit.SECONDS.sleep(wallboardDefinition.getRefreshTime());
			} catch (InterruptedException e) {
				// TODO: better stuff
				System.out.println("Interrupted: " + e);

			} catch (Exception e) {
				// TODO: better stuff
				System.out.println("Other exception: " + e);
			}
		}
	}

	private void refresh() {
		StopWatch watch = new StopWatch();
		LOGGER.info("Starting up refresh process.");
		watch.start();

		final int columns = gridPane.getColumnCount();
		final int columnsSquare = columns * columns;

		for (int current = 0; current < columnsSquare; current++) {
			final CardPane cardPane = (CardPane) gridPane.get(current);
			final int nextPane = (cardPane.getSelectedIndex() + 1) % 2;
			final ImageView imageView = (ImageView) cardPane.get(nextPane);

			currentPosition = (++currentPosition) % wallboardDefinition.getGraphiteUrls().size();
			final String url = wallboardDefinition.getGraphiteUrls().get(currentPosition);
			try {
				imageView.setImage(new URL(url));
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}

			cardPane.setSelectedIndex(nextPane);
		}

		watch.stop();
		LOGGER.info("Refresh process ended in {}", watch.toString());
	}
}
