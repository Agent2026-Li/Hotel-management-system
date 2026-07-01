package com.hrms.mapper;

import com.hrms.entity.BillItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 账单明细 Mapper，负责消费项目查询和新增。
 */
@Mapper
public interface BillItemMapper {
    /** 查询指定账单的全部明细。 */
    @Select("select * from bill_item where bill_id = #{billId} order by created_at, id")
    List<BillItem> listByBillId(String billId);

    /** 新增账单明细。 */
    @Insert("""
            insert into bill_item (id, bill_id, item_name, category, amount, quantity, remark)
            values (#{id}, #{billId}, #{itemName}, #{category}, #{amount}, #{quantity}, #{remark})
            """)
    int insert(BillItem item);
}

