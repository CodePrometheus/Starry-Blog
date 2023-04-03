package pool

import (
	"errors"
	"github.com/apache/thrift/lib/go/thrift"
	"github.com/miekg/dns"
	"net"
	"strconv"
)

type ResourcePool struct {
	name              string
	host              string
	port              string
	creationFunc      ClientCreationFunc
	closeFunc         ClientCloseFunc
	maxSize           int
	getTimeout        int
	allocatedResource int
	idleList          chan interface{}
}

// ClientCreationFunc is the function used for creating new client.
type ClientCreationFunc func(host, port string) (interface{}, error)

// ClientCloseFunc is the function used for closing client.
type ClientCloseFunc func(interface{}) error

func NewResourcePool(host, port string, fnCreation ClientCreationFunc,
	fnClose ClientCloseFunc, maxSize, getTimeout int) (*ResourcePool, error) {
	pool := ResourcePool{
		maxSize:           maxSize,
		host:              host,
		port:              port,
		creationFunc:      fnCreation,
		closeFunc:         fnClose,
		getTimeout:        getTimeout,
		idleList:          make(chan interface{}, maxSize),
		allocatedResource: 0,
	}
	return &pool, nil
}

// CreateThriftPool function creates a connection for specified thrift servie.
func CreateThriftPool(protocolType, transportType, endpoint string,
	clientFunc func(thrift.TTransport, thrift.TProtocolFactory) interface{},
	poolSize int, timeoutSecond int) (*ResourcePool, error) {
	pool, err := NewResourcePool("", "", func(host, port string) (interface{}, error) {
		host, port, err := ResolveSRV(endpoint)
		if err != nil {
			return nil, err
		}

		protocolFactory, transport, err := CreateThriftClient(protocolType, transportType, host, port)

		if err != nil {
			return nil, err
		}
		err = transport.Open()
		client := clientFunc(transport, protocolFactory)
		return client, err
	}, func(interface{}) error {
		return nil
	}, poolSize, timeoutSecond)
	return pool, err
}

// CreateThriftClient creates a thrift client.
func CreateThriftClient(protocolType, transportType, host, port string) (
	protocolFactory thrift.TProtocolFactory, transport thrift.TTransport, err error) {

	switch protocolType {
	case "binary":
		protocolFactory = thrift.NewTBinaryProtocolFactoryDefault()
	case "json":
		protocolFactory = thrift.NewTJSONProtocolFactory()
	default:
		err = errors.New("unsupported protocol " + protocolType)
	}
	if err != nil {
		return nil, nil, err
	}
	switch transportType {
	case "socket":
		endpoint := host + ":" + port
		transport, err = thrift.NewTSocket(endpoint)
	default:
		err = errors.New("unsupported transport " + transportType)
	}
	return
}

// CreateThriftServer creates a thrift server.
func CreateThriftServer(protocolType, transportType, endpoint string, processor thrift.TProcessor) (
	server *thrift.TSimpleServer, err error) {
	host, port, err := ResolveSRV(endpoint)
	if err != nil {
		return nil, err
	}
	transportFactory := thrift.NewTTransportFactory()

	var protocolFactory thrift.TProtocolFactory
	switch protocolType {
	case "binary":
		protocolFactory = thrift.NewTBinaryProtocolFactoryConf(&thrift.TConfiguration{})
	case "json":
		protocolFactory = thrift.NewTJSONProtocolFactory()
	case "compact":
		protocolFactory = thrift.NewTCompactProtocolFactoryConf(&thrift.TConfiguration{})
	case "simplejson":
		protocolFactory = thrift.NewTSimpleJSONProtocolFactoryConf(&thrift.TConfiguration{})
	default:
		err = errors.New("unsupported protocol " + protocolType)
	}
	if err != nil {
		return nil, err
	}

	var transport thrift.TServerTransport
	switch transportType {
	case "socket":
		endpoint := host + ":" + port
		transport, err = thrift.NewTServerSocket(endpoint)
	default:
		err = errors.New("unsupported transport " + transportType)
	}
	if err != nil {
		return nil, err
	}

	server = thrift.NewTSimpleServer4(processor, transport, transportFactory, protocolFactory)
	return server, err
}

func ResolveSRV(record string) (ip string, port string, err error) {
	// try split first, since the input may be a host:port combination
	ip, port, err = net.SplitHostPort(record)
	if err == nil {
		return
	}
	config, _ := dns.ClientConfigFromFile("/etc/resolv.conf")

	qType, _ := dns.StringToType["SRV"]
	name := dns.Fqdn(record)

	client := &dns.Client{Net: ""}
	msg := &dns.Msg{}
	msg.SetQuestion(name, qType)
	response, _, err := client.Exchange(msg, config.Servers[0]+":"+config.Port)
	if err != nil {
		return "", "", err
	}
	srvs := make([]net.SRV, 0)
	for _, v := range response.Answer {
		if srv, ok := v.(*dns.SRV); ok {
			target := srv.Target
			for _, v := range response.Extra {
				// check if there is additional A record for the node
				if a, ok := v.(*dns.A); ok && a.Hdr.Name == target {
					target = a.A.String()
				}
			}
			srvs = append(srvs, net.SRV{
				Priority: srv.Priority,
				Weight:   srv.Weight,
				Port:     srv.Port,
				Target:   target,
			})
		}
	}

	if len(srvs) == 0 {
		return "", "", errors.New("no record found for SRV")
	}

	return srvs[0].Target, strconv.Itoa(int(srvs[0].Port)), nil
}
