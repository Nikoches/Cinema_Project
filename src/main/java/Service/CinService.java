package Service;

import Persistence.DbConnect;
import org.json.simple.JSONArray;

public class CinService {
    private final DbConnect dbConnect =  DbConnect.getInstance();
    private final static CinService cinService = new CinService();
    private CinService() {

    }
    public static CinService getInstance(){
        return cinService;
    }
    public String getJsonTable() {
        int[][] ss = dbConnect.getPlaces();
        JSONArray jsArray2 = new JSONArray();
        JSONArray jsArray3;
        for (int x=0; x<3; x++) {
            jsArray3 = new JSONArray();
            for (int y=0; y<3; y++) {
                jsArray3.add(ss[x][y]);
            }
            jsArray2.add(jsArray3);
        }
        return jsArray2.toJSONString();
    }
    public boolean makeTransaction(String xx) {
        return false;
    }
}
