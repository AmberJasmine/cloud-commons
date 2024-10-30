package org.example.domain.systagmanage.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.pags.config.constant.Constants;
//import com.example.pags.mysql.demo.sys.systagmanage.dto.LoginInfoDto;
//import com.example.pags.mysql.demo.sys.systagmanage.dto.SysTagManageDto;
//import com.example.pags.mysql.demo.sys.systagmanage.entity.SysTagManage;
//import com.example.pags.mysql.demo.sys.systagmanage.service.SysTagManageService;
//import com.example.pags.until.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.consts.Constants;
import org.example.domain.systagmanage.dto.LoginInfoDto;
import org.example.domain.systagmanage.dto.SysTagManageDto;
import org.example.domain.systagmanage.entity.SysTagManage;
import org.example.domain.systagmanage.service.SysTagManageService;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 标签表(SysTagManage)表控制层
 *
 * @author makejava
 * @since 2022-08-14 12:41:58
 */
@Slf4j
@Api(tags = {"（note）标签管理"})
@RestController
@RequestMapping("/sys-tag-manage")
public class SysTagManageController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private SysTagManageService sysTagManageService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @ApiOperation(value = "数据存到redis", notes = "数据存到redis", httpMethod = "POST")
    @PostMapping("/saveRedis")
    public Result saveRedis() {
        List<SysTagManage> sysTagManages = this.sysTagManageService.list();
        for (SysTagManage sysTagManage : sysTagManages) {
            this.redisTemplate.opsForValue().set(Constants.CODE_VALUE_PREFIX + sysTagManage.getId(), sysTagManage.getTagName());
        }

//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
//        for (SysTagManage students : sysTagManages) {
//            stringRedisTemplate.opsForList().rightPush("student", JSON.toJSONString(students));
//        }
//        List<String> studentList = stringRedisTemplate.opsForList()
//                .range("student", 0, -1);
//        // 将redis中的数据转换为对象集合
        List<String> studentsAll = new ArrayList<>();
//        for (String studentString : studentList) {
//            studentsAll.add(studentString);
//        }
//        log.info("studentsAll = {}", studentsAll);
        return Result.ofSuccess(studentsAll);
    }


    @ApiOperation(value = "从redis获取值", notes = "从redis获取值", httpMethod = "POST")
    @PostMapping("/valueRedis")
    public Result valueRedis(@RequestParam String id) {
        long l = System.currentTimeMillis();
        String value = this.redisTemplate.opsForValue().get(Constants.CODE_VALUE_PREFIX + id);
        if (StringUtils.isBlank(value)) {
            SysTagManage one = this.sysTagManageService.getOne(new LambdaQueryWrapper<SysTagManage>()
                    .eq(SysTagManage::getId, id));
            if (Objects.nonNull(one)) {
                System.out.println("从数据库取值one = " + one);
                value = one.getTagName();
                this.redisTemplate.opsForValue().set(Constants.CODE_VALUE_PREFIX + one.getId(), value);
            }
        }
        long l0 = System.currentTimeMillis() - l;
        log.info("耗时 = {}",l0);
        return Result.ofSuccess("", value);
    }



    /**
     * 分页查询所有数据
     *
     * @param page         分页对象
     * @param sysTagManage 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<SysTagManage> page, SysTagManage sysTagManage) {
        return success(this.sysTagManageService.page(page, new QueryWrapper<>(sysTagManage)));
    }

    @PostMapping("/page")
    public Result<Page<SysTagManage>> page(@RequestBody SysTagManageDto dto) {
        try {
            return Result.ofSuccess("标签page", this.sysTagManageService.page(dto));
        } catch (Exception e) {
            return Result.ofFail("");
        }
    }

    @PostMapping("/list")
    public Result<List<SysTagManage>> list(@RequestBody SysTagManageDto dto) {
        try {
            return Result.ofSuccess("标签list", this.sysTagManageService.getList(dto));
        } catch (Exception e) {
            return Result.ofFail("");
        }
    }

    @PostMapping("/add-one")
    public Result<SysTagManage> addOne(@RequestBody SysTagManage sysTagManage) {
        try {
            return this.sysTagManageService.addOne(sysTagManage);
        } catch (Exception e) {
            return Result.ofFail(e.getMessage());
        }
    }

    @PostMapping("/delete-one")
    public Result<Void> deleteOne(@RequestBody SysTagManage sysTagManage) {
        try {
            return this.sysTagManageService.deleteOne(sysTagManage);
        } catch (Exception e) {
            return Result.ofFail(e.getMessage());
        }
    }

    @PostMapping("/get-id")
    public Result getId(HttpServletRequest request) {
        try {
            if (request == null) {
                throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
            }
            String ipStr = request.getHeader("x-forwarded-for");
            if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
                ipStr = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
                ipStr = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
                ipStr = request.getRemoteAddr();
            }
            // 多个路由时，取第一个非unknown的ip
            final String[] arr = ipStr.split(",");
            for (final String str : arr) {
                if (!"unknown".equalsIgnoreCase(str)) {
                    ipStr = str;
                    break;
                }
            }

//            String agent = request.getHeader("user-agent");
//            StringTokenizer st = new StringTokenizer(agent, ";");
//            st.nextToken();
//            //得到用户的浏览器名
//            String userbrowser = st.nextToken();
//            //得到用户的操作系统名
//            String useros = st.nextToken();
//            String IP = request.getRemoteAddr();
//
//            System.out.println("userbrowser = " + userbrowser);
//            System.out.println("useros = " + useros);
//            System.out.println("IP = " + IP);

//            // 获取IP地址
//            String ip = InetAddress.getLocalHost().getHostAddress();
//            // 获取计算机名
//            String name = InetAddress.getLocalHost().getHostName();
//            System.out.println("ip = " + ip);
//            System.out.println("name = " + name);

            ipStr = ipStr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ipStr;

            LoginInfoDto build = LoginInfoDto.builder().ip(null).name(null).ipStr(ipStr).build();

            //目的是将localhost访问对应的ip 0:0:0:0:0:0:0:1 转成 127.0.0.1。
            return Result.ofSuccess("获取ip成功", build);
        } catch (Exception e) {
            return Result.ofFail(e.getMessage());
        }
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.sysTagManageService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysTagManage 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody SysTagManage sysTagManage) {
        return success(this.sysTagManageService.save(sysTagManage));
    }

    /**
     * 修改数据
     *
     * @param sysTagManage 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody SysTagManage sysTagManage) {
        return success(this.sysTagManageService.updateById(sysTagManage));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.sysTagManageService.removeByIds(idList));
    }
}

