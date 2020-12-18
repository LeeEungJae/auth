import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store/index';
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import User from '../views/User.vue'

Vue.use(VueRouter)

const requireAuth = () => (to, from, next) => {
  if (store.state.token != null) {
    return next();
  }
  alert("로그인을 하고 이용해주세요!");
  next('/login');
}

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path :'/login',
    name : 'login',
    component : Login
  },
  {
    path :'/signup',
    name : 'signup',
    component : Signup
  },
  {
    path : '/user',
    name : 'user',
    component : User,
    beforeEnter: requireAuth()
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})



export default router
