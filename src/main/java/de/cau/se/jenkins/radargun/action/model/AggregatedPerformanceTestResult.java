package de.cau.se.jenkins.radargun.action.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import org.kohsuke.stapler.StaplerProxy;

import hudson.Functions;
import hudson.model.Action;
import hudson.model.Job;
import jenkins.model.Jenkins;

public class AggregatedPerformanceTestResult implements Action, StaplerProxy, PerformanceTest {

	public final String benchmarkName;
	public final String packageName;
	public final String unit;
	public final Job<?, ?> job;
	private final Collection<PerformanceTestResult> performanceTestResults = new TreeSet<>();

	public AggregatedPerformanceTestResult(final PerformanceTestResult performanceTestResult, final Job<?, ?> job) {
		this.benchmarkName = performanceTestResult.benchmarkName;
		this.packageName = performanceTestResult.packageName;
		this.unit = performanceTestResult.getTestResult().getAssertion().getTimeunit();
		this.performanceTestResults.add(performanceTestResult);
		this.job = job;
	}

	public Collection<PerformanceTestResult> getPerformanceTestResults() {
		return this.performanceTestResults;
	}

	@Override
	public String getDisplayName() {
		return this.benchmarkName;
	}

	@Override
	public String getIconFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrlName() {
		return this.benchmarkName+"?unit="+this.unit;
	}

	@Override
	public Object getTarget() {
		return this;
	}

	public int getSuccesfullCount() {
		int succesfullCount = 0;

		for (PerformanceTestResult performanceTestResult : this.getPerformanceTestResults()) {
			if (performanceTestResult.getTestResult().isInBounds()) {
				succesfullCount++;
			}
		}

		return succesfullCount;
	}

	public int getTotalCount() {
		return this.performanceTestResults.size();
	}

	public Collection<PerformanceTestResult> getFailedTest() {
		return Collections.emptyList();
	}

	@Override
	public boolean wasSuccessful() {
		// TODO Auto-generated method stub
		return true;
	}

	public void addPerformanceTestResult(final PerformanceTestResult performanceTestResult) {
		if(!(performanceTestResult == null)) {
			if(this.unit.equals(performanceTestResult.getTestResult().getAssertion().getTimeunit())) {
				this.performanceTestResults.add(performanceTestResult);
			}
		}		
	}

	public Job<?, ?> getJob() {
		return this.job;
	}

	public String getResURL() {
		final String url = Jenkins.get().getRootUrl() + Functions.getResourcePath();
		return url;
	}

	public String getDataPoints() {
		final List<DataPoint> dataPoints = new ArrayList<>();
		performanceTestResults.forEach(performanceTestResult -> dataPoints
				.add(new DataPoint(performanceTestResult.getTestResult().getMean(), performanceTestResult.run.number)));

		return dataPoints.toString();
	}

	public String getUpperBoundsDataPoints() {
		final List<DataPoint> dataPoints = new ArrayList<>();
		performanceTestResults.forEach(performanceTestResult -> dataPoints
				.add(new DataPoint(performanceTestResult.getTestResult().getAssertion().getUpperBound(),
						performanceTestResult.run.number)));

		return dataPoints.toString();
	}
	
	public String getLowerBoundsDataPoints() {
		final List<DataPoint> dataPoints = new ArrayList<>();
		performanceTestResults.forEach(performanceTestResult -> dataPoints
				.add(new DataPoint(performanceTestResult.getTestResult().getAssertion().getLowerBound(),
						performanceTestResult.run.number)));

		return dataPoints.toString();
	}

	@Override
	public boolean hasFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getLowerBound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getUpperBound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getConfidenceLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMean() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMin() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDuration() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public String getPackageName() {
		return this.packageName;
	}

	@Override
	public String getBenchmarkName() {
		// TODO Auto-generated method stub
		return this.benchmarkName;
	}

	@Override
	public double getScore() {
		return 0.0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(benchmarkName, packageName, unit);
	}

	@Override
	public boolean equals(Object o) {
		return Objects.equals(this, o);
	}		
	
}
