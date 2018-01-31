/**
 * Copyright © 2018 Alexander Barbie (alexanderbarbie@gmx.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		return null;
	}

	@Override
	public String getUrlName() {
		return this.benchmarkName + "?unit=" + this.unit;
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
		return true;
	}

	public void addPerformanceTestResult(final PerformanceTestResult performanceTestResult) {
		if (!(performanceTestResult == null)) {
			if (this.unit.equals(performanceTestResult.getTestResult().getAssertion().getTimeunit())) {
				this.performanceTestResults.add(performanceTestResult);
			}
		}
	}

	public Job<?, ?> getJob() {
		return this.job;
	}

	public String getResURL() {
		final String url = Jenkins.getInstance().getRootUrl() + Functions.getResourcePath();
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
		return false;
	}

	@Override
	public double getLowerBound() {
		return 0;
	}

	@Override
	public double getUpperBound() {
		return 0;
	}

	@Override
	public double getConfidenceLevel() {
		return 0;
	}

	@Override
	public double getMean() {
		return 0;
	}

	@Override
	public double getMax() {
		return 0;
	}

	@Override
	public double getMin() {
		return 0;
	}

	@Override
	public String getDuration() {
		return "0";
	}

	@Override
	public String getPackageName() {
		return this.packageName;
	}

	@Override
	public String getBenchmarkName() {
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
