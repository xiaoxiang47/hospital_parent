package com.xiao.hospital.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.hospital.cmn.mapper.DictMapper;
import com.xiao.hospital.cmn.service.DictService;
import com.xiao.hospital.model.cmn.Dict;
import com.xiao.hospital.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Override
    public List<Dict> findChildrenData(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(dictQueryWrapper);

        dicts.stream().forEach(item->{
            Long id1 = item.getId();
            boolean children = this.isChildren(id1);
            item.setHasChildren(children);
        });
        return dicts;
    }

    //导出数据字典
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;fileName="+fileName+".xlsx");

        //查询数据库
        List<Dict> dicts = baseMapper.selectList(null);

        //将Dict对象转换成DictEeVo
        ArrayList<DictEeVo> dictEeVos = new ArrayList<>();
        for(Dict dict : dicts){
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVos.add(dictEeVo);
        }

        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictEeVos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer i = baseMapper.selectCount(wrapper);
        return i > 0;
    }
}
