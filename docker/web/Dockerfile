FROM amazoncorretto:8

WORKDIR /webapp

RUN mkdir ../tmp/importTmp
RUN mkdir ../tmp/exportTmp
RUN chmod 777 ../tmp/importTmp
RUN chmod 777 ../tmp/exportTmp

COPY webapp/lib ./lib
COPY webapp/Info-21-v2.0-Web-0.0.1-SNAPSHOT-plain.jar .

CMD ["java", "-jar", "Info-21-v2.0-Web-0.0.1-SNAPSHOT-plain.jar"]