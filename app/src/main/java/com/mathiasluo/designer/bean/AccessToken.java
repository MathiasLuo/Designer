package com.mathiasluo.designer.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public class AccessToken {


    /**
     * access_token : 7c24ece1fc058cd41e84a5734d128be6933dc524db8ee37811969edaf32a28be
     * token_type : bearer
     * scope : public
     */

    private String access_token;
    private String token_type;
    private String scope;

    public static AccessToken objectFromData(String str) {

        return new Gson().fromJson(str, AccessToken.class);
    }

    public static AccessToken objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), AccessToken.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<AccessToken> arrayAccessTokenFromData(String str) {

        Type listType = new TypeToken<ArrayList<AccessToken>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<AccessToken> arrayAccessTokenFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<AccessToken>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }
}
