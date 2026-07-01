import App from './App'
import Vue from 'vue'
import api from './utils/api'

Vue.config.productionTip = false
Vue.prototype.$api = api

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
