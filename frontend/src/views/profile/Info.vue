<template>
  <div class="info-page">
    <h2>个人信息</h2>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
          <el-radio label="未知">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="个人简介">
        <el-input v-model="form.bio" type="textarea" :rows="4" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleUpdate">
          保存修改
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getUserInfo, updateUserInfo } from '@/api/user'

const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = ref({
  username: '',
  email: '',
  phone: '',
  gender: '未知',
  bio: ''
})

const rules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    Object.assign(form.value, res.data)
    userStore.setUserInfo(res.data)
  } catch (error) {
    console.error('加载用户信息失败', error)
  }
}

const handleUpdate = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updateUserInfo(form.value)
        ElMessage.success('修改成功')
        loadUserInfo()
      } catch (error) {
        console.error('修改失败', error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.info-page {
  max-width: 600px;
}

.info-page h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 24px;
}
</style>
