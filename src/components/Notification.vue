<template>
  <el-button plain></el-button>
</template>

<script>
export default {
  name: "Notification",
  mounted() {
    this.open();
  },
  methods: {
    open() {
      this.$notify({
        title: "消息提示",
        offset: 80,
        message:
          "博客后台正在紧急开发，现时段处于测试当中，暂时开放前台页面，源码在Github上，欢迎你提出宝贵的建议 🍷"
      });
    }
  }
};
</script>

<style scoped></style>
