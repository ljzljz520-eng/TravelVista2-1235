<template>
  <div class="scenic-card" @click="goDetail">
    <div class="card-image">
      <img :src="item.coverImage || 'https://picsum.photos/400/300'" :alt="item.name" />
      <div class="card-rating">
        <el-icon><Star /></el-icon>
        <span>{{ item.rating || '5.0' }}</span>
      </div>
    </div>
    <div class="card-content">
      <h3 class="card-title">{{ item.name }}</h3>
      <p class="card-address">
        <el-icon><Location /></el-icon>
        {{ item.address }}
      </p>
      <div class="card-footer">
        <span class="card-price">
          <span class="price-symbol">¥</span>
          <span class="price-value">{{ item.price || '0' }}</span>
          <span class="price-unit">起</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const goDetail = () => {
  router.push(`/scenic/${props.item.id}`)
}
</script>

<style scoped>
.scenic-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.scenic-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.scenic-card:hover .card-image img {
  transform: scale(1.05);
}

.card-rating {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(255, 152, 0, 0.9);
  color: #fff;
  padding: 4px 8px;
  border-radius: 20px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-content {
  padding: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-address {
  font-size: 13px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
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

.card-price {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.price-symbol {
  font-size: 12px;
  color: #f56c6c;
}

.price-value {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;
}

.price-unit {
  font-size: 12px;
  color: #999;
}
</style>
