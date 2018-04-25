<template>
  <v-data-table
    class="table elevation-1"
    :headers="columns"
    :items="logs"
    hide-actions>
    <template slot="items" slot-scope="props">
      <td class="text-xs-left" v-for="prop of props.item" v-bind:key="prop.item">{{ prop }}</td>
    </template>
  </v-data-table>
</template>

<script>
import EventBus from '../event-bus'

export default {
  computed: {
    columns: function() {
      return this.$store.getters.columns.map(e => ({text : e, value : e}))
    },
    logs: function() {
      return this.$store.getters.logs
    },
  },
  created() {
    // Start stream
    EventBus.$emit('ws-action', 'on')
  },
  watch: {
    logs () {
      const el = this.$el
      if(el) {
        el.scrollTo(0, el.scrollHeight + 100)
      }
    }
  }
}
</script>

<style scoped>
.table {
  margin: -30px 50px 0 50px;
  background-color: #FFF;
  border: 1px solid rgba(0, 40, 100, 0.12);
  height: calc( 100% - 80px);
  border-radius: 2px;
  overflow: auto;
}

table {
  display: block;
  width: 100%;
  height: 100%;
}
</style>
