package com.imooc.activiti.activitidemo.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MyCustomMapper {
    @Select("SELECT *　From ACT_RU_TASK")
    public List<Map<String, Object>> findAll();
}
