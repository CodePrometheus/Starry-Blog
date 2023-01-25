package model

type HotNews struct {
	ID    int    `json:"id"`
	Link  string `json:"link"`
	Title string `json:"title"`
	Des   string `json:"des"`
}
