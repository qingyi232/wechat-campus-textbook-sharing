<template>
  <div>
    <div style="margin-bottom:16px;display:flex;gap:12px">
      <el-input v-model="query.keyword" placeholder="搜索笔记" clearable style="width:240px" @keyup.enter="fetchData" />
      <el-select v-model="query.status" placeholder="状态" clearable @change="fetchData" style="width:140px">
        <el-option label="待审核" value="REVIEWING" /><el-option label="已发布" value="PUBLISHED" /><el-option label="已拒绝" value="REJECTED" />
      </el-select>
      <el-button type="primary" @click="fetchData">查询</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading" style="border-radius:8px">
      <el-table-column prop="title" label="笔记标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="courseName" label="课程" width="120" />
      <el-table-column prop="authorName" label="上传者" width="100" />
      <el-table-column label="是否免费" width="80"><template #default="{ row }"><el-tag :type="row.isFree?'success':'warning'" size="small">{{ row.isFree?'免费':'付费' }}</el-tag></template></el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status==='PUBLISHED'?'success':row.status==='REVIEWING'?'warning':'danger'" size="small">{{ {REVIEWING:'待审核',PUBLISHED:'已发布',REJECTED:'已拒绝'}[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="上传时间" width="170" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="success" v-if="row.status==='REVIEWING'" @click="handleReview(row.id,'APPROVE')">通过</el-button>
          <el-button link type="danger" v-if="row.status==='REVIEWING'" @click="handleReview(row.id,'REJECT')">拒绝</el-button>
          <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.size" :total="total" layout="total,prev,pager,next" @change="fetchData" style="margin-top:16px" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { noteApi } from '../../api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ page: 1, size: 10, keyword: '', status: '' })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await noteApi.list(query.value)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    console.error('加载笔记列表失败', e)
  } finally {
    loading.value = false
  }
}
const handleReview = async (id, action) => {
  try {
    const status = action === 'APPROVE' ? 'PUBLISHED' : 'REJECTED'
    await noteApi.updateStatus({ id, status })
    ElMessage.success('操作成功')
    fetchData()
  } catch (e) {
    console.error('审核操作失败', e)
  }
}
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除？')
    await noteApi.delete(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (e) {
    if (e !== 'cancel') console.error('删除失败', e)
  }
}

onMounted(fetchData)
</script>
