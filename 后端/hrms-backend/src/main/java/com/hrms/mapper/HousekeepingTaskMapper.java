package com.hrms.mapper;

import com.hrms.entity.HousekeepingTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 清洁任务 Mapper，负责客房清洁任务查询和状态更新。
 */
@Mapper
public interface HousekeepingTaskMapper {
    /** 根据任务编号查询清洁任务。 */
    @Select("select * from housekeeping_task where id = #{id}")
    HousekeepingTask getById(String id);

    /** 新增清洁任务。 */
    @Insert("""
            insert into housekeeping_task (id, room_number, task_type, status, assigned_to, priority, created_at, remark)
            values (#{id}, #{roomNumber}, #{taskType}, #{status}, #{assignedTo}, #{priority}, #{createdAt}, #{remark})
            """)
    int insert(HousekeepingTask task);

    /** 更新清洁任务状态。 */
    @Update("""
            update housekeeping_task
            set status = #{status}, assigned_to = #{assignedTo}, completed_at = #{completedAt}, remark = #{remark}
            where id = #{id}
            """)
    int updateStatus(@Param("id") String id, @Param("status") String status,
                     @Param("assignedTo") String assignedTo,
                     @Param("completedAt") LocalDateTime completedAt, @Param("remark") String remark);

    /** 查询清洁任务列表。 */
    List<HousekeepingTask> selectTaskList(@Param("status") String status, @Param("roomNumber") String roomNumber,
                                          @Param("assignedTo") String assignedTo);
}
