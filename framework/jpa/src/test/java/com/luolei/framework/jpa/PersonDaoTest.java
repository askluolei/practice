package com.luolei.framework.jpa;

import com.luolei.framework.jpa.dao.PersonDao;
import com.luolei.framework.jpa.dao.PersonDetailDao;
import com.luolei.framework.jpa.model.Person;
import com.luolei.framework.jpa.model.PersonDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * 这里测试的是 单向一对一
 *
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 15:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PersonDaoTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PersonDetailDao detailDao;

    /**
     * 单向一对一
     * 通常用在一张表的内容是另一张表的补充
     * 比如，这里的personDetail 表的内容 就是 Person 表的补充
     * 通常情况下
     * 我们在前端编辑数据的时候，应该是可以同时编辑 person 和 personDetail 的数据库
     * 在hibernate 里面 person 对象里面 有 persondetail 对象
     * 因此通常情况下，希望的是 当保存 person的时候 personDetail 的数据也会保存或者修改
     *
     * 当 person 主动设置 personDetail 为null 的时候，如果以前是有值的，那么应该要删除personDetail 并 把 person 这里的外键也删除掉
     * 也就是说 personDetail 单独存在是没有意义的
     *
     * 但是，也可能有特殊情况，只是要断开外键连接，但是不删除 personDetail
     * 这种情况通常是保存数据，可能用来做其他用途的分析
     * 针对这个情况只要将 person 类里面的 personDetail字段 上的一对一配置 加上 orphanRemoval = true 就行了
     *
     */
    @Test
    public void testSave() {
        PersonDetail detail = new PersonDetail();
        detail.setAge(18);
        detail.setPhone("123");

        Person person = new Person();
        person.setName("luolei");
        person.setDetail(detail);

        person = personDao.save(person);

        assertThat(personDao.count()).isEqualTo(1L);
        assertThat(detailDao.count()).isEqualTo(1L);
        PersonDetail detailTemp = personDao.findAll().get(0).getDetail();
        assertThat(detailTemp).isNotNull();
        assertThat(detailTemp.getAge()).isEqualTo(18);

        person.setName("haha");
        person.getDetail().setAge(22);
        person = personDao.save(person);

        assertThat(personDao.count()).isEqualTo(1L);
        assertThat(detailDao.count()).isEqualTo(1L);
        assertThat(personDao.findAll().get(0).getDetail()).isNotNull();
        assertThat(personDao.findAll().get(0).getName()).isEqualTo("haha");
        assertThat(detailDao.findAll().get(0).getAge()).isEqualTo(22);

        person.setDetail(null);
        person = personDao.save(person);

        assertThat(personDao.count()).isEqualTo(1L);
        assertThat(detailDao.count()).isEqualTo(1L);
        assertThat(person.getName()).isEqualTo("haha");
        assertThat(person.getDetail()).isNull();
    }


}
