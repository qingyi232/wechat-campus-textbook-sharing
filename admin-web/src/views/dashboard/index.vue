<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="card in cards" :key="card.label">
        <el-card shadow="hover" style="border-radius:8px">
          <div style="display:flex;align-items:center;gap:16px">
            <el-icon :size="40" :color="card.color"><component :is="card.icon" /></el-icon>
            <div>
              <div style="font-size:28px;font-weight:700;color:#1a1a2e">{{ stats[card.key] || 0 }}</div>
              <div style="font-size:13px;color:#999">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top:20px">
      <el-col :span="12">
        <el-card style="border-radius:8px"><template #header>教材数据</template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="总教材数">{{ stats.textbookCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="在售中">{{ stats.textbookOnSale || 0 }}</el-descriptions-item>
            <el-descriptions-item label="已售出">{{ stats.textbookSold || 0 }}</el-descriptions-item>
            <el-descriptions-item label="待处理举报">{{ stats.pendingReports || 0 }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="border-radius:8px"><template #header>笔记数据</template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="总笔记数">{{ stats.noteCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="已发布">{{ stats.notePublished || 0 }}</el-descriptions-item>
            <el-descriptions-item label="待审核">{{ stats.noteReviewing || 0 }}</el-descriptions-item>
            <el-descriptions-item label="待处理反馈">{{ stats.pendingFeedback || 0 }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminApi } from '../../api'

const stats = ref({})
const cards = [
  { label: '注册用户', key: 'userCount', icon: 'User', color: '#409eff' },
  { label: '学生用户', key: 'studentCount', icon: 'School', color: '#67c23a' },
  { label: '教师用户', key: 'teacherCount', icon: 'Reading', color: '#e6a23c' },
  { label: '教材总量', key: 'textbookCount', icon: 'Goods', color: '#f56c6c' }
]

onMounted(async () => {
  const res = await adminApi.statistics()
  stats.value = res.data
})
</script>
