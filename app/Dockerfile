FROM openjdk:11.0.12-jre

RUN mkdir /app
COPY build/install/app /app

WORKDIR /app

ENTRYPOINT ["/app/bin/app"]
