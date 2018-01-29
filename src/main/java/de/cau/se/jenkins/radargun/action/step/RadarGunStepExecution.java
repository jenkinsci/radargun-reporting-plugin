package de.cau.se.jenkins.radargun.action.step;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import de.cau.se.jenkins.radargun.action.RadarGunPublisher;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;

public class RadarGunStepExecution extends SynchronousNonBlockingStepExecution<Void> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient final RadarGunStep step;

    public RadarGunStepExecution(@Nonnull RadarGunStep step, StepContext context) {
        super(context);
        this.step = step;
    }	
	    @Override
	    protected Void run() throws Exception {
	        FilePath workspace = getContext().get(FilePath.class);
	        workspace.mkdirs();
	        Run<?,?> run = getContext().get(Run.class);
	        TaskListener listener = getContext().get(TaskListener.class);
	        Launcher launcher = getContext().get(Launcher.class);
	    		listener.getLogger().println("Reporting RadarGun Results.");

	        RadarGunPublisher publisher = new RadarGunPublisher();
	        publisher.perform(run, workspace, launcher, listener);

	        return null;
	    }
}
