package com.monitoring.wallboard.com.monitoring.wallboard.json;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * @author reynald
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WallboardDefinition {

	private int nbColumns;

	private int refreshTime;

	private List<String> graphiteUrls;

	public int getNbColumns() {
		return nbColumns;
	}

	public void setNbColumns(int nbColumns) {
		this.nbColumns = nbColumns;
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}

	public List<String> getGraphiteUrls() {
		return graphiteUrls;
	}

	public void setGraphiteUrls(List<String> graphiteUrls) {
		this.graphiteUrls = graphiteUrls;
	}
}
