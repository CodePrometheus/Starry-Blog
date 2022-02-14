<template>
  <div>
    <!-- 目录 -->
    <v-card class='box-card right-container m-toc'>
      <div class='right-title'>
        <i class='iconfont iconhanbao' style='font-size:16.8px' />
        <span style='margin-left:10px'>目录</span>
      </div>
      <div class='item'>
        <div class='js-toc'></div>
      </div>
    </v-card>
  </div>
</template>

<script>
import tocbot from 'tocbot'
import { mapState } from 'vuex'

export default {
  name: 'Tocbot',

  methods: {
    initTocbot() {
      tocbot.init({
        // 要把目录添加元素位置，支持选择器
        tocSelector: '.js-toc',
        // 获取html的元素
        contentSelector: '.article-content',
        // 要显示的id的目录
        headingSelector: 'h1, h2, h3',
        positionFixedSelector: '.m-toc',
        onClick: function(e) {
          e.preventDefault()
        },
        scrollSmooth: true,
        // Smooth scroll duration.
        scrollSmoothDuration: 420,
        // 到顶部导航条的距离
        scrollSmoothOffset: -55,
        // Headings offset between the headings and the top of the document (this is meant for minor adjustments).
        // Can also be used to account for scroll height discrepancies from the use of css scroll-padding-top
        headingsOffset: -18
      })
    }
  },

  computed: {
    ...mapState(['isBlogRenderComplete'])
  },

  mounted() {
    // 有可能组件创建比较慢，文章渲染已经完成，watch的时候，isBlogRenderComplete已经是true，
    // 监听不到 isBlogRenderComplete 的改变，也就不会执行watch中的方法 就需要在 mounted 中init
    if (this.isBlogRenderComplete) {
      this.initTocbot()
    }
  },

  watch: {
    // 文章渲染完成时，生成目录
    isBlogRenderComplete() {
      if (this.isBlogRenderComplete) {
        this.initTocbot()
      }
    }
  }

}
</script>

<style scoped>
.right-container {
  padding: 20px 24px;
  font-size: 14px;
}

.right-title {
  display: flex;
  align-items: center;
  line-height: 2;
  font-size: 16.8px;
  margin-bottom: 6px;
}

.box-card:hover {
  transform: translate(0, -2px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.box-card .item:hover {
  cursor: pointer;
}

.box-card span {
  font-weight: bold;
}

.toc {
  overflow-y: auto;
}

.toc > ul {
  overflow: hidden;
  position: relative;
}

.toc > ul li {
  list-style: none;
}

.toc-list {
  list-style-type: none;
  margin: 0;
  padding-left: 10px;
}

.toc-list li a {
  display: block;
  padding: 4px 0;
  font-weight: 300;
}

.toc-list li a:hover {
  color: #409eff;
}

a.toc-link {
  color: currentColor;
  height: 100%;
}

.is-collapsible {
  max-height: 1000px;
  overflow: hidden;
  transition: all 300ms ease-in-out;
}

.is-collapsed {
  max-height: 0;
}

.is-position-fixed {
  position: sticky !important;
  top: 60px;
}

.is-active-link {
  font-weight: 700;
  color: #409eff !important;
}

.item >>> .is-active-link {
  border-left-color: #409eff;
  background: #409eff;
}

.toc-link::before {
  background-color: #eee;
  content: " ";
  display: inline-block;
  height: 0;
  left: 0;
  margin-top: -1px;
  position: absolute;
  width: 2px;
}
</style>
