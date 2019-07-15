package one.zjh.ldap.utils;

import one.zjh.ldap.common.Constants;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.util.StringUtils;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ldap工具
 *
 * @author zhoujianghui
 */
public class LdapUtil {
    public static final String OU_NAME = "ou";
    public static final String CN_NAME = "cn";
    public static final String DC_NAME = "dc";

    public static final String GENERALIZED_TIME_STYLE = "yyyyMMddHHmmss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(GENERALIZED_TIME_STYLE);
    /**
     * 类型缓存 缓存AD类型Integer值
     */
    public static final ConcurrentHashMap<String, Integer> INTEGER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    /**
     * GMT时间后缀
     */
    private static final String SUFFIX_NO_DIFFERENTIAL = "Z";

    /**
     * 获取截掉DC字符串后的DN字符串
     *
     * @param dn
     */
    public static String getDnWithoutDc(String dn) {
        if (dn == null) {
            return null;
        }

        try {
            LdapName ldapName = new LdapName(dn);
            List<Rdn> rdnList = new ArrayList<>();
            for (Rdn rdn : ldapName.getRdns()) {
                if (DC_NAME.equalsIgnoreCase(rdn.getType())) {
                    continue;
                }
                rdnList.add(rdn);
            }
            return new LdapName(rdnList).toString();
        } catch (InvalidNameException ex) {
            return null;
        }
    }

    /**
     * 获取域名
     *
     * @param dn
     * @return abc.com
     */
    public static String getDomain(String dn) {
        if (dn == null) {
            return null;
        }

        try {
            LdapName ldapName = new LdapName(dn);
            List<String> dcList = new ArrayList<String>();
            for (Rdn rdn : ldapName.getRdns()) {
                if (DC_NAME.equalsIgnoreCase(rdn.getType())) {
                    dcList.add(rdn.getValue().toString());
                }
            }
            Collections.reverse(dcList);
            return String.join(".", dcList);
        } catch (InvalidNameException ex) {
            return null;
        }
    }

    /**
     * 获取不带DC字符串的OU字符串
     *
     * @param dn
     * @return OU=ou1,OU=ou2
     */
    public static String getOuDnWithoutDc(String dn) {
        if (dn == null) {
            return null;
        }

        try {
            LdapName ldapName = new LdapName(dn);
            List<Rdn> rdnList = new ArrayList<>();
            for (Rdn rdn : ldapName.getRdns()) {
                if (OU_NAME.equalsIgnoreCase(rdn.getType())) {
                    rdnList.add(rdn);
                }
            }
            return new LdapName(rdnList).toString();
        } catch (InvalidNameException ex) {
            return null;
        }
    }

    /**
     * 获取父DN字符串
     *
     * @param dn
     */
    public static String getParentDn(String dn) {
        if (dn == null) {
            return null;
        }

        try {
            LdapName ldapName = new LdapName(dn);
            ldapName.remove(ldapName.size() - 1);
            return ldapName.toString();
        } catch (InvalidNameException | ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * 获取父DN字符串
     *
     * @param dn
     */
    public static String getStringValue(String dn, String key) {
        if (dn == null) {
            return null;
        }

        try {
            LdapName ldapName = new LdapName(dn);
            return LdapUtils.getStringValue(ldapName, key);
        } catch (InvalidNameException ex) {
            return null;
        }
    }

    /**
     * 通过域名获取DC
     *
     * @param domain
     */
    public static String getDcByDomain(String domain) {
        if (domain == null) {
            return null;
        }

        String[] domains = domain.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (String s : domains) {
            builder.append("DC=");
            builder.append(s);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * 将二进制的objectGUID转换为字符串
     *
     * @param objectGUID
     */
    public static String convertObjectGUID(byte[] objectGUID) {
        StringBuilder sb = new StringBuilder();
        sb.append(addLeadingZero((int) objectGUID[3] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[2] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[1] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[0] & 0xFF));
        sb.append("-");
        sb.append(addLeadingZero((int) objectGUID[5] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[4] & 0xFF));
        sb.append("-");
        sb.append(addLeadingZero((int) objectGUID[7] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[6] & 0xFF));
        sb.append("-");
        sb.append(addLeadingZero((int) objectGUID[8] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[9] & 0xFF));
        sb.append("-");
        sb.append(addLeadingZero((int) objectGUID[10] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[11] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[12] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[13] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[14] & 0xFF));
        sb.append(addLeadingZero((int) objectGUID[15] & 0xFF));
        return sb.toString();
    }

    private static String addLeadingZero(int k) {
        return (k <= 0xF) ? "0" + Integer.toHexString(k) : Integer.toHexString(k);
    }

    /**
     * 将字符串的objectGUID进行分隔处理，用于查询
     *
     * @param objectGUID
     */
    public static String delimitObjectGUID(String objectGUID) {
        StringTokenizer st = new StringTokenizer(objectGUID, "-");
        String[] s = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            s[i] = st.nextToken();
        }

        String delimiter = "\\";

        StringBuilder sb = new StringBuilder();
        sb.append(delimiter).append(s[0], 6, 8);
        sb.append(delimiter).append(s[0], 4, 6);
        sb.append(delimiter).append(s[0], 2, 4);
        sb.append(delimiter).append(s[0], 0, 2);

        sb.append(delimiter).append(s[1], 2, 4);
        sb.append(delimiter).append(s[1], 0, 2);

        sb.append(delimiter).append(s[2], 2, 4);
        sb.append(delimiter).append(s[2], 0, 2);

        sb.append(delimiter).append(s[3], 0, 2);
        sb.append(delimiter).append(s[3], 2, 4);

        sb.append(delimiter).append(s[4], 0, 2);
        sb.append(delimiter).append(s[4], 2, 4);
        sb.append(delimiter).append(s[4], 4, 6);
        sb.append(delimiter).append(s[4], 6, 8);
        sb.append(delimiter).append(s[4], 8, 10);
        sb.append(delimiter).append(s[4], 10, 12);

        return sb.toString();
    }

    /**
     * 判断一个DN是否为另一个DN的祖先节点
     *
     * @param ancestorDn
     * @param childDn
     */
    public static boolean isAncestor(String ancestorDn, String childDn) {
        if (StringUtils.isEmpty(ancestorDn)) {
            return false;
        }
        if (StringUtils.isEmpty(childDn)) {
            return false;
        }

        return childDn.endsWith(ancestorDn);
    }

    /**
     * 获取AD时间(Generalized-Time)
     * 详情参考
     * <p>https://docs.microsoft.com/zh-cn/windows/desktop/ADSchema/s-string-generalized-time</p>
     *
     * @param generalizedTime
     * @return
     */
    public static Date getUTCTime(String generalizedTime) {
        if (generalizedTime == null) {
            return null;
        }
        String[] times = generalizedTime.split("\\.");
        LocalDateTime dateTime = LocalDateTime.parse(times[0], FORMATTER);
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }

    /**
     * 判断AD是否已被禁用
     *
     * @param userAccountControl 控制标志
     * @return
     */
    public static Boolean disabled(String userAccountControl) {
        Integer control = null;
        try {
            if (!StringUtils.isEmpty(userAccountControl)) {
                if (INTEGER_CONCURRENT_HASH_MAP.containsKey(userAccountControl)) {
                    control = INTEGER_CONCURRENT_HASH_MAP.get(userAccountControl);
                } else {
                    control = Integer.parseInt(userAccountControl);
                    INTEGER_CONCURRENT_HASH_MAP.putIfAbsent(userAccountControl, control);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return control == null ? null : (control & 2) > 0;
    }

    /**
     * 获取电话号码
     * telephoneNumber优先
     *
     * @param ctx
     * @return
     */
    public static String getPhone(DirContextOperations ctx) {
        String phone = ctx.getStringAttribute(Constants.Attributes.TELEPHONE);
        if (StringUtils.isEmpty(phone)) {
            phone = ctx.getStringAttribute(Constants.Attributes.MOBILE);
        }
        return phone;
    }
}
