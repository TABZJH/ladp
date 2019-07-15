package one.zjh.ldap.mapper.wrapper;

import lombok.Getter;
import lombok.Setter;
import one.zjh.ldap.bean.AdObject;
import one.zjh.ldap.utils.LdapUtil;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * AD对象包装
 *
 * @author zhoujianghui
 */
@Getter
@Setter
public class AdObjectWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private AdObject adObject;

    private String domain;
    private String dnWithoutDc;
    private List objectClass;

    public AdObjectWrapper(AdObject adObject) {
        if (adObject == null) {
            throw new IllegalArgumentException("adObject is null");
        }

        this.adObject = adObject;
        this.domain = LdapUtil.getDomain(adObject.getDn());
        this.dnWithoutDc = LdapUtil.getDnWithoutDc(adObject.getDn());
        if (adObject.getObjectClass() != null) {
            this.objectClass = CollectionUtils.arrayToList(adObject.getObjectClass());
        }
    }

    /**
     * 是否为OU对象
     */
    public boolean isOrganizationalUnit() {
        return isObjectOf("organizationalUnit");
    }

    /**
     * 是否为OU下的个人对象
     */
    public boolean isOrganizationalPerson() {
        return isObjectOf("organizationalPerson");
    }

    /**
     * 是否为person对象
     */
    public boolean isPerson() {
        return isObjectOf("person");
    }

    /**
     * 是否为指定对象
     */
    public boolean isObjectOf(String objectClass) {
        if (this.objectClass == null || this.objectClass.size() < 1) {
            return false;
        }
        return this.objectClass.contains(objectClass);
    }
}
