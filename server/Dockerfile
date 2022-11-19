FROM adoptopenjdk/openjdk8
EXPOSE 8080
VOLUME /var/lib/abbkit/
ADD ./server-starter/target/abbkit.jar abbkit.jar
RUN sh -c 'touch /abbkit.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.config.location=/var/lib/abbkit/ /abbkit.jar" ]
