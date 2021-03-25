import org.apache.commons.net.*;
import org.apache.commons.net.util.SubnetUtils;
import inet.ipaddr.IPAddress;
import static spark.Spark.*;

public class TestClass {
    public static void main(String[] args) {
        IPStorage currentIPStorage = new IPStorage();

        post("/addCIDR", (req, res) -> currentIPStorage.addCIDR(req.queryParams("cidr")));
        post("/releaseIP", (req, res) -> currentIPStorage.releaseIP(req.queryParams("ip")));
        get("/listIPs", (req, res) -> currentIPStorage.listIPs());
        post("/acquireIP", (req, res) -> currentIPStorage.acquireIP(req.queryParams("ip")));
    }
}
