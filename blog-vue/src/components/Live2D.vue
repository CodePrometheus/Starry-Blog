<template>
  <div class='app'>
    <div class='live2d-panel'>
      <live2d v-if='isLive2d' :modelPath='modelPath' ref='l2dMange'></live2d>
    </div>
    <div class='tools-panel'>
      <live2dTools v-for='(item, index) in toolsData'
                   :key='index'
                   :position='item.position'
                   @click='toolsClick(item)'
                   :width='item.width'
                   :toolsID='item.tabMsg'
                   :customDialogue='item.customDialogue'
                   :backgroundColor='item.backgroundColor'
                   ref='tool'
      >
        <span class="tab-msg">{{ item.tabMsg }}</span>
      </live2dTools>
    </div>
  </div>
</template>

<script>

import custom from '../assets/js/custom'

export default {
  name: 'Live2D',
  data: () => ({
    isLive2d: false,
    modelPath: '',
    modelPaths: '',
    customDialogue: custom,
    isDialogue: false,

    toolsData: [
      {
        tabMsg: 'dialogue',
        width: 200,
        customDialogue: custom,
        backgroundColor: '49b1f5',
        show: true,
        position: 'left'
      },
      {
        tabMsg: 'save',
        backgroundColor: '#49b1f5',
        show: true,
        position: 'left'
      },
      {
        tabMsg: 'hide',
        backgroundColor: '#49b1f5',
        show: true,
        position: 'left'
      }
    ]
  }),

  methods: {
    toolsClick(item) {
      switch (item.tabMsg) {
        case 'save':
          this.$refs.l2dMange.save(`Starry-Blog-${new Date()}.png`)
          break
        case 'hide':
          this.isLive2d = false
          this.toolsDisplay('hide')
          break
        case 'show':
          this.isLive2d = true
          this.toolsDisplay('show')
          break
      }
    },

    toolsDisplay(display) {
      for (let i = 0, len = this.toolsData.length; i < len; i++) {
        let tabMsg = this.toolsData[i].tabMsg
        if (display === 'hide') {
          this.toolsData[i].show = false
          if (tabMsg === 'hide') {
            this.toolsData[i].show = true
            this.toolsData[i].tabMsg = 'show'
          }
        } else {
          this.toolsData[i].show = true
          if (tabMsg === 'show') {
            this.toolsData[i].tabMsg = 'hide'
          }
        }
      }
    }
  },

  mounted() {
    setInterval(() => {
      fetch('https://v1.hitokoto.cn/?c=b')
        .then(res => res.json())
        .then(data => {
          if (!this.isDialogue) {
            let tool = this.$refs.tool.filter(item => {
              return item.customDialogue
            })
            if (tool && tool.length > 0) {
              tool[0].showMessage(data.hitokoto)
            }
          } else {
            this.$refs.dialogue.showMessage(data.hitokoto)
          }
        })
    }, 100000)

    this.modelPath = 'https://cdn.jsdelivr.net/gh/mqk2233/blog-file/live2d/live2d-widget-model-unitychan/assets/unitychan.model.json'

    setTimeout(() => {
      this.modelPaths = 'https://cdn.jsdelivr.net/gh/mqk2233/blog-file/live2d/live2d-widget-model-unitychan/assets/unitychan.physics.json'
    }, 2000000)

  }

}
</script>

<style lang='stylus' scoped>
.live2d-panel {
  position: fixed;
  left: 0;
  bottom: 0;
  z-index: 999;
}

.tools-panel {
  position: fixed;
  left: 0;
  bottom: 7em;
  max-width: 100px;
  z-index: 999;
}

.tab-msg {
  font-size: 14px;
}

#app {
  height: 100px;
  z-index: 999;
}
</style>
