package one.zjh.ldap.mapper;

import one.zjh.ldap.mapper.wrapper.AdObjectWrapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;

/**
 * ad信息转换
 *
 * @author zhoujianghui
 */
public class AdObjectWrapperMapper extends AbstractContextMapper<AdObjectWrapper> implements IAdMapper {

    @Override
    protected AdObjectWrapper doMapFromContext(DirContextOperations ctx) {
        return mapper(ctx);
    }
}