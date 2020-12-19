package Service.RESTServer.Model;

import lombok.Data;

@Data
public class TradeCurlAdapter {
    String id;
    String cardToTrade;
    String type;
    double minimumDamage;
}
