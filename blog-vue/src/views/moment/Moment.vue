<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">动态</h1>
    </div>
    <div class="moment-container">
      <v-card
        class="animated zoomIn moment-card"
        style="border-radius: 12px 8px 8px 12px"
        v-for="v of momentList"
        :key="v.id"
      >
        <v-avatar size="120">
          <img class="author-avatar"
               :src="$store.state.blogInfo.websiteConfig.websiteAvatar" />
        </v-avatar>
        <div class="moment-wrapper">
          <div class="author">
            {{ $store.state.blogInfo.websiteConfig.websiteAuthor }}
          </div>
          <div class="moment-info">
            <span style="color:#ff7242" v-if="v.isTop == 1">
              <i class="iconfont iconzhiding"/> 置顶
              <span class="separator">|</span>
            </span>
            <v-icon size="14">mdi-calendar-month-outline</v-icon>
            {{ v.createTime | date }}
          </div>
          <div class="moment-content">
            {{ v.momentContent }}
          </div>
          <div class="like">
            <span
              :class="isLike(v.id) + ' iconfont icondianzan'"
              @click="like(v)"
            />
            <span v-show="v.likeCount > 0">{{ v.likeCount }}</span>
          </div>
        </div>
      </v-card>
    </div>
    <!-- 分页按钮 -->
    <v-pagination
      color="#00C4B6"
      v-model="current"
      :length="Math.ceil(count / 10)"
      total-visible="7"
    />
  </div>
</template>

<script>
export default {
  created() {
    this.listMoments();
  },
  data() {
    return {
      momentList: [],
      current: 1,
      count: 0
    }
  },
  methods: {
    listMoments() {
      this.axios.get("/api/moments", {
        params: {
          current: this.current
        }
      }).then(({data}) => {
        this.momentList = data.data.recordList;
        this.count = data.data.count;
      })
    },
    like(moment) {
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      console.log(moment.id);
      this.axios.post("/api/moments/" + moment.id + "/like")
      .then(({ data }) => {
        if (data.flag) {
          if (this.$store.state.momentLikeSet.indexOf(moment.id) != -1) {
            this.$set(moment, "likeCount", moment.likeCount - 1)
          } else {
            this.$set(moment, "likeCount", moment.likeCount + 1)
          }
          this.$store.commit("momentLike", moment.id);
        }
      })
    }
  },
  computed: {
    cover() {
      let cover = '';
      this.$store.state.blogInfo.pageList.forEach(v => {
        if (v.pageLabel === 'moment') {
          cover = v.pageCover
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    isLike() {
      return function(momentId) {
        let momentLikeSet = this.$store.state.momentLikeSet;
        return momentLikeSet.indexOf(momentId) != -1 ? "like-active" : "like";
      };
    }
  }
}
</script>

<style scoped>
@media (min-width: 760px) {
  .moment-container {
    max-width: 1200px;
    margin-top: 380px;
  }

  .moment-card {
    display: flex;
    align-items: center;
    height: 200px;
    width: 100%;
    margin-top: 20px;
    margin-left: 320px;
  }

  .moment-wrapper {
    padding: 0 2.5rem;
    width: 55%;
  }
}

@media (max-width: 759px) {
  .moment-container {
    width: 100%;
    margin-top: 380px;
  }

  .moment-wrapper {
    padding: 1.25rem 1.25rem 1.875rem;
  }
}

.moment-wrapper {
  font-size: 14px;
}

.author {
  line-height: 1.4;
  font-size: 24px
}

.moment-content {
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.like:hover {
  color: #eb5055;
}

.like-active {
  cursor: pointer;
  font-size: 0.875rem;
  color: #eb5055;
}

.moment-info {
  font-size: 95%;
  color: #858585;
  line-height: 2;
  margin: 0.375rem 0;
}
</style>