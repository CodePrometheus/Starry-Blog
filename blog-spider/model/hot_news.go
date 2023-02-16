package model

import "go.mongodb.org/mongo-driver/bson/primitive"

type HotNews struct {
	ID    int    `json:"id"`
	Url   string `json:"url"`
	Title string `json:"title"`
	Des   string `json:"des"`
	Icon  string `json:"icon"`
}

type HotList struct {
	Data []Item `json:"data"`
}

type Item struct {
	RankWithTime string  `json:"id"`
	Target       HotNews `json:"target"`
	DetailText   string  `json:"detail_text"`
}

type MongoHotNews struct {
	ID       primitive.ObjectID `bson:"_id" json:"_id"`
	Tag      string             `bson:"tag" json:"tag"`
	DateTime string             `bson:"datetime" json:"datetime"`
	Data     []HotNews          `bson:"data" json:"data"`
}
