
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Simulator
{
    Thread runnable;

    public Simulator()
    {
        super();
    }

    void start ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(serialPort.FLOWCONTROL_RTSCTS_OUT);
                OutputStream out = serialPort.getOutputStream();

                runnable = new Thread(new SerialWriter(out));
                runnable.start();
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public static class SerialWriter implements Runnable
    {
        OutputStream out;
        Scanner user_input = new Scanner( System.in );
        public static final String connectionCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"102\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";

        public static final String disConnectionCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"103\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";

        public static final String tempCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"100\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";

        public static final String energyCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"101\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";

        /* --------------------------------- FOR TESTING ONLY ------------------------------------------- */
        public static final String moveToSafeCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"1\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String moveToSBCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"2\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String moveToOppCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"3\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String formatEngCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"4\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String formatTempCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"5\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String formatStaticCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"6\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String formatMixedCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"7\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String sbandOnCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"8\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String sbandSBCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"9\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String payloadOnCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"10\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String payloadSBCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"11\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String thermalCTRLOnCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"12\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        public static final String thermalCTRLSBCommand =
                "<?xml version=\"1.0\"?>" +
                        "<packet>" +
                        "<upstreamPacket time=\"12332\">" +
                        "<mission time=\"0\" opcode=\"13\" priority=\"1\"/>" +
                        "</upstreamPacket>" +
                        "</packet>";
        /* ---------------------------------------------------------------------------------------------- */
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run ()
        {
            byte[] bytes = null;
            try{
                while (true)
                {
                    System.out.println("\nPlease enter a command to simulate: \n");
                    System.out.println("Press       T       -       Set High Temperature ");
                    System.out.println("Press       E       -       Set Low Energy ");
                    System.out.println("Press       C       -       Enter Connected Zone ");
                    System.out.println("Press       D       -       Disconnect, Exit Connected Zone ");
                    System.out.println("Press       Q       -       Exit Simulator ");
                    /* --------------------------------- FOR TESTING ONLY ------------------------------------------- */
                    System.out.println("\n            Ground Station Commands            \n");
                    System.out.println("Press       T1      -       MOVE_TO_SAFE  ");
                    System.out.println("Press       T2      -       MOVE_TO_STANDBY  ");
                    System.out.println("Press       T3      -       MOVE_TO_OP  ");
                    System.out.println("Press       T4      -       FORMAT_ENERGY  ");
                    System.out.println("Press       T5      -       FORMAT_TEMP  ");
                    System.out.println("Press       T6      -       FORMAT_STATIC  ");
                    System.out.println("Press       T7      -       FORMAT_MIXED  ");
                    System.out.println("Press       T8      -       SBAND_ON  ");
                    System.out.println("Press       T9      -       SBAND_STANDBY  ");
                    System.out.println("Press       T10     -       PAYLOAD_ON  ");
                    System.out.println("Press       T11     -       PAYLOAD_STANDBY  ");
                    System.out.println("Press       T12     -       THERMAL_CTRL_ON  ");
                    System.out.println("Press       T13     -       THERMAL_CTRL_STANDBY  ");
                    /* ---------------------------------------------------------------------------------------------- */
                    String command = "";
                    command = user_input.next( );
                    System.out.println("\nYou have entered: "+command);
                    if (command.equalsIgnoreCase("Q")){
                        System.out.println("Exiting . . . ");
                        break;
                    }
                    else if(command.equalsIgnoreCase("T")){
                        bytes = tempCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("E")){
                        bytes = energyCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("C")){
                        bytes = connectionCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("D")){
                        bytes = disConnectionCommand.getBytes();
                    }
                    /* --------------------------------- FOR TESTING ONLY ------------------------------------------- */
                    else if(command.equalsIgnoreCase("T1")){
                        bytes = moveToSafeCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T2")){
                        bytes = moveToSBCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T3")){
                        bytes = moveToOppCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T4")){
                        bytes = formatEngCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T5")){
                        bytes = formatTempCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T6")){
                        bytes = formatStaticCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T7")){
                        bytes = formatMixedCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T8")){
                        bytes = sbandOnCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T9")){
                        bytes = sbandSBCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T10")){
                        bytes = payloadOnCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T11")){
                        bytes = payloadSBCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T12")){
                        bytes = thermalCTRLOnCommand.getBytes();
                    }
                    else if(command.equalsIgnoreCase("T13")){
                        bytes = thermalCTRLSBCommand.getBytes();
                    }
                    /* ---------------------------------------------------------------------------------------------- */
                    else{
                        System.out.println("Invalid Command \n");
                        continue;
                    }
                    if(bytes != null){
                        for (int i=0; i<bytes.length; i++){
                            this.out.write(bytes[i]);
                        }
                        this.out.write(10);
                    }
                    this.out.flush();
                }
                //this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main ( String[] args )
    {
        try
        {
            int argSize = args.length;
            String port = "COM1";
            if (argSize == 1 ){
                String arg = args[0].trim();
                if (args[0].contains("COM")){
                    port = arg;
                }
                else {
                    System.out.println("Incorrect usage: please enter serial port, e.g: COM1");
                    return;
                }
            }
            Simulator simulator = new Simulator();
            simulator.start(port);
            simulator.runnable.join();
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}