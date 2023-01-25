package model

type HotNews struct {
	ID    int    `json:"id"`
	Url   string `json:"url"`
	Title string `json:"title"`
	Des   string `json:"des"`
	Icon  string `json:"icon"`
}

type HotList struct {
	Data []Item `json:"data"`
}

type Item struct {
	RankWithTime string  `json:"id"`
	Target       HotNews `json:"target"`
	DetailText   string  `json:"detail_text"`
}
