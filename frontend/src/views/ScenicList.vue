<template>
  <div class="scenic-list-page">
    <Header />
    
    <div class="search-section">
      <div class="search-content">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索景点名称..." 
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
    </div>

    <div class="filter-section">
      <div class="filter-content">
        <el-select v-model="filterParams.type" placeholder="景点类型" clearable size="large" style="width: 150px;">
          <el-option label="全部类型" value="" />
          <el-option label="自然风光" value="自然风光" />
          <el-option label="历史古迹" value="历史古迹" />
          <el-option label="主题乐园" value="主题乐园" />
          <el-option label="文化体验" value="文化体验" />
        </el-select>
        <el-select v-model="filterParams.location" placeholder="地区" clearable size="large" style="width: 150px;">
          <el-option label="全部地区" value="" />
          <el-option label="北京" value="北京" />
          <el-option label="上海" value="上海" />
          <el-option label="杭州" value="杭州" />
          <el-option label="西安" value="西安" />
          <el-option label="成都" value="成都" />
        </el-select>
        <el-select v-model="filterParams.sortBy" placeholder="排序方式" size="large" style="width: 150px;">
          <el-option label="默认排序" value="" />
          <el-option label="评分最高" value="rating" />
          <el-option label="浏览最多" value="viewCount" />
          <el-option label="价格最低" value="price" />
        </el-select>
      </div>
    </div>

    <div class="list-section">
      <div class="list-content">
        <div class="scenic-grid">
          <el-card 
            v-for="item in scenicList" 
            :key="item.id"
            class="scenic-card"
            shadow="hover"
            @click="$router.push(`/scenic/${item.id}`)"
          >
            <div class="card-image">
              <img :src="item.imageUrl || 'https://picsum.photos/400/300?random=' + item.id" :alt="item.name" />
              <div class="card-rating">
                <el-icon><Star /></el-icon>
                <span>{{ item.rating || 4.5 }}</span>
              </div>
              <div class="card-type">{{ item.type }}</div>
            </div>
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="location">
                <el-icon><Location /></el-icon>
                {{ item.location }}
              </p>
              <p class="description">{{ item.description }}</p>
              <div class="card-footer">
                <span class="price">¥{{ item.ticketPrice }}起</span>
                <span class="views">{{ item.viewCount }}人浏览</span>
              </div>
            </div>
          </el-card>
        </div>
        
        <el-empty v-if="scenicList.length === 0 && !loading" description="暂无相关景点" />
        
        <div class="pagination" v-if="total > 0">
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[8, 16, 24, 32]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { getScenicList } from '@/api/scenic'

const searchKeyword = ref('')
const filterParams = ref({
  type: '',
  location: '',
  sortBy: ''
})
const scenicList = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(8)
const total = ref(0)

const loadScenicList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value,
      type: filterParams.value.type,
      location: filterParams.value.location,
      sortBy: filterParams.value.sortBy
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

const handleSearch = () => {
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

onMounted(() => {
  loadScenicList()
})
</script>

<style scoped>
.scenic-list-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.search-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.search-content {
  max-width: 600px;
  margin: 0 auto;
}

.filter-section {
  background: #fff;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.filter-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.list-section {
  padding: 40px 20px;
}

.list-content {
  max-width: 1200px;
  margin: 0 auto;
}

.scenic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.scenic-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.scenic-card:hover {
  transform: translateY(-8px);
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
  margin: -20px -20px 16px -20px;
  border-radius: 4px 4px 0 0;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-rating {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(0, 0, 0, 0.6);
  color: #ffc107;
  padding: 4px 8px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.card-type {
  position: absolute;
  top: 12px;
  left: 12px;
  background: #409eff;
  color: #fff;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.card-content h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
}

.location {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 14px;
  margin-bottom: 8px;
}

.description {
  color: #606266;
  font-size: 14px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.views {
  color: #909399;
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
