import Vue from 'vue'
import App from './App.vue'

import Store from './store'
import router from './router'

// Custom components
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
Vue.use(Vuetify)

Vue.config.productionTip = false

new Vue({
  router,
  store: Store,
  render: h => h(App)
}).$mount('#app')