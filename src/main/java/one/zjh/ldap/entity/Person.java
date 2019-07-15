package one.zjh.ldap.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.Date;
import java.util.List;

/**
 * 用户
 *
 * @author zhoujianghui
 */
@Data
@Entry(objectClasses = "person")
@ToString
public final class Person {

    @Id
    private Name name;
    private String distinguishedName;
    private String samAccountName;
    private String cn;
    private String displayName;
    private String objectGUID;
    private String mail;
    private String mobile;
    private String telephoneNumber;
    private String whenCreated;
    private String whenChanged;
    private Integer userAccountControl;
    private List<String> objectClass;
    private List<String> memberOf;

}
