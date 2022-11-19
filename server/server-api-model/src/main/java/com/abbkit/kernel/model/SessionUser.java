/**
 * 
 */
package com.abbkit.kernel.model;

import java.util.Map;

/**
 * @author J
 *
 */
public interface SessionUser extends Model {

    String getId();

    void setId(String id) ;

    String getUserName();

    void setUserName(String userName);

    String getNatureName();

    void setNatureName(String natureName);

    String getPassword();

    void setPassword(String password);

    Map getMeta();

    void setMeta(Map meta);
}
