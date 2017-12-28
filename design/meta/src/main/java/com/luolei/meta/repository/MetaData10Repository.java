package com.luolei.meta.repository;

import com.luolei.meta.domain.MetaData10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 17:45
 */
public interface MetaData10Repository extends JpaRepository<MetaData10, Long>, JpaSpecificationExecutor<MetaData10> {
}
