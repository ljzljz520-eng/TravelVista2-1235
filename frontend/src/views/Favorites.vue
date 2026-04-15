<template>
  <div class="favorites-page">
    <Header />
    <div class="container">
      <div class="page-title">
        <h2>我的收藏</h2>
      </div>
      <div class="favorites-grid">
        <div v-for="item in favorites" :key="item.id" class="favorite-item">
          <div class="scenic-card">
            <img :src="item.scenicImage" :alt="item.scenicName" class="scenic-image" />
            <div class="scenic-info">
              <h3>{{ item.scenicName }}</h3>
              <div class="rating">
                <el-rate v-model="item.rating" disabled show-score text-color="#ff9900" />
              </div>
              <p class="price">¥{{ item.price }} 起</p>
              <p class="address">{{ item.address }}</p>
            </div>
            <div class="card-actions">
              <el-button type="primary" @click="viewDetail(item.scenicId)">查看详情</el-button>
              <el-button type="danger" @click="removeFavorite(item.id)">取消收藏</el-button>
            </div>
          </div>
        </div>
        <div v-if="favorites.length === 0" class="empty-state">
          <el-empty description="暂无收藏" />
        </div>
      </div>
      <el-pagination
        v-if="total > 0"
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 48]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadFavorites"
        @current-change="loadFavorites"
        class="pagination"
      />
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import { getFavoriteList, removeFavorite as removeFavoriteApi } from '../api/favorite'

const router = useRouter()
const favorites = ref([])
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadFavorites = async () => {
  try {
    const res = await getFavoriteList(pageNum.value, pageSize.value)
    favorites.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载收藏列表失败', error)
  }
}

const viewDetail = (scenicId) => {
  router.push(`/scenic/${scenicId}`)
}

const removeFavorite = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await removeFavoriteApi(id)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  loadFavorites()
})
</script>

<style scoped>
.favorites-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
}

.page-title h2 {
  font-size: 24px;
  color: #333;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.favorite-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.favorite-item:hover {
  transform: translateY(-5px);
}

.scenic-card {
  display: flex;
  flex-direction: column;
}

.scenic-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.scenic-info {
  padding: 15px;
}

.scenic-info h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
}

.rating {
  margin-bottom: 10px;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: 600;
  margin-bottom: 8px;
}

.address {
  font-size: 13px;
  color: #999;
}

.card-actions {
  display: flex;
  gap: 10px;
  padding: 15px;
  border-top: 1px solid #eee;
}

.card-actions .el-button {
  flex: 1;
}

.empty-state {
  grid-column: 1 / -1;
  padding: 60px 0;
  background: #fff;
  border-radius: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>
