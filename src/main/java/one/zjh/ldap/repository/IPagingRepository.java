package one.zjh.ldap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.naming.Name;

/**
 * @author zhoujianghui
 */
@NoRepositoryBean
public interface IPagingRepository<T> extends CrudRepository<T, Name> {

}
