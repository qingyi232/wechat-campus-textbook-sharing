package com.textbook.controller;

import com.textbook.entity.Textbook;
import com.textbook.entity.TextbookComment;
import com.textbook.entity.TextbookReport;
import com.textbook.service.TextbookService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/textbook")
public class TextbookController {

    @Autowired
    private TextbookService textbookService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword, @RequestParam(required = false) Long courseId,
                          @RequestParam(required = false) Long majorId, @RequestParam(required = false) String grade,
                          @RequestParam(required = false) String condition, @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String status,
                          @RequestParam(required = false) Long sellerId, @RequestParam(required = false) String orderBy) {
        return Result.success(textbookService.pageList(page, size, keyword, courseId, majorId, grade, condition, minPrice, maxPrice, status, sellerId, orderBy));
    }

    @GetMapping("/detail/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(textbookService.getDetail(id));
    }

    @PostMapping("/publish")
    public Result<?> publish(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Textbook t = new Textbook();
        t.setTitle((String) params.get("title"));
        t.setAuthor((String) params.get("author"));
        t.setPublisher((String) params.get("publisher"));
        t.setIsbn((String) params.get("isbn"));
        t.setEdition((String) params.get("edition"));
        if (params.get("originalPrice") != null) t.setOriginalPrice(new BigDecimal(params.get("originalPrice").toString()));
        t.setPrice(new BigDecimal(params.get("price").toString()));
        t.setBookCondition((String) params.get("bookCondition"));
        if (params.get("courseId") != null) t.setCourseId(Long.valueOf(params.get("courseId").toString()));
        if (params.get("majorId") != null) t.setMajorId(Long.valueOf(params.get("majorId").toString()));
        t.setGrade((String) params.get("grade"));
        t.setDescription((String) params.get("description"));
        t.setSellerId(userId);
        t.setContactType((String) params.get("contactType"));
        t.setContactInfo((String) params.get("contactInfo"));
        List<String> images = (List<String>) params.get("images");
        return textbookService.publish(t, images);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Textbook textbook) {
        textbookService.updateById(textbook);
        return Result.success("修改成功");
    }

    @PostMapping("/favorite/{id}")
    public Result<?> favorite(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return textbookService.toggleFavorite(userId, id);
    }

    @GetMapping("/comments/{id}")
    public Result<?> comments(@PathVariable Long id) {
        return Result.success(textbookService.getComments(id));
    }

    @PostMapping("/comment")
    public Result<?> comment(@RequestBody TextbookComment comment, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        return textbookService.addComment(comment);
    }

    @PostMapping("/report")
    public Result<?> report(@RequestBody TextbookReport report, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        report.setReporterId(userId);
        return textbookService.report(report);
    }

    @PutMapping("/status")
    public Result<?> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = (String) params.get("status");
        String reason = (String) params.get("reason");
        return textbookService.updateStatus(id, status, reason);
    }

    @GetMapping("/myFavorites")
    public Result<?> myFavorites(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(textbookService.getFavorites(userId));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        textbookService.removeById(id);
        return Result.success("删除成功");
    }
}
