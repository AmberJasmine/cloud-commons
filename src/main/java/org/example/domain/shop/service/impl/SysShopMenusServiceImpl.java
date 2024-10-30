package org.example.domain.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.consts.Constant;
import org.example.domain.shop.dto.SysShopMenusDto;
import org.example.domain.shop.entity.SysShopMenus;
import org.example.domain.shop.mapper.SysShopMenusMapper;
import org.example.domain.shop.service.SysShopMenusService;
import org.example.until.Result;
import org.example.until.tree.TreeBuild;
import org.example.until.tree.TreeNode;
import org.example.until.tree.TreeNodeBuild;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("shopMenusService")
public class SysShopMenusServiceImpl
        extends ServiceImpl<SysShopMenusMapper, SysShopMenus>
        implements SysShopMenusService {

    @Autowired
    private SysShopMenusMapper sysShopMenusMapper;

    @Autowired
    private SysShopMenusService sysShopMenusService;

    @Autowired
    private HttpSession httpSession;


    @Override
    public Result<List<SysShopMenus>> node(String id) {
        List<SysShopMenus> list = this.sysShopMenusService.list(new LambdaQueryWrapper<SysShopMenus>()
                .eq(SysShopMenus::getPId, id)
                .eq(SysShopMenus::getDeleted, Constant.STATUS.STATUS_0)
                .orderByAsc(SysShopMenus::getSort));
        return Result.ofSuccess("查询成功", list);
    }

    @Override
    public Result add(SysShopMenusDto dto) {
        String pId = dto.getPId();
        String tentId = dto.getTentId();
        String name = dto.getName();
        Optional<SysShopMenus> first = this.sysShopMenusService.list(new LambdaQueryWrapper<SysShopMenus>()
                .eq(SysShopMenus::getPId, pId)
                .eq(SysShopMenus::getTentId, tentId)
        ).stream().max(Comparator.comparing(SysShopMenus::getSort));
        int sort = 1;
        if (first.isPresent()) {
            SysShopMenus sysShopMenus = first.get();
            sort = sysShopMenus.getSort() + 1;
        }
        SysShopMenus sysShopMenus = new SysShopMenus();
        BeanUtils.copyProperties(dto, sysShopMenus);
        sysShopMenus.setCreateBy((String) this.httpSession.getAttribute(Constant.SESSION.USER_ID));
        sysShopMenus.setCreateTime(LocalDateTime.now());
        sysShopMenus.setDeleted(Constant.STATUS.STATUS_0);
        sysShopMenus.setSort(sort);
        this.sysShopMenusService.save(sysShopMenus);
        return Result.ofSuccess("新增成功", sysShopMenus);
    }

    @Override
    public Result<List<TreeNode>> menu(String tentId) {
        List<SysShopMenus> list = this.sysShopMenusService.list(new LambdaQueryWrapper<SysShopMenus>()
                .eq(SysShopMenus::getDeleted, Constant.STATUS.STATUS_0)
                .eq(SysShopMenus::getTentId, tentId));
        List<TreeNode> treeNodes = new ArrayList<>();
        for (SysShopMenus sysShopMenus : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(sysShopMenus.getId());
            treeNode.setName(sysShopMenus.getName());
            treeNode.setPId(sysShopMenus.getPId());
            treeNode.setSort(sysShopMenus.getSort());
            treeNodes.add(treeNode);
        }
        // treeNodes
        TreeBuild treeBuild = new TreeBuild(treeNodes);
        treeNodes = treeBuild.buildTree();
        return Result.ofSuccess("", treeNodes);
    }

    @Override
    public Result delete(List<String> list) {
        boolean update = this.sysShopMenusService.update(new LambdaUpdateWrapper<SysShopMenus>()
                .set(SysShopMenus::getDeleted, Constant.STATUS.STATUS_1)
                .in(SysShopMenus::getId, list));
        System.out.println("update = " + update);
        return Result.ofSuccess("");
    }

    @Override
    public Result shopping() {
        List<String> collect = this.sysShopMenusService.list(new LambdaQueryWrapper<SysShopMenus>()
                .eq(SysShopMenus::getDeleted, Constant.STATUS.STATUS_0))
                .stream().map(SysShopMenus::getTentId).distinct().collect(Collectors.toList());
        return Result.ofSuccess("所有商场集合", collect);
    }

    @Override
    public Result<TreeNode> childTree(String id) {
        SysShopMenus byId = this.sysShopMenusService.getById(id);
        log.info("byId = {}", byId);

        List<SysShopMenus> list = this.sysShopMenusService.list(new LambdaQueryWrapper<SysShopMenus>()
                        .eq(SysShopMenus::getDeleted, Constant.STATUS.STATUS_0)
//                .eq(SysShopMenus::getTentId, tentId)
        );
        List<TreeNode> treeNodes = new ArrayList<>();
        for (SysShopMenus sysShopMenus : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(sysShopMenus.getId());
            treeNode.setName(sysShopMenus.getName());
            treeNode.setPId(sysShopMenus.getPId());
            treeNode.setSort(sysShopMenus.getSort());
            treeNodes.add(treeNode);
        }
        TreeNodeBuild treeNodeBuild = new TreeNodeBuild(treeNodes, id);
        treeNodes = treeNodeBuild.buildTree();
        return Result.ofSuccess("查询结果", CollectionUtils.isNotEmpty(treeNodes) ? treeNodes.get(0) : null);
    }

    @Override
    public Result<List<SysShopMenus>> childIds(String id, Boolean bool) {
        List<SysShopMenus> sysShopMenus = this.sysShopMenusMapper.nodeChildIds(id);
        if (Boolean.FALSE.equals(bool)) {
            sysShopMenus = sysShopMenus.stream().filter(p -> !Objects.equals(p.getId(), id)).collect(Collectors.toList());
        }
        return Result.ofSuccess("节点ids集合", sysShopMenus);
    }
}

