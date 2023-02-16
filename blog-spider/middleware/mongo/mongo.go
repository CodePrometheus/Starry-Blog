package mongo

import (
	"blog-spider/config"
	"blog-spider/logger"
	"context"
	"fmt"
	"github.com/qiniu/qmgo"
	opts "github.com/qiniu/qmgo/options"
	"go.mongodb.org/mongo-driver/bson"
	"sync"
)

var (
	mongoCli *qmgo.QmgoClient
	once     sync.Once
	ctx      = context.TODO()
	err      error
	database *qmgo.Database
)

func InitMongo() {
	once.Do(func() {
		mongoCli, err = qmgo.Open(
			ctx,
			&qmgo.Config{
				Uri: fmt.Sprintf("mongodb://%s:%d", config.Conf.Mongo.Host, config.Conf.Mongo.Port),
				Auth: &qmgo.Credential{
					Username: config.Conf.Mongo.Username,
					Password: config.Conf.Mongo.Password,
				},
			},
		)
		if err != nil {
			logger.Log.Error("mongo init failed: ", err)
			panic(err)
		}
		logger.Log.Info("mongo init success")
	})
}

// ========== CRUD ==========

func InsertOne(doc interface{}, opts ...opts.InsertOneOptions) (result *qmgo.InsertOneResult, err error) {
	return mongoCli.InsertOne(ctx, doc, opts...)
}

func DeleteOne(filter interface{}, opts ...opts.RemoveOptions) (err error) {
	return mongoCli.Remove(ctx, filter, opts...)
}

func UpdateOne(filter interface{}, update interface{}, opts ...opts.UpdateOptions) (err error) {
	return mongoCli.UpdateOne(ctx, filter, update, opts...)
}

func FindAll(filter interface{}, opts ...opts.FindOptions) qmgo.QueryI {
	return mongoCli.Find(ctx, filter, opts...).Select(bson.M{"content": 0})
}

func FindOne(filter interface{}, opts ...opts.FindOptions) qmgo.QueryI {
	return mongoCli.Find(ctx, filter, opts...)
}
