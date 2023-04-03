package tests

import (
	"blog-spider/logger"
	"blog-spider/rpc/hello/gen-go/hello"
	"context"
	"github.com/apache/thrift/lib/go/thrift"
	"log"
	"os"
	"testing"
)

func TestThriftClient(t *testing.T) {
	transportFactory := thrift.NewTBufferedTransportFactory(1024)
	thrift.NewTBinaryProtocolFactoryConf(&thrift.TConfiguration{})
	transport := thrift.NewTSocketConf("127.0.0.1:9000", &thrift.TConfiguration{})
	transportFactory.GetTransport(transport)
	protocolTransport := thrift.NewTBinaryProtocolTransport(transport)
	protocol := thrift.NewTMultiplexedProtocol(protocolTransport, "helloService")
	c := hello.NewHelloServiceClient(thrift.NewTStandardClient(protocol, protocol))

	if err := transport.Open(); err != nil {
		logger.Log.Error("Error opening socket to 127.0.0.1:9000, err: ", err)
		os.Exit(1)
	}
	defer transport.Close()
	req := &hello.HelloReq{Msg: "Hello Server ~"}
	res, err := c.Hello(context.Background(), req)
	if err != nil {
		log.Printf("Echo failed: %v", err)
		return
	}
	log.Printf("response: %v", res.Msg)
}
