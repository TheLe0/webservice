package ws;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String args[]) {
        WebService webService = new WebService();
        webService.GET("autenticar_dispositivo");
        try {
            webService.execute();
            WSStatus wSStatus = webService.wSStatus();
            wSStatus.getCodigo();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
