const { request } = require('./request')

const authApi = {
  login: data => request({ url: '/api/auth/login', method: 'POST', data }),
  register: data => request({ url: '/api/auth/register', method: 'POST', data })
}

const userApi = {
  info: () => request({ url: '/api/user/info' }),
  majors: () => request({ url: '/api/user/majors' }),
  update: data => request({ url: '/api/user/update', method: 'PUT', data }),
  changePwd: data => request({ url: '/api/user/changePwd', method: 'PUT', data })
}

const textbookApi = {
  list: data => request({ url: '/api/textbook/list', data }),
  detail: id => request({ url: '/api/textbook/detail/' + id }),
  publish: data => request({ url: '/api/textbook/publish', method: 'POST', data }),
  update: data => request({ url: '/api/textbook/update', method: 'PUT', data }),
  favorite: id => request({ url: '/api/textbook/favorite/' + id, method: 'POST' }),
  comments: id => request({ url: '/api/textbook/comments/' + id }),
  comment: data => request({ url: '/api/textbook/comment', method: 'POST', data }),
  report: data => request({ url: '/api/textbook/report', method: 'POST', data }),
  myFavorites: () => request({ url: '/api/textbook/myFavorites' }),
  delete: id => request({ url: '/api/textbook/' + id, method: 'DELETE' })
}

const noteApi = {
  list: data => request({ url: '/api/note/list', data }),
  detail: id => request({ url: '/api/note/detail/' + id }),
  upload: data => request({ url: '/api/note/upload', method: 'POST', data }),
  favorite: id => request({ url: '/api/note/favorite/' + id, method: 'POST' }),
  rate: data => request({ url: '/api/note/rate', method: 'POST', data }),
  ratings: id => request({ url: '/api/note/ratings/' + id }),
  download: id => request({ url: '/api/note/download/' + id, method: 'POST' }),
  myFavorites: () => request({ url: '/api/note/myFavorites' }),
  updateStatus: data => request({ url: '/api/note/status', method: 'PUT', data }),
  delete: id => request({ url: '/api/note/' + id, method: 'DELETE' })
}

const courseApi = {
  list: data => request({ url: '/api/course/list', data }),
  all: data => request({ url: '/api/course/all', data }),
  byTeacher: () => request({ url: '/api/course/byTeacher' })
}

const teacherApi = {
  recommends: () => request({ url: '/api/teacher/recommends' }),
  byCourse: courseId => request({ url: '/api/teacher/recommends/course/' + courseId }),
  addRecommend: data => request({ url: '/api/teacher/recommend', method: 'POST', data }),
  deleteRecommend: id => request({ url: '/api/teacher/recommend/' + id, method: 'DELETE' }),
  recommendNote: noteId => request({ url: '/api/teacher/recommendNote/' + noteId, method: 'POST' }),
  rejectNote: (noteId, reason) => request({ url: '/api/teacher/rejectNote/' + noteId, method: 'POST', data: { reason } }),
  publishNotice: data => request({ url: '/api/teacher/publishNotice', method: 'POST', data })
}

const notificationApi = {
  list: data => request({ url: '/api/notification/list', data }),
  unread: () => request({ url: '/api/notification/unread' }),
  read: id => request({ url: '/api/notification/read/' + id, method: 'PUT' })
}

const feedbackApi = {
  submit: data => request({ url: '/api/feedback/submit', method: 'POST', data })
}

const adminApi = {
  statistics: () => request({ url: '/api/admin/statistics' }),
  reports: data => request({ url: '/api/admin/reports', data }),
  handleReport: data => request({ url: '/api/admin/report/handle', method: 'PUT', data }),
  feedback: data => request({ url: '/api/admin/feedback', data }),
  replyFeedback: data => request({ url: '/api/admin/feedback/reply', method: 'PUT', data }),
  userList: data => request({ url: '/api/user/list', data }),
  auditUser: (id, status) => request({ url: '/api/user/audit/' + id, method: 'PUT', data: { status } }),
  resetPwd: id => request({ url: '/api/user/resetPwd/' + id, method: 'PUT' }),
  deleteUser: id => request({ url: '/api/user/' + id, method: 'DELETE' }),
  textbookList: data => request({ url: '/api/textbook/list', data }),
  updateTextbookStatus: data => request({ url: '/api/textbook/status', method: 'PUT', data }),
  noteList: data => request({ url: '/api/note/list', data }),
  updateNoteStatus: data => request({ url: '/api/note/status', method: 'PUT', data })
}

const messageApi = {
  send: data => request({ url: '/api/message/send', method: 'POST', data }),
  list: targetId => request({ url: '/api/message/list/' + targetId }),
  conversations: () => request({ url: '/api/message/conversations' })
}

module.exports = { authApi, userApi, textbookApi, noteApi, courseApi, teacherApi, notificationApi, feedbackApi, adminApi, messageApi }
