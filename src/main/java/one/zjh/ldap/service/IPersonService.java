package one.zjh.ldap.service;

import one.zjh.ldap.entity.Person;
import one.zjh.ldap.repository.IPagingRepository;

import java.util.Optional;

/**
 * @author zhoujianghui
 */
public interface IPersonService extends IPagingRepository<Person> {

    /**
     * 通过DN查询
     *
     * @param distinguishedName dn
     * @return
     */
    Optional<Person> findByDistinguishedName(String distinguishedName);
}
