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
			if(buildAction.getNumberOfFailedTests() > 0) {
				run.setResult(Result.UNSTABLE);
			} else if(buildAction.getNumberOfErrorTests() > 0) {
				run.setResult(Result.FAILURE);
			} else {
				run.setResult(Result.SUCCESS);	
			}
		}

	}

	public Run<?, ?> getRun() {
		return this.run;
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Extension // This indicates to Jenkins that this is an implementation of an extension
				// point.
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		// private boolean useFrench;

		public DescriptorImpl() {
			load();
		}

		// public FormValidation doCheckName(@QueryParameter String value)
		// throws IOException, ServletException {
		// if (value.length() == 0)
		// return FormValidation.error("Please set a name");
		// if (value.length() < 4)
		// return FormValidation.warning("Isn't the name too short?");
		// return FormValidation.ok();
		// }

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project types
			return true;
		}

		public String getDisplayName() {
			return "Report Performance Test Results (RadarGun)";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			// To persist global configuration information,
			// set that to properties and call save().
			// useFrench = formData.getBoolean("useFrench");
			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this, like
			// setUseFrench)
			save();
			return super.configure(req, formData);
		}

		// public boolean getUseFrench() {
		// return useFrench;
		// }
	}
}