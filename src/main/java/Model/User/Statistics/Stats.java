package Model.User.Statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    String UserName = "";
    int mmr = 0;
    int gamesPlayed = 0;
    double winrate = 0;
}
