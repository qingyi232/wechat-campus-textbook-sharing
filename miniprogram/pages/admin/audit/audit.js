const { adminApi } = require('../../../utils/api')

Page({
  data: {
    tab: 'textbook',
    textbooks: [], notes: [], reports: [],
    textbookStatus: 'REVIEWING', noteStatus: 'REVIEWING', reportStatus: 'PENDING',
    loading: false,
    statusMap: {
      REVIEWING: '待审核', ON_SALE: '在售', SOLD: '已售', REJECTED: '已拒绝',
      PUBLISHED: '已发布', PENDING: '待处理', HANDLED: '已处理'
    }
  },

  onLoad() {
    this.loadTextbooks()
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ tab })
    if (tab === 'textbook' && this.data.textbooks.length === 0) this.loadTextbooks()
    if (tab === 'note' && this.data.notes.length === 0) this.loadNotes()
    if (tab === 'report' && this.data.reports.length === 0) this.loadReports()
  },

  filterTextbook(e) {
    this.setData({ textbookStatus: e.currentTarget.dataset.status, textbooks: [] })
    this.loadTextbooks()
  },

  filterNote(e) {
    this.setData({ noteStatus: e.currentTarget.dataset.status, notes: [] })
    this.loadNotes()
  },

  filterReport(e) {
    this.setData({ reportStatus: e.currentTarget.dataset.status, reports: [] })
    this.loadReports()
  },

  loadTextbooks() {
    this.setData({ loading: true })
    const params = { page: 1, size: 50 }
    if (this.data.textbookStatus) params.status = this.data.textbookStatus
    adminApi.textbookList(params).then(res => {
      this.setData({ textbooks: res.data.records || res.data.list || res.data || [], loading: false })
    }).catch(() => this.setData({ loading: false }))
  },

  loadNotes() {
    this.setData({ loading: true })
    const params = { page: 1, size: 50 }
    if (this.data.noteStatus) params.status = this.data.noteStatus
    adminApi.noteList(params).then(res => {
      this.setData({ notes: res.data.records || res.data.list || res.data || [], loading: false })
    }).catch(() => this.setData({ loading: false }))
  },

  loadReports() {
    this.setData({ loading: true })
    const params = { page: 1, size: 50 }
    if (this.data.reportStatus) params.status = this.data.reportStatus
    adminApi.reports(params).then(res => {
      this.setData({ reports: res.data.records || res.data.list || res.data || [], loading: false })
    }).catch(() => this.setData({ loading: false }))
  },

  auditTextbook(e) {
    const { id, status } = e.currentTarget.dataset
    const action = status === 'ON_SALE' ? '通过' : '拒绝'
    wx.showModal({
      title: '确认', content: `确定${action}该教材吗？`,
      success: res => {
        if (!res.confirm) return
        adminApi.updateTextbookStatus({ id, status }).then(() => {
          wx.showToast({ title: `${action}成功` })
          this.loadTextbooks()
        })
      }
    })
  },

  auditNote(e) {
    const { id, status } = e.currentTarget.dataset
    const action = status === 'PUBLISHED' ? '通过' : '拒绝'
    wx.showModal({
      title: '确认', content: `确定${action}该笔记吗？`,
      success: res => {
        if (!res.confirm) return
        adminApi.updateNoteStatus({ id, status }).then(() => {
          wx.showToast({ title: `${action}成功` })
          this.loadNotes()
        })
      }
    })
  },

  handleReport(e) {
    const { id } = e.currentTarget.dataset
    wx.showModal({
      title: '处理举报', content: '确定标记该举报为已处理吗？',
      success: res => {
        if (!res.confirm) return
        adminApi.handleReport({ id, status: 'HANDLED', result: '管理员已处理' }).then(() => {
          wx.showToast({ title: '处理成功' })
          this.loadReports()
        })
      }
    })
  }
})
