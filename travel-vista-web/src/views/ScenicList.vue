<template>
  <div class="scenic-list-container">
    <h2 class="page-title">景点列表</h2>

    <!-- 搜索和筛选区域 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" inline>
        <el-form-item label="关键词">
          <el-input v-model="filterForm.name" placeholder="搜索景点名称" clearable />
        </el-form-item>
        <el-form-item label="省份">
          <el-select v-model="filterForm.province" placeholder="选择省份" clearable>
            <el-option label="北京" value="北京" />
            <el-option label="上海" value="上海" />
            <el-option label="广东" value="广东" />
            <el-option label="江苏" value="江苏" />
            <el-option label="浙江" value="浙江" />
            <el-option label="四川" value="四川" />
            <el-option label="云南" value="云南" />
            <el-option label="陕西" value="陕西" />
          </el-select>
        </el-form-item>
        <el-form-item label="城市">
          <el-select v-model="filterForm.city" placeholder="选择城市" clearable>
            <el-option label="北京" value="北京" />
            <el-option label="上海" value="上海" />
            <el-option label="广州" value="广州" />
            <el-option label="深圳" value="深圳" />
            <el-option label="杭州" value="杭州" />
            <el-option label="南京" value="南京" />
            <el-option label="成都" value="成都" />
            <el-option label="西安" value="西安" />
            <el-option label="丽江" value="丽江" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="filterForm.type" placeholder="景点类型" clearable>
            <el-option label="自然风光" value="自然风光" />
            <el-option label="人文古迹" value="人文古迹" />
            <el-option label="主题乐园" value="主题乐园" />
            <el-option label="海滨沙滩" value="海滨沙滩" />
            <el-option label="美食体验" value="美食体验" />
          </el-select>
        </el-form-item>
        <el-form-item label="等级">
          <el-select v-model="filterForm.level" placeholder="景点等级" clearable>
            <el-option label="5A" value="5A" />
            <el-option label="4A" value="4A" />
            <el-option label="3A" value="3A" />
          </el-select>
        </el-form-item>
        <el-form-item label="评分">
          <el-select v-model="filterForm.minRating" placeholder="最低评分" clearable>
            <el-option label="3分以上" :value="3" />
            <el-option label="4分以上" :value="4" />
            <el-option label="4.5分以上" :value="4.5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScenicList">搜索</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 景点列表 -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in scenicList" :key="item.id">
        <el-card class="scenic-card" @click="$router.push(`/scenic/${item.id}`)" shadow="hover">
          <img :src="item.coverImage || 'https://via.placeholder.com/300x200?text=暂无图片'" class="scenic-cover" />
          <div class="scenic-info">
            <h4 class="scenic-name">{{ item.name }}</h4>
            <div class="scenic-tag" v-if="item.level">
              <el-tag type="success" size="small">{{ item.level }}</el-tag>
              <el-tag type="warning" size="small" style="margin-left: 8px;">{{ item.type }}</el-tag>
            </div>
            <div class="scenic-meta">
              <el-rate v-model="item.rating" disabled show-score text-color="#ff9900" />
              <span class="review-count">{{ item.reviewCount }}条评论</span>
            </div>
            <div class="scenic-address">{{ item.province }} {{ item.city }}</div>
            <div class="scenic-price">
              <span class="price">¥{{ item.ticketPrice }}</span> 起
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 空状态 -->
    <el-empty description="暂无景点数据" v-if="scenicList.length === 0 && !loading" />

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScenicPage } from '../api/scenic'

const route = useRoute()
const loading = ref(false)
const scenicList = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(12)

const filterForm = reactive({
  name: '',
  province: '',
  city: '',
  type: '',
  level: '',
  minRating: null,
  maxRating: null
})

const loadScenicList = async () => {
  loading.value = true
  try {
    const params = {
      page: page.value,
      pageSize: pageSize.value,
      ...filterForm
    }
    const res = await getScenicPage(params)
    scenicList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('获取景点列表失败:', error)
  } finally {
    loading.value = false
  }
}

const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = key === 'minRating' || key === 'maxRating' ? null : ''
  })
  page.value = 1
  loadScenicList()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadScenicList()
}

const handleCurrentChange = (val) => {
  page.value = val
  loadScenicList()
}

onMounted(() => {
  if (route.query.keyword) {
    filterForm.name = route.query.keyword
  }
  loadScenicList()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}

.scenic-card {
  margin-bottom: 20px;
  cursor: pointer;
}

.scenic-cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 4px 4px 0 0;
}

.scenic-info {
  padding: 12px 0 0 0;
}

.scenic-name {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scenic-tag {
  margin-bottom: 8px;
}

.scenic-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.review-count {
  color: #909399;
  font-size: 12px;
}

.scenic-address {
  color: #606266;
  font-size: 13px;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scenic-price {
  font-size: 16px;
  color: #f56c6c;
  font-weight: bold;
}

.price {
  font-size: 20px;
}

.pagination-container {
  text-align: center;
  margin-top: 30px;
}
</style>
