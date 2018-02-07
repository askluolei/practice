package com.luolei.template.web.rest;

import com.luolei.template.domain.OssImage;
import com.luolei.template.service.OssImageService;
import com.luolei.template.web.rest.errors.BizError;
import com.luolei.template.web.rest.util.HeaderUtil;
import com.luolei.template.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 19:12
 */
@RequestMapping("/api/")
@RestController
public class OssImageController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final OssImageService ossImageService;

    public OssImageController(OssImageService ossImageService) {
        this.ossImageService = ossImageService;
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<OssImage> upload(@RequestParam(name = "file") MultipartFile file) {
        log.debug("request to upload file");
        if (file.isEmpty()) {
            throw BizError.UPLOAD_ERROR.exception("上传的文件不能为空");
        }
        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        try {
            return ResponseEntity.ok(ossImageService.upload(file.getBytes(), suffix));
        } catch (IOException e) {
            throw BizError.UPLOAD_ERROR.exception("上传的文件失败");
        }
    }

    /**
     * 获取文件列表
     * @param pageable
     * @return
     */
    @GetMapping("/oss-image")
    public ResponseEntity<List<OssImage>> query(Pageable pageable) {
        log.debug("request to query image list by page : {}", pageable);
        Page<OssImage> page = ossImageService.query(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/oss-image");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * 根据id 获取单个文件
     * @param id
     * @return
     */
    @GetMapping("/oss-image/{id}")
    public ResponseEntity<OssImage> get(@PathVariable Long id) {
        log.debug("request to get image by id : {}", id);
        return ResponseUtil.wrapOrNotFound(ossImageService.get(id));
    }

    /**
     * 根据id删除
     * 只删除数据，不删除云上的内容
     * @param id
     * @return
     */
    @DeleteMapping("/oss-image/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("request to delete by id: {}", id);
        ossImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("oss image task deleted", String.valueOf(id))).build();
    }

    /**
     * 根据 ids 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/oss-image")
    public ResponseEntity<Void> deleteInBatch(@RequestBody Long[] ids) {
        log.debug("request to delete in batch ids:{}", Arrays.toString(ids));
        ossImageService.deleteInBatch(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("oss image task deleted", Arrays.toString(ids))).build();
    }
}
