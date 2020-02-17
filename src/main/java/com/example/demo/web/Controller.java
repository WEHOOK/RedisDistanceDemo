package com.example.demo.web;

import com.example.demo.pojo.NearbyBO;
import com.example.demo.pojo.NearbyPO;
import com.example.demo.pojo.Result;
import com.example.demo.service.NearbyBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private NearbyBiz nearbyBiz;

    @RequestMapping
    public String helloWord() {
        return "HelloWord";
    }

    // 附近的人
    @RequestMapping(value = "nearby")
    public Result<List<NearbyBO>> nearby(@Valid NearbyPO paramObj) {
        return nearbyBiz.nearby(paramObj);
    }
}