<template>
  <div>
    <div style="margin-bottom:16px">
      <el-select v-model="query.status" placeholder="状态筛选" clearable @change="fetchData" style="width:160px">
        <el-option label="待回复" value="PENDING" /><el-option label="已回复" value="REPLIED" />
      </el-select>
    </div>
    <el-table :data="list" stripe v-loading="loading" style="border-radius:8px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="userName" label="用户" width="100" />
      <el-table-column prop="type" label="类型" width="80">
        <template #default="{ row }">{{ {BUG:'Bug',SUGGEST:'建议',OTHER:'其他'}[row.type] || row.type }}</template>
      </el-table-column>
      <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status==='PENDING'?'warning':'success'" size="small">{{ row.status==='PENDING'?'待回复':'已回复' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="170" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button link type="primary" v-if="row.status==='PENDING'" @click="showReply(row)">回复</el-button>
          <el-button link type="info" v-else @click="showReply(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.size" :total="total" layout="total,prev,pager,next" @change="fetchData" style="margin-top:16px" />

    <el-dialog v-model="replyVisible" :title="current?.status==='PENDING'?'回复反馈':'反馈详情'" width="500px">
      <div style="margin-bottom:12px"><strong>反馈内容：</strong>{{ current?.content }}</div>
      <el-input v-model="replyText" type="textarea" :rows="4" placeholder="请输入回复内容" :disabled="current?.status==='REPLIED'" />
      <div v-if="current?.reply" style="margin-top:12px;color:#67c23a"><strong>已回复：</strong>{{ current.reply }}</div>
      <template #footer v-if="current?.status==='PENDING'">
        <el-button @click="replyVisible=false">取消</el-button>
        <el-button type="primary" @click="submitReply">提交回复</el-button>
      </template>
    </el-dialog>
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
const replyVisible = ref(false)
const current = ref(null)
const replyText = ref('')

const fetchData = async () => {
  loading.value = true
  try { const res = await adminApi.feedback(query.value); list.value = res.data.records; total.value = res.data.total }
  finally { loading.value = false }
}
const showReply = (row) => { current.value = row; replyText.value = row.reply || ''; replyVisible.value = true }
const submitReply = async () => {
  if (!replyText.value.trim()) return ElMessage.warning('请输入回复内容')
  await adminApi.replyFeedback({ id: current.value.id, reply: replyText.value })
  ElMessage.success('回复成功')
  replyVisible.value = false
  fetchData()
}

onMounted(fetchData)
</script>
