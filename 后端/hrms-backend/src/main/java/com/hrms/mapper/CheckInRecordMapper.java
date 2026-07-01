package com.hrms.mapper;

import com.hrms.entity.CheckInRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入住记录 Mapper，负责入住、退房、换房和续住记录更新。
 */
@Mapper
public interface CheckInRecordMapper {
    /** 根据入住编号查询入住记录。 */
    @Select("select * from check_in_record where id = #{id}")
    CheckInRecord getById(String id);

    /** 根据房间号查询在住记录。 */
    @Select("select * from check_in_record where room_number = #{roomNumber} and status = 'living' order by checkin_time desc limit 1")
    CheckInRecord findLivingByRoomNumber(String roomNumber);

    /** 根据入住编号查询在住记录。 */
    @Select("select * from check_in_record where id = #{id} and status = 'living'")
    CheckInRecord findLivingById(String id);

    /** 新增入住记录。 */
    @Insert("""
            insert into check_in_record (id, reservation_id, guest_id, room_number, checkin_time, expected_checkout, status, bill_id)
            values (#{id}, #{reservationId}, #{guestId}, #{roomNumber}, #{checkinTime}, #{expectedCheckout}, #{status}, #{billId})
            """)
    int insert(CheckInRecord record);

    /** 退房时更新入住记录状态。 */
    @Update("update check_in_record set status = 'checked_out', checkout_time = #{checkoutTime} where id = #{id}")
    int checkout(@Param("id") String id, @Param("checkoutTime") LocalDateTime checkoutTime);

    /** 换房时迁移入住记录房号。 */
    @Update("update check_in_record set room_number = #{targetRoomNumber} where room_number = #{sourceRoomNumber} and status = 'living'")
    int updateRoomNumber(@Param("sourceRoomNumber") String sourceRoomNumber, @Param("targetRoomNumber") String targetRoomNumber);

    /** 续住时更新预计退房日期。 */
    @Update("update check_in_record set expected_checkout = #{checkout} where room_number = #{roomNumber} and status = 'living'")
    int updateExpectedCheckout(@Param("roomNumber") String roomNumber, @Param("checkout") LocalDate checkout);
}

