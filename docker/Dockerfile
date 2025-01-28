FROM quay.io/wildfly/wildfly:26.1.3.Final-jdk17

# Add admin user
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

# Set environment variables
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_USER=postgres
ENV DB_PASS=password
ENV DB_NAME=communications
ENV DATASOURCE_NAME=PostgresDS
ENV DATASOURCE_JNDI=java:/PostgresDS

RUN curl -f -o /tmp/postgresql-42.7.2.jar https://jdbc.postgresql.org/download/postgresql-42.7.2.jar
COPY docker/start-wildfly.sh /opt/jboss/wildfly/bin/
COPY target/communication-app.war /tmp/

USER root
RUN chmod +x /opt/jboss/wildfly/bin/start-wildfly.sh
RUN chown -R jboss:jboss /tmp /opt/jboss/wildfly/standalone/deployments
RUN chmod -R 775 /tmp /opt/jboss/wildfly/standalone/deployments
RUN chmod 644 /tmp/postgresql-42.7.2.jar
USER jboss

EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/start-wildfly.sh"]