package com.textbook.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Notification;
import com.textbook.entity.User;
import com.textbook.mapper.NotificationMapper;
import com.textbook.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService extends ServiceImpl<NotificationMapper, Notification> {

    @Autowired
    private UserMapper userMapper;

    public IPage<Notification> pageList(int page, int size, Long userId) {
        return page(new Page<>(page, size), new LambdaQueryWrapper<Notification>()
                .and(w -> w.eq(Notification::getReceiverId, userId).or().isNull(Notification::getReceiverId))
                .orderByDesc(Notification::getCreateTime));
    }

    public long unreadCount(Long userId) {
        return count(new LambdaQueryWrapper<Notification>()
                .and(w -> w.eq(Notification::getReceiverId, userId).or().isNull(Notification::getReceiverId))
                .eq(Notification::getIsRead, 0));
    }

    public void markRead(Long id) {
        Notification n = getById(id);
        if (n != null) { n.setIsRead(1); updateById(n); }
    }

    public void sendNotice(String title, String content, String type, Long senderId, Long receiverId) {
        Notification n = new Notification();
        n.setTitle(title);
        n.setContent(content);
        n.setType(type);
        n.setSenderId(senderId);
        n.setReceiverId(receiverId);
        n.setIsRead(0);
        save(n);
    }

    public void broadcastByCourse(String title, String content, Long teacherId, Long courseId) {
        if (courseId != null) {
            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<User>()
                    .eq(User::getRole, "STUDENT")
                    .eq(User::getStatus, 1);
            List<User> students = userMapper.selectList(qw);
            for (User s : students) {
                sendNotice(title, content, "TEACHER", teacherId, s.getId());
            }
        } else {
            sendNotice(title, content, "TEACHER", teacherId, null);
        }
    }
}
