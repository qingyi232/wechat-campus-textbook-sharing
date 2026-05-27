const { messageApi } = require('../../../utils/api')
const app = getApp()
Page({
  data: {
    targetId: '', targetName: '', textbookId: '', textbookTitle: '',
    messages: [], inputText: '', scrollTo: '', pollTimer: null
  },
  onLoad(options) {
    this.setData({
      targetId: options.targetId,
      targetName: decodeURIComponent(options.targetName || ''),
      textbookId: options.textbookId || '',
      textbookTitle: decodeURIComponent(options.textbookTitle || '')
    })
    wx.setNavigationBarTitle({ title: this.data.targetName || '消息' })
    this.loadMessages()
    const timer = setInterval(() => this.loadMessages(), 5000)
    this.setData({ pollTimer: timer })
  },
  onUnload() {
    clearInterval(this.data.pollTimer)
  },
  async loadMessages() {
    try {
      const res = await messageApi.list(this.data.targetId)
      const myId = app.globalData.userInfo?.id
      const msgs = (res.data || []).map(m => ({
        ...m,
        isMine: m.senderId === myId,
        timeStr: this.formatTime(m.createTime)
      }))
      this.setData({ messages: msgs })
      if (msgs.length > 0) {
        this.setData({ scrollTo: 'msg-' + msgs[msgs.length - 1].id })
      }
    } catch (e) {}
  },
  onInput(e) {
    this.setData({ inputText: e.detail.value })
  },
  async sendMsg() {
    const content = this.data.inputText.trim()
    if (!content) return
    try {
      await messageApi.send({
        receiverId: Number(this.data.targetId),
        content: content,
        textbookId: this.data.textbookId ? Number(this.data.textbookId) : null
      })
      this.setData({ inputText: '' })
      this.loadMessages()
    } catch (e) {
      wx.showToast({ title: '发送失败', icon: 'none' })
    }
  },
  formatTime(ts) {
    if (!ts) return ''
    const d = new Date(ts)
    const pad = n => n < 10 ? '0' + n : n
    return pad(d.getHours()) + ':' + pad(d.getMinutes())
  }
})
