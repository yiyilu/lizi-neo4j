package com.hocassian.people.controller;

import com.hocassian.people.controller.base.BaseController;
import com.hocassian.people.domain.web.ConnectWeb;
import com.hocassian.people.domain.web.PersonWeb;
import com.hocassian.people.entity.User;
import com.hocassian.people.service.web.WebService;
import com.hocassian.people.transport.AjaxResult;
import com.hocassian.people.transport.CommonResult;
import com.hocassian.people.transport.Result;
import org.neo4j.driver.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述：
 *
 * @author Hocassian
 * @date 2021-01-16 11:09
 */
@RestController
@RequestMapping("/people/web")
public class WebController extends BaseController {

    @Autowired
    private WebService webService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestBody User user) {
        String name = user.getUsername();
        String pwd = user.getPassword();
        if(name.equals("admin")){
            if(pwd.equals("111111"))
                return CommonResult.success("admin");
            else
                return CommonResult.validateFailed();
        }
        List<PersonWeb> list = webService.validatePerson(user.getUsername(),user.getPassword());

        if(list==null){
            return CommonResult.validateFailed();
        }else{
            PersonWeb person = list.get(0);
            return CommonResult.success(person);
        }

    }
    /*根据用户名查找用户*/
    @GetMapping("/listByName/{peopleWebName}")
    public Result getPersonWebListByName(@PathVariable("peopleWebName") String peopleWebName) {
        List<PersonWeb> list = webService.selectPersonWebListByName(peopleWebName);
        return getDataResult(list);
    }

    @GetMapping("/searchConnectId/{from}/{to}")
    public Result getPersonWebListByName(@PathVariable("from") String from,@PathVariable("to") String to) {
        List<ConnectWeb> connectWeb = webService.selectConnectWebId(from,to);
        return getDataResult(connectWeb);
    }

    @GetMapping("/map")
    public Result getPersonWebMap() {
        List<Record> list = webService.selectPersonWebMap();
        return getDataResult(list);
    }


    @GetMapping("/map/{from}/{to}")
    public Result getPersonWebMapFromTo(@PathVariable("from") String from,@PathVariable("to") String to) {
        List<Record> list = webService.selectPersonWebMapFromTo(from,to);
        return getDataResult(list);
    }

    @GetMapping("/map/from/{from}")
    public Result getPersonWebMapFrom(@PathVariable("from") String from) {
        List<Record> list = webService.selectPersonWebMapFrom(from);
        return getDataResult(list);
    }

    @GetMapping("/map/to/{to}")
    public Result getPersonWebMapTo(@PathVariable("to") String to) {
        List<Record> list = webService.selectPersonWebMapTo(to);
        return getDataResult(list);
    }

    @GetMapping("/list")
    public Result getPersonWebList() {
        List<PersonWeb> list = webService.selectPersonWebList();
        return getDataResult(list);
    }
//查找用户关注的人
    @GetMapping("/outNeighbors/{from}")
    public Result getOutNeighbors(@PathVariable("from") String from) {
        List<PersonWeb> list = webService.selectOutNeighbors(from);
        return getDataResult(list);
    }

    //查找关注用户的人
    @GetMapping("/inNeighbors/{to}")
    public Result getInNeighbors(@PathVariable("to") String to) {
        List<PersonWeb> list = webService.selectInNeighbors(to);
        return getDataResult(list);
    }

    /*根据分页返回List*/
    @GetMapping("/listByPage/{pageNum}/{pageSize}")
    public Result getPersonWebListByPage(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize) {
        List<PersonWeb> list = webService.selectPersonWebListByPage((pageNum-1)*pageSize,pageSize);
        return getDataResult(list);
    }

    /*返回所有people的数量*/
   /* @GetMapping("/total")
    public Result getPersonWebNum() {
        int num = webService.selectPersonWebNum();
        return getDataResult(num);
    }*/

    @GetMapping("/search/{personWebName}")
    public Result getPersonWebSearchList(@PathVariable("personWebName") String personWebName) {
        List<PersonWeb> list = webService.selectPersonWebSearchList(personWebName);
        return getDataResult(list);
    }

    @GetMapping("/search/{personWebId}/{personWebName}")
    public Result getPersonWebSearchListOther(@PathVariable("personWebName") String personWebName, @PathVariable("personWebId") String personWebId) {
        List<PersonWeb> list = webService.selectPersonWebSearchListOther(personWebName, personWebId);
        return getDataResult(list);
    }

    @GetMapping(value = "/person/{personWebId}")
    public AjaxResult getPersonWebInfo(@PathVariable("personWebId") String personWebId) {
        return AjaxResult.success(webService.selectPersonWebById(personWebId));
    }

    @PostMapping("/person")
    public AjaxResult addPersonWeb(@RequestBody PersonWeb personWeb) {
        return toAjax(webService.insertPersonWeb(personWeb));
    }

    @PutMapping("/person")
    public AjaxResult editPersonWeb(@RequestBody PersonWeb personWeb) {
        return toAjax(webService.updatePersonWeb(personWeb));
    }

    @DeleteMapping("/person/{personWebId}")
    public AjaxResult removePersonWeb(@PathVariable String personWebId) {
        try{
            int rsg = webService.deletePersonWeb(personWebId);
            return toAjax(rsg);
        } catch (Exception e){
            return AjaxResult.error(500, "此节点仍与其他节点有关联关系！");
        }
    }

    @GetMapping(value = "/connect/{connectWebId}")
    public AjaxResult getInfoConnectWeb(@PathVariable("connectWebId") String connectWebId) {
        return AjaxResult.success(webService.selectConnectWebById(connectWebId));
    }

    @PostMapping("/connect")
    public AjaxResult addConnectWeb(@RequestBody ConnectWeb connectWeb) {
        return toAjax(webService.insertConnectWeb(connectWeb));
    }

    @PutMapping("/connect")
    public AjaxResult editConnectWeb(@RequestBody ConnectWeb connectWeb) {
        return toAjax(webService.updateConnectWeb(connectWeb));
    }

    @DeleteMapping("/connect/{connectWebId}")
    public AjaxResult removeConnectWeb(@PathVariable String connectWebId) {
        return toAjax(webService.deleteConnectWeb(connectWebId));
    }
}
