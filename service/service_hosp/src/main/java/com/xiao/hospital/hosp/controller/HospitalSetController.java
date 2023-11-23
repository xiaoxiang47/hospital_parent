package com.xiao.hospital.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiao.hospital.common.result.Result;
import com.xiao.hospital.common.utils.MD5;
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
import java.util.Random;


//医院设置接口
@CrossOrigin
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
        if(!id1){
            return Result.fail();
        }
        return Result.ok();
    }

    //条件查询加分页功能
    @ApiOperation(value = "条件查询医院设置信息带分页功能")
    @PostMapping("findHospSetPage/{current}/{limit}")
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

    //添加医院设置
    @ApiOperation(value = "添加医院设置接口")
    @PostMapping("saveHospitalSet")
    public Result saveHospSet(@RequestBody HospitalSet hospitalSet){
        //设置状态 1：可以使用  0：不可以使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        boolean save = hospitalSetService.save(hospitalSet);
        if(!save){
            return Result.fail();
        }
        return Result.ok();
    }

    @ApiOperation(value = "根据医院设置id获取医院设置")
    @GetMapping("getHospitalSet/{id}")
    public Result getHospitalSetById(@PathVariable("id") Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    @ApiOperation(value = "修改医院设置信息")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean b = hospitalSetService.updateById(hospitalSet);
        if(!b){
            return Result.fail();
        }
        return Result.ok();
    }

    @ApiOperation(value = "批量删除医院设置信息")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospital(@RequestBody List<Long> idList){
        boolean b = hospitalSetService.removeByIds(idList);
        if(!b){
            return Result.fail();
        }
        return Result.ok();
    }

    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("lock/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean b = hospitalSetService.updateById(hospitalSet);
        if(!b){
            return Result.fail();
        }
        return Result.ok();
    }

    @ApiOperation(value = "发送签名秘钥")
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable("id") Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String hoscode = hospitalSet.getHoscode();
        String signKey = hospitalSet.getSignKey();
        //TODO 发送短信

        return Result.ok();
    }

}
