package com.example.controller;

import com.example.annotations.PermissionAnno;
import com.example.bean.PlanItem;
import com.example.service.PlanItemService;
import com.example.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by fenghao on 2017/6/1.
 */
@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @RequestMapping(value = "/planitem", method = RequestMethod.GET)
    @PermissionAnno
    public String sharePlanItem(@RequestParam("id") String id, @RequestParam("phoneNumber")String phoneNumber, Model model){
        List<PlanItem> list = shareService.getPlanItemWithPlanIdAndPhoneNumber(phoneNumber, id);
        System.out.print(list);
        model.addAttribute("list", list);
        return "share";
    }
}
