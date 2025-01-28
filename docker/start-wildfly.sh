#!/bin/bash

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

function wait_for_server() {
  until `$JBOSS_CLI -c "ls /deployment" &> /dev/null`; do
    sleep 1
  done
}

echo "Starting WildFly..."
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

echo "Waiting for server to start..."
wait_for_server

echo "Configuring datasource..."
$JBOSS_CLI -c << EOF
batch
module add --name=org.postgres --resources="/tmp/postgresql-42.7.2.jar" --dependencies=javax.api,javax.transaction.api

/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name="org.postgresql.Driver")

data-source add \
    --name=$DATASOURCE_NAME \
    --jndi-name=$DATASOURCE_JNDI \
    --driver-name=postgres \
    --connection-url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
    --user-name=$DB_USER \
    --password=$DB_PASS \
    --use-java-context=true \
    --max-pool-size=25 \
    --blocking-timeout-wait-millis=5000 \
    --enabled=true

run-batch
EOF

echo "Deploying application..."
mv /tmp/communication-app.war $JBOSS_HOME/standalone/deployments/

echo "WildFly configuration complete"
tail -f $JBOSS_HOME/standalone/log/server.log