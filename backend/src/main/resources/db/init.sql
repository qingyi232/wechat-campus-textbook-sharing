-- 校园二手教材流转与笔记共享系统 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS textbook_db DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE textbook_db;

-- 1. 专业信息表
DROP TABLE IF EXISTS sys_major;
CREATE TABLE sys_major (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    major_name VARCHAR(100) NOT NULL COMMENT '专业名称',
    major_code VARCHAR(50) COMMENT '专业编码',
    department VARCHAR(100) COMMENT '所属院系',
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '专业信息表';

-- 2. 用户信息表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(200) NOT NULL COMMENT '密码(BCrypt)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    role VARCHAR(20) NOT NULL COMMENT 'STUDENT/TEACHER/ADMIN',
    major_id BIGINT COMMENT '所属专业ID',
    grade VARCHAR(20) COMMENT '年级(2022/2023/2024/2025)',
    student_no VARCHAR(30) COMMENT '学号',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(500) COMMENT '头像URL',
    wechat VARCHAR(50) COMMENT '微信号',
    bio VARCHAR(500) COMMENT '个人简介',
    status INT DEFAULT 0 COMMENT '0-待审核 1-正常 2-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    KEY idx_role (role),
    KEY idx_major (major_id),
    KEY idx_grade (grade)
) COMMENT '用户信息表';

-- 3. 课程信息表
DROP TABLE IF EXISTS sys_course;
CREATE TABLE sys_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(200) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(50) COMMENT '课程编号',
    major_id BIGINT COMMENT '所属专业ID',
    grade VARCHAR(20) COMMENT '适用年级',
    semester VARCHAR(20) COMMENT '学期(上/下)',
    credit DECIMAL(3,1) COMMENT '学分',
    description VARCHAR(500) COMMENT '课程描述',
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_major (major_id),
    KEY idx_grade (grade)
) COMMENT '课程信息表';

-- 4. 教师-课程关联表
DROP TABLE IF EXISTS teacher_course;
CREATE TABLE teacher_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_teacher (teacher_id),
    KEY idx_course (course_id)
) COMMENT '教师课程关联表';

-- 5. 二手教材信息表
DROP TABLE IF EXISTS textbook;
CREATE TABLE textbook (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '教材名称',
    author VARCHAR(100) COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    isbn VARCHAR(30) COMMENT 'ISBN号',
    edition VARCHAR(50) COMMENT '版次',
    original_price DECIMAL(10,2) COMMENT '原价',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    book_condition VARCHAR(20) NOT NULL COMMENT '成色:NEW/LIKE_NEW/GOOD/FAIR/WORN',
    course_id BIGINT COMMENT '关联课程ID',
    major_id BIGINT COMMENT '适用专业ID',
    grade VARCHAR(20) COMMENT '适用年级',
    description TEXT COMMENT '详细描述',
    seller_id BIGINT NOT NULL COMMENT '发布者ID',
    contact_type VARCHAR(20) DEFAULT 'WECHAT' COMMENT '联系方式:WECHAT/PHONE',
    contact_info VARCHAR(100) COMMENT '联系信息',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏数',
    status VARCHAR(20) DEFAULT 'ON_SALE' COMMENT 'ON_SALE/SOLD/OFF_SHELF/REVIEWING/REJECTED',
    reject_reason VARCHAR(300) COMMENT '驳回原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    KEY idx_seller (seller_id),
    KEY idx_course (course_id),
    KEY idx_major (major_id),
    KEY idx_status (status)
) COMMENT '二手教材信息表';

-- 6. 教材图片表
DROP TABLE IF EXISTS textbook_image;
CREATE TABLE textbook_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    textbook_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_textbook (textbook_id)
) COMMENT '教材图片表';

-- 7. 教材收藏表
DROP TABLE IF EXISTS textbook_favorite;
CREATE TABLE textbook_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    textbook_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_textbook (user_id, textbook_id),
    KEY idx_textbook (textbook_id)
) COMMENT '教材收藏表';

-- 8. 教材评论表
DROP TABLE IF EXISTS textbook_comment;
CREATE TABLE textbook_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    textbook_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    parent_id BIGINT DEFAULT 0 COMMENT '回复的评论ID,0表示顶级评论',
    status INT DEFAULT 1 COMMENT '1-正常 0-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_textbook (textbook_id),
    KEY idx_user (user_id)
) COMMENT '教材评论表';

-- 9. 教材举报表
DROP TABLE IF EXISTS textbook_report;
CREATE TABLE textbook_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    textbook_id BIGINT NOT NULL,
    reporter_id BIGINT NOT NULL,
    reason VARCHAR(500) NOT NULL COMMENT '举报原因',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/HANDLED/DISMISSED',
    handle_result VARCHAR(500) COMMENT '处理结果',
    handler_id BIGINT COMMENT '处理人ID',
    handle_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_textbook (textbook_id)
) COMMENT '教材举报表';

-- 10. 学习笔记信息表
DROP TABLE IF EXISTS study_note;
CREATE TABLE study_note (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(300) NOT NULL COMMENT '笔记标题',
    description TEXT COMMENT '笔记描述',
    course_id BIGINT COMMENT '关联课程ID',
    major_id BIGINT COMMENT '适用专业ID',
    grade VARCHAR(20) COMMENT '适用年级',
    author_id BIGINT NOT NULL COMMENT '上传者ID',
    note_type VARCHAR(20) NOT NULL COMMENT '类型:DOCUMENT/IMAGE/MIXED',
    is_free INT DEFAULT 1 COMMENT '1-免费 0-付费',
    price DECIMAL(10,2) DEFAULT 0 COMMENT '价格(付费时)',
    cover_url VARCHAR(500) COMMENT '封面图URL',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏数',
    avg_rating DECIMAL(3,1) DEFAULT 0 COMMENT '平均评分',
    rating_count INT DEFAULT 0 COMMENT '评分人数',
    is_recommended INT DEFAULT 0 COMMENT '是否被推荐',
    recommend_teacher_id BIGINT COMMENT '推荐教师ID',
    status VARCHAR(20) DEFAULT 'REVIEWING' COMMENT 'PUBLISHED/REVIEWING/REJECTED/OFF_SHELF',
    reject_reason VARCHAR(300),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    KEY idx_author (author_id),
    KEY idx_course (course_id),
    KEY idx_status (status),
    KEY idx_recommended (is_recommended)
) COMMENT '学习笔记信息表';

-- 11. 笔记文件表
DROP TABLE IF EXISTS note_file;
CREATE TABLE note_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    note_id BIGINT NOT NULL,
    file_name VARCHAR(300) NOT NULL COMMENT '原始文件名',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_type VARCHAR(50) COMMENT '文件类型(pdf/doc/jpg/png)',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小(字节)',
    sort_order INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_note (note_id)
) COMMENT '笔记文件表';

-- 12. 笔记收藏表
DROP TABLE IF EXISTS note_favorite;
CREATE TABLE note_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    note_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_note (user_id, note_id),
    KEY idx_note (note_id)
) COMMENT '笔记收藏表';

-- 13. 笔记评分表
DROP TABLE IF EXISTS note_rating;
CREATE TABLE note_rating (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    note_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    score INT NOT NULL COMMENT '评分1-5',
    comment VARCHAR(500) COMMENT '评价内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_note (user_id, note_id),
    KEY idx_note (note_id)
) COMMENT '笔记评分表';

-- 14. 笔记下载记录表
DROP TABLE IF EXISTS note_download;
CREATE TABLE note_download (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    note_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_note (note_id),
    KEY idx_user (user_id)
) COMMENT '笔记下载记录表';

-- 15. 教师推荐教材表
DROP TABLE IF EXISTS teacher_recommend;
CREATE TABLE teacher_recommend (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    textbook_title VARCHAR(200) NOT NULL COMMENT '推荐教材名称',
    author VARCHAR(100) COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    isbn VARCHAR(30) COMMENT 'ISBN',
    edition VARCHAR(50) COMMENT '版次',
    reason VARCHAR(500) COMMENT '推荐理由',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_teacher (teacher_id),
    KEY idx_course (course_id)
) COMMENT '教师推荐教材表';

-- 16. 系统通知表
DROP TABLE IF EXISTS sys_notification;
CREATE TABLE sys_notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    type VARCHAR(30) COMMENT 'SYSTEM/TEXTBOOK/NOTE/TEACHER',
    sender_id BIGINT,
    receiver_id BIGINT COMMENT 'NULL表示全体通知',
    is_read INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_receiver (receiver_id),
    KEY idx_type (type)
) COMMENT '系统通知表';

-- 17. 用户反馈表
DROP TABLE IF EXISTS user_feedback;
CREATE TABLE user_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    contact VARCHAR(100) COMMENT '联系方式',
    images VARCHAR(2000) COMMENT '截图URL(JSON数组)',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/REPLIED/CLOSED',
    reply VARCHAR(1000) COMMENT '管理员回复',
    reply_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user (user_id),
    KEY idx_status (status)
) COMMENT '用户反馈表';

-- 18. 站内消息表
DROP TABLE IF EXISTS sys_message;
CREATE TABLE sys_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    receiver_id BIGINT NOT NULL COMMENT '接收者ID',
    content TEXT NOT NULL COMMENT '消息内容',
    textbook_id BIGINT COMMENT '关联教材ID',
    is_read INT DEFAULT 0 COMMENT '0-未读 1-已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_sender (sender_id),
    KEY idx_receiver (receiver_id),
    KEY idx_create_time (create_time)
) COMMENT '站内消息表';

-- 19. 操作日志表
DROP TABLE IF EXISTS sys_operation_log;
CREATE TABLE sys_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    username VARCHAR(50),
    module VARCHAR(50) COMMENT '操作模块',
    operation VARCHAR(100) COMMENT '操作类型',
    method VARCHAR(200),
    params TEXT,
    ip VARCHAR(50),
    status INT COMMENT '1-成功 0-失败',
    error_msg VARCHAR(500),
    execution_time BIGINT COMMENT '执行时长(ms)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user (user_id),
    KEY idx_time (create_time)
) COMMENT '操作日志表';

-- ==================== 示例数据 ====================

-- 专业数据
INSERT INTO sys_major (major_name, major_code, department, sort_order) VALUES
('软件工程', 'SE', '计算机科学与技术学院', 1),
('计算机科学与技术', 'CS', '计算机科学与技术学院', 2),
('数据科学与大数据技术', 'DS', '计算机科学与技术学院', 3),
('电子信息工程', 'EE', '电子信息工程学院', 4),
('通信工程', 'CE', '电子信息工程学院', 5),
('工商管理', 'BA', '经济管理学院', 6),
('市场营销', 'MK', '经济管理学院', 7),
('英语', 'EN', '外国语学院', 8);

-- 用户数据 (密码均为 123456)
INSERT INTO sys_user (username, password, real_name, role, major_id, grade, student_no, phone, wechat, status) VALUES
('admin', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '系统管理员', 'ADMIN', NULL, NULL, NULL, '13800000001', 'admin_wx', 1),
('teacher_zhang', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '张教授', 'TEACHER', 1, NULL, NULL, '13800000010', 'zhang_teacher', 1),
('teacher_li', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '李老师', 'TEACHER', 2, NULL, NULL, '13800000011', 'li_teacher', 1),
('teacher_wang', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '王老师', 'TEACHER', 4, NULL, NULL, '13800000012', 'wang_teacher', 1),
('student_cheng', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '程旭', 'STUDENT', 1, '2024', '2407442202', '13800000020', 'chengxu_wx', 1),
('student_liu', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '刘思涵', 'STUDENT', 1, '2024', '2407442203', '13800000021', 'liush_wx', 1),
('student_zhao', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '赵明宇', 'STUDENT', 2, '2023', '2307441105', '13800000022', 'zhaomy_wx', 1),
('student_chen', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '陈雅琪', 'STUDENT', 2, '2024', '2407441208', '13800000023', 'chenyq_wx', 1),
('student_sun', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '孙佳宁', 'STUDENT', 4, '2023', '2307443310', '13800000024', 'sunjn_wx', 1),
('student_wu', '$2a$10$rsy/RwKIiqn8q3K5q1XTvO0af80rr2L2WQOFRZ1vy06a9OKxAgmQ2', '吴博文', 'STUDENT', 6, '2024', '2407445512', '13800000025', 'wubw_wx', 1);

-- 课程数据
INSERT INTO sys_course (course_name, course_code, major_id, grade, semester, credit, description) VALUES
('Java程序设计', 'SE201', 1, '2024', '上', 4.0, 'Java语言基础、面向对象编程、常用API'),
('数据结构与算法', 'SE202', 1, '2024', '下', 3.5, '线性表、树、图、排序、查找等'),
('数据库原理', 'SE301', 1, '2024', '下', 3.0, '关系模型、SQL、事务、索引优化'),
('软件工程导论', 'SE302', 1, '2024', '上', 3.0, '需求分析、系统设计、测试方法'),
('Web前端开发', 'SE303', 1, '2024', '下', 3.0, 'HTML/CSS/JavaScript/Vue.js'),
('操作系统', 'CS201', 2, '2023', '上', 4.0, '进程管理、内存管理、文件系统'),
('计算机网络', 'CS202', 2, '2023', '下', 3.5, 'TCP/IP、HTTP、网络安全'),
('Python数据分析', 'DS201', 3, '2024', '上', 3.0, 'NumPy、Pandas、Matplotlib'),
('数字电路', 'EE101', 4, '2023', '上', 4.0, '逻辑门、组合电路、时序电路'),
('信号与系统', 'EE201', 4, '2023', '下', 3.5, '连续信号、离散信号、傅里叶变换'),
('管理学原理', 'BA101', 6, '2024', '上', 3.0, '管理理论、组织行为、战略管理'),
('大学英语', 'EN101', 8, '2024', '上', 4.0, '综合英语、听说读写');

-- 教师课程关联
INSERT INTO teacher_course (teacher_id, course_id) VALUES
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
(3, 6), (3, 7),
(4, 9), (4, 10);

-- 二手教材数据
INSERT INTO textbook (title, author, publisher, isbn, edition, original_price, price, book_condition, course_id, major_id, grade, description, seller_id, contact_type, contact_info, view_count, favorite_count, status) VALUES
('Java核心技术 卷I', 'Cay S. Horstmann', '机械工业出版社', '9787111547426', '第12版', 149.00, 45.00, 'GOOD', 1, 1, '2024', '使用了一学期，有少量笔记标注，整体保存完好，无缺页。配套习题答案也一起送。', 7, 'WECHAT', 'zhaomy_wx', 128, 12, 'ON_SALE'),
('数据结构（C语言版）', '严蔚敏', '清华大学出版社', '9787302023685', '第2版', 39.00, 12.00, 'FAIR', 2, 1, '2024', '有较多笔记和划线标注，前几章有课堂练习，适合预习复习用。', 7, 'PHONE', '13800000022', 85, 6, 'ON_SALE'),
('数据库系统概论', '王珊', '高等教育出版社', '9787040406641', '第5版', 49.50, 18.00, 'LIKE_NEW', 3, 1, '2024', '几乎全新，只翻阅过几次，无任何标注。', 9, 'WECHAT', 'sunjn_wx', 56, 8, 'ON_SALE'),
('软件工程（第4版）', 'Ian Sommerville', '机械工业出版社', '9787111526285', '第4版', 79.00, 25.00, 'GOOD', 4, 1, '2024', '用了一学期，部分章节有高亮标注，整体干净。', 5, 'WECHAT', 'chengxu_wx', 42, 3, 'ON_SALE'),
('计算机网络（第8版）', '谢希仁', '电子工业出版社', '9787121411748', '第8版', 59.90, 20.00, 'GOOD', 7, 2, '2023', '经典教材，有部分笔记，附赠复习大纲手写版。', 8, 'WECHAT', 'chenyq_wx', 93, 9, 'ON_SALE'),
('操作系统概念', 'Abraham Silberschatz', '机械工业出版社', '9787111671732', '第10版', 99.00, 35.00, 'LIKE_NEW', 6, 2, '2023', '买了没怎么看，自学用的其他资料，几乎全新。', 7, 'WECHAT', 'zhaomy_wx', 67, 5, 'ON_SALE'),
('数字电子技术基础', '阎石', '高等教育出版社', '9787040444933', '第6版', 55.10, 15.00, 'FAIR', 9, 4, '2023', '用过两学期，有不少笔记标注和课后习题解答。', 9, 'PHONE', '13800000024', 38, 2, 'ON_SALE'),
('管理学（第15版）', 'Stephen P. Robbins', '中国人民大学出版社', '9787300284200', '第15版', 75.00, 28.00, 'NEW', 11, 6, '2024', '全新未拆封，买多了一本。', 10, 'WECHAT', 'wubw_wx', 31, 4, 'ON_SALE'),
('Vue.js设计与实现', '霍春阳', '人民邮电出版社', '9787115583864', '第1版', 109.80, 40.00, 'GOOD', 5, 1, '2024', '前端课程推荐参考书，有部分阅读笔记。', 6, 'WECHAT', 'liush_wx', 112, 15, 'SOLD'),
('Python编程：从入门到实践', 'Eric Matthes', '人民邮电出版社', '9787115546081', '第3版', 89.80, 30.00, 'GOOD', 8, 3, '2024', '数据分析课用书，有代码练习批注。', 5, 'WECHAT', 'chengxu_wx', 76, 7, 'ON_SALE');

-- 教材图片数据
INSERT INTO textbook_image (textbook_id, image_url, sort_order) VALUES
(1, '/uploads/textbook/java_core_1.jpg', 0),
(1, '/uploads/textbook/java_core_2.jpg', 1),
(2, '/uploads/textbook/data_struct_1.jpg', 0),
(3, '/uploads/textbook/database_1.jpg', 0),
(4, '/uploads/textbook/se_1.jpg', 0),
(5, '/uploads/textbook/network_1.jpg', 0),
(5, '/uploads/textbook/network_2.jpg', 1),
(6, '/uploads/textbook/os_1.jpg', 0),
(7, '/uploads/textbook/digital_1.jpg', 0),
(8, '/uploads/textbook/mgmt_1.jpg', 0),
(9, '/uploads/textbook/vue_1.jpg', 0),
(10, '/uploads/textbook/python_1.jpg', 0);

-- 教材收藏数据
INSERT INTO textbook_favorite (user_id, textbook_id) VALUES
(5, 1), (5, 5), (5, 6),
(6, 1), (6, 3), (6, 10),
(8, 1), (8, 2), (8, 5),
(9, 4), (9, 8),
(10, 9);

-- 教材评论数据
INSERT INTO textbook_comment (textbook_id, user_id, content, parent_id) VALUES
(1, 5, '这本Java核心技术讲得很透彻，学弟学妹值得入手！', 0),
(1, 6, '请问笔记标注多吗？影响阅读吗？', 0),
(1, 7, '不多，主要是重点知识点的标注，反而方便复习。', 2),
(5, 8, '谢希仁的计算机网络是经典，这个价格很划算！', 0),
(5, 7, '附赠的复习大纲太棒了，考研复习也能用。', 0),
(9, 5, '霍春阳的Vue书写得很好，可惜已经卖出了。', 0);

-- 教材举报数据
INSERT INTO textbook_report (textbook_id, reporter_id, reason, status) VALUES
(2, 8, '图片与实际教材不符，可能是盗版', 'PENDING');

-- 学习笔记数据
INSERT INTO study_note (title, description, course_id, major_id, grade, author_id, note_type, is_free, price, view_count, download_count, favorite_count, avg_rating, rating_count, is_recommended, recommend_teacher_id, status) VALUES
('Java核心知识点思维导图', '涵盖Java基础语法、面向对象、集合框架、IO流、多线程、网络编程等核心知识点的思维导图笔记', 1, 1, '2024', 5, 'IMAGE', 1, 0, 256, 89, 34, 4.5, 18, 1, 2, 'PUBLISHED'),
('数据结构期末复习笔记', '严蔚敏数据结构教材的完整复习笔记，包含所有算法代码实现和复杂度分析', 2, 1, '2024', 7, 'DOCUMENT', 1, 0, 189, 67, 28, 4.3, 15, 1, 2, 'PUBLISHED'),
('数据库原理课堂笔记+期末真题', '王珊数据库教材的课堂笔记整理，附带近三年期末考试真题及解析', 3, 1, '2024', 6, 'MIXED', 0, 2.00, 342, 156, 52, 4.8, 32, 1, 2, 'PUBLISHED'),
('软件工程UML建模实例详解', '结合课程案例详细讲解UML各种图的画法和实际应用', 4, 1, '2024', 5, 'DOCUMENT', 1, 0, 98, 35, 12, 4.0, 8, 0, NULL, 'PUBLISHED'),
('Web前端Vue.js项目实战笔记', '从零搭建Vue项目的完整笔记，包含路由、状态管理、组件封装等', 5, 1, '2024', 6, 'DOCUMENT', 0, 3.00, 428, 198, 67, 4.7, 45, 1, 2, 'PUBLISHED'),
('操作系统核心概念速记卡', '操作系统重点概念、算法的速记卡片式笔记，适合考前快速复习', 6, 2, '2023', 7, 'IMAGE', 1, 0, 167, 78, 23, 4.2, 12, 0, NULL, 'PUBLISHED'),
('计算机网络协议栈详解笔记', '从物理层到应用层的完整协议分析笔记，配有大量图解', 7, 2, '2023', 8, 'MIXED', 1, 0, 215, 92, 31, 4.6, 22, 1, 3, 'PUBLISHED'),
('数字电路实验报告合集', '数字电路课程全部实验的详细报告，含电路图和仿真截图', 9, 4, '2023', 9, 'DOCUMENT', 0, 1.50, 134, 45, 15, 3.9, 10, 0, NULL, 'PUBLISHED'),
('管理学期末重点整理', '管理学原理课程期末考试重点知识整理，按章节分类', 11, 6, '2024', 10, 'DOCUMENT', 1, 0, 87, 32, 9, 4.1, 7, 0, NULL, 'PUBLISHED'),
('Python数据分析实战案例', 'Pandas/NumPy/Matplotlib实战案例代码笔记', 8, 3, '2024', 5, 'DOCUMENT', 1, 0, 198, 88, 29, 4.4, 19, 0, NULL, 'REVIEWING');

-- 笔记文件数据
INSERT INTO note_file (note_id, file_name, file_url, file_type, file_size, sort_order) VALUES
(1, 'Java核心知识思维导图.png', '/uploads/note/java_mindmap.png', 'png', 2048000, 0),
(1, 'Java集合框架.png', '/uploads/note/java_collection.png', 'png', 1536000, 1),
(2, '数据结构复习笔记.pdf', '/uploads/note/ds_review.pdf', 'pdf', 5120000, 0),
(3, '数据库课堂笔记.pdf', '/uploads/note/db_notes.pdf', 'pdf', 3840000, 0),
(3, '数据库期末真题.pdf', '/uploads/note/db_exam.pdf', 'pdf', 2560000, 1),
(4, 'UML建模详解.pdf', '/uploads/note/uml_guide.pdf', 'pdf', 4096000, 0),
(5, 'Vue项目实战.pdf', '/uploads/note/vue_project.pdf', 'pdf', 6144000, 0),
(6, '操作系统速记卡-进程.png', '/uploads/note/os_card1.png', 'png', 1024000, 0),
(6, '操作系统速记卡-内存.png', '/uploads/note/os_card2.png', 'png', 1024000, 1),
(7, '计算机网络笔记.pdf', '/uploads/note/network_notes.pdf', 'pdf', 4608000, 0),
(7, '网络协议图解.png', '/uploads/note/network_diagram.png', 'png', 2048000, 1),
(8, '数字电路实验报告.pdf', '/uploads/note/digital_lab.pdf', 'pdf', 8192000, 0),
(9, '管理学重点整理.pdf', '/uploads/note/mgmt_review.pdf', 'pdf', 2048000, 0),
(10, 'Python数据分析案例.pdf', '/uploads/note/python_cases.pdf', 'pdf', 5632000, 0);

-- 笔记收藏数据
INSERT INTO note_favorite (user_id, note_id) VALUES
(5, 2), (5, 3), (5, 7),
(6, 1), (6, 2), (6, 4),
(7, 3), (7, 5),
(8, 1), (8, 6), (8, 7),
(9, 8), (9, 3),
(10, 9);

-- 笔记评分数据
INSERT INTO note_rating (note_id, user_id, score, comment) VALUES
(1, 6, 5, '思维导图整理得非常清晰，一目了然！'),
(1, 8, 4, '内容不错，要是能加上代码示例就更好了'),
(2, 5, 5, '复习笔记很全面，考前必备'),
(3, 5, 5, '真题太有用了，强烈推荐！'),
(3, 7, 5, '笔记条理清晰，真题答案详细'),
(5, 5, 5, 'Vue学习最好的笔记，项目案例很实用'),
(5, 8, 4, '内容丰富，适合入门'),
(7, 5, 5, '图解非常直观，理解协议栈必读');

-- 教师推荐教材
INSERT INTO teacher_recommend (teacher_id, course_id, textbook_title, author, publisher, isbn, edition, reason) VALUES
(2, 1, 'Java核心技术 卷I', 'Cay S. Horstmann', '机械工业出版社', '9787111547426', '第12版', '经典Java入门教材，内容全面深入，适合作为主教材'),
(2, 2, '数据结构（C语言版）', '严蔚敏', '清华大学出版社', '9787302023685', '第2版', '国内数据结构教材经典之作，考研推荐'),
(2, 3, '数据库系统概论', '王珊', '高等教育出版社', '9787040406641', '第5版', '数据库领域权威教材'),
(2, 5, 'Vue.js设计与实现', '霍春阳', '人民邮电出版社', '9787115583864', '第1版', '深入理解Vue原理的必读书'),
(3, 6, '操作系统概念', 'Silberschatz', '机械工业出版社', '9787111671732', '第10版', '恐龙书，操作系统经典教材'),
(3, 7, '计算机网络（第8版）', '谢希仁', '电子工业出版社', '9787121411748', '第8版', '国内计算机网络最流行的教材'),
(4, 9, '数字电子技术基础', '阎石', '高等教育出版社', '9787040444933', '第6版', '电子信息专业必修教材');

-- 系统通知数据
INSERT INTO sys_notification (title, content, type, sender_id, receiver_id, is_read) VALUES
('欢迎使用校园教材共享平台', '亲爱的同学，欢迎使用校园二手教材流转与笔记共享平台！在这里你可以发布和查找二手教材，也可以分享和下载学习笔记。', 'SYSTEM', 1, NULL, 0),
('你的笔记被教师推荐啦！', '恭喜！你上传的"Java核心知识点思维导图"被张教授推荐为优质学习资源。', 'NOTE', 2, 5, 0),
('你的笔记被教师推荐啦！', '恭喜！你上传的"数据库原理课堂笔记+期末真题"被张教授推荐为优质学习资源。', 'NOTE', 2, 6, 0),
('新学期教材推荐', '张教授发布了2024学年Java程序设计课程推荐教材信息，请前往查看。', 'TEACHER', 2, NULL, 0),
('教材交易提醒', '有同学对你发布的"Java核心技术 卷I"表示了兴趣，请注意查看评论。', 'TEXTBOOK', NULL, 7, 0),
('笔记审核通过', '你上传的"Web前端Vue.js项目实战笔记"已通过审核，现已发布。', 'NOTE', 1, 6, 1),
('平台使用须知', '请各位同学遵守平台规范，发布真实的教材信息，共同维护良好的交易环境。', 'SYSTEM', 1, NULL, 0);

-- 用户反馈数据
INSERT INTO user_feedback (user_id, content, contact, status, reply) VALUES
(5, '建议增加教材比价功能，可以和二手书网站的价格对比', '13800000020', 'REPLIED', '感谢建议，已纳入后续版本规划！'),
(8, '笔记下载速度有点慢，能不能优化一下？', 'chenyq_wx', 'PENDING', NULL);
