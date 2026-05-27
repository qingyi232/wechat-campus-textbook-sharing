package com.textbook.controller;

import com.textbook.entity.User;
import com.textbook.mapper.MajorMapper;
import com.textbook.service.UserService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MajorMapper majorMapper;

    @GetMapping("/info")
    public Result<?> getInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getBaseMapper().selectUserWithMajor(userId));
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword, @RequestParam(required = false) String role,
                          @RequestParam(required = false) Long majorId, @RequestParam(required = false) Integer status) {
        return Result.success(userService.pageList(page, size, keyword, role, majorId, status));
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success("修改成功");
    }

    @PutMapping("/audit/{id}")
    public Result<?> audit(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        return userService.audit(id, params.get("status"));
    }

    @PutMapping("/resetPwd/{id}")
    public Result<?> resetPassword(@PathVariable Long id) {
        return userService.resetPassword(id);
    }

    @PutMapping("/changePwd")
    public Result<?> changePwd(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.changePassword(userId, params.get("oldPassword"), params.get("newPassword"));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/majors")
    public Result<?> majors() {
        return Result.success(majorMapper.selectList(null));
    }
}
