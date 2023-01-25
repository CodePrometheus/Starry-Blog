package weibo

import (
	"blog-spider/utils/result"
	"github.com/gin-gonic/gin"
	"net/http"
)

func HandleWeiboHot(c *gin.Context) {
	c.JSON(http.StatusOK, result.SUCCESS.AjaxResult(nil))
}
