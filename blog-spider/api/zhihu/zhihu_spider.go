package zhihu

import (
	"blog-spider/constant"
	"blog-spider/handler"
	"blog-spider/logger"
	"blog-spider/model"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strconv"
	"strings"
)

const (
	zhihuUrl = "https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=100"
)

type ZhihuSpider struct {
}

func (zhihu *ZhihuSpider) DoProcess() []model.HotNews {
	newsHandler, err := handler.MongoNewsHandler(constant.Zhihu)
	if err != nil {
		logger.Log.Error("HandlerZhihuHot failed: ", err, ", begin search data")
		zhihuRes := zhihu.SearchData()
		handler.AddMongoData(zhihuRes, constant.Zhihu)
		return zhihuRes
	}
	return newsHandler
}

func (zhihu *ZhihuSpider) SearchData() []model.HotNews {
	var ret []model.HotNews
	resp, err := http.Get(zhihuUrl)
	if err != nil {
		logger.Log.Error("err: ", err)
		return nil
	}
	defer resp.Body.Close()
	if resp.StatusCode == 200 {
		body, _ := io.ReadAll(resp.Body)
		var list model.HotList
		if err := json.Unmarshal(body, &list); err == nil {
			ret = genSearchResp(list)
		}
	}
	return ret
}

func genSearchResp(list model.HotList) []model.HotNews {
	resp := make([]model.HotNews, 0, 50)
	for _, v := range list.Data {
		rankStr := strings.Split(v.RankWithTime, "_")[0]
		rank, _ := strconv.Atoi(rankStr)
		resp = append(resp, model.HotNews{
			ID:    rank + 1,
			Title: v.Target.Title,
			Url:   fmt.Sprintf("https://www.zhihu.com/question/%v", v.Target.ID),
			Des:   v.DetailText,
		})
	}
	return resp
}
