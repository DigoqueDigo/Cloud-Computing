package client.machine;
import packets.Packet;
import packets.Packet.Protocol;


public class MachineClientUI{

    private static final String RESET = "\033[0m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String GREEN_BOLD = "\033[1;32m";

    private static MachineClientUI machineClientUI = null;

    
    private MachineClientUI() {}

    
    public static MachineClientUI getInstance(){
        if (MachineClientUI.machineClientUI == null)
            MachineClientUI.machineClientUI = new MachineClientUI();
        return MachineClientUI.machineClientUI;
    }

    
    public void showPacketMessage(Packet packet){
        if (packet.getProtocol() == Protocol.ERROR)
            System.out.println(RED_BOLD + packet.getOptionalMessage() + RESET);
        System.out.println(GREEN_BOLD + packet.getOptionalMessage() + RESET);
    }
}