package Service.RESTServer.Model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class TradeCurlAdapter {

    public String id;
    String cardToTrade;
    String type;
    double minimumDamage;
}
