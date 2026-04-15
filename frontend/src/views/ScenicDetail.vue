<template>
  <div class="scenic-detail-page">
    <Header />
    
    <div class="container">
      <div class="detail-content" v-if="scenic">
        <div class="scenic-header">
          <div class="scenic-images">
            <img :src="scenic.coverImage || 'https://picsum.photos/800/500'" :alt="scenic.name" />
          </div>
          <div class="scenic-info">
            <h1 class="scenic-name">{{ scenic.name }}</h1>
            <div class="scenic-meta">
              <span class="rating">
                <el-icon><Star /></el-icon>
                {{ scenic.rating || '5.0' }}
              </span>
              <span class="category">{{ scenic.categoryName }}</span>
              <span class="address">
                <el-icon><Location /></el-icon>
                {{ scenic.address }}
              </span>
            </div>
            <div class="scenic-price">
              <span class="price-symbol">¥</span>
              <span class="price-value">{{ scenic.price || '0' }}</span>
              <span class="price-unit">/人起</span>
            </div>
            <div class="scenic-actions">
              <el-button type="primary" size="large" @click="showOrderDialog = true">
                立即预订
              </el-button>
              <el-button size="large" @click="toggleFavorite">
                <el-icon><Heart :fill="isFavorited ? '#f56c6c' : 'none'" :color="isFavorited ? '#f56c6c' : ''" /></el-icon>
                {{ isFavorited ? '已收藏' : '收藏' }}
              </el-button>
            </div>
          </div>
        </div>

        <div class="scenic-description">
          <h3>景点介绍</h3>
          <p>{{ scenic.description }}</p>
        </div>

        <div class="review-section">
          <h3>用户评价</h3>
          <div class="review-list">
            <div class="review-item" v-for="review in reviews" :key="review.id">
              <div class="review-header">
                <div class="review-user">
                  <el-avatar :size="40">{{ review.username?.charAt(0) || 'U' }}</el-avatar>
                  <div class="review-user-info">
                    <span class="review-username">{{ review.username || '用户' }}</span>
                    <span class="review-time">{{ review.createTime }}</span>
                  </div>
                </div>
                <div class="review-rating">
                  <el-rate v-model="review.rating" disabled />
                </div>
              </div>
              <div class="review-content">{{ review.content }}</div>
            </div>
          </div>
          <div class="empty" v-if="reviews.length === 0">
            <el-empty description="暂无评价" />
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="showOrderDialog" title="预订门票" width="500px">
      <el-form :model="orderForm" label-width="80px">
        <el-form-item label="景点名称">
          <span>{{ scenic?.name }}</span>
        </el-form-item>
        <el-form-item label="门票价格">
          <span>¥{{ scenic?.price }}</span>
        </el-form-item>
        <el-form-item label="购买数量" prop="quantity">
          <el-input-number v-model="orderForm.quantity" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="出行日期" prop="visitDate">
          <el-date-picker
            v-model="orderForm.visitDate"
            type="date"
            placeholder="选择出行日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="联系人" prop="visitorName">
          <el-input v-model="orderForm.visitorName" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="visitorPhone">
          <el-input v-model="orderForm.visitorPhone" placeholder="请输入联系电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showOrderDialog = false">取消</el-button>
        <el-button type="primary" @click="submitOrder" :loading="orderLoading">
          确认预订
        </el-button>
      </template>
    </el-dialog>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getScenicDetail } from '@/api/scenic'
import { addFavorite, cancelFavorite, checkFavorite } from '@/api/favorite'
import { getReviewList } from '@/api/review'
import { createOrder } from '@/api/order'

const route = useRoute()
const scenic = ref(null)
const reviews = ref([])
const isFavorited = ref(false)
const showOrderDialog = ref(false)
const orderLoading = ref(false)

const orderForm = reactive({
  scenicId: null,
  quantity: 1,
  visitDate: null,
  visitorName: '',
  visitorPhone: ''
})

const fetchDetail = async () => {
  try {
    const res = await getScenicDetail(route.params.id)
    scenic.value = res.data
    orderForm.scenicId = res.data.id
    checkIsFavorited()
    fetchReviews()
  } catch (error) {
    console.error(error)
  }
}

const checkIsFavorited = async () => {
  try {
    const res = await checkFavorite(route.params.id)
    isFavorited.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const toggleFavorite = async () => {
  try {
    if (isFavorited.value) {
      await cancelFavorite(route.params.id)
      isFavorited.value = false
      ElMessage.success('取消收藏成功')
    } else {
      await addFavorite(route.params.id)
      isFavorited.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchReviews = async () => {
  try {
    const res = await getReviewList({ scenicId: route.params.id, pageNum: 1, pageSize: 10 })
    reviews.value = res.data.records || []
  } catch (error) {
    console.error(error)
  }
}

const submitOrder = async () => {
  if (!orderForm.visitorName || !orderForm.visitorPhone || !orderForm.visitDate) {
    ElMessage.warning('请填写完整信息')
    return
  }
  orderLoading.value = true
  try {
    await createOrder({
      scenicId: orderForm.scenicId,
      quantity: orderForm.quantity,
      visitDate: orderForm.visitDate,
      visitorName: orderForm.visitorName,
      visitorPhone: orderForm.visitorPhone
    })
    ElMessage.success('预订成功')
    showOrderDialog.value = false
  } catch (error) {
    console.error(error)
  } finally {
    orderLoading.value = false
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.scenic-detail-page {
  min-height: 100vh;
}

.detail-content {
  padding: 40px 0;
}

.scenic-header {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  margin-bottom: 40px;
}

.scenic-images img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 12px;
}

.scenic-name {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.scenic-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.scenic-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
}

.rating {
  color: #ff9800 !important;
  font-weight: bold;
}

.category {
  background: #667eea;
  color: #fff !important;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.scenic-price {
  margin-bottom: 30px;
}

.price-symbol {
  font-size: 18px;
  color: #f56c6c;
}

.price-value {
  font-size: 48px;
  font-weight: bold;
  color: #f56c6c;
}

.price-unit {
  color: #999;
  font-size: 14px;
}

.scenic-actions {
  display: flex;
  gap: 15px;
}

.scenic-description {
  background: #fff;
  padding: 30px;
  border-radius: 12px;
  margin-bottom: 30px;
}

.scenic-description h3 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #333;
}

.scenic-description p {
  color: #666;
  line-height: 1.8;
}

.review-section {
  background: #fff;
  padding: 30px;
  border-radius: 12px;
}

.review-section h3 {
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
}

.review-item {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.review-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.review-user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.review-username {
  font-weight: 500;
  color: #333;
}

.review-time {
  font-size: 12px;
  color: #999;
}

.review-content {
  color: #666;
  line-height: 1.6;
}

.empty {
  padding: 40px 0;
}
</style>
