package com.travelvista.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenic")
public class ScenicSpotController {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @GetMapping("/list")
    public Result<IPage<ScenicSpot>> getScenicList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sort) {
        IPage<ScenicSpot> page = scenicSpotService.getScenicList(pageNum, pageSize, keyword, province, city, type, sort);
        return Result.success(page);
    }

    @GetMapping("/detail/{id}")
    public Result<ScenicSpot> getScenicDetail(@PathVariable Long id) {
        scenicSpotService.addViewCount(id);
        ScenicSpot scenicSpot = scenicSpotService.getScenicDetail(id);
        return Result.success(scenicSpot);
    }

    @GetMapping("/hot")
    public Result<List<ScenicSpot>> getHotScenic(@RequestParam(defaultValue = "6") Integer limit) {
        List<ScenicSpot> list = scenicSpotService.getHotScenic(limit);
        return Result.success(list);
    }

    @GetMapping("/recommend")
    public Result<List<ScenicSpot>> getRecommendScenic(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "6") Integer limit) {
        List<ScenicSpot> list = scenicSpotService.getRecommendScenic(userId, limit);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<Void> addScenic(@RequestBody ScenicSpot scenicSpot) {
        scenicSpotService.save(scenicSpot);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateScenic(@RequestBody ScenicSpot scenicSpot) {
        scenicSpotService.updateById(scenicSpot);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteScenic(@PathVariable Long id) {
        scenicSpotService.removeById(id);
        return Result.success();
    }
}
