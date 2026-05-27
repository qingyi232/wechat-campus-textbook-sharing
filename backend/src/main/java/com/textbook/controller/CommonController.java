package com.textbook.controller;

import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Value("${file.upload-path}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file,
                            @RequestParam(defaultValue = "general") String type) throws IOException {
        if (file == null || file.isEmpty() || file.getSize() == 0) return Result.error("文件内容为空，请选择有内容的文件");

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;

        File dir = new File(uploadPath + type + "/").getAbsoluteFile();
        if (!dir.exists()) dir.mkdirs();

        File destFile = new File(dir, fileName);
        file.transferTo(destFile);
        String url = "/uploads/" + type + "/" + fileName;
        return Result.success("上传成功", url);
    }
}
