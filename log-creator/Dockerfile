FROM openjdk:8

COPY ./build/libs/log-creator-0.0.1-SNAPSHOT.jar /
WORKDIR /
CMD ["java", "-jar", "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap", "log-creator-0.0.1-SNAPSHOT.jar"]