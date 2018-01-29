package de.cau.se.jenkins.radargun.action.model;

public interface PerformanceTest {
//	public Collection<PerformanceTest> getPerformanceTestResult();
	public boolean wasSuccessful();
	public boolean hasFailed();
	public double getLowerBound();
	public double getUpperBound();
	public double getConfidenceLevel();
	public double getMean();
	public double getMax();
	public double getMin();
	public double getScore();
	public String getDuration();
	public String getPackageName();
	public String getBenchmarkName();
}
