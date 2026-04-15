<template>
  <div class="dashboard">
    <div class="page-header">
      <h2>数据统计</h2>
    </div>
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <p class="stat-label">用户总数</p>
              <p class="stat-value">{{ statistics.userCount || 0 }}</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon scenic-icon">
              <el-icon><Location /></el-icon>
            </div>
            <div class="stat-content">
              <p class="stat-label">景点总数</p>
              <p class="stat-value">{{ statistics.scenicCount || 0 }}</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon order-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-content">
              <p class="stat-label">订单总数</p>
              <p class="stat-value">{{ statistics.orderCount || 0 }}</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon review-icon">
              <el-icon><ChatDotRound /></el-icon>
            </div>
            <div class="stat-content">
              <p class="stat-label">评论总数</p>
              <p class="stat-value">{{ statistics.reviewCount || 0 }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="today-stats">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="stat-card">
            <div class="stat-header">
              <h3>今日订单</h3>
            </div>
            <div class="stat-body">
              <p class="big-value">{{ statistics.todayOrderCount || 0 }}</p>
              <p class="stat-desc">笔</p>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="stat-card">
            <div class="stat-header">
              <h3>今日销售额</h3>
            </div>
            <div class="stat-body">
              <p class="big-value">¥{{ statistics.todayAmount || 0 }}</p>
              <p class="stat-desc">元</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="total-amount">
      <div class="stat-card">
        <div class="stat-header">
          <h3>总销售额</h3>
        </div>
        <div class="stat-body">
          <p class="big-value amount">¥{{ statistics.totalAmount || 0 }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Location, Document, ChatDotRound } from '@element-plus/icons-vue'
import { getStatistics } from '../../api/admin'

const statistics = ref({})

const loadStatistics = async () => {
  try {
    const res = await getStatistics()
    statistics.value = res.data
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.stats-cards,
.today-stats,
.total-amount {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.scenic-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.order-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.review-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.stat-header h3 {
  font-size: 16px;
  color: #666;
  margin: 0 0 10px 0;
}

.stat-body {
  text-align: center;
  padding: 20px 0;
}

.big-value {
  font-size: 48px;
  font-weight: 700;
  color: #409eff;
  margin: 0;
}

.big-value.amount {
  color: #f56c6c;
}

.stat-desc {
  font-size: 14px;
  color: #999;
  margin: 5px 0 0 0;
}
</style>
