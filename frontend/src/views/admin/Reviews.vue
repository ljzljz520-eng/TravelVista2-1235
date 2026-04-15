<template>
  <div class="reviews-page">
    <div class="page-header">
      <h2>评论管理</h2>
    </div>
    <div class="table-container">
      <el-table :data="reviews" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="scenicId" label="景点ID" width="100" />
        <el-table-column prop="scenicName" label="景点名称" width="180" />
        <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip />
        <el-table-column prop="rating" label="评分" width="120">
          <template #default="scope">
            <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button type="danger" size="small" @click="deleteReview(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination
      v-if="total > 0"
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadReviews"
      @current-change="loadReviews"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllReviews, deleteReview as deleteReviewApi } from '../../api/admin'

const reviews = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadReviews = async () => {
  try {
    const res = await getAllReviews(pageNum.value, pageSize.value)
    reviews.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载评论列表失败', error)
  }
}

const deleteReview = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteReviewApi(id)
    ElMessage.success('删除成功')
    loadReviews()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadReviews()
})
</script>

<style scoped>
.reviews-page {
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

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
