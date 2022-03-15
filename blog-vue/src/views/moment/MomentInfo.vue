<template>
  <div>
    <div class='banner' :style='cover'>
      <h1 class='banner-title'>动态</h1>
    </div>
    <v-card class='blog-container'>
      <div class='moment-item'>
        <div class='moment-info'>
          <v-avatar size='36' class='user-avatar'>
            <img :src='momentInfo.avatar' />
          </v-avatar>
          <div class='moment-detail'>
            <div class='nickname'>
              {{ momentInfo.nickname }}
              <v-icon class='user-sign' size='20' color='#ffa51e'>
                mdi-check-decagram
              </v-icon>
            </div>
            <div class='time'>
              {{ momentInfo.createTime | date }}
            </div>
            <div class='content' v-html='momentInfo.momentContent' />
            <v-row class='images' v-if='momentInfo.imgList'>
              <v-col
                :md='4'
                :cols='6'
                v-for='(v, idx) of momentInfo.imgList'
                :key='idx'
              >
                <v-img
                  class='image-item'
                  :src='v'
                  aspect-ratio='1'
                  max-height='200'
                  @click.prevent='previewImg(v)'
                />
              </v-col>
            </v-row>
            <div class='operation'>
              <div class='operation-item'>
                <v-icon
                  size='16'
                  :color='isLike(momentInfo.id)'
                  class='like-btn'
                  @click.prevent='like(momentInfo)'
                >
                  mdi-thumb-up
                </v-icon>
                <div class='operation-count'>
                  {{ momentInfo.likeCount == null ? 0 : momentInfo.likeCount }}
                </div>
              </div>
              <div class='operation-item'>
                <v-icon size='16' color='#999'>mdi-chat</v-icon>
                <div class='operation-count'>
                  {{ commentCount == null ? 0 : commentCount }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 评论 -->
      <moment-comment :type='commentType' @reloadComment='listComment' />
    </v-card>
  </div>
</template>

<script>
import MomentComment from '../../components/MomentComment'

export default {
  created() {
    this.getMomentById()
  },
  components: { MomentComment },
  data() {
    return {
      commentType: 3,
      commentCount: 0,
      momentInfo: {},
      previewList: []
    }
  },
  methods: {
    listComment(count) {
      this.commentCount = count
    },
    getMomentById() {
      this.axios.get('/api/moments/' + this.$route.params.momentId)
        .then(({ data }) => {
          this.momentInfo = data.data
          this.previewList = this.momentInfo.imgList
        })
    },
    previewImg(img) {
      this.$imagePreview({
        images: this.previewList,
        index: this.previewList.indexOf(img)
      })
    },
    like(moment) {
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true
        return false
      }
      this.axios.post('/api/moments/' + moment.id + '/like')
        .then(({ data }) => {
          if (data.flag) {
            if (this.$store.state.momentLikeSet.indexOf(moment.id) !== -1) {
              this.$set(moment, 'likeCount', moment.likeCount - 1)
            } else {
              this.$set(moment, 'likeCount', moment.likeCount + 1)
            }
            this.$store.commit('momentLike', moment.id)
          }
        })
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.blogInfo.pageList.forEach(v => {
        if (v.pageLabel === 'moment') {
          cover = v.pageCover
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    isLike() {
      return function(momentId) {
        let momentLikeSet = this.$store.state.momentLikeSet
        return momentLikeSet.indexOf(momentId) !== -1 ? '#eb5055' : '#999'
      }
    }
  }
}
</script>

<style scoped>
.moment-item {
  padding: 15px 20px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 3px 8px 6px rgb(7 17 27 / 6%);
  /** 0.3s 完成所有过度效果 */
  transition: all 0.3s ease 0s;
}

.moment-item:hover {
  box-shadow: 0 5px 10px 8px rgb(7 17 27 / 16%);
  transform: translateY(-3px);
}

.moment-info {
  display: flex;
  width: 100%;
}

.user-avatar {
  margin-right: 10px;
  border-radius: 50%;
  transition: all 0.5s;
}

.user-avatar:hover {
  /** 360旋转 */
  transform: rotate(360deg);
}

.nickname {
  font-size: 16px;
  font-weight: bold;
  vertical-align: middle;
}

.user-sign {
  margin-left: 4px;
}

.time {
  color: #999;
  margin-top: 2px;
  font-size: 12px;
}

.top {
  color: #ff7242;
  margin-left: 10px;
}

.content {
  margin-top: 8px;
  font-size: 14px;
  /** 连续的空白符会被合并 */
  white-space: pre-line;
  word-wrap: break-word;
  /** 对于non-CJK (CJK 指中文/日文/韩文) 文本，可在任意字符间断行 */
  word-break: break-all;
}

.images {
  padding: 0 10px;
  margin-top: 8px;
}

.image-item {
  cursor: pointer;
  border-radius: 4px;
}

.moment-detail {
  flex: 1;
  margin-left: 10px;
  width: 0;
}

.operation {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.operation-item {
  display: flex;
  align-items: center;
  margin-right: 40px;
  font-size: 12px;
}

.operation-count {
  margin-left: 4px;
}

.like-btn:hover {
  color: #eb5055 !important;
}

.operation {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.operation-item {
  display: flex;
  align-items: center;
  margin-right: 40px;
  font-size: 12px;
}

.operation-count {
  margin-left: 4px;
}
</style>