package one.zjh.ldap.service;

import one.zjh.ldap.bean.AdObject;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.LdapQuery;

import javax.naming.Name;
import java.util.List;

/**
 * Ldap操作定义
 * 该接口使用ldapTemplate操作
 * 主要实现分页操作
 *
 * @author zhoujianghui
 */
public interface ILdapService {

    /**
     * 通过dn名查询实体
     *
     * @param dn     DN名
     * @param mapper 映射mapper
     * @param <T>    返回实体类型
     * @return
     */
    <T> T getObjectByDn(String dn, AbstractContextMapper<T> mapper);

    /**
     * 默认mapper
     * 通过dn名查询实体
     *
     * @param dn
     * @return
     */
    AdObject getObjectByDn(String dn);

    /**
     * 验证用户信息
     *
     * @param userDn      用户DN
     * @param credentials 用户凭证（密码）
     * @return
     */
    boolean authenticate(String userDn, String credentials);

    /**
     * 验证用户信息
     *
     * @param guid        用户GUID
     * @param credentials 用户凭证（密码）
     * @return
     */
    boolean authenticateByGuid(String guid, String credentials);

    /**
     * 通过objectGUID属性获取AD对象
     *
     * @param objectGUID
     * @return
     */
    AdObject getObjectByGUID(String objectGUID);

    /**
     * 通过objectGUID属性获取AD对象
     *
     * @param objectGUID
     * @return
     */
    <T> T getObjectByGUID(String objectGUID, AbstractContextMapper<T> mapper);

    /**
     * 使用分页查询所有OU
     *
     * @return
     */
    List<AdObject> getAllOusPaged();

    /**
     * 使用分页查询所有OU
     *
     * @param mapper
     * @return
     */
    <T> List<T> getAllOusPaged(AbstractContextMapper<T> mapper);

    /**
     * 使用分页查询所有用户
     *
     * @return
     */
    List<AdObject> getAllPersonPaged();

    /**
     * 使用分页查询所有用户
     *
     * @param mapper
     * @return
     */
    <T> List<T> getAllPersonPaged(AbstractContextMapper<T> mapper);

    /**
     * 使用分页查询所有域用户组
     *
     * @return
     */
    List<AdObject> getAllDomainUserGroupsPaged();

    /**
     * 使用分页查询指定dn下所有域用户组
     *
     * @param ouDnWithoutDc
     * @return
     */
    List<AdObject> getAllDomainUserGroupsPaged(String ouDnWithoutDc);

    /**
     * 使用分页查询指定dn下所有域用户组
     *
     * @param ouDnWithoutDc
     * @param mapper
     * @return
     */
    <T> List<T> getAllDomainUserGroupsPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper);

    /**
     * 使用分页查询所有域用户组
     *
     * @param mapper
     * @return
     */
    <T> List<T> getAllDomainUserGroupsPaged(AbstractContextMapper<T> mapper);

    /**
     * 使用分页查询所有指定类型对象
     *
     * @param filter
     * @param mapper
     * @return
     */
    <T> List<T> getAllPaged(String filter, AbstractContextMapper<T> mapper);

    /**
     * 使用分页查询指定位置下所有指定类型对象
     *
     * @param ouDnWithoutDc
     * @param filter
     * @param mapper
     * @return
     */
    <T> List<T> getAllPaged(String ouDnWithoutDc, String filter, AbstractContextMapper<T> mapper);

    /**
     * 获取AD域根对象（DC对象，即域名对象）的DN
     *
     * @return
     */
    String getRootDn();

    /**
     * 通过名称搜索
     *
     * @param name
     * @param mapper
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T lookup(Name name, AbstractContextMapper<T> mapper) throws Exception;

    /**
     * 搜索
     *
     * @param ldapQuery
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> search(LdapQuery ldapQuery, AbstractContextMapper<T> mapper) throws Exception;

    /**
     * 分页查询指定OU下一个层级的所有OU
     *
     * @param ouDnWithoutDc
     * @return
     */
    List<AdObject> getOneLevelOusByOuPaged(String ouDnWithoutDc);

    /**
     * 分页查询指定OU下一个层级的所有OU
     *
     * @param ouDnWithoutDc
     * @param mapper
     * @return
     */
    <T> List<T> getOneLevelOusByOuPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper);

    /**
     * 分页查询指定OU下一个层级的所有用户信息
     *
     * @param ouDnWithoutDc
     * @return
     */
    List<AdObject> getOneLevelPersonByOuPaged(String ouDnWithoutDc);

    /**
     * 分页查询指定OU下一个层级的所有用户信息
     *
     * @param ouDnWithoutDc
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> getOneLevelPersonByOuPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper);

    /**
     * 分页查询指定OU下一个层级的所有域用户组信息
     *
     * @param ouDnWithoutDc
     * @return
     */
    List<AdObject> getOneLevelDomainUserGroupsByOuPaged(String ouDnWithoutDc);

    /**
     * 分页查询指定OU下一个层级的所有域用户组信息
     *
     * @param ouDnWithoutDc
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> getOneLevelDomainUserGroupsByOuPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper);

    /**
     * 分页查询指定类别对象下一个层级的同一类型对象
     *
     * @param ouDnWithoutDc
     * @param filter
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> getOneLevelPaged(String ouDnWithoutDc, String filter, AbstractContextMapper<T> mapper);
}