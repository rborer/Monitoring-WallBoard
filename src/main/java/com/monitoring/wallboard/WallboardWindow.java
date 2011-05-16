package com.monitoring.wallboard;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.CardPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.GridPane;
import org.apache.pivot.wtk.Window;

import java.io.IOException;
import java.net.URL;

/**
 * @author reynald
 */
public class WallboardWindow extends Window implements Bindable {
	@BXML
	private GridPane grid;

	public GridPane getGrid() {
		return grid;
	}

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void setGridColumnCount(final int gridSize) {
		prepareGridPaneSize(gridSize);
	}

	private void prepareGridPaneSize(final int gridSize) {
		final BXMLSerializer bxmlSerializer = new BXMLSerializer();

		grid.setColumnCount(gridSize);

		for (int rowNb = 0; rowNb < gridSize; rowNb++) {
			final GridPane.Row row = new GridPane.Row();
			grid.getRows().add(row);

			for (int colNb = 0; colNb < gridSize; colNb++) {
				try {
					row.insert((Component) bxmlSerializer.readObject(CardPane.class,
							"/card_pane.bxml"), colNb);
				} catch (IOException e) {
					// TODO: better handling
					throw new RuntimeException(e);

				} catch (SerializationException e) {
					// TODO: better handling
					throw new RuntimeException(e);
				}
			}
		}
	}
}
