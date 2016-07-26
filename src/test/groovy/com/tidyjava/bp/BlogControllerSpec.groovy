package com.tidyjava.bp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
class BlogControllerSpec extends Specification {

    @Autowired
    def WebApplicationContext wac

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

    def "home"() {
        expect:
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("posts", [testSummary(1), testSummary(2)]))
    }

    def testSummary(int n) {
        new PostSummary("Post $n", "Summary $n", "/post$n")
    }

    def "post"(n) {
        expect:
        mockMvc.perform(get("/post$n"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", testPost(n)))

        where:
        n << [1, 2]
    }

    def testPost(n) {
        new Post("Post $n", "Summary $n", "<p><strong>Content $n</strong></p>\n")
    }

    // TODO error cases
}
