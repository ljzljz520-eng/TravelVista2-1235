<template>
  <div class="orders-page">
    <Header />
    <div class="container">
      <div class="page-title">
        <h2>我的订单</h2>
      </div>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部订单" name="all"></el-tab-pane>
        <el-tab-pane label="待支付" name="0"></el-tab-pane>
        <el-tab-pane label="已支付" name="1"></el-tab-pane>
        <el-tab-pane label="已取消" name="2"></el-tab-pane>
      </el-tabs>
      <div class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <span class="order-no">订单号: {{ order.orderNo }}</span>
            <span class="order-status" :class="getStatusClass(order.status)">
              {{ getStatusText(order.status) }}
            </span>
          </div>
          <div class="order-content">
            <div class="scenic-info">
              <img :src="order.scenicImage" :alt="order.scenicName" class="scenic-image" />
              <div class="scenic-details">
                <h3>{{ order.scenicName }}</h3>
                <p class="order-time">下单时间: {{ formatTime(order.createTime) }}</p>
                <p class="ticket-count">门票数量: {{ order.ticketCount }} 张</p>
              </div>
            </div>
            <div class="order-amount">
              <p class="amount">¥{{ order.totalAmount }}</p>
            </div>
          </div>
          <div class="order-actions">
            <el-button v-if="order.status === 0" type="primary" @click="payOrder(order.orderNo)">
              立即支付
            </el-button>
            <el-button v-if="order.status === 0" @click="cancelOrder(order.orderNo)">
              取消订单
            </el-button>
            <el-button @click="viewDetail(order.orderNo)">查看详情</el-button>
          </div>
        </div>
        <div v-if="orders.length === 0" class="empty-state">
          <el-empty description="暂无订单" />
        </div>
      </div>
      <el-pagination
        v-if="total > 0"
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOrders"
        @current-change="loadOrders"
        class="pagination"
      />
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import { getUserOrders, payOrder as payOrderApi, cancelOrder as cancelOrderApi } from '../api/order'

const router = useRouter()
const activeTab = ref('all')
const orders = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const getStatusText = (status) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已取消' }
  return map[status] || '未知'
}

const getStatusClass = (status) => {
  const map = { 0: 'status-pending', 1: 'status-paid', 2: 'status-canceled' }
  return map[status] || ''
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadOrders = async () => {
  try {
    const status = activeTab.value === 'all' ? null : parseInt(activeTab.value)
    const res = await getUserOrders(status, pageNum.value, pageSize.value)
    orders.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载订单失败', error)
  }
}

const handleTabChange = () => {
  pageNum.value = 1
  loadOrders()
}

const payOrder = async (orderNo) => {
  try {
    await payOrderApi(orderNo)
    ElMessage.success('支付成功')
    loadOrders()
  } catch (error) {
    ElMessage.error('支付失败')
  }
}

const cancelOrder = async (orderNo) => {
  try {
    await cancelOrderApi(orderNo)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    ElMessage.error('取消失败')
  }
}

const viewDetail = (orderNo) => {
  ElMessage.info('订单详情功能开发中')
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
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

.order-list {
  margin-top: 20px;
}

.order-item {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.order-no {
  color: #666;
  font-size: 14px;
}

.order-status {
  font-weight: 500;
  font-size: 14px;
}

.status-pending {
  color: #e6a23c;
}

.status-paid {
  color: #67c23a;
}

.status-canceled {
  color: #909399;
}

.order-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.scenic-info {
  display: flex;
  gap: 15px;
  flex: 1;
}

.scenic-image {
  width: 120px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.scenic-details h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.scenic-details p {
  font-size: 13px;
  color: #999;
  margin-bottom: 4px;
}

.order-amount .amount {
  font-size: 20px;
  color: #f56c6c;
  font-weight: 600;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.empty-state {
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
