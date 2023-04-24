import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import VueGeolocation from 'vue-geolocation-api'

Vue.config.productionTip = false

Vue.use(VueGeolocation)

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
