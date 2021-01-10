<template>
  <v-app id="app">
    <!-- 导航栏 -->
    <TopNavBar/>
    <!-- 侧边导航栏 -->
    <SideNavBar/>
    <!-- 内容 -->
    <v-content>
      <router-view :key="$route.fullPath"></router-view>
    </v-content>
    <!-- 页脚 -->
    <Footer/>
    <!-- 返回顶部 -->
    <BackTop/>
    <!-- 搜索模态框 -->
    <searchModel/>
    <!-- 登录模态框 -->
    <LoginModel/>
    <!-- 注册模态框 -->
    <RegisterModel/>
    <!-- 忘记密码模态框 -->
    <ForgetModel/>
    <Note/>
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
import Note from "./components/Notification";

export default {
  components: {
    TopNavBar,
    SideNavBar,
    Footer,
    BackTop,
    searchModel,
    LoginModel,
    RegisterModel,
    ForgetModel,
    Note,
  }
};
</script>
