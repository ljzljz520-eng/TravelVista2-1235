<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px" class="admin-aside">
        <div class="logo">
          <el-icon><Setting /></el-icon>
          <span>管理后台</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          class="admin-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/admin/scenic">
            <el-icon><Location /></el-icon>
            <span>景点管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/review">
            <el-icon><ChatDotRound /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="admin-header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>管理后台</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <span>管理员：{{ userStore.adminInfo?.username }}</span>
            <el-button type="danger" size="small" @click="handleLogout">
              退出登录
            </el-button>
          </div>
        </el-header>
        <el-main class="admin-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const activeMenu = computed(() => route.path)

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.adminLogout()
    ElMessage.success('退出成功')
    router.push('/admin/login')
  }).catch(() => {})
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

:deep(.el-container) {
  height: 100%;
}

.admin-aside {
  background: #304156;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}

.admin-menu {
  border-right: none;
}

.admin-header {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  border-bottom: 1px solid #ebeef5;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.admin-main {
  background: #f0f2f5;
  padding: 20px;
}
</style>
