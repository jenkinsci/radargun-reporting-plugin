package de.cau.se.jenkins.radargun.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import de.cau.se.jenkins.radargun.action.model.AggregatedPackageResult;
import hudson.model.Action;
import jenkins.model.TransientActionFactory;

public class RadarGunProjectActionFactory extends TransientActionFactory<RadarGunProjectAction>  {

	@Override
	public Collection<? extends Action> createFor(@Nonnull RadarGunProjectAction radarGunProjectAction) {
		final List<Action> result = new ArrayList<>();
		for (AggregatedPackageResult action : radarGunProjectAction.getPackageResults()) {
			result.add((Action) action);
		}

		return result;
	}

	@Override
	public Class<RadarGunProjectAction> type() {
		return RadarGunProjectAction.class;
	}

}
