package one.zjh.ldap.mapper;

import one.zjh.ldap.bean.AdObject;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;

/**
 * AD对象mapper
 * 用来将AD实体转换为自定义bean
 *
 * @author zhoujianghui
 */
public class AdObjectContextMapper extends AbstractContextMapper<AdObject> implements IAdMapper {

    @Override
    protected AdObject doMapFromContext(DirContextOperations ctx) {
        return mapper(ctx).getAdObject();
    }
}
