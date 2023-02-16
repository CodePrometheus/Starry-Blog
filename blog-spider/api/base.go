package api

import (
	"blog-spider/api/weibo"
	"blog-spider/api/zhihu"
	"blog-spider/model"
	"blog-spider/utils/result"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

type Spider interface {
	DoProcess() []model.HotNews
	SearchData() []model.HotNews
}

func HandlerSpider(c *gin.Context) {
	item, err := strconv.Atoi(c.Query("item"))
	if err != nil {
		c.JSON(http.StatusOK, result.ParamCheckErr.AjaxResult(nil))
		return
	}
	switch item {
	case 0:
		weiboSpider := weibo.WeiboSpider{}
		c.JSON(http.StatusOK, result.SUCCESS.AjaxResult(weiboSpider.DoProcess()))
		return
	case 1:
		zhihuSpider := zhihu.ZhihuSpider{}
		c.JSON(http.StatusOK, result.SUCCESS.AjaxResult(zhihuSpider.DoProcess()))
		return
	default:
	}
}
