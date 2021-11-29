import Vue from 'vue'
import Router from 'vue-router'
import Index from '../views/index'
import Login from '../views/login'
import Test from '../views/Test'
import Userlayout from '../views/User/userlayout'
import Adminlayout from '../views/Admin/adminlayout'
import Userregister from '../views/userregister'
import Videobase from '../views/User/videobase'
import Audioeditor from '../views/User/audioeditor'
import MainView from '../views/User/recorder'
import Adminecharts from '../views/Admin/adminecharts'
import Userecharts from '../views/User/userecharts'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path:'/test',
      name:'Test',
      component: Test
    },
    {
      path:'/userlayout',
      name:'Userlayout',
      component: Userlayout,
      children:[
        {
          path:'/videobase',
          name:'Videobase',
          component: Videobase,
        },
        {
          path:'/audioeditor',
          name:'Audioeditor',
          component: Audioeditor,
          // children:[
          //   {
          //     path:'/recorder',
          //     name:'MainView',
          //     component:MainView
          //   }
          // ]
        },
        {
          path:'/userecharts',
          name:'Userecharts',
          component: Userecharts,
        }
          // {
          //   path:'/recorder',
          //   name:'MainView',
          //   component:MainView
          // }
      ]
    },
    {
      path:'/adminlayout',
      name:'Adminlayout',
      component: Adminlayout,
      children:[
        {
          path:'/adminecharts',
          name:'Adminecharts',
          component: Adminecharts,
        }
        ]
    },
    // {
    //   path:'/adminecharts',
    //   name:'Adminecharts',
    //   component: Adminecharts,
    // },
    {
      path:'/userregister',
      name:'Userregister',
      component: Userregister
    }
  ]
})
