package de.cau.se.jenkins.radargun.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import de.cau.se.jenkins.radargun.action.model.AggregatedPackageResult;
import de.cau.se.jenkins.radargun.action.model.PackageResult;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;

public class RadarGunProjectAction implements Action {
	Logger log = LogManager.getLogManager().getLogger("de.cau.se.radargun.jenkins.action.RadarGunProjectAction");
	private final Map<String, AggregatedPackageResult> packageResults = new HashMap<>();
	private final Job<?, ?> job;

	public RadarGunProjectAction(Job<?, ?> job) {
		this.job = job;
	}

	@Override
	public String getIconFileName() {
		return "/plugin/radargun/img/dragonradar.png";
	}

	@Override
	public String getDisplayName() {
		return "RadarGun Test Results";
	}

	@Override
	public String getUrlName() {
		return "radargun";
	}

	public Job<?, ?> getJob() {
		return this.job;
	}

	public String getProjectName() {
		return this.job.getName();
	}

	public void createPackageResults() {
		for (Run<?, ?> build : this.job.getBuilds()) {
			RadarGunBuildAction action = build.getAction(RadarGunBuildAction.class);
			if(action != null) {
				this.mergePackages(action.getPackages());
			}
		}
	}

	public void mergePackages(final Set<PackageResult> packageResults) {
		for (final PackageResult packageResult : packageResults) {
			if (this.packageResults.get(packageResult.packageName) == null) {
				this.packageResults.put(packageResult.packageName, new AggregatedPackageResult(packageResult.packageName, job));
			}
			
			AggregatedPackageResult aggregatedPackage = this.packageResults.get(packageResult.packageName);
			aggregatedPackage.mergePerformanceTestResults(packageResult.getPerformanceTestResults());
		}
	}
	
	public Collection<AggregatedPackageResult> getPackageResults() {
		if(this.packageResults.isEmpty()) {
			this.createPackageResults();
		}	
		
		return this.packageResults.values();
	}
	
	public Object getDynamic(final String token, final StaplerRequest req, final StaplerResponse rsp) {
		for (final AggregatedPackageResult a : this.getPackageResults()) {
			if (a == null)
				continue; // be defensive
			final String urlName = a.getUrlName();
			if (urlName == null)
				continue;
			if (urlName.equals(token))
				return a;
		}
		return null;
	}

}
