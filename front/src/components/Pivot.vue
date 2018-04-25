<template>
  <div class="pivot">
    <p class="waiting" v-if="!hasData">Waiting for new data...</p>
    <perspective-viewer index='id' class="viewer" view="scatter"></perspective-viewer>
  </div>
</template>

<script>
import EventBus from '../event-bus'

import "@jpmorganchase/perspective-viewer";
import "@jpmorganchase/perspective-viewer-hypergrid";
import "@jpmorganchase/perspective-viewer-highcharts";

export default {
  data() {
    return {
      hasData: false
    }
  },
  mounted() {
    EventBus.$on('ws-data', (lastData) => {
      this.hasData = true
      this.$el.querySelector('.viewer').update([lastData])
    })
  },
  created() {
    // Start stream
    EventBus.$emit('ws-action', 'on')
  }
};
</script>

<style scoped>
.pivot {
  margin: -30px 50px 0 50px;
  background-color: #FFF;
  border: 1px solid rgba(0, 40, 100, 0.12);
  border-radius: 2px;
  height: calc( 100% - 80px);
  overflow: auto;
  text-align: left;
}
.viewer {
  position: relative;
  width: 100%;
  height: 100%;
}
.waiting{
  position: relative;
  width: 100%;
  margin-top: 40px;
  text-align: center;
}
</style>
