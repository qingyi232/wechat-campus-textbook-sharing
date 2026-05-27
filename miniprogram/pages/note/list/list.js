const { noteApi } = require('../../../utils/api')
Page({
  data: { list: [], page: 1, loading: false, finished: false, tab: '' },
  onLoad() { this.loadList() },
  onShow() { if (this.data.list.length) this.refreshList() },
  async loadList() {
    if (this.data.finished || this.data.loading) return
    this.setData({ loading: true })
    const params = { page: this.data.page, size: 10 }
    if (this.data.tab === 'recommended') params.recommended = 1
    if (this.data.tab === 'free') params.isFree = 1
    if (this.data.tab === 'rating') params.orderBy = 'rating'
    try {
      const res = await noteApi.list(params)
      const records = res.data.records || []
      this.setData({ list: [...this.data.list, ...records], page: this.data.page + 1, finished: records.length < 10 })
    } catch (e) {} finally { this.setData({ loading: false }) }
  },
  refreshList() { this.setData({ list: [], page: 1, finished: false }); this.loadList() },
  onReachBottom() { this.loadList() },
  setTab(e) { this.setData({ tab: e.currentTarget.dataset.tab }); this.refreshList() },
  goDetail(e) { wx.navigateTo({ url: '/pages/note/detail/detail?id=' + e.currentTarget.dataset.id }) }
})
