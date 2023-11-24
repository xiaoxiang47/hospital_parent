package com.xiao.hospital.cmn.controller;

import com.xiao.hospital.cmn.service.DictService;
import com.xiao.hospital.common.result.Result;
import com.xiao.hospital.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典接口")
@RequestMapping("/admin/cmn/dict")
@RestController
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    //根据数据的id查询出所有子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findAllChildren/{id}")
    public Result findAllChildren(@PathVariable Long id){
        List<Dict> list = dictService.findChildrenData(id);
        return Result.ok(list);
    }

    //导出数据字典接口
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }
}
