<template>
  <v-app id="app">
    <!-- 导航栏 -->
    <TopNavBar />
    <!-- 侧边导航栏 -->
    <SideNavBar />
    <!-- 内容 -->
    <v-content>
      <router-view :key="$route.fullPath" />
    </v-content>
    <!-- 页脚 -->
    <Footer />
    <!-- 返回顶部 -->
    <BackTop />
    <!-- 搜索模态框 -->
    <searchModel />
    <!-- 登录模态框 -->
    <LoginModel />
    <!-- 注册模态框 -->
    <RegisterModel />
    <!-- 忘记密码模态框 -->
    <ForgetModel />
    <Live2D v-if="blogInfo.websiteConfig.isLive2D == 1" />
    <ChatRoom v-if="blogInfo.websiteConfig.isChatRoom == 1" />
  </v-app>
</template>

<script>
import TopNavBar from "./components/layout/TopNavBar";
import SideNavBar from "./components/layout/SideNavBar";
import Footer from "./components/layout/Footer";
import BackTop from "./components/BackTop";
import searchModel from "./components/model/SearchModel";
import LoginModel from "./components/model/LoginModel";
import RegisterModel from "./components/model/RegisterModel";
import ForgetModel from "./components/model/ForgetModel";
import Live2D from "./components/Live2D";
import ChatRoom from "./components/ChatRoom";

export default {
  created() {
    this.getBlogInfo();
    this.axios.post("/api/report");
  },
  components: {
    TopNavBar,
    SideNavBar,
    Footer,
    BackTop,
    searchModel,
    LoginModel,
    RegisterModel,
    ForgetModel,
    Live2D,
    ChatRoom
  },
  methods: {
    getBlogInfo() {
      this.axios.get("/api").then(({ data }) => {
        this.$store.commit("checkBlogInfo", data.data);
      });
    }
  },
  beforeCreate() {
    console.log(
      "%c Starry-Blog | 你的美好，我都记得 %c v".concat("3.0", " %c"),
      "background: #35495e;" +
        " padding: 1px;" +
        " border-radius: 3px 0 0 3px;" +
        " color: #fff",
      "background: #483d8b; " +
        "padding: 1px; " +
        "border-radius: 0 3px 3px 0; " +
        "color: #fff",
      "background: transparent"
    );
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    }
  }
};
</script>
