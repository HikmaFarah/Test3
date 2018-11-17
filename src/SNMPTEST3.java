import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPTEST3 {

    public static final String READ_COMMUNITY = "public";
    
    public static final int mSNMPVersion = 0;
    
    public static final String memory = ".1.3.6.1.4.1.2021.4.6.0";
  
    public static final String localhost = "8080";

    public static void main(String[] args){
    	 SNMPTEST3 s = new SNMPTEST3();
         try{
             String strIPAddress = "127.0.0.1/8080";
             SNMPTEST3 objSNMP = new SNMPTEST3();
             int Value = 2;
           
             String memory = objSNMP.snmpGet(strIPAddress, SNMPTEST3.READ_COMMUNITY, SNMPTEST3.memory);
             System.out.println("memory =" + memory);
         }
         catch (Exception e){
             e.printStackTrace();
        }
    }

   

    public String snmpGet(String strAddress, String community, String strOID){
        String str = "";
        try{
            OctetString com = new OctetString(community);
            strAddress = strAddress + "/" + localhost;
            Address targetAddress = new UdpAddress(strAddress);
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(com);
            comtarget.setVersion(SnmpConstants.version1);
            comtarget.setAddress(targetAddress);
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);

            PDU pdu = new PDU();
            ResponseEvent response;
            Snmp snmp;
            pdu.add(new VariableBinding(new OID(strOID)));
            pdu.setType(PDU.GET);
            snmp = new Snmp(transport);
            response = snmp.get(pdu,comtarget);
            if (response != null){
                if(response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")){
                    PDU pduresponse = response.getResponse();
                    str = pduresponse.getVariableBindings().firstElement().toString();
                    if(str.contains("=")){
                        int len = str.indexOf("=");
                        str = str.substring(len+1,str.length());
                    }
                }
            }
            else {
                System.out.println("TIMEOUT");
            }
            snmp.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("RESPONSE = " + str);
        return str;
    }
}