package com.textbook.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.textbook.entity.Message;
import com.textbook.entity.Textbook;
import com.textbook.entity.User;
import com.textbook.mapper.MessageMapper;
import com.textbook.service.TextbookService;
import com.textbook.service.UserService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TextbookService textbookService;

    @PostMapping("/send")
    public Result<?> send(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long senderId = (Long) request.getAttribute("userId");
        Long receiverId = Long.valueOf(params.get("receiverId").toString());
        String content = (String) params.get("content");

        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        if (params.get("textbookId") != null) {
            msg.setTextbookId(Long.valueOf(params.get("textbookId").toString()));
        }
        msg.setIsRead(0);
        msg.setCreateTime(LocalDateTime.now());
        messageMapper.insert(msg);
        return Result.success("发送成功");
    }

    @GetMapping("/list/{targetId}")
    public Result<?> list(@PathVariable Long targetId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Message> messages = messageMapper.selectChatMessages(userId, targetId);

        messageMapper.update(null, new LambdaUpdateWrapper<Message>()
                .eq(Message::getSenderId, targetId)
                .eq(Message::getReceiverId, userId)
                .eq(Message::getIsRead, 0)
                .set(Message::getIsRead, 1));

        return Result.success(messages);
    }

    @GetMapping("/conversations")
    public Result<?> conversations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        List<Message> allMessages = messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getSenderId, userId)
                        .or()
                        .eq(Message::getReceiverId, userId)
                        .orderByDesc(Message::getCreateTime));

        Map<Long, Message> convMap = new LinkedHashMap<>();
        for (Message m : allMessages) {
            Long targetId = m.getSenderId().equals(userId) ? m.getReceiverId() : m.getSenderId();
            if (!convMap.containsKey(targetId)) {
                m.setLastMessage(m.getContent());
                m.setLastTime(m.getCreateTime());
                convMap.put(targetId, m);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Message> entry : convMap.entrySet()) {
            Long targetId = entry.getKey();
            Message lastMsg = entry.getValue();

            User targetUser = userService.getById(targetId);
            long unread = messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                    .eq(Message::getSenderId, targetId)
                    .eq(Message::getReceiverId, userId)
                    .eq(Message::getIsRead, 0));

            Map<String, Object> conv = new HashMap<>();
            conv.put("targetId", targetId);
            conv.put("targetName", targetUser != null ? targetUser.getRealName() : "未知用户");
            conv.put("targetAvatar", targetUser != null ? targetUser.getAvatar() : null);
            conv.put("lastMessage", lastMsg.getContent());
            conv.put("lastTime", lastMsg.getCreateTime());
            conv.put("unreadCount", unread);
            if (lastMsg.getTextbookId() != null) {
                Textbook tb = textbookService.getById(lastMsg.getTextbookId());
                conv.put("textbookTitle", tb != null ? tb.getTitle() : null);
            }
            result.add(conv);
        }

        return Result.success(result);
    }
}
