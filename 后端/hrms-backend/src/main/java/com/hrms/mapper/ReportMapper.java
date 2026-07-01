package com.hrms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 报表 Mapper，负责入住率和营收统计。
 */
@Mapper
public interface ReportMapper {
    /** 查询房间总数。 */
    @Select("select count(*) from room")
    int countRooms();

    /** 按房态统计房间数量。 */
    @Select("select count(*) from room where status = #{status}")
    int countRoomsByStatus(String status);

    /** 统计房费收入。 */
    @Select("select coalesce(sum(room_amount), 0) from bill")
    BigDecimal sumRoomRevenue();

    /** 统计服务收入。 */
    @Select("select coalesce(sum(service_amount), 0) from bill")
    BigDecimal sumServiceRevenue();

    /** 统计已收金额。 */
    @Select("select coalesce(sum(paid_amount), 0) from bill")
    BigDecimal sumPaidAmount();

    /** 统计账单数量。 */
    @Select("select count(*) from bill")
    int countBills();
}
