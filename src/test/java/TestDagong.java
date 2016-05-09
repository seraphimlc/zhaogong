

import com.dagong.pojo.Company;
import com.dagong.pojo.JobType;
import com.dagong.service.JobService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchang on 16/1/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/resources/base/all.xml")
public class TestDagong {
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
@Autowired
    private JobService jobService;
    private Map<String, JobType> jobTypeMap = new HashMap<>();

    private Map<String, Company> companyMap = new HashMap<>();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

   @Test
    public void test(){
    }

//    @Test
//    public void testSearchJob() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/job/forUser.do").cookie(new Cookie("user", "123")))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print()).andReturn();
//
//    }


}
