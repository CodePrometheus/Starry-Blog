# docker build .
FROM docker.elastic.co/elasticsearch/elasticsearch:8.5.2

ENV VERSION=8.5.2

ADD https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v${VERSION}/elasticsearch-analysis-ik-${VERSION}.zip /tmp/

USER root

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install -b file:////tmp/elasticsearch-analysis-ik-$VERSION.zip

RUN rm -rf /tmp/elastic*
