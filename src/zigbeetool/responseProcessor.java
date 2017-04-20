/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zigbeetool;

/**
 *
 * @author I14746
 */
public class responseProcessor {




    final int  OP_RESET  =		1;
final int OP_WR_CFG 		=2;
final int  OP_WR_LOG_TYPE 	=3;
final int OP_WR_PAN_ID 	=4;
final int OP_WR_EPAN_ID 	=5;
final int OP_AF_REG 	=	6;
final int OP_ZDO_START =	7;
final int OP_ZDO_STATE =	8;
final int OP_AF_DATA    =  9;
final int OP_IEEE_ADDRS = 99;

final int RESET_CMD0= 0x41;
final int RESET_CMD1 =0x80;
final int WR_CMD0= 0x66;
final int WR_CMD1 =0x05;
final int AF_CMD0 =0x64;
final int AF_REG_CMD1= 0x00;
final int AF_DATA_CMD1= 0x01;
final int ZDO_CMD0= 0x65;
final int ZDO_CMD1 =0x40;
final int ZDO_STATE_CMD0 =0x45;
final int ZDO_STATE_CMD1= 0xC0;
final int READ_DATA_CMD0 =0x44;
final int READ_DATA_CMD1 =0x80;
final int READ_CMR1_CMD1= 0x81;


final int DEV_HOLD 		=		0; 	//Initialized - not started automatically
final int DEV_INIT 		=		1; 	//Initialized - not connected to anything
final int DEV_NWK_DISC 		=	2; 	//Discovering PAN's to join
final int DEV_NWK_JOINING 	=	3 ;	//Joining a PAN
final int DEV_NWK_REJOIN 		=	4;	//ReJoining a PAN, only for end devices
final int DEV_END_DEVICE_UNAUTH =	5;	//Joined but not yet authenticated by trust center
final int DEV_END_DEVICE 		=	6;	//Started as device after authentication
final int DEV_ROUTER 		=		7;	//Device joined, authenticated and is a router
final int DEV_COORD_STARTING 	=	8;	//Starting as Zigbee Coordinator
final int DEV_ZB_COORD 		=	9;	//Started as Zigbee Coordinator
final int DEV_NWK_ORPHAN 		=   10;	//Device has lost information about its parent
final int RX_BUFFER_SIZE = 128;
//char SYST_RST[] = {0xfe,0x01,0x41,0x00,0x00,0x40};
//char WRT_CONFG[] = {0xfe,0x03,0x26,0x05,0x03,0x01,0x03,0x21};	//8
//char EPID[] = {0xfe,0x0a,0x26,0x05,0x2d,0x08,0x00,0x00,0x00,0x10,0x77,0xc2,0x50,0x00,0xf9 };//15
//char PANID[] = {0xfe,0x04,0x26,0x05,0x83,0x02,0x12,0x34/*0xff*/,0x80};	//9
//char ZDO_STRT[] = {0xfe,0x02,0x25,0x40,0x00,0x00,0x67};	//7
//char AF_reg[] = {0xfe,0x11,0x24,0x00,0x01,0x09,0x01,0x01,0x05,0x01,0x00,0x02,0x02,0x07,0x12,0x34,0x15,0x00,0x01,0x00,0x00,0x0C}; //22
//char AF_Data[] = {0xfe,0x0b,0x24,0x01,0xff,0xff/*0xff*/,0x01,0x01,0x02,0x07,0x00,0x00,0x00,0x01,0x32,0x18};//16
//char LOG_CORD[] = {0xfe,0x03,0x26,0x05,0x87,0x01,0x00,0xa6};



  int  processData(int action, responseStruct resp)
{
        responseStruct rxPacket = resp;
	byte bufPtr = rxPacket.dataBuffer[0];
	switch (action)
	{
		case OP_RESET:
			if((rxPacket.cmd0 == RESET_CMD0) && (rxPacket.cmd1 == (byte)RESET_CMD1))
			{
				if((bufPtr == 0)||(bufPtr == 1)||(bufPtr == 2))
					return 1;
			}
		break;
		case OP_WR_CFG:
		case OP_WR_LOG_TYPE:
		case OP_WR_PAN_ID:
		case OP_WR_EPAN_ID:
			if((rxPacket.cmd0 == WR_CMD0) && (rxPacket.cmd1 == WR_CMD1))
			{
				if(bufPtr == 0)
					return 1;
			}
		break;
		case OP_AF_REG:
			if((rxPacket.cmd0 == AF_CMD0) && (rxPacket.cmd1 == AF_REG_CMD1))
			{
				if(bufPtr == 0)
					return 1;
			}
		break;
		case OP_ZDO_START:
			if((rxPacket.cmd0 == ZDO_CMD0) && (rxPacket.cmd1 == ZDO_CMD1))
			{
				if(bufPtr == 1)
					return 1;
			}
		break;
		case OP_ZDO_STATE:
			if((rxPacket.cmd0 == ZDO_STATE_CMD0) && (rxPacket.cmd1 == ZDO_STATE_CMD1))
			{
					return bufPtr;
			}
		case OP_AF_DATA:
			if((rxPacket.cmd0 == AF_CMD0) && (rxPacket.cmd1 == AF_DATA_CMD1))
			{
			//	result = 1;
			}
		break;

            case OP_IEEE_ADDRS:
                if(rxPacket.dataBuffer.length==9){
                    return 1;
                }
                break;
		default:
		break;
	}
	return 0;
}


     


}
