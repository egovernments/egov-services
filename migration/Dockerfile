FROM egovio/openjdk:8-jre-headless

MAINTAINER Satyam<satyam.ashish@egovernments.org>

# advertise jboss service port
EXPOSE 8080 7777

# copy pre-built JAR into image
#
# INSTRUCTIONS ON HOW TO BUILD JAR:
# Move to the location where pom.xml is exist in project and build project using below command
# "mvn clean package"
COPY /target/egov-migrations-0.0.1-SNAPSHOT.jar /opt/egov/egov-migrations.jar


# launch standalone webapp
CMD ["java","-jar", "/opt/egov/egov-migrations.jar"]
#CMD ["java", "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8787", "-jar", "/opt/egov/egov-migrations.jar"]

# NOTE: the two 'RUN' commands can probably be combined inside of a single
# script (i.e. RUN build-and-install-app.sh) so that we can also clean up the
# extra files created during the `mvn package' command. that step inflates the
# resultant image by almost 1.0GB.
