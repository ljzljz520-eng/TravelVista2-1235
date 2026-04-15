<template>
  <div class="scenic-manage">
    <div class="page-header">
      <h2>景点管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增景点
      </el-button>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="景点名称">
          <el-input v-model="searchForm.keyword" placeholder="请输入景点名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScenicList">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="scenicList" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="景点名称" min-width="150" />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="location" label="地区" width="120" />
        <el-table-column prop="ticketPrice" label="票价" width="100">
          <template #default="{ row }">
            ¥{{ row.ticketPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="景点名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入景点名称" />
        </el-form-item>
        <el-form-item label="景点类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择景点类型">
            <el-option label="自然风光" value="自然风光" />
            <el-option label="历史古迹" value="历史古迹" />
            <el-option label="主题乐园" value="主题乐园" />
            <el-option label="文化体验" value="文化体验" />
          </el-select>
        </el-form-item>
        <el-form-item label="所在地区" prop="location">
          <el-input v-model="form.location" placeholder="请输入所在地区" />
        </el-form-item>
        <el-form-item label="门票价格" prop="ticketPrice">
          <el-input-number v-model="form.ticketPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="开放时间" prop="openTime">
          <el-input v-model="form.openTime" placeholder="如：08:00-18:00" />
        </el-form-item>
        <el-form-item label="图片地址" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="简要描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入简要描述" />
        </el-form-item>
        <el-form-item label="详细介绍" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入详细介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getScenicList, addScenic, updateScenic, deleteScenic } from '@/api/scenic'

const loading = ref(false)
const scenicList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchForm = reactive({
  keyword: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增景点')
const submitting = ref(false)
const formRef = ref(null)
const isEdit = ref(false)
const form = reactive({
  id: null,
  name: '',
  type: '',
  location: '',
  ticketPrice: 0,
  openTime: '',
  imageUrl: '',
  description: '',
  content: ''
})

const rules = {
  name: [{ required: true, message: '请输入景点名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择景点类型', trigger: 'change' }],
  location: [{ required: true, message: '请输入所在地区', trigger: 'blur' }],
  ticketPrice: [{ required: true, message: '请输入门票价格', trigger: 'blur' }]
}

const loadScenicList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: searchForm.keyword
    }
    const res = await getScenicList(params)
    scenicList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载景点列表失败', error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.keyword = ''
  pageNum.value = 1
  loadScenicList()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  pageNum.value = 1
  loadScenicList()
}

const handleCurrentChange = (page) => {
  pageNum.value = page
  loadScenicList()
}

const handleAdd = () => {
  dialogTitle.value = '新增景点'
  isEdit.value = false
  Object.assign(form, {
    id: null,
    name: '',
    type: '',
    location: '',
    ticketPrice: 0,
    openTime: '',
    imageUrl: '',
    description: '',
    content: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑景点'
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除景点"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteScenic(row.id)
      ElMessage.success('删除成功')
      loadScenicList()
    } catch (error) {
      console.error('删除失败', error)
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEdit.value) {
          await updateScenic(form)
          ElMessage.success('修改成功')
        } else {
          await addScenic(form)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        loadScenicList()
      } catch (error) {
        console.error('提交失败', error)
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
  loadScenicList()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.search-card,
.table-card {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
