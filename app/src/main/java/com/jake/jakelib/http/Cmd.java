
package com.jake.jakelib.http;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 17:03
 */

public class Cmd {
    public static MultiRequestPackage test() {
        MultiRequestPackage requestPackage = new MultiRequestPackage("46", MyResponse.class);
        requestPackage.addParam("type", "1");
        requestPackage.addParam("itemtypeid", "138");
        return requestPackage;
    }
    public static MultiRequestPackage test1() {
        MultiRequestPackage requestPackage = new MultiRequestPackage("1074", MyResponse1.class);
        requestPackage.addParam("bannertypeid", "7");
        return requestPackage;
    }
}
