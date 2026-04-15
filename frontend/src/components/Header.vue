<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <div class="logo" @click="goHome">
          <el-icon><Location /></el-icon>
          <span>TravelVista</span>
        </div>
        <nav class="nav">
          <router-link to="/">首页</router-link>
          <router-link to="/scenic">景点</router-link>
          <template v-if="isLoggedIn">
            <router-link to="/favorites">收藏</router-link>
            <router-link to="/orders">订单</router-link>
          </template>
        </nav>
        <div class="user-actions">
          <template v-if="isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-name">
                {{ userInfo.nickname || userInfo.username }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="goLogin">登录</el-button>
            <el-button @click="goRegister">注册</el-button>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { logout } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => !!userStore.token)
const userInfo = computed(() => userStore.userInfo)

const goHome = () => {
  router.push('/')
}

const goLogin = () => {
  router.push('/login')
}

const goRegister = () => {
  router.push('/register')
}

const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await logout()
      userStore.logout()
      ElMessage.success('退出成功')
      router.push('/')
    } catch (error) {
      if (error !== 'cancel') {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #667eea;
  cursor: pointer;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav a {
  color: #666;
  font-size: 15px;
  transition: color 0.3s;
}

.nav a:hover,
.nav a.router-link-active {
  color: #667eea;
}

.user-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.user-name {
  cursor: pointer;
  color: #666;
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-name:hover {
  color: #667eea;
}
</style>
