package de.cau.se.jenkins.radargun.action.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.kohsuke.stapler.StaplerProxy;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hudson.model.Action;
import hudson.model.Run;

public class PackageResult implements Action, StaplerProxy, ResultOverview {

	public final String packageName;
	private final Run<?, ?> run;
	private final Multimap<String, PerformanceTestResult> performanceTestResults = HashMultimap.create();

	public PackageResult(final String packageName, final Run<?, ?> run) {
		this.packageName = packageName;
		this.run = run;
	}

	public Collection<PerformanceTestResult> getPerformanceTestResults() {
		Collection<PerformanceTestResult> performanceTestResults = new HashSet<>();

		for (Collection<PerformanceTestResult> performanceTestResult : this.performanceTestResults.asMap().values()) {
			performanceTestResults.addAll(performanceTestResult);
		}

		return performanceTestResults;
	}

	public void addPerformanceTestResult(PerformanceTestResult testResult) {
		if (!(testResult.benchmarkName == null)) {
			this.performanceTestResults.put(testResult.benchmarkName, testResult);
		}
	}

	@Override
	public String getDisplayName() {
		return this.packageName;
	}

	@Override
	public String getIconFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrlName() {
		return this.packageName;
	}

	@Override
	public Object getTarget() {
		return this;
	}

	public Object getDynamic(final String token, final StaplerRequest req, final StaplerResponse rsp) {
		return this.performanceTestResults.get(token).iterator().next();

	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getDisplayName());
	}

	@Override
	public boolean equals(Object o) {
		return Objects.equals(this, o);

	}

	public Run<?, ?> getRun() {
		return this.run;
	}

	@Override
	public int getNumberOfTests() {
		return this.performanceTestResults.values().size();
	}

	@Override
	public int getNumberOfFailedTests() {
		int failedTestsCounter = 0;

		for (PerformanceTestResult performanceTestResult : this.performanceTestResults.values()) {
			if (performanceTestResult.getTestResult().hasFailed()) {
				failedTestsCounter++;
			}
		}

		return failedTestsCounter;
	}

	@Override
	public int getNumberOfErrorTests() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfSuccessfulTests() {
		int successfulTestsCounter = 0;

		for (PerformanceTestResult performanceTestResult : this.performanceTestResults.values()) {
			if (performanceTestResult.getTestResult().isInBounds()) {
				successfulTestsCounter++;
			}
		}

		return successfulTestsCounter;
	}

}
