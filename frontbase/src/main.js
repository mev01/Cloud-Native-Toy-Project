import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import StoryblokVue from 'storyblok-vue'
import VueAnalytics from 'vue-analytics'
import store from './store';
import Vuetify from 'vuetify'
import Vuetify2 from 'vuetify/lib'

//fort-awesome
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import AOS from 'aos';
import "aos/dist/aos.css";


library.add(faUserSecret)

Vue.component('font-awesome-icon', FontAwesomeIcon)

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
Vue.use(BootstrapVue);
Vue.config.productionTip = false
const isProd = process.env.NODE_ENV === "production"
Vue.use(Vuetify);
Vue.use(Vuetify2);
Vue.use(IconsPlugin);
Vue.use(Antd);

new Vue({
  router,
  store,
  async beforeCreate() {
    let token = localStorage.getItem("access-token");
    if (store.state.memberInfo == null && token) {
      await store.dispatch("GET_MEMBER_INFO", token);
    }
  },
  created() {
    AOS.init();
  },
  render: h => h(App),
}).$mount('#app')

Vue.use(StoryblokVue)
Vue.use(VueAnalytics, {
  id: 'UA-139190314-1',
  router,
  debug: {
    enabled: !isProd,
    sendHitTask: isProd
  }
})
