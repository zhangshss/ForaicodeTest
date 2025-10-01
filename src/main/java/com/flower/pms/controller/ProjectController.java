package com.flower.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flower.pms.entity.Project;
import com.flower.pms.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int current,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        IPage<Project> page = projectService.page(new Page<>(current, size));
        model.addAttribute("page", page);
        return "projects/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/form";
    }

    @PostMapping
    public String save(@ModelAttribute Project project) {
        projectService.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getById(id));
        return "projects/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Project project) {
        project.setId(id);
        projectService.updateById(project);
        return "redirect:/projects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        projectService.removeById(id);
        return "redirect:/projects";
    }

    @PostMapping("/batch-delete")
    @ResponseBody
    public String batchDelete(@RequestBody List<Long> ids) {
        projectService.removeByIds(ids);
        return "OK";
    }
}
