FROM egovio/jboss:latest

MAINTAINER Sonny Garcia <sonnygarcia@icloud.com>

# copy src code into image
COPY egov /usr/local/src/egov

# set the context for the rest of the cmds
WORKDIR /usr/local/src/egov

# expose envvars used to control runtime behavior
ENV REST_HOST=egov-pgrrest

# build artifacts using maven
RUN mvn package -DskipTests

# deploy application into jboss
RUN DEPLOY_DIR=$JBOSS_HOME/standalone/deployments \
 && cp egov-ear/target/egov-ear-*-SNAPSHOT.ear $DEPLOY_DIR \
 && cd $DEPLOY_DIR \
 && touch $(ls egov-ear-*-SNAPSHOT.ear).dodeploy

# NOTE: the two 'RUN' commands can probably be combined inside of a single
# script (i.e. RUN build-and-install-app.sh) so that we can also clean up the
# extra files created during the `mvn package' command. that step inflates the
# resultant image by almost 1.0GB.
