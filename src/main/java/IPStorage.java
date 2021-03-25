import inet.ipaddr.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IPStorage {

    ArrayList<IPAddress> currentCIDRBlocks;
    ArrayList<String> currentStringArray;
    HashMap<String, String> currentUsedIPAddr;

    public IPStorage(){
        currentCIDRBlocks = null;
        currentStringArray = null;
        currentUsedIPAddr = new HashMap<>();
    }

    //Adds a CIDR block, appropriately merging and decomposing the CIDR block list as needed.
    //ALso removes IPs contianed in the new block.
    public String addCIDR(String inputString){
        try{
            //Create a new CIDR formatted IP
            IPAddressString str = new IPAddressString(inputString);
            IPAddress addr = str.toAddress();
            String[] mergeArray;
            if(currentCIDRBlocks == null){
                currentCIDRBlocks = new ArrayList<>();
                currentCIDRBlocks.add(addr);
            }
            else{
                //Merge the new CIDR with the previously existing CIDR blocks
                IPAddress[] arrayVal = {addr};
                currentStringArray = toArrayString(currentCIDRBlocks, arrayVal);
                mergeArray = currentStringArray.toArray(new String[currentStringArray.size()]);
                setCurrentCIDRBlocks(merge(mergeArray));
                System.out.println("current CIDR blocks are: " + Arrays.asList(currentCIDRBlocks));
            }

            //If we're re-adding a CIDR block and it contains an IP that IP needs to be removed.
            for(String ip : currentUsedIPAddr.keySet()){
                IPAddress checkIP = new IPAddressString(ip).getAddress();
                if(addr.contains(checkIP)){
                    currentUsedIPAddr.remove(ip);
                }
            }
            return Arrays.asList(currentCIDRBlocks).toString();
        }
        catch(AddressStringException e){
            System.out.println("Caught an AddressString exception with value: " + inputString);
            System.out.println(e.toString());
            return null;
        }
    }

    //Acquires an IP and properly removes it from the CIDR block list
    public String acquireIP(String newIP){
        String currentValue = currentUsedIPAddr.get(newIP);

        if(currentValue == null){
            IPAddressString newIPString = new IPAddressString(newIP);
            IPAddress newIPAddress = newIPString.getAddress();
            for(int x = 0; x < currentCIDRBlocks.size(); x++){
                if(currentCIDRBlocks.get(x).contains(newIPAddress)){
                    String[] mergeArray;
                    IPAddress removeValue = currentCIDRBlocks.remove(x);
                    currentUsedIPAddr.put(newIPString.toString(), "acquired");
                    currentStringArray = toArrayString(currentCIDRBlocks, exclude(removeValue.toString(), newIP));
                    mergeArray = currentStringArray.toArray(new String[currentStringArray.size()]);
                    setCurrentCIDRBlocks(merge(mergeArray));
                    return Arrays.asList(currentCIDRBlocks).toString();
                }
            }
            return "This IP cannot be acquired as it was not in the currently available CIDR blocks";
        }
        else if(currentUsedIPAddr.get(newIP).equals("acquired")){
            return "This IP has already been acquired";
        }
        else{
            currentUsedIPAddr.replace(newIP, "acquired");
            return "Available IP set to Acquired";
        }
    }

    //Releases an IP
    public String releaseIP(String ipCheck){
        String currentValue = currentUsedIPAddr.get(ipCheck);
        if(currentValue == null){
            return "Cannot release an IP which is not contained in the current CIDR blocks";
        }
        else if(currentUsedIPAddr.get(ipCheck).equals("available")){
            return "This IP is already available";
        }
        else{
            currentUsedIPAddr.replace(ipCheck, "available");
            return "IP marked as available";
        }
    }

    //Prints a list of the currently used IPs with their status and the current CIDR blocks
    public String listIPs(){
        StringBuilder sb = new StringBuilder();
        sb.append("IP ADDRESSES\n");
        for(String IP : currentUsedIPAddr.keySet()){
            sb.append(IP + " " + currentUsedIPAddr.get(IP) + "\n");
        }
        sb.append("CIDR BLOCKS\n");
        for(IPAddress address : currentCIDRBlocks){
            sb.append(address + "\n");
        }
        return sb.toString();
    }

    //Pulls an IP address out of an existing CIDR block and returns a list of the remaining CIDR blocks
    //This was obtained, with some modifications, from the examples given by the inet library
    static IPAddress[] exclude(String addrStr, String sub) {
        IPAddress one = new IPAddressString(addrStr).getAddress();
        IPAddress two = new IPAddressString(sub).getAddress();
        IPAddress result[] = one.subtract(two);
        System.out.println("Removing " + two + " from " + one + " results in: " +
                Arrays.asList(result));
        ArrayList<IPAddress> blockList = new ArrayList<>();
        for(IPAddress addr : result) {
            blockList.addAll(Arrays.asList(addr.spanWithPrefixBlocks()));
        }
        return result;
    }

    //Merges an array of CIDR blocks together, properly creating larger blocks as needed
    //This was obtained, with some modifications, from the examples given by the inet library
    static ArrayList<IPAddress> merge(String ...strs) {
        IPAddress first = new IPAddressString(strs[0]).getAddress();
        IPAddress remaining[] = Arrays.stream(strs, 1, strs.length).
                map(str -> new IPAddressString(str).getAddress()).
                toArray(IPAddress[]::new);
        return new ArrayList(Arrays.asList(first.mergeToPrefixBlocks(remaining)));
    }

    //Takes an Arraylist (usually the current CIDR blocks) and an array of new IPs and makes a single Arraylist out of them
    static ArrayList<String> toArrayString(ArrayList<IPAddress> ips, IPAddress[] secondIPs){
        ArrayList<String> returnArrayList = new ArrayList<>();
        ips.forEach((val)->{
            returnArrayList.add(val.toString());
        });
        for(IPAddress ipVal : secondIPs){
            returnArrayList.add(ipVal.toString());
        }
        return returnArrayList;
    }

    public ArrayList<IPAddress> getCurrentCIDRBlocks() {
        return currentCIDRBlocks;
    }

    public void setCurrentCIDRBlocks(ArrayList<IPAddress> currentCIDRBlocks) {
        this.currentCIDRBlocks = currentCIDRBlocks;
    }

    public HashMap<String, String> getCurrentUsedIPAddr() {
        return currentUsedIPAddr;
    }

    public void setCurrentUsedIPAddr(HashMap<String, String> currentUsedIPAddr) {
        this.currentUsedIPAddr = currentUsedIPAddr;
    }
}
