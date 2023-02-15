package com.star.rpc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.rpc.domain.dto.JobDTO;
import com.star.rpc.domain.vo.JobSearchVO;
import com.star.rpc.entity.ScheduleJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 02-15-2023
 */
@Repository
public interface JobMapper extends BaseMapper<ScheduleJob> {

    /**
     * Job 总数
     *
     * @param jobSearchVO
     * @return
     */
    Long countJobs(@Param("jobSearchVO") JobSearchVO jobSearchVO);


    /**
     * listJobs
     *
     * @param current
     * @param size
     * @param jobSearchVO
     * @return
     */
    List<JobDTO> listJobs(@Param("current") Long current, @Param("size") Long size, @Param("jobSearchVO") JobSearchVO jobSearchVO);

    /**
     * listJobGroups
     *
     * @return
     */
    List<String> listJobGroups();

}
