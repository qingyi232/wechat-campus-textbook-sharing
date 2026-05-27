package com.textbook.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.TeacherRecommend;
import com.textbook.mapper.TeacherRecommendMapper;
import com.textbook.util.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService extends ServiceImpl<TeacherRecommendMapper, TeacherRecommend> {

    public List<TeacherRecommend> getByCourseId(Long courseId) {
        return list(new LambdaQueryWrapper<TeacherRecommend>().eq(TeacherRecommend::getCourseId, courseId));
    }

    public List<TeacherRecommend> getByTeacherId(Long teacherId) {
        return list(new LambdaQueryWrapper<TeacherRecommend>().eq(TeacherRecommend::getTeacherId, teacherId));
    }

    public Result<?> addRecommend(TeacherRecommend recommend) {
        save(recommend);
        return Result.success("推荐教材已发布");
    }

    public Result<?> deleteRecommend(Long id, Long teacherId) {
        TeacherRecommend r = getById(id);
        if (r == null) return Result.error("推荐不存在");
        if (!r.getTeacherId().equals(teacherId)) return Result.error("无权删除");
        removeById(id);
        return Result.success("删除成功");
    }
}
