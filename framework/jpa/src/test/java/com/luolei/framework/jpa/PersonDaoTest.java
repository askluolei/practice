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
     * 这里想要实现的一个需求是级联插入或者更新
     * 在保存或更新的时候  顺便 保存和更新关联的类
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

        person.setName("haha");
        person.getDetail().setAge(22);
        personDao.save(person);

        assertThat(personDao.count()).isEqualTo(1L);
        assertThat(detailDao.count()).isEqualTo(1L);
        assertThat(personDao.findAll().get(0).getDetail()).isNotNull();
        assertThat(personDao.findAll().get(0).getName()).isEqualTo("haha");
        assertThat(detailDao.findAll().get(0).getAge()).isEqualTo(22);
    }


}
