package com.textbook.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.*;
import com.textbook.mapper.*;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextbookService extends ServiceImpl<TextbookMapper, Textbook> {

    @Autowired
    private TextbookImageMapper imageMapper;
    @Autowired
    private TextbookFavoriteMapper favoriteMapper;
    @Autowired
    private TextbookCommentMapper commentMapper;
    @Autowired
    private TextbookReportMapper reportMapper;

    public IPage<Textbook> pageList(int page, int size, String keyword, Long courseId, Long majorId,
                                     String grade, String condition, Double minPrice, Double maxPrice,
                                     String status, Long sellerId, String orderBy) {
        IPage<Textbook> result = baseMapper.selectTextbookPage(new Page<>(page, size), keyword, courseId, majorId, grade, condition, minPrice, maxPrice, status, sellerId, orderBy);
        result.getRecords().forEach(t -> {
            List<TextbookImage> imgs = imageMapper.selectList(new LambdaQueryWrapper<TextbookImage>().eq(TextbookImage::getTextbookId, t.getId()).orderByAsc(TextbookImage::getSortOrder));
            t.setImages(imgs.stream().map(TextbookImage::getImageUrl).collect(Collectors.toList()));
        });
        return result;
    }

    public Textbook getDetail(Long id) {
        Textbook t = baseMapper.selectDetail(id);
        if (t != null) {
            List<TextbookImage> imgs = imageMapper.selectList(new LambdaQueryWrapper<TextbookImage>().eq(TextbookImage::getTextbookId, id).orderByAsc(TextbookImage::getSortOrder));
            t.setImages(imgs.stream().map(TextbookImage::getImageUrl).collect(Collectors.toList()));
            baseMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Textbook>()
                    .eq(Textbook::getId, id).setSql("view_count = view_count + 1"));
        }
        return t;
    }

    @Transactional
    public Result<?> publish(Textbook textbook, List<String> imageUrls) {
        textbook.setStatus("ON_SALE");
        textbook.setViewCount(0);
        textbook.setFavoriteCount(0);
        save(textbook);
        if (imageUrls != null) {
            for (int i = 0; i < imageUrls.size(); i++) {
                TextbookImage img = new TextbookImage();
                img.setTextbookId(textbook.getId());
                img.setImageUrl(imageUrls.get(i));
                img.setSortOrder(i);
                imageMapper.insert(img);
            }
        }
        return Result.success("教材发布成功");
    }

    public Result<?> toggleFavorite(Long userId, Long textbookId) {
        TextbookFavorite existing = favoriteMapper.selectOne(new LambdaQueryWrapper<TextbookFavorite>()
                .eq(TextbookFavorite::getUserId, userId).eq(TextbookFavorite::getTextbookId, textbookId));
        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
            update(new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Textbook>()
                    .eq(Textbook::getId, textbookId).setSql("favorite_count = favorite_count - 1"));
            return Result.success("已取消收藏");
        } else {
            TextbookFavorite fav = new TextbookFavorite();
            fav.setUserId(userId);
            fav.setTextbookId(textbookId);
            favoriteMapper.insert(fav);
            update(new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Textbook>()
                    .eq(Textbook::getId, textbookId).setSql("favorite_count = favorite_count + 1"));
            return Result.success("收藏成功");
        }
    }

    public List<TextbookComment> getComments(Long textbookId) {
        return commentMapper.selectByTextbookId(textbookId);
    }

    public Result<?> addComment(TextbookComment comment) {
        comment.setStatus(1);
        commentMapper.insert(comment);
        return Result.success("评论成功");
    }

    public Result<?> report(TextbookReport report) {
        report.setStatus("PENDING");
        reportMapper.insert(report);
        return Result.success("举报已提交，管理员将尽快处理");
    }

    public Result<?> updateStatus(Long id, String status, String reason) {
        Textbook t = getById(id);
        if (t == null) return Result.error("教材不存在");
        t.setStatus(status);
        if (reason != null) t.setRejectReason(reason);
        updateById(t);
        return Result.success("操作成功");
    }

    public boolean isFavorited(Long userId, Long textbookId) {
        return favoriteMapper.selectCount(new LambdaQueryWrapper<TextbookFavorite>()
                .eq(TextbookFavorite::getUserId, userId).eq(TextbookFavorite::getTextbookId, textbookId)) > 0;
    }

    public List<Textbook> getFavorites(Long userId) {
        List<TextbookFavorite> favs = favoriteMapper.selectList(new LambdaQueryWrapper<TextbookFavorite>().eq(TextbookFavorite::getUserId, userId));
        if (favs.isEmpty()) return List.of();
        List<Long> ids = favs.stream().map(TextbookFavorite::getTextbookId).collect(Collectors.toList());
        return listByIds(ids);
    }
}
