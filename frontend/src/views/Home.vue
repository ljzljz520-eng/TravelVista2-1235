<template>
  <div class="home">
    <Header />
    
    <div class="banner">
      <div class="banner-content">
        <h1>发现世界的美好</h1>
        <p>探索全球热门景点，开启您的精彩旅程</p>
        <el-button type="primary" size="large" @click="$router.push('/scenic')">
          开始探索
          <el-icon class="el-icon--right"><ArrowRight /></el-icon>
        </el-button>
      </div>
    </div>

    <div class="section">
      <div class="section-title">
        <h2>热门景点</h2>
        <p>最受欢迎的旅游目的地</p>
      </div>
      <div class="scenic-grid">
        <el-card 
          v-for="item in hotScenic" 
          :key="item.id"
          class="scenic-card"
          shadow="hover"
          @click="$router.push(`/scenic/${item.id}`)"
        >
          <div class="card-image">
            <img :src="item.imageUrl || 'https://picsum.photos/400/300?random=' + item.id" :alt="item.name" />
            <div class="card-rating">
              <el-icon><Star /></el-icon>
              <span>{{ item.rating || 4.5 }}</span>
            </div>
          </div>
          <div class="card-content">
            <h3>{{ item.name }}</h3>
            <p class="location">
              <el-icon><Location /></el-icon>
              {{ item.location }}
            </p>
            <p class="description">{{ item.description }}</p>
            <div class="card-footer">
              <span class="price">¥{{ item.ticketPrice }}起</span>
              <span class="views">{{ item.viewCount }}人浏览</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <div class="section bg-gray">
      <div class="section-title">
        <h2>推荐景点</h2>
        <p>精选优质旅游目的地</p>
      </div>
      <div class="scenic-grid">
        <el-card 
          v-for="item in recommendScenic" 
          :key="item.id"
          class="scenic-card"
          shadow="hover"
          @click="$router.push(`/scenic/${item.id}`)"
        >
          <div class="card-image">
            <img :src="item.imageUrl || 'https://picsum.photos/400/300?random=' + (item.id + 10)" :alt="item.name" />
            <div class="card-rating">
              <el-icon><Star /></el-icon>
              <span>{{ item.rating || 4.5 }}</span>
            </div>
          </div>
          <div class="card-content">
            <h3>{{ item.name }}</h3>
            <p class="location">
              <el-icon><Location /></el-icon>
              {{ item.location }}
            </p>
            <p class="description">{{ item.description }}</p>
            <div class="card-footer">
              <span class="price">¥{{ item.ticketPrice }}起</span>
              <span class="views">{{ item.viewCount }}人浏览</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <el-footer class="footer">
      <p>© 2024 TravelVista. All rights reserved.</p>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { getHotScenic, getRecommendScenic } from '@/api/scenic'

const hotScenic = ref([])
const recommendScenic = ref([])

const loadData = async () => {
  try {
    const [hotRes, recommendRes] = await Promise.all([
      getHotScenic(),
      getRecommendScenic()
    ])
    hotScenic.value = hotRes.data || []
    recommendScenic.value = recommendRes.data || []
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.banner {
  height: 500px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  text-align: center;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 16px;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 32px;
  opacity: 0.9;
}

.section {
  padding: 60px 20px;
}

.section.bg-gray {
  background: #f5f7fa;
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
}

.section-title h2 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 8px;
}

.section-title p {
  color: #909399;
}

.scenic-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.scenic-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.scenic-card:hover {
  transform: translateY(-8px);
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
  margin: -20px -20px 16px -20px;
  border-radius: 4px 4px 0 0;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-rating {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(0, 0, 0, 0.6);
  color: #ffc107;
  padding: 4px 8px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.card-content h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
}

.location {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 14px;
  margin-bottom: 8px;
}

.description {
  color: #606266;
  font-size: 14px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.views {
  color: #909399;
  font-size: 14px;
}

.footer {
  background: #303133;
  color: #fff;
  text-align: center;
  padding: 24px;
}
</style>
