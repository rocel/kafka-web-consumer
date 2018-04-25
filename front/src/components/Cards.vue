<template>
  <v-container grid-list-md text-xs-center class="cards">
    <v-layout row wrap>
      <v-flex xs3 v-for="(key, index) in keys" v-bind:key="index">
        <v-card tile>
          <v-card-text primary-title>
            <h3 class="headline mb-0">{{keys[index].value ? logs[logs.length - 1][keys[index].value] : 'Please select a key.'}}</h3>
          </v-card-text>
          <v-card-actions>
            <v-select :items="columns" dense v-model="key.value" v-bind:label="keyTitle" autocomplete v-bind:disabled="isDisabled"></v-select>
          </v-card-actions>
        </v-card>
      </v-flex>
      <v-flex xs3>
        <v-card tile class="newCard">
          <v-card-text primary-title ripple>
            <h3 class="headline mb-0" @click="newCard" title="Create a new card">+</h3>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import EventBus from '../event-bus'

export default {
  data() {
    return {
      keys : []
    }
  },
  computed: {
    isDisabled() {
      return this.columns.length === 0
    },
    keyTitle() {
      return (this.$store.getters.columns.length > 0) ? 'Key' : 'Key (loading ...)'
    },
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
  methods: {
    newCard() {
      this.keys.push({
        value: null
      })
    }
  }
}
</script>

<style scoped>
.cards {
  margin: -30px 50px 0 50px;
  background-color: #FFF;
  border: 1px solid rgba(0, 40, 100, 0.12);
  height: calc( 100% - 80px);
  border-radius: 2px;
  overflow: auto;
  max-width: 100%;
  width: auto;
}

.newCard h3 {
  cursor: pointer;
}
.newCard {
  top: 30%;
  height: auto;
  width: 90px;
  left: calc(50% - 45px);
}
</style>
