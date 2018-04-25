<template>
  <v-app id="app" data-app light>
    <Header />
    <router-view></router-view>
  </v-app>
</template>

<script>
import Header from "./components/Header.vue"
import store from './store'
import EventBus from './event-bus'

export default {
  name: "app",
  components: {
    Header
  },
  data() {
    return {
      socket : null,
    }
  },
  methods: {
    startStream: function ()  {
      const uri = `ws://${window.location.host}/consume?topicName=${this.topicName}`
      // const uri = `ws://localhost:9000/consume?topicName=${this.topicName}`
      console.log(`Connecting to ${uri} ...`)
      this.socket = new WebSocket(uri)
      this.socket.onerror = function(error) {
          console.error(error)
          EventBus.$emit('ws-status', 'off')
      }
      this.socket.onopen = function() {
          console.log("Connected")
          EventBus.$emit('ws-status', 'on')
          
          let parsedData;
          this.onclose = ()  => {console.log("Connection ended")}
          this.onmessage = function(event) {
            parsedData = JSON.parse(event.data)
            store.commit('addLog', parsedData)
            EventBus.$emit('ws-data', parsedData)
          }
      }
    },
    stopStream: function () {
      console.log('Closing websocket')
      if (this.socket) {
        this.socket.close();
      }
      EventBus.$emit('ws-status', 'off')
    }
  },
  created: function () {
    this.bufferSize = this.$route.query.bufferSize
    store.commit('bufferSize', (this.bufferSize || 1000))
    this.topicName = this.$route.query.topicName
    this.isAvro = this.$route.query.isAvro

    EventBus.$on('ws-action', (payload) => {
      console.log(`New action ${payload}`)
      if ('on' === payload) {
        const topicName = this.$route.query.topicName
        const isAvro = this.$route.query.isAvro
        this.startStream(topicName, isAvro)
      } else if ('off' === payload) {
        this.stopStream()
      }
    })
  
  }
}
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #f5f7fb;
  width: 100%;
  height: 100%;
}
</style>
