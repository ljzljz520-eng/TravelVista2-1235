<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h2>管理后台</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/reviews">
            <el-icon><ChatDotRound /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-left">
            <span>旅游景点推荐系统</span>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="admin-info">
                <el-icon><Avatar /></el-icon>
                {{ adminName }}
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  DataAnalysis,
  User,
  ChatDotRound,
  Avatar,
  ArrowDown
} from '@element-plus/icons-vue'
import { useUserStore } from '../../stores/user'
import { adminLogout } from '../../api/admin'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const adminName = computed(() => userStore.userInfo?.username || '管理员')

const activeMenu = computed(() => route.path)

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await adminLogout()
      userStore.logout()
      ElMessage.success('退出成功')
      router.push('/admin/login')
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('退出失败')
      }
    }
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
}

.logo h2 {
  color: #fff;
  font-size: 16px;
  margin: 0;
}

.header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.header-left span {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #666;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
