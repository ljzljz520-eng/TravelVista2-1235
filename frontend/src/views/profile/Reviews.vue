<template>
  <div class="reviews-page">
    <h2>我的评论</h2>
    <div class="review-list">
      <div class="review-item" v-for="item in reviewList" :key="item.id">
        <div class="review-header">
          <div class="scenic-info" @click="$router.push(`/scenic/${item.scenicId}`)">
            <h3>{{ item.scenicName }}</h3>
            <el-rate v-model="item.rating" disabled size="small" />
          </div>
          <div class="review-actions">
            <span class="review-time">{{ formatTime(item.createTime) }}</span>
            <el-button 
              type="danger" 
              size="small" 
              link
              @click="handleDelete(item.id)"
            >
              删除
            </el-button>
          </div>
        </div>
        <p class="review-content">{{ item.content }}</p>
      </div>
    </div>
    <el-empty v-if="reviewList.length === 0 && !loading" description="暂无评论" />
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadReviews"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyReviews, deleteReview } from '@/api/review'

const reviewList = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadReviews = async () => {
  loading.value = true
  try {
    const res = await getMyReviews({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    reviewList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载评论列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteReview(id)
      ElMessage.success('删除成功')
      loadReviews()
    } catch (error) {
      console.error('删除失败', error)
    }
  }).catch(() => {})
}

const formatTime = (time) => {
  return new Date(time).toLocaleString()
}

onMounted(() => {
  loadReviews()
})
</script>

<style scoped>
.reviews-page h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 24px;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 24px;
}

.review-item {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.scenic-info {
  cursor: pointer;
}

.scenic-info h3 {
  font-size: 16px;
  color: #409eff;
  margin-bottom: 8px;
}

.review-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.review-time {
  color: #909399;
  font-size: 14px;
}

.review-content {
  color: #606266;
  line-height: 1.6;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
