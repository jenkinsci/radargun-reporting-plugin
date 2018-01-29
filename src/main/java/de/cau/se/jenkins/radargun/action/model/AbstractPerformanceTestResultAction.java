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
