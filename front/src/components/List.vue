<template>
    <div class="list">
      <p class="waiting" v-if="!logs || logs.length === 0">Waiting for new data...</p>
      <p v-for="(log, index) in logs" :key="index" class="row">{{log}}</p>
    </div>
</template>

<script>
import EventBus from '../event-bus'

export default {
  computed: {
    logs : function() {
      return this.$store.getters.logs
    }
  },  
  created() {
    // Start stream
    EventBus.$emit('ws-action', 'on')
  },
  watch: {
    logs () {
      this.$el.scrollTo(0, this.$el.scrollHeight)
    }
  }
};
</script>

<style scoped>
.list {
  margin: -30px 50px 0 50px;
  background-color: #FFF;
  border: 1px solid rgba(0, 40, 100, 0.12);
  border-radius: 2px;
  height: calc( 100% - 80px);
  overflow: auto;
}
.row {
  text-align: left;
  margin: 0;
  padding: 10px;
  border-bottom: 1px solid rgba(0, 40, 100, 0.12);
}
</style>
