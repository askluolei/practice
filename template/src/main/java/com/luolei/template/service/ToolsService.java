package com.luolei.template.service;

import com.luolei.template.domain.ExecuteSql;
import com.luolei.template.domain.SavedSql;
import com.luolei.template.repository.ExecuteSqlRepository;
import com.luolei.template.repository.SavedSqlRepository;
import com.luolei.template.web.rest.errors.BizError;
import com.luolei.template.web.rest.vm.SqlVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 14:01
 */
@Service
public class ToolsService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<DataSource> dataSources;
    private final JdbcTemplate jdbcTemplate;
    private final ExecuteSqlRepository executeSqlRepository;
    private final SavedSqlRepository savedSqlRepository;

    public ToolsService(@Autowired(required = false) List<DataSource> dataSources, JdbcTemplate jdbcTemplate, ExecuteSqlRepository executeSqlRepository, SavedSqlRepository savedSqlRepository) {
        if (Objects.nonNull(dataSources)) {
            this.dataSources = dataSources;
        } else {
            this.dataSources = Collections.emptyList();
        }
        this.jdbcTemplate = jdbcTemplate;
        this.executeSqlRepository = executeSqlRepository;
        this.savedSqlRepository = savedSqlRepository;
    }

    public void getDatabaseInfo() {
        for (DataSource datasource : dataSources) {
            Connection connection = null;
            try {
                connection = datasource.getConnection();
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    log.info(schemas.getString("TABLE_SCHEME"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (Objects.nonNull(connection)) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }
    }

    /**
     * 查询操作
     * @return
     */
    public List<Map<String, Object>> executeSelect(SqlVM sqlVM) {
        recordExecuteSql(sqlVM);
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sqlVM.getExecuteSql());
        } catch (DataAccessException e) {
            throw BizError.EXECUTE_SQL_ERROR.exception(e);
        }
        saveSql(sqlVM);
        return list;
    }

    public Page<ExecuteSql> latestExecuteSql(Pageable pageable) {
        return executeSqlRepository.findAll(pageable);
    }

    public Page<SavedSql> latestSavedSql(Pageable pageable) {
        return savedSqlRepository.findAll(pageable);
    }

    /**
     * alter 在这里执行
     * @param sqlVM
     */
    public void execute(SqlVM sqlVM) {
        recordExecuteSql(sqlVM);
        try {
            jdbcTemplate.execute(sqlVM.getExecuteSql());
        } catch (DataAccessException e) {
            throw BizError.EXECUTE_SQL_ERROR.exception(e);
        }
        saveSql(sqlVM);
    }

    /**
     * 执行更新语句（insert, update, delete）
     * @param sqlVM
     * @return
     */
    public int executeUpdate(SqlVM sqlVM) {
        recordExecuteSql(sqlVM);
        int result = 0;
        try {
            result = jdbcTemplate.update(sqlVM.getExecuteSql());
        } catch (DataAccessException e) {
            throw BizError.EXECUTE_SQL_ERROR.exception(e);
        }
        saveSql(sqlVM);
        return result;
    }

    /**
     * 保存sql
     * @param sqlVM
     */
    private void saveSql(SqlVM sqlVM) {
        if (sqlVM.getSave()) {
            SavedSql savedSql = new SavedSql();
            savedSql.setSql(sqlVM.getExecuteSql());
            savedSql.setOriginSql(sqlVM.getSql());
            savedSql.setTitle(sqlVM.getTitle());
            savedSql.setExplanation(sqlVM.getExplanation());
            savedSql.setType(sqlVM.getType());
            savedSql.setValidate(true);
            savedSqlRepository.save(savedSql);
        }
    }

    /**
     * 记录执行的sql
     * 记录的是执行的sql
     * @param sqlVM
     */
    private void recordExecuteSql(SqlVM sqlVM) {
        ExecuteSql executeSql = new ExecuteSql();
        executeSql.setOriginSql(sqlVM.getSql());
        executeSql.setSql(sqlVM.getExecuteSql());
        executeSql.setTitle(sqlVM.getTitle());
        executeSql.setExplanation(sqlVM.getExplanation());
        executeSql.setType(sqlVM.getType());
        executeSqlRepository.save(executeSql);
    }
}
