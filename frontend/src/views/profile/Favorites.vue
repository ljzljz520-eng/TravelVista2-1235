<template>
  <div class="favorites-page">
    <h2>我的收藏</h2>
    <div class="scenic-grid">
      <el-card 
        v-for="item in favoriteList" 
        :key="item.scenicId"
        class="scenic-card"
        shadow="hover"
        @click="$router.push(`/scenic/${item.scenicId}`)"
      >
        <div class="card-image">
          <img :src="item.imageUrl || 'https://picsum.photos/400/300?random=' + item.scenicId" :alt="item.scenicName" />
          <el-button 
            class="remove-btn"
            type="danger"
            size="small"
            circle
            @click.stop="handleRemove(item.scenicId)"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        <div class="card-content">
          <h3>{{ item.scenicName }}</h3>
          <p class="location">{{ item.location }}</p>
          <p class="description">{{ item.description }}</p>
        </div>
      </el-card>
    </div>
    <el-empty v-if="favoriteList.length === 0 && !loading" description="暂无收藏" />
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadFavorites"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFavoriteList, removeFavorite } from '@/api/favorite'

const favoriteList = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(8)
const total = ref(0)

const loadFavorites = async () => {
  loading.value = true
  try {
    const res = await getFavoriteList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    favoriteList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载收藏列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleRemove = (scenicId) => {
  ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await removeFavorite(scenicId)
      ElMessage.success('取消收藏成功')
      loadFavorites()
    } catch (error) {
      console.error('取消收藏失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadFavorites()
})
</script>

<style scoped>
.favorites-page h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 24px;
}

.scenic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.scenic-card {
  cursor: pointer;
}

.card-image {
  position: relative;
  height: 180px;
  overflow: hidden;
  margin: -20px -20px 16px -20px;
  border-radius: 4px 4px 0 0;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.scenic-card:hover .remove-btn {
  opacity: 1;
}

.card-content h3 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 8px;
}

.location {
  color: #909399;
  font-size: 14px;
  margin-bottom: 8px;
}

.description {
  color: #606266;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
