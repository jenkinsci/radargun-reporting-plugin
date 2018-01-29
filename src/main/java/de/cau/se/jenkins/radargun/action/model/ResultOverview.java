package de.cau.se.jenkins.radargun.action.model;

public interface ResultOverview {
	public int getNumberOfTests();
	public int getNumberOfFailedTests();
	public int getNumberOfErrorTests();
	public int getNumberOfSuccessfulTests();
}
