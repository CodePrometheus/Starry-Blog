<template>
  <v-footer app padless absolute v-if='!this.isMessage'>
    <div class='footer-wrap'>
      <div>Copyright &copy; 2020-{{ new Date().getFullYear() }} By zzStar</div>
      <div>
        <a target='_blank' href='https://github.com/CodePrometheus'
           class='mr-9 iconfont icongithub' />
        <a target='_blank' href='https://space.bilibili.com/342251858'
           class='iconfont icon-bilibili-fill' />
      </div>
    </div>
  </v-footer>
</template>

<script>
export default {
  computed: {
    isMessage() {
      return this.$route.path === '/message'
    }
  }
}
</script>

<style scoped>
.footer-wrap {
  width: 100%;
  line-height: 2;
  position: relative;
  padding: 40px 20px;
  color: #eee;
  font-size: 14px;
  text-align: center;
  background: linear-gradient(-45deg, #ee7752, #ce3e75, #23a6d5, #a871d6, #ec7259, #ffea7f);
  background-size: 400% 400%;
  animation: Gradient 10s ease infinite;
}

.footer-wrap a {
  color: #eee !important;
}

@keyframes Gradient {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>
