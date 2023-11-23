package com.xiao.hospital.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiao.hospital.model.cmn.Dict;
import com.xiao.hospital.model.hosp.HospitalSet;

import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildrenData(Long id);
}
