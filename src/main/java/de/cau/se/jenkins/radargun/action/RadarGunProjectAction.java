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
	private final Map<String, AggregatedPackageResult> packageResults = new HashMap<>();
	private final Job<?, ?> job;
	public static final String PLUGIN_ID = "radargun-reporting";
	public static final String ICON_URL_PREFIX = "/plugin/radargun-reporting/img/";
	public static final String ICON_URL = ICON_URL_PREFIX + "dragonradar.png";

	public RadarGunProjectAction(Job<?, ?> job) {
		this.job = job;
	}

	@Override
	public String getIconFileName() {
		return ICON_URL;
	}

	@Override
	public String getDisplayName() {
		return "RadarGun Test Results";
	}

	@Override
	public String getUrlName() {
		return PLUGIN_ID;
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
			if (action != null) {
				this.mergePackages(action.getPackages());
			}
		}
	}

	public void mergePackages(final Set<PackageResult> packageResults) {
		for (final PackageResult packageResult : packageResults) {
			if (this.packageResults.get(packageResult.packageName) == null) {
				this.packageResults.put(packageResult.packageName,
						new AggregatedPackageResult(packageResult.packageName, job));
			}

			AggregatedPackageResult aggregatedPackage = this.packageResults.get(packageResult.packageName);
			aggregatedPackage.mergePerformanceTestResults(packageResult.getPerformanceTestResults());
		}
	}

	public Collection<AggregatedPackageResult> getPackageResults() {
		if (this.packageResults.isEmpty()) {
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
