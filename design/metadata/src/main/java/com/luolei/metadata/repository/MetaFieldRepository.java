package com.luolei.metadata.repository;

import com.luolei.metadata.tables.MetaField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 罗雷
 * @date 2017/12/26 0026
 * @time 17:22
 */
public interface MetaFieldRepository extends JpaRepository<MetaField, Long>, JpaSpecificationExecutor<MetaField> {
}
