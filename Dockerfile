# 使用官方的Java运行时基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将打包好的jar文件复制到镜像中
COPY B5-0.0.1-SNAPSHOT.jar /app/app.jar

# 暴露端口，与你的SpringBoot项目中配置的端口一致
EXPOSE 8080

# 启动容器时运行的命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
