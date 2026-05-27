const { authApi, userApi } = require('../../utils/api')
Page({
  data: {
    form: { username: '', realName: '', password: '', role: '', majorId: null, grade: '', studentNo: '', phone: '' },
    roles: [{ value: 'STUDENT', label: '学生' }, { value: 'TEACHER', label: '教师' }],
    roleIdx: 0, majors: [], majorIdx: 0,
    grades: ['2022', '2023', '2024', '2025', '2026'], gradeIdx: 0, loading: false
  },
  async onLoad() {
    const res = await userApi.majors()
    this.setData({ majors: res.data })
  },
  onInput(e) { this.setData({ [`form.${e.currentTarget.dataset.field}`]: e.detail.value }) },
  onRoleChange(e) { this.setData({ roleIdx: e.detail.value, 'form.role': this.data.roles[e.detail.value].value }) },
  onMajorChange(e) { this.setData({ majorIdx: e.detail.value, 'form.majorId': this.data.majors[e.detail.value].id }) },
  onGradeChange(e) { this.setData({ gradeIdx: e.detail.value, 'form.grade': this.data.grades[e.detail.value] }) },
  async handleRegister() {
    const { form } = this.data
    if (!form.username || !form.realName || !form.password || !form.role) {
      return wx.showToast({ title: '请填写必要信息', icon: 'none' })
    }
    this.setData({ loading: true })
    try {
      await authApi.register(form)
      wx.showToast({ title: '注册成功，等待审核', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 1000)
    } catch (e) {} finally { this.setData({ loading: false }) }
  }
})
