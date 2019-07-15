package one.zjh.ldap.config;

import one.zjh.ldap.service.ILdapService;
import one.zjh.ldap.service.impl.LdapServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;

/**
 * Ldap配置
 *
 * @author zhoujianghui
 */
@Configuration
@EnableLdapRepositories
public class LdapConfig {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Bean
    public ILdapService ldapService() {
        return new LdapServiceImpl(ldapTemplate);
    }
}
