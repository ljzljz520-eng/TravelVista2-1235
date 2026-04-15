<template>
  <div class="scenic-list-page">
    <Header />
    
    <div class="page-header">
      <div class="container">
        <h1>探索景点</h1>
        <p>发现全球热门旅游景点，开启您的精彩旅程</p>
      </div>
    </div>

    <div class="container">
      <div class="filter-section">
        <el-input
          v-model="keyword"
          placeholder="搜索景点名称"
          size="large"
          clearable
          style="width: 300px"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="categoryId"
          placeholder="选择分类"
          size="large"
          clearable
          @change="handleSearch"
        >
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>

      <div class="scenic-grid">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in list" :key="item.id">
            <ScenicCard :item="item" />
          </el-col>
        </el-row>
      </div>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[8, 16, 24, 32]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>

      <div class="empty" v-if="list.length === 0 && !loading">
        <el-empty description="暂无景点数据" />
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import ScenicCard from '@/components/ScenicCard.vue'
import { getScenicList, getCategoryList } from '@/api/scenic'

const keyword = ref('')
const categoryId = ref('')
const pageNum = ref(1)
const pageSize = ref(8)
const total = ref(0)
const list = ref([])
const categoryList = ref([])
const loading = ref(false)

const fetchCategoryList = async () => {
  try {
    const res = await getCategoryList()
    categoryList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      keyword: keyword.value,
      categoryId: categoryId.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    const res = await getScenicList(params)
    list.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchList()
}

onMounted(() => {
  fetchCategoryList()
  fetchList()
})
</script>

<style scoped>
.scenic-list-page {
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 60px 0;
  text-align: center;
}

.page-header h1 {
  font-size: 36px;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 16px;
  opacity: 0.9;
}

.filter-section {
  padding: 30px 0;
  display: flex;
  gap: 20px;
  align-items: center;
}

.scenic-grid {
  padding-bottom: 30px;
}

.pagination {
  text-align: center;
  padding: 30px 0;
}

.empty {
  padding: 60px 0;
}
</style>
