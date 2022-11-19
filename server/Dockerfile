FROM adoptopenjdk/openjdk8
EXPOSE 8080
VOLUME /var/lib/abbkit/
ADD /server-starter/target/abbkit.jar abbkit.jar
RUN sh -c 'touch /abbkit.jar'
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseConcMarkSweepGC -XX:ReservedCodeCacheSize=32m -XX:CompressedClassSpaceSize=32m -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=128m  -Dfile.encoding=utf-8"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dloader.path=/var/lib/abbkit/config/ -Dspring.config.location=/var/lib/abbkit/config/ -jar /abbkit.jar" ]
