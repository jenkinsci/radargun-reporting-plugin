package de.cau.se.jenkins.radargun.action.model;

public class DataPoint {
	final double time;
	final int build;

	DataPoint(final double time, final int build) {
		this.time = time;
		// http://localhost:8080/jenkins/static/0cb2f212/
		this.build = build;
	}

	@Override
	public String toString() {
		return "[" + this.build + "," + this.time + "]";
	}
}
