package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.MomentDTO;
import com.star.inf.dto.PageData;
import com.star.inf.service.MomentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:55 PM
 */
@Api(tags = "动态模块")
@RestController
public class MomentController {

    @Resource
    private MomentServiceImpl momentService;

    @ApiOperation(value = "查看首页动态")
    @GetMapping("/home/moments")
    public Result<List<String>> listHomeMoments() {
        return Result.success(momentService.listHomeMoments());
    }

    @ApiOperation(value = "查询前台动态")
    @GetMapping("/moments")
    public Result<PageData<MomentDTO>> listMoments() {
        return Result.success(momentService.listMoments());
    }

    @ApiOperation(value = "根据id查看动态")
    @ApiImplicitParam(name = "momentId", value = "动态id", required = true, dataType = "Integer")
    @GetMapping("/moments/{momentId}")
    public Result<MomentDTO> getMomentById(@PathVariable("momentId") Integer momentId) {
        return Result.success(momentService.getMomentById(momentId));
    }

    @ApiOperation(value = "点赞动态")
    @PostMapping("/moments/{momentId}/like")
    public Result<?> saveMomentLike(@PathVariable("momentId") Integer momentId) {
        momentService.saveMomentLike(momentId);
        return Result.success();
    }

}
