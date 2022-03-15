<template>
  <router-link to='/moments' class='swiper-main'>
    <v-icon size='20' color='#4c4948'>mdi-chat-outline</v-icon>
    <div
      :style="{ height: height * lineNum + 'px' }"
      class='roll-main'
      id='roll-main'
    >
      <ul
        :style='{ transform: transform }'
        class='roll-list'
        :class='{ roll_list_num: num === 0 }'
      >
        <li
          class='roll-once'
          v-for='(v, idx) in list'
          :key='idx'
          :style="{ height: height + 'px' }"
        >
          <span class='item' v-html='v' />
        </li>
        <li
          class='roll-once'
          v-for='(v, idx) in list'
          :key='idx + list.length'
          :style="{ height: height + 'px' }"
        >
          <span class='item' v-html='v' />
        </li>
      </ul>
    </div>
    <v-icon size='20' color='#4c4948' class='arrow'>
      mdi-chevron-double-right
    </v-icon>
  </router-link>
</template>

<script>
export default {
  props: {
    height: {
      default: 25,
      type: Number
    },
    lineNum: {
      default: 1,
      type: Number
    },
    list: {
      type: Array
    }
  },
  created() {
    let that = this
    setInterval(() => {
      if (that.num !== that.list.length) {
        that.num++
      } else {
        that.num = 0
      }
    }, 3000)
  },
  data() {
    return {
      num: 0
    }
  },
  computed: {
    // 滚动效果
    transform() {
      return 'translateY(-' + this.num * this.height + 'px)'
    }
  }
}
</script>

<style scoped>
.swiper-main {
  margin-top: 20px;
  padding: 0.6rem 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.roll-main {
  display: inline-block;
  position: relative;
  /** 上下的隐藏 */
  overflow: hidden;
  width: 100%;
  line-height: 25px;
  text-align: center;
}

.item {
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  transition: all 0.3s;
}

.roll-list:hover .item {
  color: #49b1f5;
}

.roll-list {
  transition: 1s linear;
}

.roll_list_num {
  animation: none;
}

.arrow {
  animation: 1s passing infinite;
}

@keyframes passing {
  0% {
    transform: translateX(-50%);
    opacity: 0;
  }
  50% {
    transform: translateX(0);
    opacity: 1;
  }
  100% {
    transform: translateX(50%);
    opacity: 0;
  }
}
</style>