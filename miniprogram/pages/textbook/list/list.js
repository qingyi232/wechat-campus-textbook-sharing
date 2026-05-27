const { textbookApi, userApi } = require('../../../utils/api')
const { fixImageUrl } = require('../../../utils/request')
Page({
  data: {
    list: [], page: 1, size: 10, loading: false, finished: false,
    majors: [], selectedMajor: '', majorId: null,
    conditions: [{ value: '', label: '全部' }, { value: 'NEW', label: '全新' }, { value: 'LIKE_NEW', label: '几乎全新' }, { value: 'GOOD', label: '良好' }, { value: 'FAIR', label: '一般' }],
    selectedCondition: '', condition: '',
    sortOptions: [{ value: '', label: '最新发布' }, { value: 'price_asc', label: '价格低→高' }, { value: 'price_desc', label: '价格高→低' }, { value: 'popular', label: '最多浏览' }],
    selectedSort: '', orderBy: '',
    conditionMap: { NEW: '全新', LIKE_NEW: '几乎全新', GOOD: '良好', FAIR: '一般', WORN: '较旧' }
  },
  async onLoad() {
    const res = await userApi.majors()
    this.setData({ majors: [{ id: null, majorName: '全部专业' }, ...res.data] })
    this.loadList()
  },
  onShow() { if (this.data.list.length) this.refreshList() },
  async loadList() {
    if (this.data.finished || this.data.loading) return
    this.setData({ loading: true })
    try {
      const params = { page: this.data.page, size: this.data.size }
      if (this.data.majorId) params.majorId = this.data.majorId
      if (this.data.condition) params.condition = this.data.condition
      if (this.data.orderBy) params.orderBy = this.data.orderBy
      const res = await textbookApi.list(params)
      const records = (res.data.records || []).map(t => ({
        ...t, images: (t.images || []).map(fixImageUrl)
      }))
      this.setData({ list: [...this.data.list, ...records], page: this.data.page + 1, finished: records.length < this.data.size })
    } catch (e) {} finally { this.setData({ loading: false }) }
  },
  refreshList() { this.setData({ list: [], page: 1, finished: false }); this.loadList() },
  onReachBottom() { this.loadList() },
  onMajorFilter(e) { const m = this.data.majors[e.detail.value]; this.setData({ majorId: m.id, selectedMajor: m.majorName }); this.refreshList() },
  onConditionFilter(e) { const c = this.data.conditions[e.detail.value]; this.setData({ condition: c.value, selectedCondition: c.label }); this.refreshList() },
  onSortChange(e) { const s = this.data.sortOptions[e.detail.value]; this.setData({ orderBy: s.value, selectedSort: s.label }); this.refreshList() },
  goDetail(e) { wx.navigateTo({ url: '/pages/textbook/detail/detail?id=' + e.currentTarget.dataset.id }) }
})
