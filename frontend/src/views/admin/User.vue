<template>
  <div class="admin-user">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-input
            v-model="keyword"
            placeholder="搜索用户名/昵称/邮箱"
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

      <el-table :data="userList" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              size="small"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
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
import { getAdminUsers, updateUserStatus, deleteAdminUser } from '@/api/user'

const loading = ref(false)
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const userList = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAdminUsers({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value
    })
    userList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleToggleStatus = (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await updateUserStatus(row.id, newStatus)
      ElMessage.success(`${action}成功`)
      fetchData()
    } catch (error) {
      ElMessage.error(`${action}失败`)
    }
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗？删除后不可恢复！', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAdminUser(row.id)
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
