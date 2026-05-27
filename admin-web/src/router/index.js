import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/login/index.vue'), meta: { title: '登录' } },
  {
    path: '/',
    component: () => import('../layout/index.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/dashboard/index.vue'), meta: { title: '数据概览' } },
      { path: 'user', component: () => import('../views/user/index.vue'), meta: { title: '用户管理' } },
      { path: 'textbook', component: () => import('../views/textbook/index.vue'), meta: { title: '教材审核' } },
      { path: 'note', component: () => import('../views/note/index.vue'), meta: { title: '笔记审核' } },
      { path: 'report', component: () => import('../views/report/index.vue'), meta: { title: '举报处理' } },
      { path: 'feedback', component: () => import('../views/feedback/index.vue'), meta: { title: '用户反馈' } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  document.title = (to.meta.title || '管理后台') + ' - 校园教材笔记共享'
  const token = localStorage.getItem('admin_token')
  if (to.path !== '/login' && !token) next('/login')
  else next()
})

export default router
