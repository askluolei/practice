package com.luolei.template.web.rest;

import cn.hutool.core.util.StrUtil;
import com.luolei.template.domain.ExecuteSql;
import com.luolei.template.domain.SavedSql;
import com.luolei.template.service.ToolsService;
import com.luolei.template.web.rest.errors.BizError;
import com.luolei.template.web.rest.util.PaginationUtil;
import com.luolei.template.web.rest.vm.SqlVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 14:33
 */
@RequestMapping("/api/tools")
@RestController
public class ToolsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String TYPE_SELECT = "SELECT";
    public static final String TYPE_UPDATE = "UPDATE";
    public static final String TYPE_DELETE = "DELETE";
    public static final String TYPE_INSERT = "INSERT";
    public static final String TYPE_ALTER = "ALTER";

    private final ToolsService toolsService;

    public ToolsController(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    /**
     * 执行查询 sql
     * @param sqlVM
     * @return
     */
    @PreAuthorize("hasAuthority('EXECUTE_SQL:READ')")
    @PostMapping("/sql")
    public ResponseEntity<Object> executeSelect(@RequestBody SqlVM sqlVM) {
        String type = checkSql(sqlVM);
        if (type.equals(TYPE_SELECT)) {
            return ResponseEntity.ok(toolsService.executeSelect(sqlVM));
        } else if (type.equals(TYPE_ALTER)){
            toolsService.execute(sqlVM);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.ok(toolsService.executeUpdate(sqlVM));
        }
    }

    @GetMapping("/execute-sql")
    public ResponseEntity<List<ExecuteSql>> searchExecuteSql(Pageable pageable) {
        Page<ExecuteSql> page = toolsService.latestExecuteSql(pageable);
        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tools/execute-sql");
        return ResponseEntity.ok().headers(httpHeaders).body(page.getContent());
    }

    @GetMapping("/saved-sql")
    public ResponseEntity<List<SavedSql>> searchSavedSql(Pageable pageable) {
        Page<SavedSql> page = toolsService.latestSavedSql(pageable);
        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tools/saved-sql");
        return ResponseEntity.ok().headers(httpHeaders).body(page.getContent());
    }

    /**
     * 检查sql类型
     * 只支持
     * SELECT
     * UPDATE
     * DELETE
     * INSERT
     * ALTER
     * 开头的sql
     */
    private String checkSql(SqlVM sqlVM) {
        String sql = sqlVM.getSql();
        StringBuilder sb = new StringBuilder();
        for (String s : StrUtil.split(sql, "\n")) {
            if (StrUtil.trim(s).startsWith("--")) {
                continue;
            } else {
                sb.append(s);
            }
        }
        String executeSql = sb.toString();
        sqlVM.setExecuteSql(executeSql);
        //TODO 缺sql检查
        String trim = StrUtil.trim(executeSql).toUpperCase();
        if (trim.startsWith(TYPE_SELECT)) {
            sqlVM.setType(TYPE_SELECT);
            return TYPE_SELECT;
        }
        if (trim.startsWith(TYPE_UPDATE)) {
            sqlVM.setType(TYPE_UPDATE);
            return TYPE_UPDATE;
        }
        if (trim.startsWith(TYPE_DELETE)) {
            sqlVM.setType(TYPE_DELETE);
            return TYPE_DELETE;
        }
        if (trim.startsWith(TYPE_INSERT)) {
            sqlVM.setType(TYPE_INSERT);
            return TYPE_INSERT;
        }
        if (trim.startsWith(TYPE_ALTER)) {
            sqlVM.setType(TYPE_ALTER);
            return TYPE_ALTER;
        }
        throw BizError.EXECUTE_SQL_CHECK_ERROR.exception("不合法的sql" + sql);
    }
}
