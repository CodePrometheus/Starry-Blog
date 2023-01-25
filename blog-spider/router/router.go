package router

import (
	"blog-spider/api/weibo"
	"blog-spider/middleware/cors"
	"blog-spider/utils/result"
	"github.com/gin-gonic/gin"
	"github.com/pelletier/go-toml"
	"net/http"
)

func InitRouter(config *toml.Tree) *gin.Engine {
	r := gin.New()
	r.Use(cors.Cors(), gin.Recovery())

	r.NoRoute(func(c *gin.Context) {
		c.JSON(http.StatusNotFound, result.NotFoundCode.AjaxResult(nil))
	})

	spider := r.Group("/")
	{
		spider.GET("/weibo", weibo.HandleWeiboHot)
	}
	return r
}
