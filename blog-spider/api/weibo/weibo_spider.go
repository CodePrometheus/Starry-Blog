package weibo

import (
	"blog-spider/logger"
	"blog-spider/model"
	"context"
	"github.com/PuerkitoBio/goquery"
	"github.com/chromedp/chromedp"
	"strconv"
	"strings"
	"time"
)

const (
	weiboUrl      = "https://s.weibo.com/top/summary?cate=realtimehot"
	realTimeHotID = "#pl_top_realtimehot"
)

func HandleWeiboHot() []model.HotNews {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	ctx, cancel = chromedp.NewContext(ctx)
	defer cancel()

	var content string
	if err := chromedp.Run(ctx, spiderWeibo(&content)); err != nil {
		logger.Log.Error("err: ", err)
		return nil
	}
	return genSearchResp(content)
}

func genSearchResp(content string) []model.HotNews {
	doc, err := goquery.NewDocumentFromReader(strings.NewReader(content))
	if err != nil {
		logger.Log.Error("Load the HTML document failed")
	}
	resp := make([]model.HotNews, 0, 50)
	doc.Find("#pl_top_realtimehot > table > tbody > tr").
		Each(func(i int, selection *goquery.Selection) {
			rankStr := selection.Find("td").Eq(0).Text()
			rank, err := strconv.Atoi(rankStr)
			if err != nil {
				return
			}
			content := selection.Find("td").Eq(1).Find("a").Text()
			url := "https://s.weibo.com" + selection.Find("td").Eq(1).Find("a").
				AttrOr("href", "/weibo?="+content)
			des := selection.Find("td").Eq(1).Find("span").Text()
			icon := selection.Find("td").Eq(2).Text()
			resp = append(resp, model.HotNews{
				ID:    rank,
				Url:   url,
				Title: content,
				Des:   des,
				Icon:  icon,
			})
		})
	return resp
}

func spiderWeibo(content *string) chromedp.Action {
	return chromedp.Tasks{
		chromedp.Navigate(weiboUrl),                                  // 将浏览器导航到某个页面
		chromedp.WaitVisible(realTimeHotID, chromedp.ByQuery),        // 等候某个元素可见，再继续执行
		chromedp.OuterHTML(realTimeHotID, content, chromedp.ByQuery), // 获取元素的outer html
	}
}
