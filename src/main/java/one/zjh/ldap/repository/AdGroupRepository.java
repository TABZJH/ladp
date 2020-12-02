package one.zjh.ldap.repository;

import one.zjh.ldap.entity.AdUser;
import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

/**
 * @author zhoujianghui
 */
public interface AdUserRepository extends CrudRepository<AdUser, Name> {
}
