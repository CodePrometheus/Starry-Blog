@echo off
echo "===Solr 启动==="
start cmd /k "cd/d D:\solr-8.9.0\bin &&solr start"
echo "===Solr 启动成功==="

echo "===RabbitMQ 启动==="
set RABBITMQ_HOME=D:\RabbitMQ\rabbitmq_server-3.9.7
start %RABBITMQ_HOME%/sbin/rabbitmq-server.bat
echo "===RabbitMQ 启动成功==="

echo "===Redis 启动==="
set REDIS_HOME=D:\Redis
start %REDIS_HOME%/redis-server.exe
echo "===Redis 启动成功==="

echo "===Elasticsearch 启动==="
set ES_HOME=D:\Elasticsearch\elasticsearch-7.12.0
start %ES_HOME%/bin/elasticsearch.bat
echo "===Elasticsearch 启动成功==="
