<script>
export default {
  data() {
    return {
      selectedItem: 0,
      hotList: [],
      newsActiveUrl: 'https://file.ipadown.com/tophub/assets/images/media/s.weibo.com.png_50x50.png',
      spiderSource: [
        {
          id: '0',
          name: '微博',
          url: 'https://file.ipadown.com/tophub/assets/images/media/s.weibo.com.png_50x50.png'
        },
        {
          id: '1',
          name: '知乎',
          url: 'https://file.ipadown.com/tophub/assets/images/media/zhihu.com.png_50x50.png'
        }
      ]
    }
  },
  created() {
    this.getSpiderData(this.spiderSource[this.selectedItem])
  },
  methods: {
    getSpiderData(source) {
      this.axios.get('http://localhost:8001/api/spider', {
        params: {
          item: source.id
        }
      }).then(({ data }) => {
        this.hotList = data.data
      })
      this.selectedItem = source.id
      this.newsActiveUrl = source.url
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.blogInfo.pageList.forEach(v => {
        if (v.pageLabel === 'news') {
          cover = v.pageCover
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  }
}
</script>

<template>
  <div>
    <div class='banner' :style='cover'>
      <h1 class='banner-title'>热搜</h1>
    </div>
    <v-card class='blog-container'>
      <div class='news-left'>
        <ul>
          <li
            :class="item.name === spiderSource[selectedItem].name ? 'active' : ''"
            v-for='item of spiderSource'
            :key='item.id'
            @click='getSpiderData(item)'
          >
            <img :src='item.url'>
            {{ item.name }}
          </li>
        </ul>
      </div>
      <div class='news-right'>
        <div class='title'>
          <img :src='newsActiveUrl'>
          <h2>{{ spiderSource[selectedItem].name }} · 热搜榜</h2>
        </div>
        <div class='host-list'>
          <ul>
            <li
              class='hot-list-item'
              v-for='item of hotList'
              :key='item.id'
            >
            <span class='hot-list-index'>
              {{ item.id }}
            </span>
              <a :href='item.url' target='_blank'>
                {{ item.title }}
              </a>
              <span class='hot-list-des'>
              {{ item.des }}
            </span>
            </li>
          </ul>
        </div>
      </div>
    </v-card>
  </div>
</template>

<style scoped>
.blog-container {
  display: flex;
  max-width: 1100px;
}

.news-left {
  width: 20%;
  border-right: 1px solid #e2e2e2;
}

.news-left ul {
  margin-top: 20px;
}

.news-left ul img {
  height: 25px;
  border-radius: 5px;
  margin-right: 10px;
}

.news-left ul li {
  color: #333;
  height: 50px;
  display: flex;
  align-items: center;
  padding-left: 30%;
  transition: all 0.1s;
}

.news-left ul li.active {
  background-color: #409eff;
  color: white;
}

.news-left ul li:not(li.active) {
  cursor: pointer;
}

.news-left ul li:not(li.active):hover {
  color: #3a8ee6;
}

.news-right {
  width: 80%;
  padding: 20px;
}

.news-right .title {
  display: flex;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #e2e2e2;
}

.news-right .title img {
  border-radius: 50%;
  margin-right: 10px;
  width: 35px;
}

.news-right .title h2 {
  font-size: 20px;
}

.host-list {
  margin-top: 10px;
}

.host-list ul {
  /* 去掉列表项标记 */
  list-style: none;
}

.host-list ul li {
  display: flex;
  font-size: 14px;
  margin-bottom: 10px;
  align-items: start;
}

.host-list .hot-list-index {
  width: 20px;
  height: 20px;
  border-radius: 5px;
  color: #333;
  background-color: #e2e2e2;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

.host-list ul li:nth-child(1) .hot-list-index {
  background-color: #fe2d46;
  color: white;
}

.host-list ul li:nth-child(2) .hot-list-index {
  background-color: #ff6600;
  color: white;
}

.host-list ul li:nth-child(3) .hot-list-index {
  background-color: #faa90e;
  color: white;
}

.host-list .hot-list-item a {
  width: 80%;
  transition: all 0.2s;
  color: #333;
}

.host-list .hot-list-item a:hover {
  color: #3a8ee6;
}

.host-list .hot-list-des {
  width: 15%;
  display: flex;
  justify-content: right;
}
</style>
