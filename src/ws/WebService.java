package ws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class WebService {

    private Map<String, String> params;
    private Map<String, String> object;
    private String action;
    private String result;
    private String ws_url;
    private String method;
    private Boolean ignore_type;

    public WebService() {
        cancel();
    }

    public final void cancel() {
        params = new HashMap();
        action = null;
        result = null;
        object = null;
        ws_url = null;
        method = "";
        ignore_type = false;
    }

    public void POST(String page) {
        prepare(page, "POST", "", "");
    }

    public void POST(String page, String action) {
        prepare(page, "POST", action, "");
    }

    public void POST(String page, String action, String param) {
        prepare(page, "POST", action, param);
    }

    public void GET(String page) {
        prepare(page, "GET", "", "");
    }

    public void GET(String page, String action) {
        prepare(page, "GET", action, "");
    }

    public void GET(String page, String action, String param) {
        prepare(page, "GET", action, param);
    }

    public void PUT(String page) {
        prepare(page, "PUT", "", "");
    }

    public void PUT(String page, String action) {
        prepare(page, "PUT", action, "");
    }

    public void PUT(String page, String action, String param) {
        prepare(page, "PUT", action, param);
    }

    public void DELETE(String page) {
        prepare(page, "DELETE", "", "");
    }

    public void DELETE(String page, String action) {
        prepare(page, "DELETE", action, "");
    }

    public void DELETE(String page, String action, String param) {
        prepare(page, "DELETE", action, param);
    }

    /**
     * Prepara a query
     *
     * @param page
     * @param method
     * @param action
     * @param param
     */
    public void prepare(String page, String method, String action, String param) {
        ws_url = "";
        if (!ignore_type) {
            if (!page.contains(".jsf")) {
                page = page + ".jsf";
            }
        }
        ignore_type = false;
        result = null;
        String mac = Mac.getInstance();
        ConfWebService cws = new ConfWebService();
        cws.loadJson();
        if (cws.getSsl()) {
            ws_url += "https://";
        } else {
            ws_url += "http://";
        }
        Boolean errors = false;
        String string_errors = "";
        if (cws.getUrl().isEmpty()) {
            errors = true;
            string_errors += "url; ";
        }
        if (cws.getUser().isEmpty()) {
            string_errors += "user; ";
        }
        if (cws.getPassword().isEmpty()) {
            string_errors += "password; ";
        }
        if (cws.getClient().isEmpty()) {
            string_errors += "client; ";
        }
        if (cws.getApp().isEmpty()) {
           string_errors += "app; ";
        }
        if (cws.getKey().isEmpty()) {
           string_errors += "key; ";
        }
        if (param.isEmpty()) {
            string_errors += "page not found; ";
        }
        if (page.isEmpty()) {
            string_errors += "parans is not null; ";
        }
        if (errors) {
            JOptionPane.showMessageDialog(null,
                    string_errors,
                    "Arquivo de configuração > conf",
                    JOptionPane.WARNING_MESSAGE);
            System.err.print("Arquivo de configuração > conf" + string_errors);
        }
        List urlParams = new ArrayList<>();
        if (cws.getPort() == null || cws.getPort() == 80 || cws.getPort() == 0) {
            ws_url += cws.getUrl() + "/";
        } else {
            ws_url += cws.getUrl() + ":" + cws.getPort() + "/";
        }
        if (this.action == null || this.action.isEmpty()) {
            if (!action.isEmpty()) {
                urlParams.add("action=" + cws.getAction());
            }
        } else {
            urlParams.add("action=" + this.action);
        }
        if (!param.isEmpty()) {
            urlParams.add(param);
        }
        if (!params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlParams.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        if (!this.method.isEmpty()) {
            urlParams.add("method=" + method);
        }
        if (!cws.getContext().isEmpty()) {
            ws_url += cws.getContext() + "/ws/" + page;
        }
        for (int i = 0; i < urlParams.size(); i++) {
            if (i == 0) {
                ws_url += "?" + urlParams.get(i).toString();
            } else {
                ws_url += "&" + urlParams.get(i).toString();
            }
        }
        this.method = method;
    }

    /**
     * Executa a query
     *
     * @return
     * @throws java.io.IOException
     */
    public String execute() throws Exception {
        URL url = new URL(ws_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        InetAddress ia = InetAddress.getLocalHost();
        String hostName = ia.getHostName();
        ConfWebService cws = new ConfWebService();
        cws.loadJson();
        con.setRequestProperty("Device-Name", hostName);
        con.setRequestProperty("Device-Mac", Mac.getInstance());
        con.setRequestProperty("Device-User", cws.getUser());
        con.setRequestProperty("Device-Password", cws.getPassword());
        con.setRequestProperty("Device-Client", cws.getClient());
        con.setRequestProperty("Device-App", cws.getApp());
        con.setRequestProperty("Device-Ssl", Boolean.toString(cws.getSsl()));
        con.setRequestProperty("Device-Key", cws.getKey());
        // 15 Segundos
        con.setConnectTimeout(15000);
        con.setUseCaches(true);
        con.setRequestMethod(this.method);
        result = "";
        String readLine = "";
        String append = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            while ((readLine = in.readLine()) != null) {
                append += readLine;
            }
            in.close();
        }
        if (append.isEmpty()) {
            result = null;
        }
        result = append;
        return result;
    }

    /**
     * Retorna status, sempre usar o padrão abaixo caso retornar mensagem de
     * status
     * <ul>
     * <li>null = nenhum status encontrado</li>
     * <li>0 = Sucesso</li>
     * <li>1 = Erro</li>
     * </ul>
     *
     * @return
     */
    public WSStatus wSStatus() {
        if (result != null && !result.isEmpty()) {
            Gson gson = new Gson();
            try {
                return gson.fromJson(result, WSStatus.class);
            } catch (Exception e) {

            }
        }
        return new WSStatus();
    }

    /**
     * Retorna um objeto
     *
     * @return
     */
    public Object simpleObject() {
        if (result != null && !result.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(result, Object.class);
        }
        return null;
    }

    /**
     * Retorna uma lista
     *
     * @return
     */
    public List list() {
        if (result != null && !result.isEmpty()) {
            try {
                Gson gson = new Gson();
                List<?> list = gson.fromJson(result, new TypeToken<List<?>>() {
                }.getType());
                return list;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Retorna um objeto do tipo
     *
     * @param type
     * @return
     */
    public Object object(Object type) {
        if (result != null && !result.isEmpty()) {
            try {
                Gson gson = new Gson();
                type = gson.fromJson(result, type.getClass());
                return type;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Retorna um objeto do tipo
     *
     * @param c
     * @return
     */
    public Object object(Class c) {
        if (result != null && !result.isEmpty()) {
            try {
                Gson gson = new Gson();
                Object o = gson.fromJson(result, c);
                return o;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * *
     * Adiciona parâmetro e converte para objeto
     *
     * @param param
     * @param object
     */
    public void paramObject(String param, Object object) {
        try {
            Gson gson = new Gson();
            String objectString = gson.toJson(object);
            params.put(param, objectString);
        } catch (Exception e) {

        }
    }

    /**
     * *
     * Adiciona parâmetro e converte para objeto
     *
     * @param object
     */
    public void paramObject(Object object) {
        try {
            Gson gson = new Gson();
            String objectString = gson.toJson(object);
            params.put(object.getClass().getSimpleName(), objectString);
        } catch (Exception e) {

        }
    }

    /**
     * *
     * Adiciona parâmetro
     *
     * @param param
     * @param value
     */
    public void param(String param, String value) {
        params.put(param, value);
    }

    /**
     * *
     * Adiciona parâmetro
     *
     * @param param
     * @param value
     */
    public void param(String param, Integer value) {
        params.put(param, Integer.toString(value));
    }

    /**
     * *
     * Adiciona parâmetro
     *
     * @param param
     * @param value
     */
    public void param(String param, Boolean value) {
        params.put(param, Boolean.toString(value));
    }

    /**
     * *
     * Adiciona parâmetro
     *
     * @param param
     * @param value
     */
    public void param(String param, Float value) {
        params.put(param, Float.toString(value));
    }

    /**
     * *
     * Adiciona parâmetro
     *
     * @param param
     * @param value
     */
    public void param(String param, Double value) {
        params.put(param, Double.toString(value));
    }

    /**
     * *
     * Adiciona parâmetro
     *
     * @param param
     * @param value
     */
    public void param(String param, Date value) {
        params.put(param, "" + value);
    }

    /**
     * *
     * Adiciona ação
     *
     * @param action
     */
    public void action(String action) {
        this.action = action;
    }

    public static Boolean existConnection() {
        try {
            URL url;
            try {
                url = new URL(new ConfWebService().getCurl());
                URLConnection conn = url.openConnection();
                conn.connect();
            } catch (MalformedURLException ex) {
                return false;
            } catch (IOException ex) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public String getBytes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean getIgnore_type() {
        return ignore_type;
    }

    public void setIgnore_type(Boolean ignore_type) {
        this.ignore_type = ignore_type;
    }
}
