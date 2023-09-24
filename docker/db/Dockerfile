FROM postgres:latest

COPY sql_scripts/part1.sql /docker-entrypoint-initdb.d/
COPY sql_scripts/part2.sql /docker-entrypoint-initdb.d/
COPY sql_scripts/part3.sql /docker-entrypoint-initdb.d/

RUN mkdir tmp/importTmp
RUN mkdir tmp/exportTmp
RUN chmod 777 tmp/importTmp
RUN chmod 777 tmp/exportTmp

ENV POSTGRES_DB=info21-web
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=123