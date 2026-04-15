<template>
  <div class="profile-page">
    <Header />
    <div class="profile-container">
      <el-container>
        <el-aside width="200px" class="profile-aside">
          <el-menu
            :default-active="activeMenu"
            router
            class="profile-menu"
          >
            <el-menu-item index="/profile/info">
              <el-icon><User /></el-icon>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item index="/profile/favorites">
              <el-icon><Star /></el-icon>
              <span>我的收藏</span>
            </el-menu-item>
            <el-menu-item index="/profile/reviews">
              <el-icon><ChatDotRound /></el-icon>
              <span>我的评论</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="profile-main">
          <router-view />
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from '@/components/Header.vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

:deep(.el-container) {
  background: #fff;
  border-radius: 8px;
  min-height: 600px;
}

.profile-aside {
  border-right: 1px solid #ebeef5;
}

.profile-menu {
  border-right: none;
}

.profile-main {
  padding: 24px;
}
</style>
