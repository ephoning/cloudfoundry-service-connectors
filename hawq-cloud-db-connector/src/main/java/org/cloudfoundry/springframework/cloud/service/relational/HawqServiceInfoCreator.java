package org.cloudfoundry.springframework.cloud.service.relational;

/**
 * Created with IntelliJ IDEA.
 * User: honine
 * Date: 10/14/13
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
   */

import org.springframework.cloud.cloudfoundry.RelationalServiceInfoCreator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HawqServiceInfoCreator extends RelationalServiceInfoCreator<HawqServiceInfo> {

    private static final String SERVICE_ID = "hawq";

    private static final Set<String> ACCEPTABLE_CREDS_KEYS = new HashSet<String>();
    static {
        ACCEPTABLE_CREDS_KEYS.add("name");
    }

    public HawqServiceInfoCreator() {
        super(SERVICE_ID);
        System.out.println("HawqServiceInfoCreator.<init>...");
    }

    @Override
    public HawqServiceInfo createServiceInfo(Map<String,Object> serviceData) {
        System.out.println(String.format("HawqServiceInfoCreator.createServiceInfo with %s", serviceData));
        return super.createServiceInfo(serviceData);
    }


    @Override
    public HawqServiceInfo createServiceInfo(String id, String uri) {
        System.out.println(String.format("HawqServiceInfoCreator.createServiceInfo with %s / %s", id, uri));
        return new HawqServiceInfo(id, uri);
    }

    /**
     * expected iri format:  oracle:username:password@host:port
     *
     * @param serviceData
     * @return
     */
    @Override
    public boolean accept(Map<String,Object> serviceData) {
        System.out.println(String.format("HawqServiceInfoCreator.accept with %s", serviceData));
        boolean acceptable = false;
        for(String key : serviceData.keySet()) {
            if (ACCEPTABLE_CREDS_KEYS.contains(key) && ((String)serviceData.get(key)).contains(SERVICE_ID)) {
                acceptable = true;
            }
        }
        System.out.println(String.format("HawqServiceInfoCreator.accept result %b", acceptable));
        return acceptable;
    }

    public boolean acceptVARIANT(Map<String,Object> serviceData) {
        System.out.println(String.format("HawqServiceInfoCreator.accept with %s", serviceData));
        boolean acceptable = false;
        @SuppressWarnings("unchecked")
        Map<String,String> creds = (Map<String,String>)serviceData.get("credentials");
        for(String key : creds.keySet()) {
            if (ACCEPTABLE_CREDS_KEYS.contains(key) && creds.get(key).contains(SERVICE_ID)) {
                acceptable = true;
            }
        }
        System.out.println(String.format("HawqServiceInfoCreator.accept result %b", acceptable));
        return acceptable;
    }

}
