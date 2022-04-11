//这两个是默认的
import Vue from 'vue' //引入 Vue
import VueRouter from 'vue-router' //引入 Vue 路由
import {createRouter, createWebHistory} from 'vue-router'

Vue.use(VueRouter); //安装插件

export const constantRouterMap = [
  //1.配置默认的路径，默认显示登录页, 这是vue 3.0新的写法

  {path: '/', component: () => import('@/views/NosqlFront/login2')},

  //老版可以这么写,记得引入component , import login from '../views/NosqlFront/login.vue'
  //{path: '/', name: 'login', component:login}

  //2.配置登录成功页面，使用时需要使用 path 路径来实现跳转
  {path: '/admin', component: () => import('@/views/NosqlFront/admin')},

  {path: '/Home', component: () => import('@/views/NosqlFront/Home')},
  /*//2.配置普通用户登陆页面
  {path: '/user', component: () => import('@/views/NosqlFront/user')},*/

 //2.配置普通用户注册页面
  {path: '/register', component: () => import('@/views/NosqlFront/register')},

  //2.导航+user页面
  {
    path: '/layout',
    name: 'layout',
    component: () => import("@/views/Layout/layout"),
    redirect: '/layout/userFollowed',
    children: [
      {
        path: 'userFollowed',
        name: 'userFollowed',
        component: () => import("@/views/NosqlFront/userFollowed"),
      },
      {
        path: 'userMapping',
        name: 'userMapping',
        component: () => import("@/views/NosqlFront/userMapping"),
      },
      {
        path: 'userFollower',
        name: 'userFollower',
        component: () => import("@/views/NosqlFront/userFollower"),
      }
    ]
  },


  //3.配置登录失败页面，使用时需要使用 path 路径来实现跳转
  {path: '/error', component: () => import('@/views/NosqlFront/error'), hidden: true}
]

export default new VueRouter({
   //mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),

  routes: constantRouterMap //指定路由列表
})

