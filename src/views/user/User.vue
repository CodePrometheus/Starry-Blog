<template>
  <div>
    <div class="user-banner banner">
      <h1 class="banner-title">个人中心</h1>
    </div>
    <v-card class="blog-container">
      <div>
        <span class="info-title">基本信息</span>
      </div>
      <v-row class="info-wrapper">
        <v-col md="3" cols="12">
          <button id="pick-avatar">
            <v-avatar size="140">
              <img :src="this.$store.state.avatar" />
            </v-avatar>
          </button>
          <avatar-cropper
            @uploaded="uploadAvatar"
            trigger="#pick-avatar"
            upload-url="/api/users/avatar"
          />
        </v-col>
        <v-col md="7" cols="12">
          <v-text-field
            v-model="userInfo.nickname"
            label="昵称"
            placeholder="起一个有个性的昵称吧"
          />
          <v-text-field
            v-model="userInfo.webSite"
            class="mt-7"
            label="个人网站"
            placeholder="http://你的网址 (没有可以跳过哦~)"
          />
          <v-text-field
            v-model="userInfo.intro"
            class="mt-7"
            label="简介"
            placeholder="简单介绍一下自己吧"
          />
          <v-btn color="blue" @click="updataUserInfo" outlined class="mt-5">修改</v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import AvatarCropper from "vue-avatar-cropper";
export default {
  components: { AvatarCropper },
  data: function() {
    return {
      userInfo: {
        nickname: this.$store.state.nickname,
        intro: this.$store.state.intro,
        webSite: this.$store.state.webSite
      }
    };
  },
  methods: {
    updataUserInfo() {
      this.axios.put("/api/users/info", this.userInfo).then(({ data }) => {
        if (data.flag) {
          this.$store.commit("updateUserInfo", this.userInfo);
          this.$toast({ type: "success", message: data.message });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    },
    uploadAvatar(data) {
      if (data.flag) {
        this.$store.commit("updateAvatar", data.data);
        this.$toast({ type: "success", message: data.message });
      } else {
        this.$toast({ type: "error", message: data.message });
      }
    }
  }
};
</script>

<style scoped>
.user-banner {
  background: url(https://cdn.jsdelivr.net/gh/zyoushuo/Blog/images/BG_10.jpg) center
    center / cover no-repeat;
  background-color: #49b1f5;
}
.info-title {
  font-size: 1.25rem;
  font-weight: bold;
}
.info-wrapper {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
#pick-avatar {
  outline: none;
}
</style>
