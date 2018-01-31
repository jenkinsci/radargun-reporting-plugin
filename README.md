# RadarGun Plugin for Jenkins to report and visualize performance tests
RadarGun (https://github.com/SoerenHenning/RadarGun) is a performance testing framework developed at the university of Kiel.
To use this RadarGun in Jenkins' build pipeline, we developed this plugin. 

## Usage
This plugin can be activated as post build step.

IMPORTANT!! RadarGun itself has to be started via a shell script. Therefor, create a shell script build step and copy:
```
java -cp ${JENKINS_HOME}/plugins/radargun-reporting/WEB-INF/lib/radargun-2.0.0-SNAPSHOT.jar:${WORKSPACE}/target/benchmarks.jar radargun.RadarGun --cp-assertions assertions/assertions.yaml --jmh-output
```

## Configuration
Following soon.

## Build
- via Maven 3.3 or above (necessary for pom-less building):
```
mvn clean package
```
