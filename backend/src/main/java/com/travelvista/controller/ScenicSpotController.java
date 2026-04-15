package com.travelvista.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.dto.ScenicQueryDTO;
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
    public Result<IPage<ScenicSpot>> list(ScenicQueryDTO dto) {
        IPage<ScenicSpot> page = scenicSpotService.searchScenic(dto);
        return Result.success(page);
    }

    @GetMapping("/detail/{id}")
    public Result<ScenicSpot> detail(@PathVariable Long id) {
        scenicSpotService.incrementViewCount(id);
        ScenicSpot scenic = scenicSpotService.getScenicDetail(id);
        return Result.success(scenic);
    }

    @GetMapping("/hot")
    public Result<List<ScenicSpot>> getHotScenic(@RequestParam(defaultValue = "6") Integer limit) {
        List<ScenicSpot> list = scenicSpotService.getHotScenic(limit);
        return Result.success(list);
    }

    @GetMapping("/recommended")
    public Result<List<ScenicSpot>> getRecommendedScenic(@RequestParam(defaultValue = "6") Integer limit) {
        List<ScenicSpot> list = scenicSpotService.getRecommendedScenic(limit);
        return Result.success(list);
    }
}
