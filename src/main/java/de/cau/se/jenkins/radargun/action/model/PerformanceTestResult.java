package de.cau.se.jenkins.radargun.action.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.kohsuke.stapler.StaplerProxy;

import jenkins.model.Jenkins;
import radargun.shared.comparison.result.TestResult;

public class PerformanceTestResult extends AbstractPerformanceTestResultAction<PerformanceTestResult>
		implements StaplerProxy, PerformanceTest, Comparable<PerformanceTestResult> {
	private final TestResult testResult;
	public final String benchmarkName;
	public final String packageName;
	public final String unit;
	public final int buildNumber;

	public PerformanceTestResult(TestResult testResult, final int buildNumber) {
		this.testResult = testResult;
		this.benchmarkName = testResult.getBenchmark().substring(testResult.getPackage().length()+1, testResult.getBenchmark().length());
		this.packageName = testResult.getPackage();
		this.unit = testResult.getAssertion().getTimeunit();
		this.buildNumber = buildNumber;
	}

	public TestResult getTestResult() {
		return this.testResult;
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
		return this.benchmarkName;
	}
	
	public String getUrlNameWithPackage() {
		return this.getPackageName()+"/"+this.getUrlName();
	}

	@Override
	public int getFailCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getTarget() {
		return this;
	}

	@Override
	public boolean wasSuccessful() {
		return this.testResult.isInBounds();
	}

	@Override
	public String getPackageName() {
		return this.packageName;
	}

	@Override
	public String getBenchmarkName() {
		return this.benchmarkName;
	}

	public String getAbsoluteUrl() {
		return Jenkins.get().getRootUrl() + this.run.getUrl() + this.packageName + "/" + this.getUrlName();
	}

	@Override
	public boolean hasFailed() {
		return this.testResult.hasFailed();
	}

	@Override
	public double getLowerBound() {
		return format(this.testResult.getAssertion().getLowerBound());
	}

	@Override
	public double getUpperBound() {
		return format(this.testResult.getAssertion().getUpperBound());
	}

	@Override
	public double getConfidenceLevel() {
		return format(this.testResult.getAssertion().getConfidenceLevel());
	}

	@Override
	public double getMean() {
		return format(this.testResult.getMean());
	}

	@Override
	public double getMax() {
		return format(this.testResult.getMax());
	}

	@Override
	public double getMin() {
		return format(this.testResult.getMin());
	}

	@Override
	public String getDuration() {
		return RadarGunUtil.difference(LocalDateTime.parse(this.testResult.getStart()),LocalDateTime.parse(this.testResult.getFinish())).toString();
	}

	@Override
	public double getScore() {
		return format(this.testResult.getScore());
	}

	public double[] getConfidenceInterval() {
//		Arrays.toString(new double[] {});
		return new double[] {format(this.testResult.getConfidenceInterval()[0]),format(this.testResult.getConfidenceInterval()[1])};
	}
	
	private static double format(final double number) {
		return Math.round(number * 100.0) / 100.0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(testResult.getBenchmark(), testResult.getAssertion().getTimeunit(), testResult.getAssertion().getConfidenceLevel(), this.buildNumber);
	}

	@Override
	public boolean equals(Object o) {
		return Objects.equals(this, o);
	}

	@Override
	public int compareTo(PerformanceTestResult o) {
		return this.run.getNumber() - o.run.getNumber();
	}	
	
	public String getUnit() {
		return this.unit;
	}
}
