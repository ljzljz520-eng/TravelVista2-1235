import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/scenic',
    name: 'ScenicList',
    component: () => import('@/views/ScenicList.vue')
  },
  {
    path: '/scenic/:id',
    name: 'ScenicDetail',
    component: () => import('@/views/ScenicDetail.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/profile/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/profile/info'
      },
      {
        path: 'info',
        name: 'ProfileInfo',
        component: () => import('@/views/profile/Info.vue')
      },
      {
        path: 'favorites',
        name: 'ProfileFavorites',
        component: () => import('@/views/profile/Favorites.vue')
      },
      {
        path: 'reviews',
        name: 'ProfileReviews',
        component: () => import('@/views/profile/Reviews.vue')
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/scenic'
      },
      {
        path: 'scenic',
        name: 'AdminScenic',
        component: () => import('@/views/admin/Scenic.vue')
      },
      {
        path: 'review',
        name: 'AdminReview',
        component: () => import('@/views/admin/Review.vue')
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('@/views/admin/User.vue')
      }
    ]
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else if (to.meta.requiresAdmin && !userStore.isAdmin) {
    next('/admin/login')
  } else {
    next()
  }
})

export default router
