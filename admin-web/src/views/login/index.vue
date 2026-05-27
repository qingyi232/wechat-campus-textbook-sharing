<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-circle bg-circle-1"></div>
      <div class="bg-circle bg-circle-2"></div>
      <div class="bg-circle bg-circle-3"></div>
    </div>

    <div class="login-content">
      <div class="login-left">
        <div class="brand-area">
          <div class="brand-icon">
            <svg viewBox="0 0 64 64" width="56" height="56" fill="none">
              <rect x="8" y="12" width="36" height="44" rx="3" stroke="#fff" stroke-width="2.5" fill="rgba(255,255,255,0.15)"/>
              <rect x="20" y="8" width="36" height="44" rx="3" stroke="#fff" stroke-width="2.5" fill="rgba(255,255,255,0.25)"/>
              <line x1="26" y1="20" x2="50" y2="20" stroke="#fff" stroke-width="2" opacity="0.7"/>
              <line x1="26" y1="28" x2="46" y2="28" stroke="#fff" stroke-width="2" opacity="0.5"/>
              <line x1="26" y1="36" x2="48" y2="36" stroke="#fff" stroke-width="2" opacity="0.5"/>
              <line x1="26" y1="44" x2="42" y2="44" stroke="#fff" stroke-width="2" opacity="0.3"/>
            </svg>
          </div>
          <h1 class="brand-title">校园教材流转平台</h1>
          <p class="brand-subtitle">二手教材交易 · 学习笔记共享 · 智慧校园生态</p>
          <div class="brand-features">
            <div class="feature-item">
              <div class="feature-dot"></div>
              <span>教材循环利用，绿色低碳</span>
            </div>
            <div class="feature-item">
              <div class="feature-dot"></div>
              <span>笔记资源共享，互助共学</span>
            </div>
            <div class="feature-item">
              <div class="feature-dot"></div>
              <span>教师推荐引导，精准匹配</span>
            </div>
          </div>
        </div>
      </div>

      <div class="login-right">
        <div class="login-card">
          <div class="card-header">
            <h2 class="card-title">管理后台</h2>
            <p class="card-desc">请使用管理员账号登录系统</p>
          </div>

          <el-form :model="form" @submit.prevent="handleLogin" class="login-form">
            <el-form-item>
              <el-input
                v-model="form.username"
                prefix-icon="User"
                placeholder="请输入管理员账号"
                size="large"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="form.password"
                prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              @click="handleLogin"
              size="large"
              class="login-btn"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form>

          <div class="quick-login">
            <el-divider>快速体验</el-divider>
            <div class="quick-tags">
              <el-tag
                @click="form.username='admin';form.password='123456'"
                effect="plain"
                class="quick-tag"
                type="info"
              >
                管理员账号
              </el-tag>
            </div>
          </div>

          <div class="card-footer">
            <span>校园二手教材流转与笔记共享系统 &copy; 2026</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '../../api'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()
const form = ref({ username: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) return ElMessage.warning('请输入账号和密码')
  loading.value = true
  try {
    const res = await authApi.login(form.value)
    if (res.data.role !== 'ADMIN') return ElMessage.error('仅管理员可登录后台')
    userStore.setUser(res.data, res.data.token)
    ElMessage.success('登录成功')
    router.push('/')
  } finally { loading.value = false }
}
</script>

<style scoped>
.login-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
}

.login-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
}

.bg-circle-1 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, #3b82f6, transparent);
  top: -10%;
  left: -5%;
  animation: float1 12s ease-in-out infinite;
}

.bg-circle-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #8b5cf6, transparent);
  bottom: -10%;
  right: -5%;
  animation: float2 15s ease-in-out infinite;
}

.bg-circle-3 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #06b6d4, transparent);
  top: 50%;
  left: 40%;
  animation: float3 10s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(40px, 30px); }
}
@keyframes float2 {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(-30px, -40px); }
}
@keyframes float3 {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(20px, -20px); }
}

.login-content {
  position: relative;
  z-index: 1;
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: center;
  gap: 80px;
  padding: 40px;
}

.login-left {
  max-width: 420px;
  color: #fff;
  animation: fadeInLeft 0.8s ease-out;
}

@keyframes fadeInLeft {
  from { opacity: 0; transform: translateX(-30px); }
  to { opacity: 1; transform: translateX(0); }
}

.brand-area {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.brand-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.3);
}

.brand-title {
  font-size: 36px;
  font-weight: 700;
  letter-spacing: 2px;
  margin: 0;
  background: linear-gradient(135deg, #fff 30%, #94a3b8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-subtitle {
  font-size: 15px;
  color: #94a3b8;
  margin: 0;
  letter-spacing: 1px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-top: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #cbd5e1;
}

.feature-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  flex-shrink: 0;
}

.login-right {
  animation: fadeInRight 0.8s ease-out;
}

@keyframes fadeInRight {
  from { opacity: 0; transform: translateX(30px); }
  to { opacity: 1; transform: translateX(0); }
}

.login-card {
  width: 400px;
  padding: 40px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(24px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 24px 48px rgba(0, 0, 0, 0.2);
}

.card-header {
  text-align: center;
  margin-bottom: 32px;
}

.card-title {
  font-size: 26px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 8px;
}

.card-desc {
  font-size: 14px;
  color: #94a3b8;
  margin: 0;
}

.login-form :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 10px;
  box-shadow: none !important;
  transition: all 0.3s;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: rgba(59, 130, 246, 0.4);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  background: rgba(255, 255, 255, 0.1);
}

.login-form :deep(.el-input__inner) {
  color: #e2e8f0;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: #64748b;
}

.login-form :deep(.el-input__prefix .el-icon) {
  color: #64748b;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 44px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  border: none;
  transition: all 0.3s;
  margin-top: 4px;
}

.login-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.4);
}

.quick-login {
  margin-top: 24px;
}

.quick-login :deep(.el-divider__text) {
  background: transparent;
  color: #64748b;
  font-size: 12px;
}

.quick-login :deep(.el-divider) {
  border-color: rgba(255, 255, 255, 0.08);
}

.quick-tags {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 8px;
}

.quick-tag {
  cursor: pointer;
  background: rgba(255, 255, 255, 0.06) !important;
  border-color: rgba(255, 255, 255, 0.12) !important;
  color: #94a3b8 !important;
  border-radius: 8px !important;
  transition: all 0.3s;
}

.quick-tag:hover {
  background: rgba(59, 130, 246, 0.15) !important;
  border-color: rgba(59, 130, 246, 0.3) !important;
  color: #3b82f6 !important;
}

.card-footer {
  margin-top: 28px;
  text-align: center;
  font-size: 12px;
  color: #475569;
}

@media (max-width: 900px) {
  .login-content {
    flex-direction: column;
    gap: 32px;
  }
  .login-left {
    text-align: center;
    max-width: 100%;
  }
  .brand-icon {
    margin: 0 auto;
  }
  .brand-features {
    align-items: center;
  }
  .login-card {
    width: 100%;
    max-width: 400px;
  }
}
</style>
