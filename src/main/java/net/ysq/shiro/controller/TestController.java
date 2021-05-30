package net.ysq.shiro.controller;

import net.ysq.shiro.utils.TreeBuilder;
import net.ysq.shiro.utils.TreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-04-12 15:22
 */
@Controller
@RequestMapping("/test")
public class TestController {


    @GetMapping("/tree")
    @ResponseBody
    public List<TreeNode<Integer>> testTreeBuilder() {
        List<TreeNode<Integer>> nodes = new ArrayList<>();
        nodes.add(new TreeNode<>(1, 0));
        nodes.add(new TreeNode<>(2, 0));
        nodes.add(new TreeNode<>(3, 0));
        nodes.add(new TreeNode<>(4, 1));
        nodes.add(new TreeNode<>(5, 1));
        nodes.add(new TreeNode<>(6, 2));
        nodes.add(new TreeNode<>(7, 4));

        TreeBuilder<Integer> builder = new TreeBuilder<>(nodes, 0);
        return builder.buildTree();
    }

}
