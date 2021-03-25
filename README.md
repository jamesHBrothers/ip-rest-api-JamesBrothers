# IP Address Management REST API
 
Create a simple IP Address Management REST API on top of any data store. It will include the ability to add IP Addresses by CIDR block and then either acquire or release IP addresses individually. Each IP address will have a status associated with it that is either “available” or “acquired”. 
 
The REST API must support four endpoint:
  * **Create IP addresses** - take in a CIDR block (e.g. 10.0.0.1/24) and add all IP addresses within that block to the data store with status “available”
  * **List IP addresses** - return all IP addresses in the system with their current status
  * **Acquire an IP** - set the status of a certain IP to “acquired”
  * **Release an IP** - set the status of a certain IP to “available”



James Brothers Notes

I used Spark as it was a quick easy way to get a valid REST API running. The API was tested primarily through the use of curl and some example commands will be listed below. It runs on port 4567.

While I contemplated adding an official datastore to the code I believe it should function without one, making the running significantly simpler. The idea being that this will store the data so long as the application is running. As well it should be difficult (without code calling the api iteslf) to add enough data to make this a problem. The CIDR blocks will appropriately decompose as IPs are taken and will overwrite any IPs in use if the block is re-added. I also used Sean C Foley's IPAddress library as rewriting large portions of already obtainable open source code seemed unnecessary

