# https://blog.csdn.net/vbhfdghff/article/details/123234816
# 里面的buildcontext就是docker build是提交的目录（这个目录，包含子目录）
#       -> docker build --network=abbkit -t abbkit:0.0.1 .
# 容器构建过程中，执行命令并创建新的镜像层
# 注意：多个 RUN 命令时，最好通过 && 进行合并操作，要不然，在 docker 构建时，每个 RUN 会生成一层，造成容器臃肿变大
# RUN echo "http://mirrors.aliyun.com/alpine/latest-stable/main/" > /etc/apk/repositories && \
#   echo "http://mirrors.aliyun.com/alpine/latest-stable/community/" >> /etc/apk/repositories && \
#   apk update && \
#   apk add --no-cache gcc libc-dev make openssl-dev pcre-dev zlib-dev linux-headers curl && \
FROM adoptopenjdk/openjdk8
# 对外暴露的端口号
EXPOSE 8080
# 挂载数据卷, 宿主机可以定义一个本地文件路径映射到这个挂载卷路径上
#   -> /data/docker/abbkit/:/var/lib/abbkit/
VOLUME /var/lib/abbkit/
# 将宿主目录中的文件拷贝到镜像中
ADD allure-commandline-2.20.1.tar /allure-commandline-2.20.1
# ADD 不仅能复制宿主主机中的文件，还能是URL指向的文件。而且如果指向的文件是tar压缩包，在复制到容器后，会进行自动解压
ADD /server-starter/target/abbkit.jar abbkit.jar
RUN sh -c 'touch /abbkit.jar'
# 定义系统环境变量
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseConcMarkSweepGC -XX:ReservedCodeCacheSize=32m -XX:CompressedClassSpaceSize=32m -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=128m  -Dfile.encoding=utf-8"
# -cp 在jar作为启动时候是不起作用的，使用的是Main-Class of MANIFEST.MF
# springboot-> Main-Class: org.springframework.boot.loader.JarLauncher
#               -> Start-Class: com.abbkit.project.Application
# -Dlogging.level.root=DEBUG 启动日志参数
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dloader.path=/var/lib/abbkit/config -Dspring.config.location=/var/lib/abbkit/config/  -jar /abbkit.jar" ]
