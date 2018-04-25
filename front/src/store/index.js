import Vue from 'vue'
import Vuex from 'vuex'

let BUFFER_SIZE = 1000

Vue.use(Vuex)

const state = {
  columns: [],
  logs: [],
}

const mutations = {
  bufferSize(state, bufferSize) {
    console.log(`Setting buffer size to ${bufferSize}`)
    BUFFER_SIZE = bufferSize
  },
  addLog: (state, log) => {
    if (state.logs.length >= BUFFER_SIZE) {
      state.logs.splice(0, 1)
    }
    state.logs.push(log)
    for (let key in log) {
      if (!state.columns.includes(key)) {
        state.columns.push(key)
      }
    }
  }
}

const actions = {}

const getters = {
  logs: state => state.logs,
  columns: state => state.columns
}

export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})