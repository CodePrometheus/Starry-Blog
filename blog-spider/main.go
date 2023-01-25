package main

import (
	"blog-spider/router"
	"context"
	"github.com/pelletier/go-toml"
	"log"
	"net/http"
	"os"
	"os/signal"
	"time"
)

func main() {
	config, err := toml.LoadFile("./config/blog-conf-dev.toml")
	if err != nil {
		panic(err)
	}
	engine := router.InitRouter(config)

	srv := &http.Server{
		Addr:    ":" + config.Get("server.port").(string),
		Handler: engine,
	}
	go func() {
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("listen: %s\n", err)
		}
	}()

	quit := make(chan os.Signal)
	signal.Notify(quit, os.Interrupt)
	<-quit
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {
		log.Fatal("Server Shutdown: ", err)
	}
}
