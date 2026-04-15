<template>
  <el-header class="header">
    <div class="header-content">
      <div class="logo" @click="$router.push('/')">
        <el-icon><Location /></el-icon>
        <span>TravelVista</span>
      </div>
      <div class="nav-menu">
        <el-menu :default-active="activeMenu" mode="horizontal" :router="true">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/scenic">景点列表</el-menu-item>
        </el-menu>
      </div>
      <div class="user-actions">
        <template v-if="userStore.isLoggedIn">
          <el-dropdown @command="handleCommand">
            <span class="user-name">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ userStore.userInfo?.username?.charAt(0) }}
              </el-avatar>
              <span>{{ userStore.userInfo?.username }}</span>
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
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      ElMessage.success('退出成功')
      router.push('/')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}

.nav-menu {
  flex: 1;
  margin-left: 40px;
}

:deep(.el-menu) {
  border-bottom: none;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}
</style>
