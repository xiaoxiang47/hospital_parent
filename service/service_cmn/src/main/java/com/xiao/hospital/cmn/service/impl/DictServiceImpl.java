package com.xiao.hospital.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.hospital.cmn.mapper.DictMapper;
import com.xiao.hospital.cmn.service.DictService;
import com.xiao.hospital.model.cmn.Dict;
import org.springframework.stereotype.Service;

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

    //判断id下面是否有子节点
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer i = baseMapper.selectCount(wrapper);
        return i > 0;
    }
}
