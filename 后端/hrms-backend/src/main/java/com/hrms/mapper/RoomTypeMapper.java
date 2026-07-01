package com.hrms.mapper;

import com.hrms.entity.RoomType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 房型 Mapper，负责房型基础信息查询。
 */
@Mapper
public interface RoomTypeMapper {
    /** 根据房型编号查询房型。 */
    @Select("select id, name, price, description from room_type where id = #{id}")
    RoomType getById(String id);
}

