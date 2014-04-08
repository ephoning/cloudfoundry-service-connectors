package org.cloudfoundry.springframework.cloud.service.relational;

/**
 * Created with IntelliJ IDEA.
 * User: honine
 * Date: 10/14/13
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

import org.springframework.cloud.service.relational.DataSourceCreator;

public class HawqDataSourceCreator extends DataSourceCreator<HawqServiceInfo> {

    private static final String VALIDATION_QUERY = "SELECT 1";

    @Override
    public String getDriverClassName() {
        System.out.println("HawqDataSourceCreator.getDriverClassName...");
        return "org.postgresql.Driver";
    }

    @Override
    public String getValidationQuery() {
        System.out.println("HawqDataSourceCreator.getValidationQuery...");
        return VALIDATION_QUERY;
    }

}
