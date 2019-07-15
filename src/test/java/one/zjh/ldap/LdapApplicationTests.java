package one.zjh.ldap;

import one.zjh.ldap.entity.Person;
import one.zjh.ldap.service.IPersonService;
import one.zjh.ldap.utils.LdapUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LdapApplicationTests {

    @Autowired
    private IPersonService personService;

    @Test
    public void findByCn() {
        Optional<Person> person = personService.findByDistinguishedName("CN=aaaa,OU=test,DC=appdev,DC=centerm,DC=com");
        person.ifPresent(value -> System.out.println(LdapUtil.convertObjectGUID(value.getObjectGUID().getBytes())));
        System.out.println(person.orElse(null));
    }

    @Test
    public void findAll() throws InvalidNameException {
        System.out.println(personService.existsById(new LdapName("CN=aaaa,OU=test,DC=appdev,DC=centerm,DC=com")));
    }

}
