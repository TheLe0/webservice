package ws;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfWebService {

    private String url;
    private String context;
    private String client;
    private String app;
    private String key;
    private String user;
    private String password;
    private String method;
    private String action;
    private Integer port;
    private Boolean ssl;
    private Boolean session;

    public ConfWebService() {
        this.url = "";
        this.context = "";
        this.client = "";
        this.app = "";
        this.key = "";
        this.user = "";
        this.password = "";
        this.method = "";
        this.action = "";
        this.port = null;
        this.ssl = false;
        this.session = false;
    }

    public ConfWebService(String url, String context, String client, String app, String key, String user, String password, String method, String action, Integer port, Boolean ssl, Boolean session) {
        this.url = url;
        this.client = client;
        this.context = context;
        this.app = app;
        this.key = key;
        this.user = user;
        this.password = password;
        this.method = method;
        this.action = action;
        this.port = port;
        this.ssl = ssl;
        this.session = session;
    }

    public void loadJson() {
        String path = "";
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException ex) {
        }
        try {
            File file = new File(path + "\\lib\\conf\\web_service.json");
            if (!file.exists()) {
                return;
            }
            String json = null;
            try {
                json = FileUtils.readFileToString(file);
            } catch (IOException ex) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, ex);
            }
            JSONObject jSONObject = new JSONObject(json);
            try {
                url = jSONObject.getString("url");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                context = jSONObject.getString("context");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                client = jSONObject.getString("client");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                app = jSONObject.getString("app");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                key = jSONObject.getString("key");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                user = jSONObject.getString("user");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                password = jSONObject.getString("password");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                action = jSONObject.getString("action");
            } catch (Exception e) {
               Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                port = jSONObject.getInt("port");
            } catch (Exception e) {
               Logger.getLogger(ConfWebService.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                method = jSONObject.getString("method");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                ssl = jSONObject.getBoolean("ssl");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                session = jSONObject.getBoolean("session");
            } catch (Exception e) {
                Logger.getLogger(ConfWebService.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (JSONException ex) {
            Logger.getLogger(ConfWebService.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Boolean getSession() {
        return session;
    }

    public void setSession(Boolean session) {
        this.session = session;
    }

    public String getCurl() throws IOException {
        loadJson();
        String urlString = "";
        if (this.getSsl()) {
            urlString += "https://";
        } else {
            urlString += "http://";
        }
        List urlParams = new ArrayList<>();
        if (this.getPort() == null || this.getPort() == 80 || this.getPort() == 0) {
            urlString += this.getUrl() + "/";
        } else {
            urlString += this.getUrl() + ":" + this.getPort() + "/";
        }
        if (!this.getContext().isEmpty()) {
            urlString += this.getContext() + "/ws/" + this;
        }
        return urlString;
    }

}
