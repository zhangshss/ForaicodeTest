package com.flower.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flower.pms.entity.Role;
import com.flower.pms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int current,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        IPage<Role> page = roleService.page(new Page<>(current, size));
        model.addAttribute("page", page);
        return "roles/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/form";
    }

    @PostMapping
    public String save(@ModelAttribute Role role) {
        roleService.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("role", roleService.getById(id));
        return "roles/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Role role) {
        role.setId(id);
        roleService.updateById(role);
        return "redirect:/roles";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.removeById(id);
        return "redirect:/roles";
    }

    @PostMapping("/batch-delete")
    @ResponseBody
    public String batchDelete(@RequestBody List<Long> ids) {
        roleService.removeByIds(ids);
        return "OK";
    }
}
