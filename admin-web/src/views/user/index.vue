<template>
  <div>
    <div style="margin-bottom:16px;display:flex;gap:12px">
      <el-input v-model="query.keyword" placeholder="搜索用户" clearable style="width:240px" @keyup.enter="fetchData" />
      <el-select v-model="query.role" placeholder="角色" clearable @change="fetchData" style="width:140px">
        <el-option label="学生" value="STUDENT" /><el-option label="教师" value="TEACHER" /><el-option label="管理员" value="ADMIN" />
      </el-select>
      <el-button type="primary" @click="fetchData">查询</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading" style="border-radius:8px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="username" label="账号" width="120" />
      <el-table-column prop="role" label="角色" width="80">
        <template #default="{ row }">
          <el-tag :type="row.role==='ADMIN'?'danger':row.role==='TEACHER'?'warning':''" size="small">{{ {STUDENT:'学生',TEACHER:'教师',ADMIN:'管理员'}[row.role] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="majorName" label="专业" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status===1?'success':row.status===0?'warning':'danger'" size="small">{{ {0:'待审核',1:'正常',2:'禁用'}[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="170" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="success" v-if="row.status===0" @click="handleAudit(row.id,1)">通过</el-button>
          <el-button link type="warning" v-if="row.status===1" @click="handleAudit(row.id,2)">禁用</el-button>
          <el-button link type="primary" v-if="row.status===2" @click="handleAudit(row.id,1)">启用</el-button>
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
import { userApi } from '../../api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ page: 1, size: 10, keyword: '', role: '' })

const fetchData = async () => {
  loading.value = true
  try { const res = await userApi.list(query.value); list.value = res.data.records; total.value = res.data.total }
  finally { loading.value = false }
}
const handleAudit = async (id, status) => { await userApi.audit(id, status); ElMessage.success('操作成功'); fetchData() }
const handleDelete = async (id) => { await ElMessageBox.confirm('确认删除？'); await userApi.delete(id); ElMessage.success('删除成功'); fetchData() }

onMounted(fetchData)
</script>
