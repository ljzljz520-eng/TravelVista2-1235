<template>
  <div class="home">
    <Header />
    
    <div class="banner">
      <div class="banner-content">
        <h1>发现世界的美好</h1>
        <p>探索全球热门旅游景点，开启您的精彩旅程</p>
        <el-button type="primary" size="large" @click="goToScenic">
          立即探索
        </el-button>
      </div>
    </div>

    <div class="section">
      <div class="container">
        <h2 class="section-title">热门景点</h2>
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in hotList" :key="item.id">
            <ScenicCard :item="item" />
          </el-col>
        </el-row>
        <div class="more-btn">
          <el-button type="primary" @click="goToScenic">查看更多</el-button>
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import ScenicCard from '@/components/ScenicCard.vue'
import { getHotScenic } from '@/api/scenic'

const router = useRouter()
const hotList = ref([])

const fetchHotList = async () => {
  try {
    const res = await getHotScenic()
    hotList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const goToScenic = () => {
  router.push('/scenic')
}

onMounted(() => {
  fetchHotList()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 120px 0;
  text-align: center;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.section {
  padding: 60px 0;
}

.section-title {
  font-size: 32px;
  text-align: center;
  margin-bottom: 40px;
  color: #333;
}

.more-btn {
  text-align: center;
  margin-top: 40px;
}
</style>
