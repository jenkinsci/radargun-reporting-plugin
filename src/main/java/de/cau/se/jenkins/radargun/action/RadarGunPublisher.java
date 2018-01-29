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

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

public class RadarGunPublisher extends Recorder implements SimpleBuildStep {

	private Run<?, ?> run;

	@DataBoundConstructor
	public RadarGunPublisher() {

	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		this.run = run;
		RadarGunBuildAction buildAction = new RadarGunBuildAction(run, workspace);
		run.addAction(buildAction);

		if (buildAction != null) {
			if (buildAction.getNumberOfFailedTests() > 0) {
				run.setResult(Result.UNSTABLE);
			} else if (buildAction.getNumberOfErrorTests() > 0) {
				run.setResult(Result.FAILURE);
			} else {
				run.setResult(Result.SUCCESS);
			}
		}

	}

	public Run<?, ?> getRun() {
		return this.run;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		public DescriptorImpl() {
			load();
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		public String getDisplayName() {
			return "Report Performance Test Results (RadarGun)";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			save();
			return super.configure(req, formData);
		}
	}
}