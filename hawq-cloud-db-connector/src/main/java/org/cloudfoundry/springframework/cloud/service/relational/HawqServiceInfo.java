package org.cloudfoundry.springframework.cloud.service.relational;

/**
 * Created with IntelliJ IDEA.
 * User: honine
 * Date: 10/14/13
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

@ServiceLabel("hawq")
public class HawqServiceInfo extends RelationalServiceInfo {

    public HawqServiceInfo(java.lang.String id, java.lang.String uri) {
        super(id, uri, "oracle");
        System.out.println(String.format("HawqServiceInfo.<init> with %s / %s", id, uri));
        this.jdbcUrl =
                String.format("jdbc:postgresql://%s:%d/%s?user=%s&password=%s",
                        getHost(), getPort(), getPath(), getUserName(), getPassword());
        System.out.println(String.format("HawqServiceInfo.<init> jdbcUrl is now: %s", jdbcUrl));
    }
}





