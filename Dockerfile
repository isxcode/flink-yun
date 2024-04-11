FROM openjdk:8

RUN rm -rf /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

VOLUME /etc/zhiliuyun/conf
VOLUME /var/lib/zhiliuyun

ARG ADMIN_PASSWORD='admin123'
ARG ACTIVE_ENV='docker'
ARG LOG_LEVEL='info'

COPY ./flink-yun-backend/flink-yun-main/build/libs/zhiliuyun.jar /opt/zhiliuyun/zhiliuyun.jar
COPY ./flink-yun-backend/flink-yun-main/src/main/resources/application-docker.yml /etc/zhiliuyun/conf/
COPY ./resources/jdbc/system /var/lib/zhiliuyun/jdbc/system

EXPOSE 8080

ENV ADMIN_PASSWORD=${ADMIN_PASSWORD}
ENV ACTIVE_ENV=${ACTIVE_ENV}
ENV LOG_LEVEL=${LOG_LEVEL}

CMD java -jar /opt/zhiliuyun/zhiliuyun.jar --logging.level.root=${LOG_LEVEL} --spring.profiles.active=${ACTIVE_ENV} --isx-app.admin-passwd=${ADMIN_PASSWORD} --spring.config.additional-location=/etc/zhiliuyun/conf/