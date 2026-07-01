package com.hrms.mapper;

import com.hrms.dto.response.RoomResponse;
import com.hrms.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 房间 Mapper，负责房间查询和房态更新。
 */
@Mapper
public interface RoomMapper {
    /** 根据房间号查询房间实体。 */
    @Select("select room_number, floor, type_id, status, remark from room where room_number = #{roomNumber}")
    Room getByRoomNumber(String roomNumber);

    /** 修改房间状态。 */
    @Update("update room set status = #{status} where room_number = #{roomNumber}")
    int updateStatus(@Param("roomNumber") String roomNumber, @Param("status") String status);

    /** 查询指定房型下第一间空闲房。 */
    @Select("""
            select room_number, floor, type_id, status, remark
            from room
            where type_id = #{typeId} and status = 'VC'
            order by room_number
            limit 1
            """)
    Room findFirstVacantByType(String typeId);

    /** 查询房间列表，关联房型和当前住客信息。 */
    List<RoomResponse> selectRoomList(@Param("status") String status, @Param("floor") Integer floor);

    /** 根据房间号查询房间响应对象。 */
    RoomResponse selectRoomResponseByNumber(String roomNumber);
}

