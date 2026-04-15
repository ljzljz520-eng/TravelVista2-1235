<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3><el-icon><Location /></el-icon> TravelVista</h3>
          <p>发现世界的美好，探索全球热门旅游景点</p>
        </div>
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/scenic">景点</router-link></li>
            <li><router-link to="/login">登录</router-link></li>
            <li><router-link to="/register">注册</router-link></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>联系我们</h4>
          <p>邮箱：contact@travelvista.com</p>
          <p>电话：400-123-4567</p>
        </div>
      </div>
      <div class="copyright">
        <p>&copy; 2024 TravelVista. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
  background: #333;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: 60px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.footer-section h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  margin-bottom: 15px;
  color: #667eea;
}

.footer-section h4 {
  font-size: 16px;
  margin-bottom: 15px;
  color: #fff;
}

.footer-section p {
  color: #999;
  line-height: 1.8;
}

.footer-section ul {
  list-style: none;
}

.footer-section li {
  margin-bottom: 10px;
}

.footer-section a {
  color: #999;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #667eea;
}

.copyright {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #999;
  font-size: 14px;
}
</style>
