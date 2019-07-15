package one.zjh.ldap.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * ad对象
 *
 * @author zhoujianghui
 */
@Getter
@Setter
@ToString
public class AdObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ad信息的属性
     */
    private String dn;
    private String name;
    private String displayName;
    private String samAccountName;
    private String objectGUID;
    private String mail;
    private String mobile;
    private String[] objectClass;
    private String[] memberOf;
    private String whenCreated;
    private String whenChanged;
    private String userAccountControl;
    private Boolean disabled;

    /**
     * 自定义属性
     */
    /**
     * 父对象GUID
     */
    private String parentObjectGUID;
}
