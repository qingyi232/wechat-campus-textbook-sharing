const { courseApi, teacherApi, noteApi } = require('../../utils/api')
Page({
  data: {
    courses: [], recommends: [], reviewNotes: [],
    noticeTitle: '', noticeContent: '',
    coursePickerList: [{ id: null, name: '全部学生（不限课程）' }],
    coursePickerIndex: 0
  },
  async onShow() {
    const [courseRes, recRes, noteRes] = await Promise.all([
      courseApi.byTeacher(),
      teacherApi.recommends(),
      noteApi.list({ status: 'REVIEWING', page: 1, size: 50 })
    ])
    const courses = courseRes.data || []
    const pickerList = [{ id: null, name: '全部学生（不限课程）' }]
    courses.forEach(c => pickerList.push({ id: c.id, name: c.courseName }))
    this.setData({
      courses,
      recommends: recRes.data || [],
      reviewNotes: noteRes.data?.records || [],
      coursePickerList: pickerList,
      coursePickerIndex: 0
    })
  },
  showAddRecommend() {
    wx.showModal({
      title: '推荐教材', editable: true, placeholderText: '输入教材名称',
      success: async (res) => {
        if (res.confirm && res.content) {
          const courseId = this.data.courses.length ? this.data.courses[0].id : null
          await teacherApi.addRecommend({ courseId, textbookTitle: res.content, reason: '教师推荐' })
          wx.showToast({ title: '推荐成功' })
          this.onShow()
        }
      }
    })
  },
  async deleteRecommend(e) {
    await teacherApi.deleteRecommend(e.currentTarget.dataset.id)
    wx.showToast({ title: '已删除' })
    this.onShow()
  },
  async approveNote(e) {
    await noteApi.updateStatus({ id: e.currentTarget.dataset.id, status: 'PUBLISHED' })
    wx.showToast({ title: '已通过审核' })
    this.onShow()
  },
  async recommendNote(e) {
    await teacherApi.recommendNote(e.currentTarget.dataset.id)
    wx.showToast({ title: '已推荐并加500热度' })
    this.onShow()
  },
  rejectNote(e) {
    const noteId = e.currentTarget.dataset.id
    wx.showModal({
      title: '驳回笔记', editable: true, placeholderText: '请输入驳回理由',
      success: async (res) => {
        if (res.confirm) {
          const reason = (res.content || '').trim() || '审核未通过'
          await teacherApi.rejectNote(noteId, reason)
          wx.showToast({ title: '已驳回' })
          this.onShow()
        }
      }
    })
  },
  onNoticeTitleInput(e) { this.setData({ noticeTitle: e.detail.value }) },
  onNoticeContentInput(e) { this.setData({ noticeContent: e.detail.value }) },
  onCoursePickerChange(e) { this.setData({ coursePickerIndex: parseInt(e.detail.value) }) },
  async publishNotice() {
    const { noticeTitle, noticeContent, coursePickerList, coursePickerIndex } = this.data
    if (!noticeTitle.trim()) { wx.showToast({ title: '请输入标题', icon: 'none' }); return }
    if (!noticeContent.trim()) { wx.showToast({ title: '请输入内容', icon: 'none' }); return }
    const courseId = coursePickerList[coursePickerIndex]?.id || null
    try {
      await teacherApi.publishNotice({ title: noticeTitle, content: noticeContent, courseId })
      wx.showToast({ title: '通知发布成功' })
      this.setData({ noticeTitle: '', noticeContent: '', coursePickerIndex: 0 })
    } catch (err) {
      wx.showToast({ title: '发布失败', icon: 'none' })
    }
  }
})
