<template>
  <div class="scenic-detail-page">
    <Header />
    
    <div class="detail-container" v-loading="loading">
      <div class="scenic-header" v-if="scenicInfo">
        <div class="scenic-images">
          <el-image
            :src="scenicInfo.imageUrl || 'https://picsum.photos/800/500?random=' + scenicInfo.id"
            :preview-src-list="[scenicInfo.imageUrl || 'https://picsum.photos/800/500?random=' + scenicInfo.id]"
            fit="cover"
            class="main-image"
          />
        </div>
        <div class="scenic-info">
          <div class="info-top">
            <el-tag type="success">{{ scenicInfo.type }}</el-tag>
            <el-rate v-model="scenicInfo.rating" disabled show-score text-color="#ff9900" />
          </div>
          <h1>{{ scenicInfo.name }}</h1>
          <p class="location">
            <el-icon><Location /></el-icon>
            {{ scenicInfo.location }}
          </p>
          <p class="description">{{ scenicInfo.description }}</p>
          <div class="info-meta">
            <div class="meta-item">
              <span class="label">开放时间</span>
              <span class="value">{{ scenicInfo.openTime }}</span>
            </div>
            <div class="meta-item">
              <span class="label">门票价格</span>
              <span class="value price">¥{{ scenicInfo.ticketPrice }}起</span>
            </div>
            <div class="meta-item">
              <span class="label">浏览量</span>
              <span class="value">{{ scenicInfo.viewCount }}次</span>
            </div>
          </div>
          <div class="action-buttons">
            <el-button 
              :type="isFavorited ? 'success' : 'primary'" 
              size="large"
              :icon="isFavorited ? 'Check' : 'Star'"
              @click="handleFavorite"
            >
              {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </div>

      <div class="detail-content">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="景点详情" name="detail">
            <div class="tab-content">
              <h3>景点介绍</h3>
              <p>{{ scenicInfo?.content || scenicInfo?.description }}</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="用户评价" name="review">
            <div class="tab-content">
              <div class="review-header">
                <h3>用户评价 ({{ total }})</h3>
                <el-button 
                  type="primary" 
                  v-if="userStore.isLoggedIn"
                  @click="showReviewDialog = true"
                >
                  发表评价
                </el-button>
              </div>
              
              <div class="review-list">
                <div class="review-item" v-for="item in reviewList" :key="item.id">
                  <div class="reviewer-info">
                    <el-avatar :size="40">
                      {{ item.userUsername?.charAt(0) }}
                    </el-avatar>
                    <div class="reviewer-detail">
                      <span class="username">{{ item.userUsername }}</span>
                      <el-rate v-model="item.rating" disabled size="small" />
                    </div>
                    <span class="review-time">{{ formatTime(item.createTime) }}</span>
                  </div>
                  <p class="review-content">{{ item.content }}</p>
                </div>
                <el-empty v-if="reviewList.length === 0" description="暂无评价" />
              </div>
              
              <div class="pagination" v-if="total > 0">
                <el-pagination
                  v-model:current-page="reviewPageNum"
                  v-model:page-size="10"
                  :total="total"
                  layout="prev, pager, next"
                  @current-change="loadReviewList"
                />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <el-dialog v-model="showReviewDialog" title="发表评价" width="500px">
      <el-form ref="reviewFormRef" :model="reviewForm" :rules="reviewRules">
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="reviewForm.rating" />
        </el-form-item>
        <el-form-item label="评价内容" prop="content">
          <el-input 
            v-model="reviewForm.content" 
            type="textarea" 
            :rows="4"
            placeholder="请输入您的评价..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showReviewDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmitReview">
          提交
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import Header from '@/components/Header.vue'
import { getScenicDetail } from '@/api/scenic'
import { getReviewList, addReview } from '@/api/review'
import { checkFavorite, addFavorite, removeFavorite } from '@/api/favorite'

const route = useRoute()
const userStore = useUserStore()
const scenicId = route.params.id

const loading = ref(false)
const scenicInfo = ref(null)
const activeTab = ref('detail')
const isFavorited = ref(false)

const reviewList = ref([])
const reviewPageNum = ref(1)
const total = ref(0)
const showReviewDialog = ref(false)
const submitting = ref(false)
const reviewFormRef = ref(null)
const reviewForm = ref({
  scenicId: scenicId,
  rating: 5,
  content: ''
})

const reviewRules = {
  rating: [{ required: true, message: '请选择评分', trigger: 'change' }],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 5, message: '评价内容至少5个字符', trigger: 'blur' }
  ]
}

const loadScenicDetail = async () => {
  loading.value = true
  try {
    const res = await getScenicDetail(scenicId)
    scenicInfo.value = res.data
  } catch (error) {
    console.error('加载景点详情失败', error)
  } finally {
    loading.value = false
  }
}

const loadReviewList = async () => {
  try {
    const res = await getReviewList({
      scenicId,
      pageNum: reviewPageNum.value,
      pageSize: 10
    })
    reviewList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载评论列表失败', error)
  }
}

const checkIsFavorited = async () => {
  if (!userStore.isLoggedIn) return
  try {
    const res = await checkFavorite(scenicId)
    isFavorited.value = res.data
  } catch (error) {
    console.error('检查收藏状态失败', error)
  }
}

const handleFavorite = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    if (isFavorited.value) {
      await removeFavorite(scenicId)
      isFavorited.value = false
      ElMessage.success('取消收藏成功')
    } else {
      await addFavorite(scenicId)
      isFavorited.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('操作失败', error)
  }
}

const handleSubmitReview = async () => {
  if (!reviewFormRef.value) return
  await reviewFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await addReview(reviewForm.value)
        ElMessage.success('评价发表成功')
        showReviewDialog.value = false
        reviewPageNum.value = 1
        loadReviewList()
        loadScenicDetail()
        reviewForm.value.content = ''
        reviewForm.value.rating = 5
      } catch (error) {
        console.error('发表评价失败', error)
      } finally {
        submitting.value = false
      }
    }
  })
}

const formatTime = (time) => {
  return new Date(time).toLocaleString()
}

onMounted(() => {
  loadScenicDetail()
  loadReviewList()
  checkIsFavorited()
})
</script>

<style scoped>
.scenic-detail-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.scenic-header {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.main-image {
  width: 100%;
  height: 400px;
  border-radius: 8px;
}

.info-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.scenic-info h1 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 12px;
}

.location {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #909399;
  margin-bottom: 16px;
}

.description {
  color: #606266;
  line-height: 1.8;
  margin-bottom: 24px;
}

.info-meta {
  display: flex;
  gap: 40px;
  margin-bottom: 32px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.meta-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item .label {
  color: #909399;
  font-size: 14px;
}

.meta-item .value {
  color: #303133;
  font-size: 18px;
  font-weight: bold;
}

.meta-item .value.price {
  color: #f56c6c;
  font-size: 24px;
}

.action-buttons {
  display: flex;
  gap: 16px;
}

.detail-content {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.tab-content h3 {
  font-size: 20px;
  color: #303133;
  margin-bottom: 16px;
}

.tab-content p {
  color: #606266;
  line-height: 1.8;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-item {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.reviewer-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.reviewer-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: bold;
  color: #303133;
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
  margin-top: 24px;
}
</style>
