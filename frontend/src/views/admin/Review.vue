<template>
  <div class="admin-review">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>评论管理</span>
          <el-input
            v-model="keyword"
            placeholder="搜索评论内容"
            style="width: 300px"
            clearable
            @clear="fetchData"
            @keyup.enter="fetchData"
          >
            <template #append>
              <el-button icon="Search" @click="fetchData" />
            </template>
          </el-input>
        </div>
      </template>

      <el-table :data="reviewList" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="用户" width="150">
          <template #default="{ row }">
            <div v-if="row.user">
              <span>{{ row.user.nickname || row.user.username }}</span>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="景点" width="150">
          <template #default="{ row }">
            <span v-if="row.scenic">{{ row.scenic.name }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评论内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="rating" label="评分" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminReviews, deleteAdminReview } from '@/api/user'

const loading = ref(false)
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const reviewList = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAdminReviews({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value
    })
    reviewList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    ElMessage.error('获取评论列表失败')
  } finally {
    loading.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAdminReview(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
