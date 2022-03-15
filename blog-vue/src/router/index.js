import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: (reslove) => require(['../views/home/Home'], reslove),
    meta: {
      title: "zzStar's Blog",
    },
  },
  {
    path: '/articles/:articleId',
    component: (reslove) => require(['../views/article/Article'], reslove),
  },
  {
    path: '/archives',
    component: (reslove) => require(['../views/archive/Archive'], reslove),
    meta: {
      title: '归档',
    },
  },
  {
    path: '/tags',
    component: (reslove) => require(['../views/tag/Tag'], reslove),
    meta: {
      title: '标签',
    },
  },
  {
    path: '/categories',
    component: (reslove) => require(['../views/category/Category'], reslove),
    meta: {
      title: '分类',
    },
  },
  {
    path: '/categories/:categoryId',
    component: (reslove) => require(['../components/ArticleList'], reslove),
  },
  {
    path: '/links',
    component: (reslove) => require(['../views/link/Link'], reslove),
    meta: {
      title: '友链列表',
    },
  },
  {
    path: '/moments',
    component: (reslove) => require(['../views/moment/Moment'], reslove),
    meta: {
      title: '动态',
    },
  },
  {
    path: '/moments/:momentId',
    component: (reslove) => require(['../views/moment/MomentInfo'], reslove),
    meta: {
      title: '动态详情',
    },
  },
  {
    path: '/about',
    component: (reslove) => require(['../views/about/About'], reslove),
    meta: {
      title: '关于我',
    },
  },
  {
    path: '/message',
    component: (reslove) => require(['../views/message/Messsage'], reslove),
    meta: {
      title: '留言板',
    },
  },
  {
    path: '/tags/:tagId',
    component: (reslove) => require(['../components/ArticleList'], reslove),
  },
  {
    path: '/user',
    component: (reslove) => require(['../views/user/User'], reslove),
    meta: {
      title: '个人中心',
    },
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

export default router
