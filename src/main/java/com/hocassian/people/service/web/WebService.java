package com.hocassian.people.service.web;


import com.github.pagehelper.PageHelper;
import com.hocassian.people.domain.web.ConnectWeb;
import com.hocassian.people.domain.web.PersonWeb;
import com.hocassian.people.mapper.web.ConnectWebMapper;
import com.hocassian.people.mapper.web.PersonWebMapper;
import com.hocassian.people.utils.SnowflakeIdWorker;
import org.apache.ibatis.annotations.Param;
import org.neo4j.driver.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述：
 *
 * @author Hocassian
 * @date 2021-01-16 01:15
 */
@Service("webService")
public class WebService {


    @Autowired
    private PersonWebMapper personWebMapper;

    @Autowired
    private ConnectWebMapper connectWebMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    public List<Record> selectPersonWebMap() {

        return personWebMapper.selectPersonWebMap();
    }

    /*根据起始点和终止点查找关系网*/
    public List<Record> selectPersonWebMapFromTo(String from, String to) {

        return personWebMapper.selectPersonWebMapFromTo(from,to);
    }


    /**
     * 查询一度好友
     * @param from
     * @return
     */
    public List<Record> selectPersonWebMapFrom(String from){

        return personWebMapper.selectPersonWebMapFrom(from);
    }

    /**
     * 查询一度粉丝
     * @param to
     * @return
     */
    public List<Record> selectPersonWebMapTo(String to){

        return personWebMapper.selectPersonWebMapTo(to);
    }

    public List<ConnectWeb> selectConnectWebId(String from,String to){

        return connectWebMapper.selectConnectWebId(from, to);
    }

    public List<PersonWeb> selectPersonWebList() {

        return personWebMapper.selectPersonWebList();
    }

    public List<PersonWeb> selectPersonWebListByPage(@Param("skip") int skip, @Param("pageSize") int pageSize) {
        List<PersonWeb> list =personWebMapper.selectPersonWebListByPage(skip,pageSize);
        return list;
    }
   /*检验用户名、密码*/
    public List<PersonWeb> validatePerson(String username, String password){
        List<PersonWeb> list =personWebMapper.validatePerson(username,password);
        return list;
    }
    /*根据用户名寻找用户*/
    public List<PersonWeb> selectPersonWebListByName(String personWebName){
        List<PersonWeb> list =personWebMapper.selectPersonWebListByName(personWebName);
        return list;
    }

    /*查找用户关注的人*/
    public List<PersonWeb> selectOutNeighbors(String from){
        List<PersonWeb> list =personWebMapper.selectOutNeighbors(from);
        return list;
    }

    /*查找关注用户的人*/
    public List<PersonWeb> selectInNeighbors(String to){
        List<PersonWeb> list =personWebMapper.selectInNeighbors(to);
        return list;
    }



    public List<PersonWeb> selectPersonWebSearchList(String personWebName) {
//        System.out.println(personWebName);
        return personWebMapper.selectPersonWebSearchList(personWebName);
    }

    public List<PersonWeb> selectPersonWebSearchListOther(String personWebName, String personWebId) {
//        System.out.println(personWebName);
        return personWebMapper.selectPersonWebSearchListOther(personWebName, personWebId);
    }

    public PersonWeb selectPersonWebById(String personWebId) {

        return personWebMapper.selectPersonWebById(personWebId);
    }

    public int insertPersonWeb(PersonWeb personWeb) {

        personWeb.setPersonWebId(snowflakeIdWorker.nextId());
        return personWebMapper.insertPersonWeb(personWeb);
    }

    public int updatePersonWeb(PersonWeb personWeb) {

        return personWebMapper.updatePersonWeb(personWeb);
    }

    public int deletePersonWeb(String personWebId) {

        return personWebMapper.deletePersonWeb(personWebId);
    }

    public ConnectWeb selectConnectWebById(String connectWebId) {

        return connectWebMapper.selectConnectWebById(connectWebId);
    }

    public int insertConnectWeb(ConnectWeb connectWeb) {

        connectWeb.setConnectWebId(snowflakeIdWorker.nextId());
        return connectWebMapper.insertConnectWeb(connectWeb);
    }

    public int updateConnectWeb(ConnectWeb connectWeb) {

        return connectWebMapper.updateConnectWeb(connectWeb);
    }

    public int deleteConnectWeb(String connectWebId) {

        return connectWebMapper.deleteConnectWeb(connectWebId);
    }
}
