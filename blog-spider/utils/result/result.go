package result

import "fmt"

type RetCode int

const (
	SUCCESS      RetCode = 200
	NotFoundCode RetCode = 404
	FAIL         RetCode = 500
)

var CodeMsg = map[RetCode]string{
	SUCCESS:      "成功",
	NotFoundCode: "资源未找到",
	FAIL:         "失败",
}

type Response struct {
	Flag    bool        `json:"flag"`
	Code    RetCode     `json:"code"`
	Message string      `json:"message"`
	Data    interface{} `json:"data"`
}

func (c RetCode) AjaxResult(data interface{}) Response {
	ans := Response{}
	if data != nil {
		ans.Data = data
	}
	if arr, ok := data.([]string); ok && c != SUCCESS {
		ans.Flag = false
		ans.Message = fmt.Sprintf(CodeMsg[c], arr)
		ans.Data = nil
	} else {
		ans.Flag = true
		ans.Message = CodeMsg[c]
	}
	ans.Code = c
	return ans
}
