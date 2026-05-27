const { notificationApi } = require('../../utils/api')
Page({
  data: {
    list: [],
    typeMap: { SYSTEM: '系统', TEXTBOOK: '教材', NOTE: '笔记', TEACHER: '教师' },
    typeClass: { SYSTEM: 'tag-blue', TEXTBOOK: 'tag-orange', NOTE: 'tag-green', TEACHER: 'tag-blue' }
  },
  async onShow() {
    const res = await notificationApi.list({ page: 1, size: 50 })
    this.setData({ list: res.data.records || [] })
  },
  async markRead(e) {
    const id = e.currentTarget.dataset.id
    await notificationApi.read(id)
    const list = this.data.list.map(n => n.id === id ? { ...n, isRead: 1 } : n)
    this.setData({ list })
  }
})
