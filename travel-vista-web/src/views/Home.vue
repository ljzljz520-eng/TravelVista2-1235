<template>
  <div class="home-container">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索景点名称"
        size="large"
        class="search-input"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
        <template #append>
          <el-button @click="handleSearch" type="primary">搜索</el-button>
        </template>
      </el-input>
    </el-card>

    <!-- 热门景点 -->
    <el-card class="section-card">
      <template #header>
        <div class="section-header">
          <h3>热门景点</h3>
          <el-button type="text" @click="$router.push('/scenic?type=hot')">查看更多</el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in hotList" :key="item.id">
          <el-card class="scenic-card" @click="$router.push(`/scenic/${item.id}`)" shadow="hover">
            <img :src="item.coverImage || 'https://via.placeholder.com/300x200?text=暂无图片'" class="scenic-cover" />
            <div class="scenic-info">
              <h4 class="scenic-name">{{ item.name }}</h4>
              <div class="scenic-meta">
                <el-rate v-model="item.rating" disabled show-score text-color="#ff9900" />
                <span class="view-count">浏览量: {{ item.viewCount }}</span>
              </div>
              <div class="scenic-address">{{ item.province }} {{ item.city }}</div>
              <div class="scenic-price">
                <span class="price">¥{{ item.ticketPrice }}</span> 起
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 推荐景点 -->
    <el-card class="section-card">
      <template #header>
        <div class="section-header">
          <h3>推荐景点</h3>
          <el-button type="text" @click="$router.push('/scenic?type=recommend')">查看更多</el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in recommendList" :key="item.id">
          <el-card class="scenic-card" @click="$router.push(`/scenic/${item.id}`)" shadow="hover">
            <img :src="item.coverImage || 'https://via.placeholder.com/300x200?text=暂无图片'" class="scenic-cover" />
            <div class="scenic-info">
              <h4 class="scenic-name">{{ item.name }}</h4>
              <div class="scenic-meta">
                <el-rate v-model="item.rating" disabled show-score text-color="#ff9900" />
                <span class="review-count">评论: {{ item.reviewCount }}</span>
              </div>
              <div class="scenic-address">{{ item.province }} {{ item.city }}</div>
              <div class="scenic-price">
                <span class="price">¥{{ item.ticketPrice }}</span> 起
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { getHotScenic, getRecommendScenic } from '../api/scenic'

const router = useRouter()
const searchKeyword = ref('')
const hotList = ref([])
const recommendList = ref([])

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/scenic', query: { keyword: searchKeyword.value } })
  }
}

const loadHotScenic = async () => {
  try {
    const res = await getHotScenic(8)
    hotList.value = res.data
  } catch (error) {
    console.error('获取热门景点失败:', error)
  }
}

const loadRecommendScenic = async () => {
  try {
    const res = await getRecommendScenic(8)
    recommendList.value = res.data
  } catch (error) {
    console.error('获取推荐景点失败:', error)
  }
}

onMounted(() => {
  loadHotScenic()
  loadRecommendScenic()
})
</script>

<style scoped>
.search-card {
  margin-bottom: 30px;
}

.search-input {
  max-width: 600px;
  margin: 0 auto;
}

.section-card {
  margin-bottom: 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.swiper-container {
  margin-bottom: 30px;
}

.banner-img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.scenic-card {
  margin-bottom: 20px;
  cursor: pointer;
}

.scenic-cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 4px 4px 0 0;
}

.scenic-info {
  padding: 12px 0 0 0;
}

.scenic-name {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scenic-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.view-count, .review-count {
  color: #909399;
  font-size: 12px;
}

.scenic-address {
  color: #606266;
  font-size: 13px;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scenic-price {
  font-size: 16px;
  color: #f56c6c;
  font-weight: bold;
}

.price {
  font-size: 20px;
}
</style>
