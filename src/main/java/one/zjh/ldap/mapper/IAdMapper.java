package one.zjh.ldap.mapper;

import one.zjh.ldap.bean.AdObject;
import one.zjh.ldap.common.Constants;
import one.zjh.ldap.mapper.wrapper.AdObjectWrapper;
import one.zjh.ldap.utils.LdapUtil;
import org.springframework.ldap.core.DirContextOperations;

/**
 * AD信息转换mapper
 *
 * @author zhoujianghui
 */
public interface IAdMapper {

    /**
     * 转换
     *
     * @param ctx
     * @return
     */
    default AdObjectWrapper mapper(DirContextOperations ctx) {
        String dn = ctx.getNameInNamespace();
        AdObject adObject = new AdObject();
        adObject.setName(ctx.getStringAttribute(Constants.Attributes.NAME));
        adObject.setDn(dn);
        adObject.setObjectClass(ctx.getStringAttributes(Constants.Attributes.OBJECT_CLASS));
        adObject.setObjectGUID(LdapUtil.convertObjectGUID(ctx.getStringAttribute(Constants.Attributes.OBJECT_GUID).getBytes()));
        adObject.setWhenCreated(ctx.getStringAttribute(Constants.Attributes.WHEN_CREATED));
        adObject.setWhenChanged(ctx.getStringAttribute(Constants.Attributes.WHEN_CHANGED));
        adObject.setMemberOf(ctx.getStringAttributes(Constants.Attributes.MEMBER_OF));
        AdObjectWrapper wrapper = new AdObjectWrapper(adObject);
        if (wrapper.isPerson()) {
            String userAccountControl = ctx.getStringAttribute(Constants.Attributes.USER_ACCOUNT_CONTROL);
            adObject.setUserAccountControl(userAccountControl);
            adObject.setDisabled(LdapUtil.disabled(userAccountControl));
            adObject.setDisplayName(ctx.getStringAttribute(Constants.Attributes.DISPLAY_NAME));
            adObject.setSamAccountName(ctx.getStringAttribute(Constants.Attributes.SAM_ACCOUNT_NAME));
            adObject.setMail(ctx.getStringAttribute(Constants.Attributes.MAIL));
            adObject.setMobile(LdapUtil.getPhone(ctx));
        }
        return wrapper;
    }
}
