// thrift -r -gen go cron.thrift

namespace go cron

struct MongoCronReq {
    1: string tag
}

struct MongoCronResp {
    1: i64 len
    2: bool ok
}

service MongoCronService {
    MongoCronResp mongoCronService(1: MongoCronReq req)
}
