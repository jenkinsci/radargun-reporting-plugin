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

import de.cau.se.jenkins.radargun.action.RadarGunProjectAction;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;

public class RadarGunStep extends Step {
	@DataBoundConstructor
	public RadarGunStep() {
	}

	@Override
	public StepExecution start(StepContext context) throws Exception {
		return new RadarGunStepExecution(this, context);
	}

	@Extension
	public static class DescriptorImpl extends StepDescriptor {
		@Override
		public String getFunctionName() {
			return "radargunreporting";
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
