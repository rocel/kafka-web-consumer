<template>
  <div class="header">
    <h1><img src="../assets/logo.png" alt="kafka icon" class="icon"> Kafka Web Consumer</h1>
    <div class="commandes">
      <img src="../assets/play.svg" alt="play" v-if="status == 'off'" @click="setStatus('on')" title="Start the streaming">
      <img src="../assets/pause.svg" alt="kafka icon" v-if="status == 'on'" @click="setStatus('off')" title="Pause the streaming">
    </div>
  </div>
</template>

<script>
import EventBus from '../event-bus'

export default {
  data : function() {
    return {
      status : 'off'
    }
  },
  methods: {
    setStatus : function(status) {
      this.status = status
      EventBus.$emit('ws-action', status)
    }
  },
  mounted: function () {
    EventBus.$on('ws-status', (newStatus) => {
      console.log('new status :', newStatus)
      this.status = newStatus
    })
  }
}
</script>

<style scoped>
  h1 {
    font-weight: 200;
    margin: 0;
    padding-top: 20px;
    padding-bottom: 40px;
    background-color: #FFF;
    border-bottom: 1px solid rgba(0, 40, 100, 0.12);
  }
  .icon {
    width: 20px;
    vertical-align: middle;
  }
  .commandes {
    position: absolute;
    right: 20px;
    top: 20px;
  }
  .commandes img {
    width: 30px;
    cursor: pointer;
  }
</style>
