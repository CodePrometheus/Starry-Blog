<template>
  <div>
    <div class='comment-title'>
      <i class='iconfont iconpinglunzu' />评论
    </div>
    <div class='comment-wrapper'>
      <div style='display:flex;width:100%'>
        <v-avatar size='36'>
          <img
            v-if='this.$store.state.avatar'
            :src='this.$store.state.avatar'
          />
          <img
            v-else
            :src='this.$store.state.blogInfo.websiteConfig.touristAvatar'
          />
        </v-avatar>
        <div style='width: 100%;' class='ml-3'>
          <div class='comment-input'>
            <textarea
              class='comment-textarea'
              v-model='commentContent'
              placeholder='留下点什么吧...'
              auto-grow
              dense
            />
          </div>
          <div class='emoji-container'>
            <span
              :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'"
              @click='chooseEmoji = !chooseEmoji'
            >
              <i class='iconfont iconbiaoqing' />
            </span>
            <button
              @click='insertComment'
              class='upload-btn v-comment-btn'
              style='margin-left:auto'
            >
              提交
            </button>
          </div>
          <emoji @addEmoji='addEmoji' :chooseEmoji='chooseEmoji' />
        </div>
      </div>
    </div>
    <div v-if='count > 0 && reFresh'>
      <div
        class='comment-wrapper'
        v-for='(v, idx) of commentList'
        :key='v.idx'
      >
        <v-avatar size='40' class='comment-avatar'>
          <img :src='v.avatar' />
        </v-avatar>
        <div class='comment-meta'>
          <div class='comment-user'>
            <span v-if='!v.webSite'>{{ v.nickname }}</span>
            <a v-else :href='v.webSite' target='_blank'>
              {{ v.nickname }}
            </a>
            <v-icon size='20' color='#ffa51e' v-if='v.userId === 1' class='icon'>
              mdi-check-decagram
            </v-icon>
          </div>
          <div class='comment-info'>
            <span style='margin-right: 10px'>{{ count - idx }}楼</span>
            <span style='margin-right: 10px'>{{ v.createTime | date }}</span>
            <span
              :class="isLike(v.id) + ' iconfont icondianzan'"
              @click='like(v)'
            />
            <span v-show='v.likeCount > 0'>{{ v.likeCount }}</span>
            <span class='reply-btn' @click='replyComment(idx, v)'>
              回复
            </span>
          </div>
          <!-- 评论内容 -->
          <p v-html='v.commentContent' class='comment-content' />
          <!-- 回复人 -->
          <div
            style='display:flex'
            v-for='reply of v.replyList'
            :key='reply.id'
          >
            <!-- 头像 -->
            <v-avatar size='36' class='comment-avatar'>
              <img :src='reply.avatar' />
            </v-avatar>
            <div class='reply-meta'>
              <!-- 用户名 -->
              <div class='comment-user'>
                <span v-if='!reply.webSite'>{{ reply.nickname }}</span>
                <a v-else :href='reply.webSite' target='_blank'>
                  {{ reply.nickname }}
                </a>
                <v-icon size='20' color='#ffa51e' v-if='reply.userId === 1' class='icon'>
                  mdi-check-decagram
                </v-icon>
              </div>
              <!-- 信息 -->
              <div class='comment-info'>
                <!-- 发表时间 -->
                <span style='margin-right:10px'>
                  {{ reply.createTime | date }}
                </span>
                <!-- 点赞 -->
                <span
                  :class="isLike(reply.id) + ' iconfont icondianzan'"
                  @click='like(reply)'
                />
                <span v-show='reply.likeCount > 0'>{{ reply.likeCount }}</span>
                <!-- 回复 -->
                <span class='reply-btn' @click='replyComment(idx, reply)'>
                  回复
                </span>
              </div>
              <!-- 回复内容 -->
              <p class='comment-content'>
                <!-- 回复用户名 -->
                <template v-if='reply.replyUserId !== v.userId'>
                  <span v-if='!reply.replyWebSite' class='ml-1'>
                    @{{ reply.replyNickname }}
                  </span>
                  <a
                    v-else
                    :href='reply.replyWebSite'
                    target='_blank'
                    class='comment-nickname ml-1'
                  >
                    @{{ reply.replyNickname }}
                  </a>
                  :
                </template>
                <span v-html='reply.commentContent' />
              </p>
            </div>
          </div>
          <!-- 回复数量 -->
          <div
            class='mb-3'
            style='font-size:0.75rem;color:#6d757a'
            v-show='v.replyCount > 3'
            ref='check'
          >
            共
            <b>{{ v.replyCount }}</b>
            条回复，
            <span
              style='color:#00a1d6;cursor:pointer'
              @click='checkReplies(idx, v)'
            >
              点击查看
            </span>
          </div>
          <!-- 回复分页 -->
          <div
            class='mb-3'
            style='font-size:0.75rem;color:#222;display:none'
            ref='paging'
          >
            <span style='padding-right:10px'>
              共{{ Math.ceil(v.replyCount / 5) }}页
            </span>
            <paging
              ref='page'
              :totalPage='Math.ceil(v.replyCount / 5)'
              :index='idx'
              :commentId='v.id'
              @changeReplyCurrent='changeReplyCurrent'
            />
          </div>
          <!-- 回复框 -->
          <Reply ref='reply' :type='type' @reloadReply='reloadReply' />
        </div>
      </div>
      <div class='load-wrapper'>
        <v-btn outlined v-if='count > commentList.length' @click='listComments'>
          加载更多...
        </v-btn>
      </div>
    </div>
    <div v-else style='padding:1.25rem;text-align:center'>
      还没有评论呢，你怎么看~
    </div>
  </div>
</template>

<script>
import Reply from './Reply'
import Paging from './Paging'
import Emoji from './Emoji'
import EmojiList from '../assets/js/emoji'

export default {
  created() {
    this.listComments()
  },
  components: {
    Reply,
    Emoji,
    Paging
  },
  props: {
    type: {
      type: Number
    }
  },
  data() {
    return {
      commentContent: [],
      chooseEmoji: false,
      count: 0,
      reFresh: true,
      commentList: []
    }
  },
  computed: {
    isLike() {
      return function(commentId) {
        let commentLikeSet = this.$store.state.commentLikeSet
        return commentLikeSet.indexOf(commentId) !== -1 ? 'like-active' : 'like'
      }
    }
  },
  watch: {
    commentList() {
      this.reFresh = false
      this.$nextTick(() => {
        this.reFresh = true
      })
    }
  },
  methods: {
    reloadReply(index) {
      this.axios
        .get('/api/comments/' + this.commentList[index].id + '/replies', {
          params: {
            current: this.$refs.page[index].current
          }
        })
        .then(({ data }) => {
          this.commentList[index].replyCount++
          // 回复大于5条展示分页
          if (this.commentList[index].replyCount > 5) {
            this.$refs.paging[index].style.display = 'flex'
          }
          this.$refs.check[index].style.display = 'none'
          this.$refs.reply[index].$el.style.display = 'none'
          this.commentList[index].replyList = data.data
        })
    },
    changeReplyCurrent(current, index, commentId) {
      this.axios
        .get('/api/comments/' + commentId + '/replies', {
          params: { current: current, size: 5 }
        })
        .then(({ data }) => {
          this.commentList[index].replyList = data.data
        })
    },
    checkReplies(index, item) {
      this.axios
        .get('/api/comments/' + item.id + '/replies', {
          params: { current: 1, size: 5 }
        })
        .then(({ data }) => {
          this.$refs.check[index].style.display = 'none'
          item.replyList = data.data
          // 超过1页才显示分页
          if (Math.ceil(item.replyCount / 5) > 1) {
            this.$refs.paging[index].style.display = 'flex'
          }
        })
    },
    replyComment(index, item) {
      this.$refs.reply.forEach(item => {
        item.$el.style.display = 'none'
      })
      this.$refs.reply[index].commentContent = ''
      this.$refs.reply[index].nickname = item.nickname
      this.$refs.reply[index].replyUserId = item.userId
      this.$refs.reply[index].parentId = this.commentList[index].id
      this.$refs.reply[index].chooseEmoji = false
      this.$refs.reply[index].index = index
      this.$refs.reply[index].$el.style.display = 'block'
    },
    like(comment) {
      // 判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true
        return false
      }
      // 发送请求
      this.axios.post('/api/comments/' + comment.id + '/like')
        .then(({ data }) => {
          if (data.flag) {
            // 判断是否点赞
            if (this.$store.state.commentLikeSet.indexOf(comment.id) !== -1) {
              this.$set(comment, 'likeCount', comment.likeCount - 1)
            } else {
              this.$set(comment, 'likeCount', comment.likeCount + 1)
            }
            this.$store.commit('commentLike', comment.id)
          }
        })
    },
    listComments() {
      const path = this.$route.path
      const arr = path.split('/')
      let params = {
        current: this.current,
        type: this.type
      }
      switch (this.type) {
        case 1:
          params.articleId = arr[2]
          break
        case 3:
          params.momentId = arr[2]
          break
        default:
          break
      }
      this.axios
        .get('/api/comments', {
          params
        })
        .then(({ data }) => {
          if (this.current === 1) {
            this.commentList = data.data.recordList
          } else {
            this.commentList.push(...data.data.recordList)
          }
          this.current++
          this.count = data.data.count
          this.$emit('reloadComment', this.count)
        })
    },
    insertComment() {
      // 判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true
        return false
      }
      // 判空
      if (this.commentContent.trim() === '') {
        this.$toast({ type: 'error', message: '评论不能为空' })
        return false
      }
      // 解析表情
      let reg = /\[.+?\]/g
      this.commentContent = this.commentContent.replace(reg, function(str) {
        return (
          '<img src= \'' +
          EmojiList[str] +
          '\' width=\'22\'height=\'20\' style=\'padding: 0 1px\'/>'
        )
      })
      // 发送请求
      const path = this.$route.path
      const arr = path.split('/')
      let comment = {
        type: this.type,
        commentContent: this.commentContent
      }
      switch (this.type) {
        case 1:
          comment.articleId = arr[2]
          break
        case 3:
          comment.momentId = arr[2]
          break
        default:
          break
      }
      this.commentContent = ''
      this.axios.post('/api/comments', comment).then(({ data }) => {
        if (data.flag) {
          // 查询最新评论
          this.current = 1
          this.listComments()
          const isReview = this.$store.state.blogInfo.websiteConfig.isCommentReview
          if (isReview === 1) {
            this.$toast({ type: 'warnning', message: '评论成功, 正在审核中' })
          } else {
            this.$toast({ type: 'success', message: '评论成功' })
          }
        } else {
          this.$toast({ type: 'error', message: data.message })
        }
      })
    },
    addEmoji(key) {
      this.content += key
    }
  }
}
</script>

<style scoped>
.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  margin-top: 25px;
  line-height: 40px;
  font-weight: bold;
}

.comment-title i {
  font-size: 1.5rem;
  margin-right: 5px;
}

.comment-wrapper {
  display: flex;
  margin-top: 20px;
  box-shadow: 0 3px 8px 6px rgb(7 17 27 / 6%);
  transition: all 0.3s ease 0s;
  background: rgba(255, 255, 255, 0.1);
  padding: 16px 20px;
  border-radius: 10px;
}

.comment-wrapper:hover {
  box-shadow: 0 5px 10px 8px rgb(7 17 27 / 16%);
  transform: translateY(-3px);
}

.comment-meta {
  margin-left: 0.8rem;
  width: 100%;
  /** dashed虚线 */
  border-bottom: 1px dashed #f5f5f5;
}

.comment-user {
  font-size: 16px;
  line-height: 1.75;
}

.comment-user a {
  color: #1abc9c !important;
  font-weight: 500;
  transition: 0.3s all;
}

.reply-meta {
  margin-left: 0.8rem;
  width: 100%;
}

.comment-nickname {
  text-decoration: none;
  color: #1abc9c !important;
  font-weight: 500;
}

.comment-info {
  line-height: 1.75;
  font-size: 0.75rem;
  color: #b3b3b3;
}

.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
  white-space: pre-line;
  /** 在长单词或 URL 地址内部进行换行 */
  word-wrap: break-word;
  /** 允许在单词内换行 */
  word-break: break-all;
}

.comment-avatar {
  transition: all 0.5s;
}

.comment-avatar:hover {
  transform: rotate(360deg);
}

.like {
  cursor: pointer;
  font-size: 0.875rem;
}

.like:hover {
  color: #eb5055;
}

.like-active {
  cursor: pointer;
  font-size: 0.875rem;
  color: #eb5055;
}

.reply-btn {
  cursor: pointer;
  float: right;
  color: #ef2f11;
}

.load-wrapper {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}

.comment-user .icon {
  margin-left: 5px;
}
</style>