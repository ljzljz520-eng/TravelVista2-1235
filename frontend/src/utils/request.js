import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import router from '@/router'

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = userStore.token
    } else if (userStore.adminToken) {
      config.headers.Authorization = userStore.adminToken
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res
    } else {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error) => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
    }
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default service
