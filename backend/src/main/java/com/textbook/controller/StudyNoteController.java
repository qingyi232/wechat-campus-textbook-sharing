package com.textbook.controller;

import com.textbook.entity.NoteFile;
import com.textbook.entity.NoteRating;
import com.textbook.entity.StudyNote;
import com.textbook.service.StudyNoteService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/note")
public class StudyNoteController {

    @Autowired
    private StudyNoteService noteService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword, @RequestParam(required = false) Long courseId,
                          @RequestParam(required = false) Long majorId, @RequestParam(required = false) String grade,
                          @RequestParam(required = false) String noteType, @RequestParam(required = false) Integer isFree,
                          @RequestParam(required = false) String status, @RequestParam(required = false) Long authorId,
                          @RequestParam(required = false) Integer recommended, @RequestParam(required = false) String orderBy) {
        return Result.success(noteService.pageList(page, size, keyword, courseId, majorId, grade, noteType, isFree, status, authorId, recommended, orderBy));
    }

    @GetMapping("/detail/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(noteService.getDetail(id));
    }

    @PostMapping("/upload")
    public Result<?> upload(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        StudyNote note = new StudyNote();
        note.setTitle((String) params.get("title"));
        note.setDescription((String) params.get("description"));
        if (params.get("courseId") != null) note.setCourseId(Long.valueOf(params.get("courseId").toString()));
        if (params.get("majorId") != null) note.setMajorId(Long.valueOf(params.get("majorId").toString()));
        note.setGrade((String) params.get("grade"));
        note.setAuthorId(userId);
        note.setNoteType((String) params.get("noteType"));
        note.setIsFree(params.get("isFree") != null ? Integer.valueOf(params.get("isFree").toString()) : 1);
        if (params.get("price") != null && !params.get("price").toString().isEmpty()) {
            note.setPrice(new BigDecimal(params.get("price").toString()));
        } else {
            note.setPrice(BigDecimal.ZERO);
        }
        note.setCoverUrl((String) params.get("coverUrl"));

        List<Map<String, Object>> fileList = (List<Map<String, Object>>) params.get("files");
        List<NoteFile> files = new ArrayList<>();
        if (fileList != null) {
            for (Map<String, Object> f : fileList) {
                NoteFile nf = new NoteFile();
                nf.setFileName((String) f.get("fileName"));
                nf.setFileUrl((String) f.get("fileUrl"));
                nf.setFileType((String) f.get("fileType"));
                if (f.get("fileSize") != null) nf.setFileSize(Long.valueOf(f.get("fileSize").toString()));
                files.add(nf);
            }
        }
        return noteService.upload(note, files);
    }

    @PostMapping("/favorite/{id}")
    public Result<?> favorite(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return noteService.toggleFavorite(userId, id);
    }

    @PostMapping("/rate")
    public Result<?> rate(@RequestBody NoteRating rating, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        rating.setUserId(userId);
        return noteService.rate(rating);
    }

    @GetMapping("/ratings/{id}")
    public Result<?> ratings(@PathVariable Long id) {
        return Result.success(noteService.getRatings(id));
    }

    @PostMapping("/download/{id}")
    public Result<?> download(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return noteService.recordDownload(userId, id);
    }

    @PutMapping("/status")
    public Result<?> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = (String) params.get("status");
        String reason = (String) params.get("reason");
        return noteService.updateStatus(id, status, reason);
    }

    @PostMapping("/recommend/{id}")
    public Result<?> recommend(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return noteService.recommend(id, userId);
    }

    @GetMapping("/myFavorites")
    public Result<?> myFavorites(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(noteService.getFavorites(userId));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        noteService.removeById(id);
        return Result.success("删除成功");
    }
}
