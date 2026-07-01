package com.hrms.mapper;

import com.hrms.entity.Bill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账单 Mapper，负责账单查询、金额更新和结算。
 */
@Mapper
public interface BillMapper {
    /** 根据账单编号查询账单。 */
    @Select("select * from bill where id = #{id}")
    Bill getById(String id);

    /** 新增账单。 */
    @Insert("""
            insert into bill (id, room_number, guest_name, status, room_amount, service_amount, total_amount, paid_amount)
            values (#{id}, #{roomNumber}, #{guestName}, #{status}, #{roomAmount}, #{serviceAmount}, #{totalAmount}, #{paidAmount})
            """)
    int insert(Bill bill);

    /** 更新账单金额和状态。 */
    @Update("""
            update bill
            set room_amount = #{roomAmount},
                service_amount = #{serviceAmount},
                total_amount = #{totalAmount},
                paid_amount = #{paidAmount},
                status = #{status}
            where id = #{id}
            """)
    int updateAmounts(Bill bill);

    /** 退房结算时更新支付金额。 */
    @Update("update bill set paid_amount = #{paidAmount}, status = #{status} where id = #{id}")
    int updatePayment(@Param("id") String id, @Param("paidAmount") BigDecimal paidAmount, @Param("status") String status);

    /** 换房时同步账单房号。 */
    @Update("update bill set room_number = #{targetRoomNumber} where room_number = #{sourceRoomNumber} and status <> 'paid'")
    int updateRoomNumber(@Param("sourceRoomNumber") String sourceRoomNumber, @Param("targetRoomNumber") String targetRoomNumber);

    /** 查询账单列表。 */
    List<Bill> selectBillList(@Param("status") String status, @Param("roomNumber") String roomNumber);
}

