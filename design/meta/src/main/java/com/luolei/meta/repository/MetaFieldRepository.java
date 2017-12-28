package com.luolei.meta.repository;

import com.luolei.meta.domain.MetaField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 17:45
 */
public interface MetaFieldRepository extends JpaRepository<MetaField, Long>, JpaSpecificationExecutor<MetaField> {
}
