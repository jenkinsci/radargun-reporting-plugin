package de.cau.se.jenkins.radargun.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Multimap;

import de.cau.se.jenkins.radargun.action.model.PackageResult;
import de.cau.se.jenkins.radargun.action.model.PerformanceTestResult;
import de.cau.se.jenkins.radargun.action.model.RadarGunUtil;
import de.cau.se.jenkins.radargun.action.model.ResultOverview;
import hudson.FilePath;
import hudson.model.Action;
import hudson.model.Run;
import jenkins.tasks.SimpleBuildStep;
import radargun.Options;
import radargun.output.stages.TestResultAggregatorStage;
import radargun.shared.comparison.result.TestResult;
import radargun.shared.model.AggregatedTestResult;
import radargun.shared.model.AggregatedTestResultImpl;

public class RadarGunBuildAction implements Action, SimpleBuildStep.LastBuildAction, ResultOverview {
	private final FilePath workspace;
	private final Run<?, ?> run;
	private final AggregatedTestResult testResult;
	private final Set<PerformanceTestResult> performanceTestResults = new HashSet<>();
	private final Set<PackageResult> packages = new HashSet<>();

	public RadarGunBuildAction(final Run<?, ?> run, final FilePath workspace) {
		this.workspace = workspace;
		this.run = run;
		this.testResult = this.setTestResult();
		this.createPackageStructure();
	}

	@Override
	public String getIconFileName() {
		return "/plugin/radargun/img/dragonradar.png";
	}

	@Override
	public String getDisplayName() {
		return "Radar Gun Results";
	}

	@Override
	public String getUrlName() {
		return "radargun";
	}

	public int getBuildNumber() {
		return this.run.number;
	}

	public Run<?, ?> getRun() {
		return this.run;
	}

	public Set<PerformanceTestResult> getPerformanceTestResults() {		
		return this.performanceTestResults;
	}

	public AggregatedTestResult getTestResult() {
		return this.testResult;
	}

	private AggregatedTestResult setTestResult() {
		if (this.testResult == null) {
			try {
				return new XmlMapper().readValue(
						Files.newInputStream(Paths.get(this.workspace.toURI().getPath(), Options.DEFAULTEXPORTPATH, TestResultAggregatorStage.AGGREGATEDFILENAME), StandardOpenOption.READ),
						AggregatedTestResultImpl.class);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	private void createPackageStructure() {
		final Multimap<String, TestResult> packageStructure = RadarGunUtil
				.createPackageStructure(this.testResult);
		for (final String packageName : packageStructure.keySet()) {
			final PackageResult packageResult = new PackageResult(packageName, this.run);

			for (final TestResult testResult : packageStructure.get(packageName)) {
				final PerformanceTestResult performanceTestResultAction = new PerformanceTestResult(testResult, this.run.number);
				this.performanceTestResults.add(performanceTestResultAction);
				packageResult.addPerformanceTestResult(performanceTestResultAction);
				this.run.addAction(performanceTestResultAction);
				this.run.addAction(packageResult);
			}

			this.packages.add(packageResult);
		}

	}

	public Set<PackageResult> getPackages() {
		return this.packages;
	}

	public Object getDynamic(final String token, final StaplerRequest req, final StaplerResponse rsp) {
		for (final PackageResult packageResult : this.getPackages()) {
			if (packageResult == null)
				continue; // be defensive
			final String urlName = packageResult.getUrlName();
			if (urlName == null)
				continue;
			if (urlName.equals(token))
				return packageResult;
		}
		return null;
	}

	@Override
	public Collection<? extends Action> getProjectActions() {
		List<RadarGunProjectAction> projectActions = new ArrayList<>();
		projectActions.add(new RadarGunProjectAction(this.run.getParent()));
		return projectActions;
	}

	@Override
	public int getNumberOfTests() {
		return this.testResult.getNumberOfTests();
	}

	@Override
	public int getNumberOfFailedTests() {
		return this.testResult.getNumberOfFailedTests();
	}

	@Override
	public int getNumberOfErrorTests() {
		return this.testResult.getNumberOfErrorTests();
	}

	@Override
	public int getNumberOfSuccessfulTests() {
		return this.testResult.getNumberOfSuccessfulTests();
	}
}
