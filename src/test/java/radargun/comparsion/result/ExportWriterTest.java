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
package radargun.comparsion.result;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.ResultRole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.SingleShotResult;
import org.openjdk.jmh.runner.BenchmarkListEntry;
import org.openjdk.jmh.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import radargun.Options;
import radargun.benchmarks.Record;
import radargun.shared.comparison.result.TestResult;
import radargun.shared.comparison.result.TestResultFactory;
import radargun.shared.comparison.result.TestWithoutAssertionResult;
import radargun.shared.model.Assertion;
import radargun.util.ExportUtil;

public class ExportWriterTest {
	private TestResultFactory testResultFactory;
	private Path directory;
	private XmlMapper xmlMapper;

	@Before
	public void setUp() throws Exception {
		this.testResultFactory = new TestResultFactory();
		this.directory = Paths.get(Options.DEFAULTEXPORTPATH);
		this.xmlMapper = new XmlMapper();
		xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

		xmlMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void writeAndRead() throws IOException {
		final RunResult runResult = this.createMockedRunResult(10.0);
		final BenchmarkListEntry benchmarkListEntry = new BenchmarkListEntry("RadarGunPerformanceTest",
				"test.RadarGunPerformanceTest", null, Mode.AverageTime, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, Optional.of(TimeUnit.NANOSECONDS), null, null);
		final Record record = new Record(benchmarkListEntry);
		record.addAssertion(Assertion.DUMMY);
		record.started(LocalDateTime.now());
		record.addRunResult(runResult);
		record.finished(LocalDateTime.now());
		final TestResult testResult = this.testResultFactory.create(record);
		final BufferedWriter xmlFile = ExportUtil.getOrCreateFile(directory, "test.xml");
		xmlMapper.writeValue(xmlFile, testResult);

		final TestResult readResult = new XmlMapper().readValue(Files.newInputStream(directory.resolve("test.xml"), StandardOpenOption.READ),
				TestWithoutAssertionResult.class);
		assertTrue(readResult instanceof TestWithoutAssertionResult);
	}

	private RunResult createMockedRunResult(final double value) {
		final IterationResult iterationResult = new IterationResult(null, null, null);
		iterationResult.addResult(new SingleShotResult(ResultRole.PRIMARY, "", (long) value, TimeUnit.NANOSECONDS));
		final Collection<IterationResult> iterationResults = Arrays.asList(iterationResult);
		final BenchmarkResult benchmarkResult = new BenchmarkResult(null, iterationResults);
		final Collection<BenchmarkResult> benchmarkResults = Arrays.asList(benchmarkResult);
		return new RunResult(null, benchmarkResults);
	}

}
