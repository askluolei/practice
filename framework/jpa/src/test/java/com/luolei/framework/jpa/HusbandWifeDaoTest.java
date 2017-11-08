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
        husband.setName("husband");

        Wife wife = new Wife();
        wife.setName("wife");

        husband.setWife(wife);

        husband = husbandDao.save(husband);

        assertThat(husbandDao.count()).isEqualTo(1L);
        assertThat(wifeDao.count()).isEqualTo(1L);
        Husband tempHusband = husbandDao.findAll().get(0);
        System.out.println(tempHusband.getWife());
        assertThat(tempHusband.getWife()).isNotNull();
        Wife tempWife = wifeDao.findAll().get(0);
        System.out.println("========================");
        System.out.println(tempWife.getHusband());
        assertThat(tempWife.getHusband()).isNotNull();
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
