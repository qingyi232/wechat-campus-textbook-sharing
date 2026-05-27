<template>
  <div>
    <div style="margin-bottom:16px;display:flex;gap:12px">
      <el-input v-model="query.keyword" placeholder="搜索教材" clearable style="width:240px" @keyup.enter="fetchData" />
      <el-select v-model="query.status" placeholder="状态" clearable @change="fetchData" style="width:140px">
        <el-option label="在售" value="ON_SALE" /><el-option label="已售" value="SOLD" /><el-option label="已下架" value="OFF_SHELF" />
      </el-select>
      <el-button type="primary" @click="fetchData">查询</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading" style="border-radius:8px">
      <el-table-column prop="title" label="教材名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="author" label="作者" width="100" />
      <el-table-column prop="price" label="价格" width="80"><template #default="{ row }">{{ row.price }}元</template></el-table-column>
      <el-table-column prop="condition" label="成色" width="80">
        <template #default="{ row }">{{ {NEW:'全新',LIKE_NEW:'几乎全新',GOOD:'良好',FAIR:'一般',WORN:'较旧'}[row.condition] || row.condition }}</template>
      </el-table-column>
      <el-table-column prop="publisherName" label="发布者" width="100" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status==='ON_SALE'?'success':row.status==='SOLD'?'info':'danger'" size="small">{{ {ON_SALE:'在售',SOLD:'已售',OFF_SHELF:'下架'}[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="170" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
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
import { textbookApi } from '../../api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ page: 1, size: 10, keyword: '', status: '' })

const fetchData = async () => {
  loading.value = true
  try { const res = await textbookApi.list(query.value); list.value = res.data.records; total.value = res.data.total }
  finally { loading.value = false }
}
const handleDelete = async (id) => { await ElMessageBox.confirm('确认删除该教材？'); await textbookApi.delete(id); ElMessage.success('删除成功'); fetchData() }

onMounted(fetchData)
</script>
