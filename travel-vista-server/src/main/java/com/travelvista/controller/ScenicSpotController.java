package com.travelvista.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.service.ScenicSpotService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/scenic")
public class ScenicSpotController {

    @Resource
    private ScenicSpotService scenicSpotService;

    @GetMapping("/page")
    public Result<IPage<ScenicSpot>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) BigDecimal maxRating) {
        IPage<ScenicSpot> pageResult = scenicSpotService.pageScenicSpot(page, pageSize, name, province, city, type, level, minRating, maxRating);
        return Result.success(pageResult);
    }

    @GetMapping("/detail/{id}")
    public Result<ScenicSpot> getDetail(@PathVariable Long id) {
        ScenicSpot scenicSpot = scenicSpotService.getDetailById(id);
        if (scenicSpot == null) {
            return Result.fail("景点不存在");
        }
        return Result.success(scenicSpot);
    }

    @GetMapping("/hot")
    public Result<List<ScenicSpot>> getHotList(@RequestParam(defaultValue = "10") Integer limit) {
        List<ScenicSpot> list = scenicSpotService.getHotScenicSpot(limit);
        return Result.success(list);
    }

    @GetMapping("/recommend")
    public Result<List<ScenicSpot>> getRecommendList(@RequestParam(defaultValue = "10") Integer limit) {
        List<ScenicSpot> list = scenicSpotService.getRecommendScenicSpot(limit);
        return Result.success(list);
    }
}
