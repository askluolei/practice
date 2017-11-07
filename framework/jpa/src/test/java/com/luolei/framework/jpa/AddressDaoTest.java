package com.luolei.framework.jpa;

import com.luolei.framework.jpa.dao.AddressDao;
import com.luolei.framework.jpa.model.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 *
 * 这里测试的是
 * 单表 无关联 的一些基本操作
 *
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 14:08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AddressDaoTest {

    @Autowired
    private AddressDao addressDao;

    /**
     * 基本插入
     */
    @Test
    public void testSave() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();
    }

    /**
     * 根据 主键 删除
     */
    @Test
    public void testDeleteById() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();

        addressDao.delete(address.getId());
        assertThat(addressDao.count()).isEqualTo(0L);
    }

    /**
     * 删除对象
     */
    @Test
    public void testDelete() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();

        addressDao.delete(address);
        assertThat(addressDao.count()).isEqualTo(0L);
    }

    /**
     * 批量删除对象
     */
    @Test
    public void testDeleteBatch() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();

        addressDao.delete(Arrays.asList(address));
        assertThat(addressDao.count()).isEqualTo(0L);
    }

    /**
     * 更新操作
     */
    @Test
    public void testUpdate() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();

        address.setStreetCode(456);
        address = addressDao.save(address);
        assertThat(address.getStreetCode()).isEqualTo(456);
    }

    /**
     * 查询操作
     * 根据方法名定义查询语句 已经 使用 @Query 注解自定义查询语句就不演示了，那是最基本的查询操作，挺简单的
     *
     * 这里测试的是 Example
     */
    @Test
    public void testQueryByExample() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();

        /**
         * 通常情况下
         * 一张表，哪些字段要明确查询，哪些字段可以模糊查询
         * 这些一开始就可以定义好，不太可能会改变
         *
         * 当然，也可以使用 ExampleMatcher 动态拼接条件
         * 不过要注意 matcher = matcher.withMatcher  这样使用
         */
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("province", m -> m.contains().caseSensitive()) //这里 对 province 忽略大小写 并且是 包含
                .withMatcher("city", m -> m.startsWith())// 对 city 字段 查询是 开头
                .withMatcher("streetCode", m -> m.exact());//相等

        Address addressExample = new Address();
        addressExample.setProvince("广");
        assertThat(addressDao.findAll(Example.of(addressExample, matcher)).size()).isEqualTo(1);

        addressExample = new Address();
        addressExample.setStreetCode(123);
        assertThat(addressDao.findAll(Example.of(addressExample, matcher)).size()).isEqualTo(1);

        addressExample = new Address();
        addressExample.setCity("深");
        assertThat(addressDao.findAll(Example.of(addressExample, matcher)).size()).isEqualTo(1);

        addressExample = new Address();
        addressExample.setCity("圳");
        assertThat(addressDao.findAll(Example.of(addressExample, matcher)).size()).isEqualTo(0);

        /**
         * 注意这里，一个空对象作为 Example 那么查询的是全表
         */
        addressExample = new Address();
        assertThat(addressDao.findAll(Example.of(addressExample, matcher)).size()).isEqualTo(1);
    }

    /**
     * 这里测试的是 Specification
     *
     * Specification 查询使用挺复杂的
     */
    @Test
    public void testQuerySpec() {
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        address.setStreetCode(123);
        address = addressDao.save(address);
        assertThat(addressDao.count()).isEqualTo(1L);
        assertThat(address.getId()).isNotNull();

        String province = "广";
        String city = "深";
        Integer code = 123;
        Specification<Address> specification = (Root<Address> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.and(
                cb.like(root.get("province"), "%" + province + "%"),
                cb.like(root.get("city"), "%" + city + "%"),
                cb.equal(root.get("streetCode"), code)
        );

        assertThat(addressDao.findAll(((root, query, cb) -> cb.like(root.get("province"), "%" + province + "%"))).size()).isEqualTo(1);
        assertThat(addressDao.findAll(((root, query, cb) -> cb.like(root.get("city"), "%" + city + "%"))).size()).isEqualTo(1);
        assertThat(addressDao.findAll(((root, query, cb) -> cb.equal(root.get("streetCode"), code))).size()).isEqualTo(1);
    }

}
