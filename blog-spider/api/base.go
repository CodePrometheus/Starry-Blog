package api

import (
	"blog-spider/api/weibo"
	"blog-spider/api/zhihu"
	"blog-spider/utils/result"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

func HandleSpider(c *gin.Context) {
	item, err := strconv.Atoi(c.Query("item"))
	if err != nil {
		c.JSON(http.StatusOK, result.ParamCheckErr.AjaxResult(nil))
		return
	}
	switch item {
	case 0:
		c.JSON(http.StatusOK, result.SUCCESS.AjaxResult(weibo.HandleWeiboHot()))
		return
	case 1:
		c.JSON(http.StatusOK, result.SUCCESS.AjaxResult(zhihu.HandleZhihuHot()))
		return
	default:
	}
}
