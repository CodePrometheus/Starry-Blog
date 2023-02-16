package main

import (
	"blog-spider/config"
	"blog-spider/logger"
	"blog-spider/middleware/mongo"
	"blog-spider/router"
	"context"
	"net/http"
	"os"
	"os/signal"
	"time"
)

func main() {
	config.InitConfig()           // 初始化 config
	logger.InitLogger()           // 初始化日志
	mongo.InitMongo()             // 初始化 mongo
	engine := router.InitRouter() // 初始化路由

	srv := &http.Server{
		Addr:    ":" + config.Conf.System.Port,
		Handler: engine,
	}
	go func() {
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Log.Error("listen: %s\n", err)
		}
	}()

	quit := make(chan os.Signal)
	signal.Notify(quit, os.Interrupt)
	<-quit
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {
		logger.Log.Error("Server Shutdown: ", err)
	}
}
