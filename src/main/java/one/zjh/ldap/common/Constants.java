package one.zjh.ldap.common;

/**
 * 常量定义
 *
 * @author zhoujianghui
 */
public interface Constants {

    /**
     * 分页大小
     */
    int LDAP_PAGE_SIZE = 998;

    /**
     * 查询OU
     */
    String FILTER_OU = "(objectclass=organizationalUnit)";
    /**
     * 查询用户
     */
    String FILTER_USER = "(objectclass=person)";
    /**
     * 查询域用户组
     */
    String FILTER_GROUP = "(objectclass=group)";

    /**
     * 可查询的Attribute
     */
    interface Attributes {
        String NAME = "name";
        String OBJECT_CLASS = "objectClass";
        String OBJECT_GUID = "objectGUID";
        String MAIL = "mail";
        String MOBILE = "mobile";
        String TELEPHONE = "telephoneNumber";
        String WHEN_CREATED = "whenCreated";
        String WHEN_CHANGED = "whenChanged";
        String MEMBER_OF = "memberof";
        String DISPLAY_NAME = "displayName";
        String SAM_ACCOUNT_NAME = "samAccountName";
        String USER_ACCOUNT_CONTROL = "userAccountControl";
    }
}
