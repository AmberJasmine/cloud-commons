package org.example.domain.start;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Create by Administrator
 * Data 15:25 2021/10/3 星期日
 */
@Controller
@Api(tags = {"页面"})
//@RequestMapping("page")
public class PageController {


    @GetMapping("/tab")
    public String tab() {
        return "tab";
    }


}
