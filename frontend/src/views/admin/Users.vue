<template>
  <div class="users-page">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>
    <div class="search-bar">
      <el-input
        v-model="keyword"
        placeholder="搜索用户名或邮箱"
        style="width: 300px"
        @keyup.enter="loadUsers"
      >
        <template #append>
          <el-button icon="Search" @click="loadUsers" />
        </template>
      </el-input>
    </div>
    <div class="table-container">
      <el-table :data="users" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button
              :type="scope.row.status === 1 ? 'warning' : 'success'"
              size="small"
              @click="toggleStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
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
      @size-change="loadUsers"
      @current-change="loadUsers"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getUserList, updateUserStatus } from '../../api/admin'

const keyword = ref('')
const users = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadUsers = async () => {
  try {
    const res = await getUserList(keyword.value, pageNum.value, pageSize.value)
    users.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载用户列表失败', error)
  }
}

const toggleStatus = async (user) => {
  const newStatus = user.status === 1 ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await updateUserStatus(user.id, newStatus)
    ElMessage.success(`${action}成功`)
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${action}失败`)
    }
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.users-page {
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

.search-bar {
  margin-bottom: 20px;
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
