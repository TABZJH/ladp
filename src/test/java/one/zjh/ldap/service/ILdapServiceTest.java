package one.zjh.ldap.service;

import one.zjh.ldap.bean.AdObject;
import one.zjh.ldap.mapper.AdObjectContextMapper;
import one.zjh.ldap.utils.LdapUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ILdapServiceTest {

    private static final String OBJECT_CLASS = "objectClass";
    private static final String OU = "organizationalUnit";

    @Autowired
    private ILdapService ldapService;

    @Test
    public void getObjectByDn() {
        AdObject adObject = ldapService.getObjectByDn("CN=aaaa,OU=test,DC=appdev,DC=centerm,DC=com");
        System.out.println(adObject);
    }

    @Test
    public void authenticate() {
        System.out.println(ldapService.authenticate("wang100", "Centerm123!@#"));
    }

    @Test
    public void getObjectByGUID() {
        System.out.println(ldapService.getObjectByGUID("89cd38b8-0c0e-40a8-a014-8057f46ebd4c"));
    }

    @Test
    public void getAllOusPaged() {
        ldapService.getAllOusPaged().forEach(System.out::println);
    }

    @Test
    public void getRootDn() {
        System.out.println(ldapService.getRootDn());
    }

    @Test
    public void getOneLevelOusByOuPaged() {
        ldapService.getOneLevelOusByOuPaged(LdapUtil.getOuDnWithoutDc("dn=OU=test,DC=appdev,DC=centerm,DC=com, name=test")).forEach(System.out::println);
    }

    @Test
    public void getOneLevelPersonByOuPaged() {
        ldapService.getOneLevelPersonByOuPaged(LdapUtil.getOuDnWithoutDc("OU=test,DC=appdev,DC=centerm,DC=com")).forEach(System.out::println);
    }

    @Test
    public void getOneLevelDomainGroupsByOuPaged() {
        ldapService.getOneLevelDomainUserGroupsByOuPaged(LdapUtil.getOuDnWithoutDc("CN=zjh,OU=DEV,DC=appdev,DC=centerm,DC=com")).forEach(System.out::println);
    }

    @Test
    public void getAllPersonPaged() {
        ldapService.getAllPersonPaged().forEach(System.out::println);
    }

    @Test
    public void getAllDomainGroupsPaged() {
        ldapService.getAllDomainUserGroupsPaged(LdapUtil.getOuDnWithoutDc("OU=test,DC=appdev,DC=centerm,DC=com")).forEach(System.out::println);
    }

    @Test
    public void lookup() throws Exception {
        List<AdObject> search = ldapService.search(
                LdapQueryBuilder.query().where("name").is("省0").and("objectClass").is("organizationalPerson"),
                new AdObjectContextMapper());
        search.forEach(System.out::println);
    }

    @Test
    public void search() throws Exception {
        LdapQuery ldapQuery = LdapQueryBuilder.query().countLimit(2).timeLimit(5).where("name").whitespaceWildcardsLike("t").and(OBJECT_CLASS).is(OU);
        List<AdObject> adObjects = ldapService.search(ldapQuery, new AdObjectContextMapper());
        adObjects.forEach(System.out::println);
    }
}