<template>
  <el-container style="height:100vh">
    <el-aside width="220px" style="background:#1a1a2e;overflow:hidden">
      <div style="padding:20px;text-align:center;color:#fff;font-size:16px;font-weight:600;border-bottom:1px solid rgba(255,255,255,0.1)">管理后台</div>
      <el-menu :default-active="$route.path" router background-color="#1a1a2e" text-color="#a0a0b0" active-text-color="#409eff" style="border:none">
        <el-menu-item index="/dashboard"><el-icon><DataAnalysis /></el-icon><span>数据概览</span></el-menu-item>
        <el-menu-item index="/user"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
        <el-menu-item index="/textbook"><el-icon><Reading /></el-icon><span>教材审核</span></el-menu-item>
        <el-menu-item index="/note"><el-icon><Document /></el-icon><span>笔记审核</span></el-menu-item>
        <el-menu-item index="/report"><el-icon><WarningFilled /></el-icon><span>举报处理</span></el-menu-item>
        <el-menu-item index="/feedback"><el-icon><ChatDotSquare /></el-icon><span>用户反馈</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid #eee;background:#fff">
        <h3 style="margin:0;font-size:18px">{{ $route.meta.title }}</h3>
        <el-button type="danger" link @click="handleLogout">退出登录</el-button>
      </el-header>
      <el-main style="background:#f5f5f5;padding:20px"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
const router = useRouter()
const userStore = useUserStore()
const handleLogout = () => { userStore.logout(); router.push('/login') }
</script>
