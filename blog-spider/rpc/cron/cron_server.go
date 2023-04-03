package cron

import (
	"blog-spider/config"
	"blog-spider/handler"
	"blog-spider/logger"
	"blog-spider/rpc/cron/gen-go/cron"
	"blog-spider/rpc/pool"
	"context"
	"fmt"
)

type MongoCronService interface {
	MongoCronService(ctx context.Context, req *cron.MongoCronReq) (resp *cron.MongoCronResp, err error)
}

type MongoCron struct{}

func NewMongoCron() *MongoCron {
	return &MongoCron{}
}

func (m *MongoCron) MongoCronService(ctx context.Context, req *cron.MongoCronReq) (resp *cron.MongoCronResp, err error) {
	resp = &cron.MongoCronResp{}
	fmt.Println("MongoCronService Server recv ===")
	// resp.Len, resp.Ok = genMongoCronData(req.Tag)
	return resp, nil
}

func HandlerCronToMongo() {
	server, err := pool.CreateThriftServer(
		"binary", "socket",
		fmt.Sprintf("%s:%s", config.Conf.Thrift.Server.Host,
			config.Conf.Thrift.Server.Port,
		),
		cron.NewMongoCronServiceProcessor(NewMongoCron()))
	if err != nil {
		logger.Log.Error(err)
	}
	server.Serve()
}

func genMongoCronData(tag string) (int64, bool) {
	res, err := handler.MongoNewsHandler(tag)
	return int64(len(res)), err == nil
}
