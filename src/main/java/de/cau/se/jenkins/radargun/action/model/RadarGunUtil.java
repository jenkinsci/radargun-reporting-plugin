package de.cau.se.jenkins.radargun.action.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hudson.Functions;
import jenkins.model.Jenkins;
import radargun.shared.comparison.result.TestResult;
import radargun.shared.model.AggregatedTestResult;

public final class RadarGunUtil {
	public static final String resURL = Jenkins.get().getRootUrl() + Functions.getResourcePath();
	
	private RadarGunUtil() {
	}

	public static Multimap<String, TestResult> createPackageStructure(AggregatedTestResult aggregatedTestResult) {
		final Multimap<String, TestResult> packageStructure = HashMultimap.create();

		for (TestResult testResult : aggregatedTestResult.getTestResults()) {
			packageStructure.put(testResult.getPackage(), testResult);
		}

		return packageStructure;
	}
	
	public static LocalTime difference(final LocalDateTime start, final LocalDateTime finish) {
		long dur = Duration.between(start, finish).toNanos();
		
		return LocalTime.ofNanoOfDay(dur);
	}
}
