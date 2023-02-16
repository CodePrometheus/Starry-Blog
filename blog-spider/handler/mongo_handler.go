package handler

import (
	"blog-spider/constant"
	"blog-spider/logger"
	"blog-spider/middleware/mongodb"
	"blog-spider/model"
	"blog-spider/utils"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func MongoNewsHandler(tag string) ([]model.HotNews, error) {
	var news model.MongoHotNews
	nowHour := utils.GetCurrentHour()
	connection := mongodb.NewConnection(constant.DateBase, constant.Collection)
	filter := bson.D{
		{"datetime", nowHour},
		{"tag", tag},
	}
	ret, err := connection.FindOne(filter)
	if err != nil {
		logger.Log.Error("MongoHandler find mongo data error: ", err)
		return nil, err
	}
	monData, err := bson.Marshal(ret)
	err = bson.Unmarshal(monData, &news)
	if err != nil {
		logger.Log.Error("MongoHandler bson M/Un error: ", err)
		return nil, err
	}
	return news.Data, nil
}

func AddMongoData(news []model.HotNews, tag string) {
	nowHour := utils.GetCurrentHour()
	var mongoHotNews = &model.MongoHotNews{
		DateTime: nowHour,
		Tag:      tag,
		Data:     news,
	}
	mongoHotNews.ID = primitive.NewObjectID()
	connection := mongodb.NewConnection(constant.DateBase, constant.Collection)
	connection.InsertOne(mongoHotNews)
}
