package one.zjh.ldap.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.zjh.ldap.bean.AdObject;
import one.zjh.ldap.common.Constants;
import one.zjh.ldap.mapper.AdObjectContextMapper;
import one.zjh.ldap.service.ILdapService;
import one.zjh.ldap.utils.LdapUtil;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.SingleContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.util.StringUtils;

import javax.naming.Name;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.LinkedList;
import java.util.List;

import static one.zjh.ldap.common.Constants.*;


/**
 * 实现
 *
 * @author zhoujianghui
 */
@Slf4j
public class LdapServiceImpl implements ILdapService {

    private final LdapTemplate ldapTemplate;

    public LdapServiceImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    private LdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    /**
     * 分页查询AD
     *
     * @param base   查询基础名称
     * @param filter 过滤器
     * @param scope  搜索范围
     * @param mapper 映射实体mapper
     * @param <T>    实体类型
     * @return 结果列表
     */
    private <T> List<T> searchPaged(final LdapName base, final String filter, int scope, final AbstractContextMapper<T> mapper) throws Exception {

        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(scope);

        final PagedResultsDirContextProcessor processor =
                new PagedResultsDirContextProcessor(Constants.LDAP_PAGE_SIZE);

        ContextSource source = getLdapTemplate().getContextSource();

        return SingleContextSource.doWithSingleContext(
                source, operations -> {
                    List<T> result = new LinkedList<>();
                    do {
                        List<T> oneResult = operations.search(base, filter, searchControls, mapper, processor);
                        result.addAll(oneResult);
                    } while (processor.hasMore());
                    return result;
                }, true, true, true);
    }


    @Override
    public <T> T getObjectByDn(String dn, AbstractContextMapper<T> mapper) {
        try {
            return getLdapTemplate().searchForObject(LdapQueryBuilder.query()
                    .filter(new EqualsFilter("distinguishedName", dn)), mapper);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("通过dn获取AD对象，没有找到该对象，dn={}", dn);
            return null;
        }
    }

    @Override
    public AdObject getObjectByDn(String dn) {
        return getObjectByDn(dn, new AdObjectContextMapper());
    }

    @Override
    public boolean authenticate(String userDn, String credentials) {
        DirContext ctx = null;
        try {
            ctx = getLdapTemplate().getContextSource().getContext(userDn, credentials);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("用户认证失败，userDn={}，credentials={}", userDn, credentials);
            return false;
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }

    @Override
    public boolean authenticateByGuid(String guid, String credentials) {
        try {
            AdObject adObject = getObjectByGUID(guid);
            if (adObject == null) {
                return false;
            }
            return authenticate(adObject.getDn(), credentials);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("用户认证失败，guid={}，credentials={}", guid, credentials);
            return false;
        }
    }

    @Override
    public AdObject getObjectByGUID(String objectGUID) {
        return getObjectByGUID(objectGUID, new AdObjectContextMapper());
    }

    @Override
    public <T> T getObjectByGUID(String objectGUID, AbstractContextMapper<T> mapper) {
        try {
            String guid = LdapUtil.delimitObjectGUID(objectGUID);
            return getLdapTemplate().searchForObject(LdapQueryBuilder.query()
                            .filter(String.format("(objectGUID=%s)", guid)),
                    mapper);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("通过objectGUID获取AD对象，没有找到该对象，objectGUID={}", objectGUID);
            return null;
        }
    }

    @Override
    public List<AdObject> getAllOusPaged() {
        return getAllOusPaged(new AdObjectContextMapper());
    }

    @Override
    public <T> List<T> getAllOusPaged(AbstractContextMapper<T> mapper) {
        return getAllPaged(FILTER_OU, mapper);
    }

    @Override
    public List<AdObject> getAllPersonPaged() {
        return getAllPersonPaged(new AdObjectContextMapper());
    }

    @Override
    public <T> List<T> getAllPersonPaged(AbstractContextMapper<T> mapper) {
        return getAllPaged(FILTER_USER, mapper);
    }

    @Override
    public List<AdObject> getAllDomainUserGroupsPaged() {
        return getAllDomainUserGroupsPaged(new AdObjectContextMapper());
    }

    @Override
    public List<AdObject> getAllDomainUserGroupsPaged(String ouDnWithoutDc) {
        return getAllDomainUserGroupsPaged(ouDnWithoutDc, new AdObjectContextMapper());
    }

    @Override
    public <T> List<T> getAllDomainUserGroupsPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper) {
        return getAllPaged(ouDnWithoutDc, FILTER_GROUP, mapper);
    }

    @Override
    public <T> List<T> getAllDomainUserGroupsPaged(AbstractContextMapper<T> mapper) {
        return getAllPaged(FILTER_GROUP, mapper);
    }

    @Override
    public <T> List<T> getAllPaged(String filter, AbstractContextMapper<T> mapper) {
        try {
            return searchPaged(LdapUtils.emptyLdapName(), filter, SearchControls.SUBTREE_SCOPE, mapper);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("获取所有对象失败");
            return null;
        }
    }

    @Override
    public <T> List<T> getAllPaged(String ouDnWithoutDc, String filter, AbstractContextMapper<T> mapper) {
        try {
            return searchPaged(LdapUtils.newLdapName(ouDnWithoutDc), filter, SearchControls.SUBTREE_SCOPE, mapper);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("获取所有对象失败");
            return null;
        }
    }

    @Override
    public String getRootDn() {
        try {
            return getLdapTemplate().getContextSource().getReadOnlyContext().getNameInNamespace();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("获取根对象DN失败");
            return null;
        }
    }

    @Override
    public <T> T lookup(Name name, AbstractContextMapper<T> mapper) throws Exception {
        return getLdapTemplate().lookup(name, mapper);
    }

    @Override
    public <T> List<T> search(LdapQuery ldapQuery, AbstractContextMapper<T> mapper) throws Exception {
        return getLdapTemplate().search(ldapQuery, mapper);
    }

    @Override
    public List<AdObject> getOneLevelOusByOuPaged(String ouDnWithoutDc) {
        return getOneLevelOusByOuPaged(ouDnWithoutDc, new AdObjectContextMapper());
    }

    @Override
    public <T> List<T> getOneLevelOusByOuPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper) {
        return getOneLevelPaged(ouDnWithoutDc, FILTER_OU, mapper);
    }


    @Override
    public List<AdObject> getOneLevelPersonByOuPaged(String ouDnWithoutDc) {
        return getOneLevelPersonByOuPaged(ouDnWithoutDc, new AdObjectContextMapper());
    }

    @Override
    public <T> List<T> getOneLevelPersonByOuPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper) {
        return getOneLevelPaged(ouDnWithoutDc, FILTER_USER, mapper);
    }


    @Override
    public List<AdObject> getOneLevelDomainUserGroupsByOuPaged(String ouDnWithoutDc) {
        return getOneLevelDomainUserGroupsByOuPaged(ouDnWithoutDc, new AdObjectContextMapper());
    }

    @Override
    public <T> List<T> getOneLevelDomainUserGroupsByOuPaged(String ouDnWithoutDc, AbstractContextMapper<T> mapper) {
        return getOneLevelPaged(ouDnWithoutDc, FILTER_GROUP, mapper);
    }

    @Override
    public <T> List<T> getOneLevelPaged(String ouDnWithoutDc, String filter, AbstractContextMapper<T> mapper) {
        try {
            return searchPaged(getBaseLdapName(ouDnWithoutDc), filter, SearchControls.ONELEVEL_SCOPE, mapper);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("获取指定OU下一个层级的所有{}对象失败，ouDnWithoutDc={}", filter, ouDnWithoutDc);
            return null;
        }
    }

    private LdapName getBaseLdapName(String dn) {
        if (StringUtils.isEmpty(dn)) {
            return LdapUtils.emptyLdapName();
        }
        return LdapUtils.newLdapName(dn);
    }
}
