package utils

import "time"

// InSlice 判断字符串是否存在切片
func InSlice(slice []string, item string) bool {
	for _, value := range slice {
		if value == item {
			return true
		}
	}
	return false
}

// StringToInterface 字符串转interface
func StringToInterface(s string) interface{} {
	var x interface{} = s
	return x
}

// GetCurrentHour 获取当前小时
func GetCurrentHour() string {
	return time.Now().Format("2006-01-02 15")
}
