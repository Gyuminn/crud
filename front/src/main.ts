import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElmentPlus from 'element-plus'
import 'element-plus/dist/index.css'

import 'bootstrap/dist/css/bootstrap-utilities.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElmentPlus)

app.mount('#app')
