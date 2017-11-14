package com.luolei.framework.jpa;

import com.luolei.framework.jpa.dao.HusbandDao;
import com.luolei.framework.jpa.dao.WifeDao;
import com.luolei.framework.jpa.model.Husband;
import com.luolei.framework.jpa.model.Wife;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 19:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class HusbandWifeDaoTest {

    @Autowired
    private HusbandDao husbandDao;

    @Autowired
    private WifeDao wifeDao;

    @Test
    public void testHusband() {
        Husband husband = new Husband();
        husband.setName("luolei");

        Wife wife = new Wife();
        wife.setName("haha");
        husband.setWife(wife);

        husbandDao.save(husband);
        assertThat(husbandDao.count()).isEqualTo(1L);
        assertThat(wifeDao.count()).isEqualTo(1L);
        assertThat(husbandDao.findAll().get(0).getWife()).isNotNull();

        /**
         * 这里有bug，在 eclipse 上面单元测试可以通过，idea 这里就过不了
         */
        assertThat(wifeDao.findAll().get(0).getHusband()).isNotNull();
    }

    @Test
    public void testWife() {
        Husband husband = new Husband();
        husband.setName("husband");

        Wife wife = new Wife();
        wife.setName("wife");

        wife.setHusband(husband);

        wife = wifeDao.save(wife);

        assertThat(husbandDao.count()).isEqualTo(1L);
        assertThat(wifeDao.count()).isEqualTo(1L);
        assertThat(husbandDao.findAll().get(0).getWife()).isNotNull();
        assertThat(wifeDao.findAll().get(0).getHusband()).isNotNull();
    }

}
