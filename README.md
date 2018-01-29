# RadarGun Plugin for Jenkins to report and visualize performance tests
RadarGun is a performance testing framework developed at the university of Kiel.
To use this plugin in Jenkins' build pipeline, we developed this plugin.

## Usage
This plugin can be activated as post build step.

IMPORTANT!! RadarGun itself has to be started via a shell script. Therefor, create a shell script build step and copy:
java -cp ${JENKINS_HOME}/plugins/radargun-reports/WEB-INF/lib/radargun-2.0.0-SNAPSHOT.jar:${WORKSPACE}/target/benchmarks.jar radargun.RadarGun --cp-assertions assertions/se-jenkins.yaml --jmh-output

## Configuration
The plugin expectes a dedicated properties file in the directory ".settings" of the Eclipse project, you like to check for issues. This file is created automatically upon the first execution of one of the run commands. Afterwards, you can change the default settings, such as the path to the tool's xml configuration file and the path(s) to the jar file(s) containing your custom rules/checks.

## Build
- via Maven 3.3 or above (necessary for pom-less building):
```
mvn clean package
```
