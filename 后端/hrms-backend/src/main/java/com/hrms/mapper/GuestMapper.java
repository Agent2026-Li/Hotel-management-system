package com.hrms.mapper;

import com.hrms.dto.response.GuestBrief;
import com.hrms.entity.Guest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
 * 客史 Mapper，负责客人资料查询和更新。
 */
@Mapper
public interface GuestMapper {
    /** 根据客史编号查询客人。 */
    @Select("select * from guest where id = #{id}")
    Guest getById(String id);

    /** 根据在住房号查询当前住客。 */
    @Select("select name, phone, checkin, checkout from guest where room_number = #{roomNumber} order by checkin desc limit 1")
    GuestBrief findBriefByRoomNumber(String roomNumber);

    /** 新增客史记录。 */
    @Insert("""
            insert into guest (id, name, phone, id_card, room_number, checkin, checkout, total_orders, total_amount, member_level)
            values (#{id}, #{name}, #{phone}, #{idCard}, #{roomNumber}, #{checkin}, #{checkout}, #{totalOrders}, #{totalAmount}, #{memberLevel})
            """)
    int insert(Guest guest);

    /** 换房时同步客史房号。 */
    @Update("update guest set room_number = #{targetRoomNumber} where room_number = #{sourceRoomNumber}")
    int updateRoomNumber(@Param("sourceRoomNumber") String sourceRoomNumber, @Param("targetRoomNumber") String targetRoomNumber);

    /** 续住时同步客史预计退房日期。 */
    @Update("update guest set checkout = #{checkout} where room_number = #{roomNumber}")
    int updateCheckout(@Param("roomNumber") String roomNumber, @Param("checkout") LocalDate checkout);

    /** 查询客史列表。 */
    List<Guest> selectGuestList(@Param("keyword") String keyword);
}
