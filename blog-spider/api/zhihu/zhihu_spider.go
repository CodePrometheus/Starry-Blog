package zhihu

import (
	"blog-spider/model"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strconv"
	"strings"
)

const (
	zhihuUrl = "https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=100"
)

func HandleZhihuHot() []model.HotNews {
	resp, err := http.Get(zhihuUrl)
	if err != nil {
		log.Println("err: ", err)
		return nil
	}
	defer resp.Body.Close()
	if resp.StatusCode == 200 {
		body, _ := io.ReadAll(resp.Body)
		var list model.HotList
		if err := json.Unmarshal(body, &list); err == nil {
			return genResp(list)
		}
	}
	return nil
}

func genResp(list model.HotList) []model.HotNews {
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
