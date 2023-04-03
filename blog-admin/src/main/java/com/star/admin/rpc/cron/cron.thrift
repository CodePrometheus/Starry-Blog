// thrift -r -gen java cron.thrift

namespace java com.star.admin.rpc.cron

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

