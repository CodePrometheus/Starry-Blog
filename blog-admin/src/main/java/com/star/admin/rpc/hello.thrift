namespace java com.star.admin.rpc

struct HelloReq {
    1: string msg;
}

struct HelloResp {
    1: string msg;
}

service HelloService {
    HelloResp Hello(1: HelloReq req);
}
