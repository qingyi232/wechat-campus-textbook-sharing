package com.textbook.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.*;
import com.textbook.mapper.*;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyNoteService extends ServiceImpl<StudyNoteMapper, StudyNote> {

    @Autowired
    private NoteFileMapper fileMapper;
    @Autowired
    private NoteFavoriteMapper favoriteMapper;
    @Autowired
    private NoteRatingMapper ratingMapper;
    @Autowired
    private NoteDownloadMapper downloadMapper;
    @Autowired(required = false)
    private NotificationService notificationService;

    public IPage<StudyNote> pageList(int page, int size, String keyword, Long courseId, Long majorId,
                                      String grade, String noteType, Integer isFree, String status,
                                      Long authorId, Integer recommended, String orderBy) {
        return baseMapper.selectNotePage(new Page<>(page, size), keyword, courseId, majorId, grade, noteType, isFree, status, authorId, recommended, orderBy);
    }

    public StudyNote getDetail(Long id) {
        StudyNote note = baseMapper.selectDetail(id);
        if (note != null) {
            List<NoteFile> files = fileMapper.selectList(new LambdaQueryWrapper<NoteFile>()
                    .eq(NoteFile::getNoteId, id).orderByAsc(NoteFile::getSortOrder));
            note.setFiles(files);
            update(new LambdaUpdateWrapper<StudyNote>().eq(StudyNote::getId, id).setSql("view_count = view_count + 1"));
        }
        return note;
    }

    @Transactional
    public Result<?> upload(StudyNote note, List<NoteFile> files) {
        note.setStatus("REVIEWING");
        note.setViewCount(0);
        note.setDownloadCount(0);
        note.setFavoriteCount(0);
        note.setAvgRating(BigDecimal.ZERO);
        note.setRatingCount(0);
        note.setIsRecommended(0);
        save(note);
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                NoteFile f = files.get(i);
                f.setNoteId(note.getId());
                f.setSortOrder(i);
                fileMapper.insert(f);
            }
        }
        return Result.success("笔记上传成功，等待审核");
    }

    public Result<?> toggleFavorite(Long userId, Long noteId) {
        NoteFavorite existing = favoriteMapper.selectOne(new LambdaQueryWrapper<NoteFavorite>()
                .eq(NoteFavorite::getUserId, userId).eq(NoteFavorite::getNoteId, noteId));
        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
            update(new LambdaUpdateWrapper<StudyNote>().eq(StudyNote::getId, noteId).setSql("favorite_count = favorite_count - 1"));
            return Result.success("已取消收藏");
        } else {
            NoteFavorite fav = new NoteFavorite();
            fav.setUserId(userId);
            fav.setNoteId(noteId);
            favoriteMapper.insert(fav);
            update(new LambdaUpdateWrapper<StudyNote>().eq(StudyNote::getId, noteId).setSql("favorite_count = favorite_count + 1"));
            return Result.success("收藏成功");
        }
    }

    public Result<?> rate(NoteRating rating) {
        NoteRating existing = ratingMapper.selectOne(new LambdaQueryWrapper<NoteRating>()
                .eq(NoteRating::getUserId, rating.getUserId()).eq(NoteRating::getNoteId, rating.getNoteId()));
        if (existing != null) return Result.error("你已经评价过了");
        ratingMapper.insert(rating);
        updateAvgRating(rating.getNoteId());
        return Result.success("评价成功");
    }

    private void updateAvgRating(Long noteId) {
        List<NoteRating> ratings = ratingMapper.selectList(new LambdaQueryWrapper<NoteRating>().eq(NoteRating::getNoteId, noteId));
        if (!ratings.isEmpty()) {
            double avg = ratings.stream().mapToInt(NoteRating::getScore).average().orElse(0);
            update(new LambdaUpdateWrapper<StudyNote>().eq(StudyNote::getId, noteId)
                    .set(StudyNote::getAvgRating, BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP))
                    .set(StudyNote::getRatingCount, ratings.size()));
        }
    }

    public Result<?> recordDownload(Long userId, Long noteId) {
        NoteDownload dl = new NoteDownload();
        dl.setUserId(userId);
        dl.setNoteId(noteId);
        downloadMapper.insert(dl);
        update(new LambdaUpdateWrapper<StudyNote>().eq(StudyNote::getId, noteId).setSql("download_count = download_count + 1"));
        return Result.success("下载记录已保存");
    }

    public List<NoteRating> getRatings(Long noteId) {
        return ratingMapper.selectByNoteId(noteId);
    }

    public Result<?> updateStatus(Long id, String status, String reason) {
        StudyNote note = getById(id);
        if (note == null) return Result.error("笔记不存在");
        note.setStatus(status);
        if (reason != null) note.setRejectReason(reason);
        updateById(note);
        return Result.success("操作成功");
    }

    public Result<?> recommend(Long noteId, Long teacherId) {
        StudyNote note = getById(noteId);
        if (note == null) return Result.error("笔记不存在");
        note.setIsRecommended(1);
        note.setRecommendTeacherId(teacherId);
        note.setStatus("PUBLISHED");
        note.setViewCount(note.getViewCount() + 500);
        updateById(note);
        if (notificationService != null) {
            notificationService.sendNotice(
                    "你的笔记被教师推荐啦！",
                    "恭喜！你上传的\"" + note.getTitle() + "\"被教师推荐为优质学习资源。",
                    "NOTE", teacherId, note.getAuthorId());
        }
        return Result.success("推荐成功");
    }

    public Result<?> rejectNote(Long noteId, String reason, Long teacherId) {
        StudyNote note = getById(noteId);
        if (note == null) return Result.error("笔记不存在");
        note.setStatus("REJECTED");
        note.setRejectReason(reason);
        updateById(note);
        if (notificationService != null) {
            notificationService.sendNotice(
                    "笔记审核未通过",
                    "你上传的\"" + note.getTitle() + "\"未通过审核，原因：" + reason,
                    "NOTE", teacherId, note.getAuthorId());
        }
        return Result.success("已驳回");
    }

    public boolean isFavorited(Long userId, Long noteId) {
        return favoriteMapper.selectCount(new LambdaQueryWrapper<NoteFavorite>()
                .eq(NoteFavorite::getUserId, userId).eq(NoteFavorite::getNoteId, noteId)) > 0;
    }

    public List<StudyNote> getFavorites(Long userId) {
        List<NoteFavorite> favs = favoriteMapper.selectList(new LambdaQueryWrapper<NoteFavorite>().eq(NoteFavorite::getUserId, userId));
        if (favs.isEmpty()) return List.of();
        return listByIds(favs.stream().map(NoteFavorite::getNoteId).collect(Collectors.toList()));
    }
}
