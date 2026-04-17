<template>
  <div class="profile-container">
    <h2 class="page-title">个人中心</h2>

    <el-card>
      <el-tabs v-model="activeTab">
        <!-- 个人信息 -->
        <el-tab-pane label="个人信息" name="info">
          <el-form :model="userForm" label-width="100px" style="max-width: 500px;">
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userForm.gender">
                <el-radio :label="0">保密</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateUserInfo" :loading="updating">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 我的评论 -->
        <el-tab-pane label="我的评论" name="reviews">
          <div class="review-list">
            <div class="review-item" v-for="item in reviewList" :key="item.id">
              <div class="review-header">
                <div class="review-left">
                  <h4 class="scenic-name">{{ item.scenicSpotName || '景点' + item.scenicSpotId }}</h4>
                  <el-rate v-model="item.rating" disabled size="small" />
                  <span class="review-time">{{ formatTime(item.createTime) }}</span>
                </div>
                <span class="like-count">
                  <el-icon><ThumbUp /></el-icon> {{ item.likeCount }}
                </span>
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

          <el-empty description="暂无评论" v-if="reviewList.length === 0 && !loading" />

          <!-- 分页 -->
          <div class="pagination-container" v-if="totalReview > 0">
            <el-pagination
              v-model:current-page="reviewPage"
              v-model:page-size="reviewPageSize"
              :total="totalReview"
              layout="prev, pager, next"
              @current-change="loadMyReviews"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ThumbUp } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUserInfo } from '../api/user'
import { getMyReviews } from '../api/review'

const activeTab = ref('info')
const updating = ref(false)
const loading = ref(false)

const userForm = reactive({
  id: null,
  username: '',
  nickname: '',
  phone: '',
  email: '',
  gender: 0
})

const reviewList = ref([])
const reviewPage = ref(1)
const reviewPageSize = ref(10)
const totalReview = ref(0)

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    Object.assign(userForm, res.data)
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const updateUserInfo = async () => {
  updating.value = true
  try {
    await updateUserInfo(userForm)
    ElMessage.success('修改成功')
    localStorage.setItem('userInfo', JSON.stringify(userForm))
  } catch (error) {
    console.error('修改失败:', error)
  } finally {
    updating.value = false
  }
}

const loadMyReviews = async () => {
  loading.value = true
  try {
    const res = await getMyReviews({
      page: reviewPage.value,
      pageSize: reviewPageSize.value
    })
    reviewList.value = res.data.records
    totalReview.value = res.data.total
  } catch (error) {
    console.error('获取我的评论失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadUserInfo()
  loadMyReviews()
})
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
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
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.review-left {
  flex: 1;
}

.scenic-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}

.scenic-name:hover {
  text-decoration: underline;
}

.review-time {
  margin-left: 16px;
  color: #909399;
  font-size: 12px;
}

.like-count {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 14px;
}

.like-count .el-icon {
  color: #f56c6c;
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
