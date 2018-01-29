package de.cau.se.jenkins.radargun.action.step;

import java.util.Set;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import com.google.common.collect.ImmutableSet;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;

public class RadarGunStep extends Step {
    @DataBoundConstructor
    public RadarGunStep() {}

    @Override
    public StepExecution start(StepContext context) throws Exception {
    		return new RadarGunStepExecution(this, context);
    }
    
    @Extension
    public static class DescriptorImpl extends StepDescriptor {
        @Override
        public String getFunctionName() {
            return "radargun";
        }

        @Override
        @Nonnull
        public String getDisplayName() {
            return "Report RadarGun Performance Test";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, FlowNode.class, TaskListener.class, Launcher.class);
        }
    }

}
