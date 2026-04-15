<template>
  <div class="register-page">
    <Header />
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <h2>创建账户</h2>
          <p>加入TravelVista，开启您的旅程</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              size="large"
              prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input
              v-model="form.nickname"
              placeholder="请输入昵称"
              size="large"
              prefix-icon="UserFilled"
            />
          </el-form-item>
          <el-form-item prop="phone">
            <el-input
              v-model="form.phone"
              placeholder="请输入手机号"
              size="large"
              prefix-icon="Phone"
            />
          </el-form-item>
          <el-form-item prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入邮箱"
              size="large"
              prefix-icon="Message"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请确认密码"
              size="large"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleRegister"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              style="width: 100%"
              :loading="loading"
              @click="handleRegister"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="register-footer">
          <span>已有账户？</span>
          <router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import Header from '@/components/Header.vue'
import { register } from '@/api/user'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' }
  ],
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
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await register(form)
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
  padding: 40px 20px;
}

.register-card {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.register-header p {
  color: #999;
  font-size: 14px;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  color: #999;
  font-size: 14px;
}

.register-footer a {
  color: #667eea;
  margin-left: 5px;
}
</style>
