@echo off
echo "===Solr ����==="
start cmd /k "cd/d D:\solr-8.9.0\bin &&solr start"
echo "===Solr �����ɹ�==="

echo "===RabbitMQ ����==="
set RABBITMQ_HOME=D:\RabbitMQ\rabbitmq_server-3.9.7
start %RABBITMQ_HOME%/sbin/rabbitmq-server.bat
echo "===RabbitMQ �����ɹ�==="

echo "===Redis ����==="
set REDIS_HOME=D:\Redis
start %REDIS_HOME%/redis-server.exe
echo "===Redis �����ɹ�==="

echo "===Elasticsearch ����==="
set ES_HOME=D:\Elasticsearch\elasticsearch-7.12.0
start %ES_HOME%/bin/elasticsearch.bat
echo "===Elasticsearch �����ɹ�==="
