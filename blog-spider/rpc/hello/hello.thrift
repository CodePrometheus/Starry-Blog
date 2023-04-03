// thrift -r -gen go hello.thrift

namespace go hello

struct HelloReq {
    1: string msg;
}

struct HelloResp {
    1: string msg;
}

service HelloService {
    HelloResp Hello(1: HelloReq req);
}
