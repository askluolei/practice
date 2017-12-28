package com.luolei.meta.repository;

import com.luolei.meta.domain.MetaClobData10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 17:45
 */
public interface MetaClobData10Repository extends JpaRepository<MetaClobData10, Long>, JpaSpecificationExecutor<MetaClobData10> {
}
