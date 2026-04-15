import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
  const adminToken = ref(localStorage.getItem('adminToken') || '')
  const adminInfo = ref(JSON.parse(localStorage.getItem('adminInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => !!adminToken.value)

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const setAdminToken = (newToken) => {
    adminToken.value = newToken
    localStorage.setItem('adminToken', newToken)
  }

  const setAdminInfo = (info) => {
    adminInfo.value = info
    localStorage.setItem('adminInfo', JSON.stringify(info))
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const adminLogout = () => {
    adminToken.value = ''
    adminInfo.value = null
    localStorage.removeItem('adminToken')
    localStorage.removeItem('adminInfo')
  }

  return {
    token,
    userInfo,
    adminToken,
    adminInfo,
    isLoggedIn,
    isAdmin,
    setToken,
    setUserInfo,
    setAdminToken,
    setAdminInfo,
    logout,
    adminLogout
  }
})
