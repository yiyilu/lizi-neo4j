package com.hocassian.people.mapper.web;

import com.hocassian.people.domain.web.PersonWeb;
import org.apache.ibatis.annotations.Param;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hocassian
 */
@Repository
public interface PersonWebMapper {
    
    /**
     * 查询全部
     *
     * @return Record格式的列表
     */
    List<Record> selectPersonWebMap();

    /**selectPersonWebMapFromTo
     * 查询从from 到 to的 路径
     * @param from
     * @param to
     * @return
     */
     List<Record> selectPersonWebMapFromTo(String from, String to);

    /**
     * 查询一度好友
     * @param from
     * @return
     */
     List<Record> selectPersonWebMapFrom(String from);

    /**
     * 查询一度粉丝
     * @param to
     * @return
     */
    List<Record> selectPersonWebMapTo(String to);

    /**
     * 查询用户关注的人
     * @param from
     * @return
     */
    List<PersonWeb> selectOutNeighbors(String from);

    /**
     * 查询关注用户的人
     * @param to
     * @return
     */
    List<PersonWeb> selectInNeighbors(String to);

    /**
     * 查询全部
     *
     * @return List格式的列表
     */
    List<PersonWeb> selectPersonWebList();

    /**
     * 分页查询List
     * @param skip
     * @param pageSize
     * @return
     */
    List<PersonWeb> selectPersonWebListByPage(@Param("skip") int skip, @Param("pageSize") int pageSize);


    /**
     * 检验用户名和密码
     * @param username
     * @param password
     * @return
     */
     List<PersonWeb> validatePerson(String username, String password);

    /**
     * 根据用户名寻找用户
     * @param peopleWebName
     * @return
     */
     List<PersonWeb> selectPersonWebListByName(String peopleWebName);
    /**
     * 查询全部（搜索使用）
     *
     * @return List格式的列表（搜索使用）
     */
    List<PersonWeb> selectPersonWebSearchList(String personWebName);

    /**
     * 查询全部（搜索使用）
     *
     * @return List格式的列表（搜索使用）
     */
    List<PersonWeb> selectPersonWebSearchListOther(String personWebName, String personWebId);

    /**
     * 查询单个节点信息
     *
     * @param personWebId 节点id
     * @return PersonWeb实体信息
     */
    PersonWeb selectPersonWebById(String personWebId);

    /**
     * 插入单个节点信息
     *
     * @param personWeb 节点实体
     * @return 插入个数
     */
    int insertPersonWeb(PersonWeb personWeb);

    /**
     * 修改单个节点信息
     *
     * @param personWeb 节点实体
     * @return 修改个数
     */
    int updatePersonWeb(PersonWeb personWeb);

    /**
     * 删除单个节点
     *
     * @param personWebId 节点实体
     * @return 删除个数
     */
    int deletePersonWeb(String personWebId);
    
}
