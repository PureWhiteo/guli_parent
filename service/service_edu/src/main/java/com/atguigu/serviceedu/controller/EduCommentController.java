package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.UcenterMember;
import com.atguigu.serviceedu.client.UcenterClient;
import com.atguigu.serviceedu.entity.EduComment;
import com.atguigu.serviceedu.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-06-11
 */
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class EduCommentController {
    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduCommentService commentService;
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R addComment(@RequestBody EduComment eduComment, HttpServletRequest request){
        //根据token获取用户信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            return R.error().message("请登录");
        }
        eduComment.setMemberId(memberId);
        UcenterMember member = ucenterClient.getUcenterById(memberId);
        eduComment.setNickname(member.getNickname());
        eduComment.setAvatar(member.getAvatar());
//        BeanUtils.copyProperties(member,eduComment);
        commentService.save(eduComment);
        return R.ok();
    }

    //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("{page}/{limit}")
    public R index(@PathVariable Long page,
                   @PathVariable Long limit,
                   String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);

        commentService.page(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }
}

