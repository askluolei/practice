import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    role: ['admin','editor']     will control the page role (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if fasle ,the page will no be cached(default is false)
  }
**/
export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  { path: '/authredirect', component: _import('login/authredirect'), hidden: true },
  { path: '/404', component: _import('errorPage/404'), hidden: true },
  { path: '/401', component: _import('errorPage/401'), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: _import('dashboard/index'),
      name: 'dashboard',
      meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
    }]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/administration',
    component: Layout,
    meta: {
      title: 'administration',
      icon: 'user-round',
      role: ['ROLE_ADMIN']
    },
    children: [
      { path: 'user-management', component: _import('administration/userManagement'), name: 'userManagement', meta: { title: 'userManagement', noCache: 'true', icon: 'user' }},
      { path: 'user-tracker', component: _import('administration/userTracker'), name: 'userTracker', meta: { title: 'userTracker', noCache: 'true', icon: 'eye-open' }},
      { path: 'metrics', component: _import('administration/metrics'), name: 'metrics', meta: { title: 'metrics', noCache: 'true', icon: 'ali_metrics' }},
      { path: 'health', component: _import('administration/health'), name: 'health', meta: { title: 'health', noCache: 'true', icon: 'ali_health' }},
      { path: 'configuration', component: _import('administration/configuration'), name: 'configuration', meta: { title: 'configuration', noCache: 'true', icon: 'ali_configuration' }},
      { path: 'audits', component: _import('administration/audits'), name: 'audits', meta: { title: 'audits', noCache: 'true', icon: 'ali_audit' }},
      { path: 'logs', component: _import('administration/logs'), name: 'logs', meta: { title: 'logs', noCache: 'true', icon: 'ali_edit' }},
      { path: 'docs', component: _import('administration/docs'), name: 'docs', meta: { title: 'docs', noCache: 'true', icon: 'ali_api' }},
      { path: 'schedule', component: _import('administration/schedule'), name: 'schedule', meta: { title: 'schedule', noCache: 'true', icon: 'ali_api' }},
      { path: 'entity-audit', component: _import('administration/entityAudit'), name: 'entityAudit', meta: { title: 'entityAudit', noCache: 'true', icon: 'ali_history' }}
    ]
  },
  {
    path: '/icon',
    component: Layout,
    children: [{
      path: 'index',
      component: _import('svg-icons/index'),
      name: 'icons',
      meta: { title: 'icons', icon: 'icon', noCache: true }
    }]
  },
  {
    path: '/error',
    component: Layout,
    redirect: 'noredirect',
    name: 'errorPages',
    meta: {
      title: 'errorPages',
      icon: '404'
    },
    children: [
      { path: '401', component: _import('errorPage/401'), name: 'page401', meta: { title: 'page401', noCache: true }},
      { path: '404', component: _import('errorPage/404'), name: 'page404', meta: { title: 'page404', noCache: true }}
    ]
  },
  {
    path: '/theme',
    component: Layout,
    redirect: 'noredirect',
    children: [{ path: 'index', component: _import('theme/index'), name: 'theme', meta: { title: 'theme', icon: 'theme' }}]
  },

  {
    path: '/i18n',
    component: Layout,
    children: [{ path: 'index', component: _import('i18n-demo/index'), name: 'i18n', meta: { title: 'i18n', icon: 'international' }}]
  },

  { path: '*', redirect: '/404', hidden: true }
]
