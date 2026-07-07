package com.hrms.mapper;

import com.hrms.dto.response.ReservationResponse;
import com.hrms.entity.Reservation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 预订 Mapper，负责预订查询和状态更新。
 */
@Mapper
public interface ReservationMapper {
    /** 根据预订编号查询预订实体。 */
    @Select("select * from reservation where id = #{id}")
    Reservation getById(String id);

    /** 新增预订。 */
    @Insert("""
            insert into reservation (id, name, phone, room_type, room_number, checkin, checkout, nights, amount, status, remark)
            values (#{id}, #{name}, #{phone}, #{roomType}, #{roomNumber}, #{checkin}, #{checkout}, #{nights}, #{amount}, #{status}, #{remark})
            """)
    int insert(Reservation reservation);

    /** 更新预订资料。 */
    @Update("""
            update reservation
            set name = #{name},
                phone = #{phone},
                room_type = #{roomType},
                room_number = #{roomNumber},
                checkin = #{checkin},
                checkout = #{checkout},
                nights = #{nights},
                amount = #{amount},
                remark = #{remark}
            where id = #{id}
            """)
    int update(Reservation reservation);

    /** 修改预订状态。 */
    @Update("update reservation set status = #{status} where id = #{id}")
    int updateStatus(@Param("id") String id, @Param("status") String status);

    /** 查询预订列表。 */
    List<ReservationResponse> selectReservationList(@Param("status") String status);
}
