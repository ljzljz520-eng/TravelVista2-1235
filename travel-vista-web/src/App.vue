<template>
  <el-container class="app-container">
    <el-header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <el-icon><Location /></el-icon>
          <span>旅游景点系统</span>
        </div>
        <el-menu mode="horizontal" :default-active="activeMenu" class="nav-menu">
          <el-menu-item index="/" @click="$router.push('/')">首页</el-menu-item>
          <el-menu-item index="/scenic" @click="$router.push('/scenic')">景点列表</el-menu-item>
          <el-menu-item index="/profile" v-if="isLogin" @click="$router.push('/profile')">个人中心</el-menu-item>
        </el-menu>
        <div class="user-info" v-if="isLogin">
          <span>欢迎，{{ userInfo.nickname }}</span>
          <el-button type="text" @click="logout">退出登录</el-button>
        </div>
        <div class="auth-buttons" v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
      </div>
    </el-header>
    <el-main class="main">
      <router-view />
    </el-main>
    <el-footer class="footer">
      <p>© 2024 旅游景点管理系统 毕业设计</p>
    </el-footer>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Location } from '@element-plus/icons-vue'
import { logout as logoutApi } from './api/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const isLogin = ref(false)
const userInfo = ref({})

const getUserInfo = () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    isLogin.value = true
  }
}

const logout = async () => {
  try {
    await logoutApi()
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    isLogin.value = false
    ElMessage.success('退出登录成功')
    router.push('/login')
  } catch (error) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    isLogin.value = false
    router.push('/login')
  }
}

onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.app-container {
  min-height: 100vh;
}

.header {
  background-color: #409eff;
  padding: 0;
  color: white;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.logo .el-icon {
  margin-right: 8px;
  font-size: 24px;
}

.nav-menu {
  flex: 1;
  margin: 0 40px;
  background-color: transparent;
  border: none;
}

.nav-menu .el-menu-item {
  color: white;
}

.nav-menu .el-menu-item:hover,
.nav-menu .el-menu-item.is-active {
  color: white;
  background-color: rgba(255, 255, 255, 0.2);
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  margin-right: 16px;
}

.user-info .el-button {
  color: white;
}

.auth-buttons {
  display: flex;
  gap: 12px;
}

.main {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 20px 0;
}

.footer {
  text-align: center;
  padding: 20px 0;
  background-color: #f5f7fa;
  color: #909399;
}
</style>
