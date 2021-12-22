FROM gradle:7.3.0-jdk11-alpine AS builder

COPY --chown=gradle:gradle ./ /tmp/app/

RUN cd /tmp/app && gradle clean assemble --no-daemon

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.13_8

ENV USER_NAME test_user
ENV PASSWORD test_password

VOLUME /tmp

RUN mkdir -p /usr/ever-changing
WORKDIR /usr/ever-changing

COPY --from=builder tmp/app/build/libs/medication-service-*.jar medication-service.jar

CMD java -jar medication-service.jar