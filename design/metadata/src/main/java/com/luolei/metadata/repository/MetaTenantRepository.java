package com.luolei.metadata.repository;

import com.luolei.metadata.tables.MetaTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 罗雷
 * @date 2017/12/26 0026
 * @time 17:20
 */
public interface MetaTenantRepository extends JpaRepository<MetaTenant, Long>, JpaSpecificationExecutor<MetaTenant> {
}
