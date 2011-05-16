package com.monitoring.wallboard;

import com.monitoring.wallboard.com.monitoring.wallboard.json.WallboardDefinition;
import com.monitoring.wallboard.com.monitoring.wallboard.task.UpdateScreenTask;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * @autor reynald
 */
public class Wallboard implements Application {

	private static final String DEFAULT_JSON_FILE = "monitoring.json";

	private static final String JSON_FILE_PROPERTY = "file";

	private static final Logger LOGGER = LoggerFactory.getLogger(Wallboard.class);

	private WallboardWindow window = null;

	@Override
	public void startup(Display display, Map<String, String> properties) throws Exception {

		final BXMLSerializer bxmlSerializer = new BXMLSerializer();
		window = (WallboardWindow) bxmlSerializer.readObject(Wallboard.class, "/layout.bxml");


		final String jsonFile;
		if (properties.containsKey(JSON_FILE_PROPERTY)) {
			jsonFile = properties.get(JSON_FILE_PROPERTY);
		} else {
			jsonFile = DEFAULT_JSON_FILE;
		}

		final WallboardDefinition wallboardDefinition = readJsonFile(jsonFile);

		if (wallboardDefinition == null) {

		} else {
			final Runnable task = new UpdateScreenTask(window, wallboardDefinition);
			Executors.newSingleThreadExecutor().execute(task);
		}

		window.open(display);
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		if (window != null) {
			window.close();
		}
		return false;
	}

	@Override
	public void suspend() throws Exception {
		//Nothing to do
	}

	@Override
	public void resume() throws Exception {
		//Nothing to do
	}

	private WallboardDefinition readJsonFile(final String jsonFile) {
		final ObjectMapper mapper = new ObjectMapper();

		final File json = new File(jsonFile);

		try {
			return mapper.readValue(json, WallboardDefinition.class);
		} catch (IOException e) {
			// TODO: display a message through the GUI
			LOGGER.warn("Unable to read json property file {}", jsonFile, e);
			return null;
		}

	}

	public static void main(final String[] args) {

		final String[] argsFullScreen = new String[args.length + 1];
		System.arraycopy(args, 0, argsFullScreen, 0, args.length);
		argsFullScreen[argsFullScreen.length - 1] = "--fullScreen=true";

		DesktopApplicationContext.main(Wallboard.class, args);
	}
}
