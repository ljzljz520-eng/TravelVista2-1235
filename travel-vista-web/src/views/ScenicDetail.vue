<template>
  <div class="scenic-detail-container" v-loading="loading">
    <div v-if="scenicInfo">
      <!-- 基本信息 -->
      <el-card class="basic-info-card">
        <div class="scenic-header">
          <div class="scenic-images">
            <el-image
              :src="scenicInfo.coverImage || 'https://via.placeholder.com/600x400?text=暂无图片'"
              class="main-image"
              :preview-src-list="previewImages"
            />
          </div>
          <div class="scenic-basic">
            <h2 class="scenic-name">{{ scenicInfo.name }}</h2>
            <div class="scenic-tags">
              <el-tag type="success" size="small" v-if="scenicInfo.level">{{ scenicInfo.level }}</el-tag>
              <el-tag type="warning" size="small" v-if="scenicInfo.type">{{ scenicInfo.type }}</el-tag>
            </div>
            <div class="rating-section">
              <el-rate v-model="scenicInfo.rating" disabled show-score text-color="#ff9900" />
              <span class="review-count">{{ scenicInfo.reviewCount }}条评论</span>
              <span class="view-count">浏览量: {{ scenicInfo.viewCount }}</span>
            </div>
            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span>{{ scenicInfo.province }} {{ scenicInfo.city }} {{ scenicInfo.address }}</span>
            </div>
            <div class="info-item" v-if="scenicInfo.openTime">
              <el-icon><Clock /></el-icon>
              <span>开放时间: {{ scenicInfo.openTime }}</span>
            </div>
            <div class="info-item" v-if="scenicInfo.visitTime">
              <el-icon><Timer /></el-icon>
              <span>建议游玩时间: {{ scenicInfo.visitTime }}</span>
            </div>
            <div class="price-section">
              <span class="price-label">门票价格:</span>
              <span class="price">¥{{ scenicInfo.ticketPrice }}</span>
              <span class="price-unit">/人</span>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 景点介绍 -->
      <el-card class="detail-card">
        <template #header>
          <h3>景点介绍</h3>
        </template>
        <div class="description" v-html="scenicInfo.content || scenicInfo.description">
          {{ scenicInfo.content || scenicInfo.description || '暂无详细介绍' }}
        </div>
      </el-card>

      <!-- 评论区域 -->
      <el-card class="detail-card">
        <template #header>
          <div class="card-header">
            <h3>用户点评</h3>
            <el-button type="primary" @click="showReviewDialog = true" v-if="isLogin">发表评论</el-button>
            <el-button type="primary" @click="$router.push('/login')" v-else>登录后可评论</el-button>
          </div>
        </template>

        <!-- 评论列表 -->
        <div class="review-list">
          <div class="review-item" v-for="item in reviewList" :key="item.id">
            <div class="review-header">
              <el-avatar :src="item.userAvatar || 'https://via.placeholder.com/40'" />
              <div class="review-user">
                <span class="username">用户{{ item.userId }}</span>
                <el-rate v-model="item.rating" disabled size="small" />
                <span class="review-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <el-button 
                type="text" 
                :icon="ThumbUp" 
                @click="handleLike(item.id)"
              >
                {{ item.likeCount }}
              </el-button>
            </div>
            <div class="review-content">{{ item.content }}</div>
            <div class="review-images" v-if="item.images">
              <el-image 
                v-for="(img, index) in JSON.parse(item.images)" 
                :key="index" 
                :src="img" 
                class="review-img"
              />
            </div>
          </div>
        </div>

        <el-empty description="暂无评论" v-if="reviewList.length === 0 && !reviewLoading" />

        <!-- 分页 -->
        <div class="pagination-container" v-if="totalReview > 0">
          <el-pagination
            v-model:current-page="reviewPage"
            v-model:page-size="reviewPageSize"
            :total="totalReview"
            layout="prev, pager, next"
            @current-change="loadReviewList"
          />
        </div>
      </el-card>
    </div>
  </div>

  <!-- 发表评论弹窗 -->
  <el-dialog v-model="showReviewDialog" title="发表评论" width="600px">
    <el-form :model="reviewForm" label-width="80px">
      <el-form-item label="评分" required>
        <el-rate v-model="reviewForm.rating" />
      </el-form-item>
      <el-form-item label="评论内容" required>
        <el-input 
          v-model="reviewForm.content" 
          type="textarea" 
          :rows="4" 
          placeholder="请输入您的评论"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="showReviewDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReview" :loading="submitting">发表</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Location, Clock, Timer, ThumbUp } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getScenicDetail } from '../api/scenic'
import { getReviewByScenic, addReview, likeReview } from '../api/review'

const route = useRoute()
const router = useRouter()
const scenicId = computed(() => route.params.id)

const loading = ref(false)
const scenicInfo = ref(null)
const previewImages = ref([])

const reviewLoading = ref(false)
const reviewList = ref([])
const reviewPage = ref(1)
const reviewPageSize = ref(10)
const totalReview = ref(0)

const isLogin = ref(!!localStorage.getItem('token'))
const showReviewDialog = ref(false)
const submitting = ref(false)
const reviewForm = reactive({
  scenicSpotId: null,
  content: '',
  rating: 5
})

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadScenicDetail = async () => {
  loading.value = true
  try {
    const res = await getScenicDetail(scenicId.value)
    scenicInfo.value = res.data
    if (scenicInfo.value.images) {
      previewImages.value = JSON.parse(scenicInfo.value.images)
    }
    if (scenicInfo.value.coverImage) {
      previewImages.value.unshift(scenicInfo.value.coverImage)
    }
  } catch (error) {
    console.error('获取景点详情失败:', error)
    ElMessage.error('景点不存在或已下架')
    router.back()
  } finally {
    loading.value = false
  }
}

const loadReviewList = async () => {
  reviewLoading.value = true
  try {
    const res = await getReviewByScenic(scenicId.value, {
      page: reviewPage.value,
      pageSize: reviewPageSize.value
    })
    reviewList.value = res.data.records
    totalReview.value = res.data.total
  } catch (error) {
    console.error('获取评论列表失败:', error)
  } finally {
    reviewLoading.value = false
  }
}

const handleLike = async (id) => {
  try {
    await likeReview(id)
    ElMessage.success('点赞成功')
    const review = reviewList.value.find(item => item.id === id)
    if (review) {
      review.likeCount++
    }
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

const submitReview = async () => {
  if (!reviewForm.content.trim()) {
    ElMessage.error('请输入评论内容')
    return
  }
  if (reviewForm.rating < 1 || reviewForm.rating > 5) {
    ElMessage.error('请选择评分')
    return
  }
  
  submitting.value = true
  try {
    reviewForm.scenicSpotId = scenicId.value
    await addReview(reviewForm)
    ElMessage.success('评论发表成功')
    showReviewDialog.value = false
    reviewForm.content = ''
    reviewForm.rating = 5
    loadReviewList()
    loadScenicDetail()
  } catch (error) {
    console.error('发表评论失败:', error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadScenicDetail()
  loadReviewList()
})
</script>

<style scoped>
.basic-info-card {
  margin-bottom: 20px;
}

.scenic-header {
  display: flex;
  gap: 30px;
}

.scenic-images {
  width: 50%;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.scenic-basic {
  flex: 1;
}

.scenic-name {
  font-size: 28px;
  font-weight: bold;
  margin: 0 0 16px 0;
}

.scenic-tags {
  margin-bottom: 16px;
}

.rating-section {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.review-count, .view-count {
  color: #909399;
  font-size: 14px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: #606266;
  font-size: 15px;
}

.info-item .el-icon {
  color: #409eff;
}

.price-section {
  margin-top: 20px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.price-label {
  font-size: 16px;
  color: #606266;
}

.price {
  font-size: 32px;
  font-weight: bold;
  color: #f56c6c;
}

.price-unit {
  color: #909399;
  font-size: 14px;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.description {
  line-height: 1.8;
  font-size: 15px;
  color: #303133;
}

.review-list {
  margin-bottom: 20px;
}

.review-item {
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.review-user {
  flex: 1;
  margin-left: 12px;
}

.username {
  display: block;
  font-weight: bold;
  margin-bottom: 4px;
}

.review-time {
  margin-left: 16px;
  color: #909399;
  font-size: 12px;
}

.review-content {
  line-height: 1.6;
  margin-bottom: 12px;
  font-size: 15px;
}

.review-images {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.review-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.pagination-container {
  text-align: center;
}
</style>
