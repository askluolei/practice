package com.luolei.template.service;

import com.luolei.template.domain.OssImage;
import com.luolei.template.oss.CloudStorageService;
import com.luolei.template.repository.OssImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 18:05
 */
@Service
public class OssImageService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final OssImageRepository ossImageRepository;
    private final CloudStorageService cloudStorageService;
    public OssImageService(OssImageRepository ossImageRepository, CloudStorageService cloudStorageService) {
        this.ossImageRepository = ossImageRepository;
        this.cloudStorageService = cloudStorageService;
    }

    public OssImage upload(byte[] bytes, String suffix) {
        log.debug("upload file by suffix : {}", suffix);
        String url = this.cloudStorageService.uploadSuffix(bytes, suffix);
        OssImage ossImage = new OssImage();
        ossImage.setLink(url);
        ossImage = ossImageRepository.save(ossImage);
        return ossImage;
    }

    public Page<OssImage> query(Pageable pageable) {
        log.debug("find by page : {}", pageable);
        return ossImageRepository.findAll(pageable);
    }

    public Optional<OssImage> get(Long id) {
        log.debug("get by id:{}", id);
        return Optional.ofNullable(ossImageRepository.findOne(id));
    }

    public void delete(Long id) {
        log.debug("delete by id:{}", id);
        ossImageRepository.delete(id);
    }

    public void deleteInBatch(Long[] ids) {
        log.debug("delete in batch. ids:{}", Arrays.toString(ids));
        for (Long id : ids) {
            ossImageRepository.delete(id);
        }
    }
}
