<template>
  <div>
    <div style="margin-bottom:16px">
      <el-select v-model="query.status" placeholder="状态筛选" clearable @change="fetchData" style="width:160px">
        <el-option label="待处理" value="PENDING" /><el-option label="已处理" value="HANDLED" /><el-option label="已忽略" value="IGNORED" />
      </el-select>
    </div>
    <el-table :data="list" stripe v-loading="loading" style="border-radius:8px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="textbookTitle" label="举报教材" min-width="180" show-overflow-tooltip />
      <el-table-column prop="reporterName" label="举报人" width="100" />
      <el-table-column prop="reason" label="举报原因" min-width="150" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status==='PENDING'?'warning':row.status==='HANDLED'?'success':'info'" size="small">{{ {PENDING:'待处理',HANDLED:'已处理',IGNORED:'已忽略'}[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="举报时间" width="170" />
      <el-table-column label="操作" width="200" v-if="query.status!=='HANDLED'">
        <template #default="{ row }">
          <template v-if="row.status==='PENDING'">
            <el-button link type="success" @click="handle(row.id,'HANDLED','已处理，违规教材已下架')">处理</el-button>
            <el-button link type="info" @click="handle(row.id,'IGNORED','举报不属实，忽略')">忽略</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.size" :total="total" layout="total,prev,pager,next" @change="fetchData" style="margin-top:16px" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '../../api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ page: 1, size: 10, status: '' })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await adminApi.reports(query.value)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    console.error('加载举报列表失败', e)
  } finally {
    loading.value = false
  }
}
const handle = async (id, status, result) => {
  try {
    await adminApi.handleReport({ id, status, result })
    ElMessage.success('操作成功')
    fetchData()
  } catch (e) {
    console.error('处理举报失败', e)
  }
}

onMounted(fetchData)
</script>
