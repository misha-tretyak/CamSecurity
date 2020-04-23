package ua.lviv.lgs.CamSecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.lviv.lgs.CamSecurity.entity.Goods;
import ua.lviv.lgs.CamSecurity.entity.Groups;
import ua.lviv.lgs.CamSecurity.exeption.NotFoundExeption;
import ua.lviv.lgs.CamSecurity.servise.GoodsServise;
import ua.lviv.lgs.CamSecurity.servise.GroupService;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final GoodsServise goodsServise;

    @GetMapping
    public String getAllGroups(Model model){
        addTotalGoods();
        model.addAttribute("group", groupService.findAll());
        return "groups-list";
    }

    private void addTotalGoods() {
        List<Groups> rezult = groupService.findAll();

        for (int i = 0; i < rezult.size(); i++) {
            Groups group = rezult.get(i);
            group.setTotalGoods(group.getGoods().size());
            groupService.update(group);
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/create")
    public String getGroupPage() {
        return "group";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    public String createGroup(@ModelAttribute Groups group){
        groupService.create(group);
        return "redirect:/groups";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/update")
    public String updateGroup(@ModelAttribute Groups group) {
        if (group.getId() != null) {
            groupService.update(group);}
        return "redirect:/groups";
    }

    @GetMapping("/list/goods")
    public String getGoodsWithGroup(@RequestParam Long id, Model model) {
        List<Goods> listGoods = groupService.findById(id).get().getGoods();
        String name = groupService.findById(id).get().getName();
        model.addAttribute("goods", listGoods);
        model.addAttribute("groupName", name);
        return "goods_list_by_group";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/list/goods/delete")
    public String deleteGoodsInGroup(@RequestParam Long id){
        Goods goods = goodsServise.findById(id).orElseThrow(() -> new NotFoundExeption("Goods with id:" + id + "was not found"));
        Groups group = goods.getGroup();
        List<Goods> goodsList = group.getGoods();
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).getId() == id) {
                Goods goods1 = goodsList.get(i);
                goods1.setGroup(null);
                goodsServise.update(goods1);
                goodsList.remove(i);
                group.setGoods(goodsList);
                groupService.update(group);
            }
        }
        return "redirect:/groups";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/delete")
    public String deleteGroup(@RequestParam Long id){
        try {
            Groups group = groupService.findById(id).orElseThrow(() -> new NotFoundExeption("Group with id:" + id + "was not found"));
            if (group.getGoods() != null) {
                List<Goods> goods = group.getGoods();
                for (int i = 0; i < goods.size(); i++) {
                    Goods goods_1 = goods.get(i);
                    goods_1.setGroup(null);
                    goodsServise.create(goods_1);
                    goods.remove(i);
                }
                group.setGoods(goods);
                groupService.update(group);
            }
            groupService.deleteById(id);
        } catch (Exception e) {
            return "group-not-found";
        }
        return "redirect:/groups";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/one")
    public String getOne(@RequestParam Long id, Model model) {
        Optional<Groups> groupsOptional = groupService.findById(id);
        if (groupsOptional.isPresent()) {
            model.addAttribute("group", groupsOptional.get());
            return "group-update";
        }
        return "group-not-found";
    }
}
