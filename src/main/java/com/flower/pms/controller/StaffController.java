package com.flower.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flower.pms.entity.Staff;
import com.flower.pms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int current,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        IPage<Staff> page = staffService.page(new Page<>(current, size));
        model.addAttribute("page", page);
        return "staffs/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "staffs/form";
    }

    @PostMapping
    public String save(@ModelAttribute Staff staff) {
        staffService.save(staff);
        return "redirect:/staffs";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("staff", staffService.getById(id));
        return "staffs/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Staff staff) {
        staff.setId(id);
        staffService.updateById(staff);
        return "redirect:/staffs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        staffService.removeById(id);
        return "redirect:/staffs";
    }

    @PostMapping("/batch-delete")
    @ResponseBody
    public String batchDelete(@RequestBody List<Long> ids) {
        staffService.removeByIds(ids);
        return "OK";
    }
}
