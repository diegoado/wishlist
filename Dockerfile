FROM gradle:8.8-jdk21 AS build

WORKDIR /app

ADD . .

RUN gradle build -x test -x check

FROM amazoncorretto:21-alpine3.19-jdk AS corretto-jdk

RUN apk add --no-cache binutils && \
    $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /customjre

FROM alpine:3.19

MAINTAINER mt-inss-team
LABEL source="https://github.com/diegoado/wishlist" maintainer="diegoado"

ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV TZ=America/Sao_Paulo

ARG APPLICATION_USER=appuser

COPY --from=corretto-jdk /customjre $JAVA_HOME
COPY --from=build /app/build/distributions/*.zip ./wishlist.zip

RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER && \
    apk add --no-cache tzdata && \
    cp -r -f /usr/share/zoneinfo/$TZ /etc/localtime && \
    mkdir /app && \
    unzip wishlist && \
    rm -rf *.zip && \
    mv wishlist* /app/wishlist && \
    chown -R $APPLICATION_USER /app

WORKDIR /app

USER $APPLICATION_USER

EXPOSE 8080
ENTRYPOINT [ "wishlist/bin/wishlist" ]
