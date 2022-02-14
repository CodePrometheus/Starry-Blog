import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)
export const SET_IS_BLOG_RENDER_COMPLETE = 'setIsBlogRenderComplete'

export default new Vuex.Store({
  state: {
    emailFlag: false,
    searchFlag: false,
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    drawer: false,
    loginUrl: '',
    userId: null,
    avatar: null,
    nickname: null,
    intro: null,
    webSite: null,
    articleLikeSet: [],
    commentLikeSet: [],
    momentLikeSet: [],
    blogInfo: {},
    isBlogRenderComplete: false,
  },
  mutations: {
    login(state, user) {
      state.userId = user.userInfoId
      state.avatar = user.avatar
      state.nickname = user.nickname
      state.intro = user.intro
      state.webSite = user.webSite
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : []
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : []
      state.momentLikeSet = user.momentLikeSet ? user.momentLikeSet : []
      state.email = user.email
      state.loginType = user.loginType
    },
    logout(state) {
      state.userId = null
      state.avatar = null
      state.nickname = null
      state.intro = null
      state.webSite = null
      state.articleLikeSet = []
      state.commentLikeSet = []
      state.momentLikeSet = []
      state.email = null
      state.loginType = null
    },
    saveLoginUrl(state, url) {
      state.loginUrl = url
    },
    updateUserInfo(state, user) {
      state.nickname = user.nickname
      state.intro = user.intro
      state.webSite = user.webSite
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar
    },
    checkBlogInfo(state, blogInfo) {
      state.blogInfo = blogInfo
    },
    closeModel(state) {
      state.registerFlag = false
      state.loginFlag = false
      state.searchFlag = false
      state.emailFlag = false
    },
    articleLike(state, articleId) {
      let articleLikeSet = state.articleLikeSet
      if (articleLikeSet.indexOf(articleId) != -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1)
      } else {
        articleLikeSet.push(articleId)
      }
    },
    commentLike(state, commentId) {
      let commentLikeSet = state.commentLikeSet
      if (commentLikeSet.indexOf(commentId) != -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1)
      } else {
        commentLikeSet.push(commentId)
      }
    },
    momentLike(state, momentId) {
      let momentLikeSet = state.momentLikeSet
      if (momentLikeSet.indexOf(momentId) != -1) {
        momentLikeSet.splice(momentLikeSet.indexOf(momentId), 1)
      } else {
        momentLikeSet.push(momentId)
      }
    },
    [SET_IS_BLOG_RENDER_COMPLETE](state, { ok }) {
      state.isBlogRenderComplete = ok
    },
    saveEmail(state, email) {
      state.email = email
    },
    savePageInfo(state, pageList) {
      state.pageList = pageList
    },
  },
  actions: {
    setIsBlogRenderComplete({ commit }, ok) {
      commit(SET_IS_BLOG_RENDER_COMPLETE, { ok })
    },
  },
  modules: {},
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
    }),
  ],
})
