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

import hudson.model.Run;
import jenkins.model.RunAction2;

public abstract class AbstractPerformanceTestResultAction<T extends AbstractPerformanceTestResultAction>
		implements RunAction2 {

	public Run<?, ?> run;

	protected AbstractPerformanceTestResultAction() {
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIconFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrlName() {
		return "radargunReport";
	}

	@Override
	public void onAttached(final Run<?, ?> run) {
		this.run = run;
	}

	@Override
	public void onLoad(final Run<?, ?> run) {
		this.run = run;
	}

	public abstract int getFailCount();

	public int getSkipCount() {
		return 0;
	}

	public abstract int getTotalCount();

	public Run<?, ?> getRun() {
		return this.run;
	}

}
