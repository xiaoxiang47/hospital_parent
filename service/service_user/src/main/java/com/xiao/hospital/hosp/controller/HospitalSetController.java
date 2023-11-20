package com.xiao.hospital.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.hospital.common.result.Result;
import com.xiao.hospital.hosp.service.HospitalSetService;
import com.xiao.hospital.model.hosp.HospitalSet;
import com.xiao.hospital.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//医院设置接口
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;
    //查询所有医院设置
    @ApiOperation(value = "获取所有的医院设置信息")
    @GetMapping("findAll")
    public Result findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "逻辑删除指定id的医院设置信息")
    @DeleteMapping("{id}")
    public Result removeById(@PathVariable Long id){
        boolean id1 = hospitalSetService.removeById(id);
        if(id1){
            return Result.ok();
        }
        return Result.fail();
    }

    //条件查询加分页功能
    @ApiOperation(value = "条件查询医院设置信息带分页功能")
    @PostMapping("findPage/{current}/{limit}")
    public Result SelectPageHospSet(@PathVariable Long current,
                                    @PathVariable Long limit,
                                    @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        //创建分页对象
        Page<HospitalSet> page = new Page<>(current, limit);
        //构建查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();
        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }
        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospitalSet);
    }
}
