<template>
  <div class="profile-page">
    <Header />
    
    <div class="page-header">
      <div class="container">
        <h1>个人中心</h1>
        <p>管理您的账户信息</p>
      </div>
    </div>

    <div class="container">
      <div class="profile-content">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="form.username" disabled />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdate" :loading="loading">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getUserInfo, updateUserInfo } from '@/api/user'

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  phone: '',
  email: ''
})

const rules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const fetchUserInfo = async () => {
  try {
    const res = await getUserInfo()
    Object.assign(form, res.data)
  } catch (error) {
    console.error(error)
  }
}

const handleUpdate = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updateUserInfo(form)
        ElMessage.success('更新成功')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.profile-page {
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

.profile-content {
  padding: 40px 0;
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  font-size: 18px;
  font-weight: 500;
}
</style>
