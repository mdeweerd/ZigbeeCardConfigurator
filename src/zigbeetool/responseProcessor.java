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

    /**
     * Application operations.
     */
    final static int OP_RESET = 1;
    final static int OP_WR_CFG = 2;
    final static int OP_WR_LOG_TYPE = 3;
    final static int OP_WR_PAN_ID = 4;
    final static int OP_WR_EPAN_ID = 5;
    final static int OP_AF_REG = 6;
    final static int OP_ZDO_START = 7;
    final static int OP_ZDO_STATE = 8;
    final static int OP_AF_DATA = 9;
    final static int OP_IEEE_ADDRS = 99;

    /**
     * ZNP constants
     */
    /**
     * SYS
     */
    final static int SYS_RESET_REQ = 0x00;
    final static int SYS_PING = 0x01;
    final static int SYS_VERSION = 0x02;
    final static int SYS_SET_EXTADDR = 0x03;
    final static int SYS_GET_EXTADDR = 0x04;
    final static int SYS_RAM_READ = 0x05;
    final static int SYS_RAM_WRITE = 0x06;
    final static int SYS_OSAL_NV_ITEM_INIT = 0x07;
    final static int SYS_OSAL_NV_READ = 0x08;
    final static int SYS_OSAL_NV_WRITE = 0x09;
    final static int SYS_OSAL_START_TIMER = 0x0A;
    final static int SYS_OSAL_STOP_TIMER = 0x0B;
    final static int SYS_RANDOM = 0x0C;
    final static int SYS_ADC_READ = 0x0D;
    final static int SYS_GPIO = 0x0E;
    final static int SYS_STACK_TUNE = 0x0F;
    final static int SYS_SET_TIME = 0x10;
    final static int SYS_GET_TIME = 0x11;
    final static int SYS_OSAL_NV_DELETE = 0x12;
    final static int SYS_OSAL_NV_LENGTH = 0x13;
    final static int SYS_SET_TX_POWER = 0x14;
    final static int SYS_JAMMER_PARAMETERS = 0x15;
    final static int SYS_SNIFFER_PARAMETERS = 0x16;

    final static int SYS_RESET_IND = 0x80;
    final static int SYS_OSAL_TIMER_EXPIRED = 0x81;
    final static int SYS_JAMMER_IND = 0x82;

    // MAC
    final static int MAC_RESET_REQ = 0x01;
    final static int MAC_INIT = 0x02;
    final static int MAC_START_REQ = 0x03;
    final static int MAC_SYNC_REQ = 0x04;
    final static int MAC_DATA_REQ = 0x05;
    final static int MAC_ASSOCIATE_REQ = 0x06;
    final static int MAC_DISASSOCIATE_REQ = 0x07;
    final static int MAC_GET_REQ = 0x08;
    final static int MAC_SET_REQ = 0x09;
    final static int MAC_GTS_REQ = 0x0a;
    final static int MAC_RX_ENABLE_REQ = 0x0b;
    final static int MAC_SCAN_REQ = 0x0c;
    final static int MAC_POLL_REQ = 0x0d;
    final static int MAC_PURGE_REQ = 0x0e;
    final static int MAC_SET_RX_GAIN_REQ = 0x0f;
    final static int MAC_SECURITY_GET_REQ = 0x10;
    final static int MAC_SECURITY_SET_REQ = 0x11;
    final static int MAC_ENHANCED_ACTIVE_SCAN_REQ = 0x12;
    final static int MAC_ENHANCED_ACTIVE_SCAN_RSP = 0x13;
    final static int MAC_SRC_MATCH_ENABLE = 0x14;
    final static int MAC_SRC_MATCH_ADD_ENTRY = 0x15;
    final static int MAC_SRC_MATCH_DELETE_ENTRY = 0x16;
    final static int MAC_SRC_MATCH_ACK_ALL = 0x17;
    final static int MAC_SRC_CHECK_ALL = 0x18;
    final static int MAC_ASSOCIATE_RSP = 0x50;
    final static int MAC_ORPHAN_RSP = 0x51;
    final static int MAC_SYNC_LOSS_IND = 0x80;
    final static int MAC_ASSOCIATE_IND = 0x81;
    final static int MAC_ASSOCIATE_CNF = 0x82;
    final static int MAC_BEACON_NOTIFY_IND = 0x83;
    final static int MAC_DATA_CNF = 0x84;
    final static int MAC_DATA_IND = 0x85;
    final static int MAC_DISASSOCIATE_IND = 0x86;
    final static int MAC_DISASSOCIATE_CNF = 0x87;
    final static int MAC_GTS_CNF = 0x88;
    final static int MAC_GTS_IND = 0x89;
    final static int MAC_ORPHAN_IND = 0x8a;
    final static int MAC_POLL_CNF = 0x8b;
    final static int MAC_SCAN_CNF = 0x8c;
    final static int MAC_COMM_STATUS_IND = 0x8d;
    final static int MAC_START_CNF = 0x8e;
    final static int MAC_RX_ENABLE_CNF = 0x8f;
    final static int MAC_PURGE_CNF = 0x90;
    final static int MAC_POLL_IND = 0x91;

    // NWK
    final static int NWK_INIT = 0x00;
    final static int NWK_DATA_REQ = 0x01;
    final static int NWK_NETWORK_FORMATION_REQ = 0x02;
    final static int NWK_PERMIT_JOINING_REQ = 0x03;
    final static int NWK_JOIN_REQ = 0x04;
    final static int NWK_LEAVE_REQ = 0x05;
    final static int NWK_RESET_REQ = 0x06;
    final static int NWK_GET_REQ = 0x07;
    final static int NWK_SET_REQ = 0x08;
    final static int NWK_NETWORK_DISCOVERY_REQ = 0x09;
    final static int NWK_ROUTE_DISCOVERY_REQ = 0x0A;
    final static int NWK_DIRECT_JOIN_REQ = 0x0B;
    final static int NWK_ORPHAN_JOIN_REQ = 0x0C;
    final static int NWK_START_ROUTER_REQ = 0x0D;
    final static int NWK_DATA_CONF = 0x80;
    final static int NWK_DATA_IND = 0x81;
    final static int NWK_NETWORK_FORMATION_CONF = 0x82;
    final static int NWK_JOIN_CONF = 0x83;
    final static int NWK_JOIN_IND = 0x84;
    final static int NWK_LEAVE_CONF = 0x85;
    final static int NWK_LEAVE_IND = 0x86;
    final static int NWK_POLL_CONF = 0x87;
    final static int NWK_SYNC_IND = 0x88;
    final static int NWK_NETWORK_DISCOVERY_CONF = 0x89;
    final static int NWK_START_ROUTER_CONF = 0x8A;

    // AF
    final static int AF_REGISTER = 0x00;
    final static int AF_DATA_REQUEST = 0x01;
    final static int AF_DATA_REQUEST_EXT = 0x02;
    final static int AF_DATA_REQUEST_SRCRTG = 0x03;
    final static int AF_DELETE = 0x04;
    final static int AF_INTER_PAN_CTL = 0x10;
    final static int AF_DATA_STORE = 0x11;
    final static int AF_DATA_RETRIEVE = 0x12;
    final static int AF_APSF_CONFIG_SET = 0x13;
    final static int AF_APSF_CONFIG_GET = 0x14;
    final static int AF_DATA_CONFIRM = 0x80;
    final static int AF_INCOMING_MSG = 0x81;
    final static int AF_INCOMING_MSG_EXT = 0x82;
    final static int AF_REFLECT_ERROR = 0x83;

    // ZDO
    final static int ZDO_NWK_ADDR_REQ = 0x00;
    final static int ZDO_IEEE_ADDR_REQ = 0x01;
    final static int ZDO_NODE_DESC_REQ = 0x02;
    final static int ZDO_POWER_DESC_REQ = 0x03;
    final static int ZDO_SIMPLE_DESC_REQ = 0x04;
    final static int ZDO_ACTIVE_EP_REQ = 0x05;
    final static int ZDO_MATCH_DESC_REQ = 0x06;
    final static int ZDO_COMPLEX_DESC_REQ = 0x07;
    final static int ZDO_USER_DESC_REQ = 0x08;
    final static int ZDO_END_DEV_ANNCE = 0x0A;
    final static int ZDO_USER_DESC_SET = 0x0B;
    final static int ZDO_SERVICE_DISC_REQ = 0x0C;
    final static int ZDO_END_DEVICE_TIMEOUT_REQ = 0x0D;
    final static int ZDO_END_DEV_BIND_REQ = 0x20;
    final static int ZDO_BIND_REQ = 0x21;
    final static int ZDO_UNBIND_REQ = 0x22;
    final static int ZDO_SET_LINK_KEY = 0x23;
    final static int ZDO_REMOVE_LINK_KEY = 0x24;
    final static int ZDO_GET_LINK_KEY = 0x25;
    final static int ZDO_NWK_DISCOVERY_REQ = 0x26;
    final static int ZDO_JOIN_REQ = 0x27;
    final static int ZDO_SEND_DATA = 0x28;
    final static int ZDO_NWK_ADDR_OF_INTEREST_REQ = 0x29;
    final static int ZDO_MGMT_NWKDISC_REQ = 0x30;
    final static int ZDO_MGMT_LQI_REQ = 0x31;
    final static int ZDO_MGMT_RTG_REQ = 0x32;
    final static int ZDO_MGMT_BIND_REQ = 0x33;
    final static int ZDO_MGMT_LEAVE_REQ = 0x34;
    final static int ZDO_MGMT_DIRECT_JOIN_REQ = 0x35;
    final static int ZDO_MGMT_PERMIT_JOIN_REQ = 0x36;
    final static int ZDO_MGMT_NWK_UPDATE_REQ = 0x37;
    final static int ZDO_MSG_CB_REGISTER = 0x3E;
    final static int ZDO_MSG_CB_REMOVE = 0x3F;
    final static int ZDO_STARTUP_FROM_APP = 0x40;
    final static int ZDO_AUTO_FIND_DESTINATION_REQ = 0x41;
    final static int ZDO_SEC_ADD_LINK_KEY = 0x42;
    final static int ZDO_SEC_ENTRY_LOOKUP_EXT = 0x43;
    final static int ZDO_SEC_DEVICE_REMOVE = 0x44;
    final static int ZDO_EXT_ROUTE_DISC = 0x45;
    final static int ZDO_EXT_ROUTE_CHECK = 0x46;
    final static int ZDO_EXT_REMOVE_GROUP = 0x47;
    final static int ZDO_EXT_REMOVE_ALL_GROUP = 0x48;
    final static int ZDO_EXT_FIND_ALL_GROUPS_ENDPOINT = 0x49;
    final static int ZDO_EXT_FIND_GROUP = 0x4A;
    final static int ZDO_EXT_ADD_GROUP = 0x4B;
    final static int ZDO_EXT_COUNT_ALL_GROUPS = 0x4C;
    final static int ZDO_EXT_RX_IDLE = 0x4D;
    final static int ZDO_EXT_UPDATE_NWK_KEY = 0x4E;
    final static int ZDO_EXT_SWITCH_NWK_KEY = 0x4F;
    final static int ZDO_EXT_NWK_INFO = 0x50;
    final static int ZDO_EXT_SEC_APS_REMOVE_REQ = 0x51;
    final static int ZDO_FORCE_CONCENTRATOR_CHANGE = 0x52;
    final static int ZDO_EXT_SET_PARAMS = 0x53;
    final static int ZDO_NWK_ADDR_RSP = 0x80;
    final static int ZDO_IEEE_ADDR_RSP = 0x81;
    final static int ZDO_NODE_DESC_RSP = 0x82;
    final static int ZDO_POWER_DESC_RSP = 0x83;
    final static int ZDO_SIMPLE_DESC_RSP = 0x84;
    final static int ZDO_ACTIVE_EP_RSP = 0x85;
    final static int ZDO_MATCH_DESC_RSP = 0x86;
    final static int ZDO_COMPLEX_DESC_RSP = 0x87;
    final static int ZDO_USER_DESC_RSP = 0x88;
    final static int ZDO_USER_DESC_CONF = 0x89;
    final static int ZDO_SERVER_DISC_RSP = 0x8A;
    final static int ZDO_DISCOVERY_CACHE_REQ = 0x92;
    final static int ZDO_END_DEVICE_TIMEOUT_RSP = 0x9F;
    final static int ZDO_END_DEVICE_BIND_RSP = 0xA0;
    final static int ZDO_BIND_RSP = 0xA1;
    final static int ZDO_UNBIND_RSP = 0xA2;
    final static int ZDO_MGMT_NWK_DISC_RSP = 0xB0;
    final static int ZDO_MGMT_LQI_RSP = 0xB1;
    final static int ZDO_MGMT_RTG_RSP = 0xB2;
    final static int ZDO_MGMT_BIND_RSP = 0xB3;
    final static int ZDO_MGMT_LEAVE_RSP = 0xB4;
    final static int ZDO_MGMT_DIRECT_JOIN_RSP = 0xB5;
    final static int ZDO_MGMT_PERMIT_JOIN_RSP = 0xB6;
    final static int ZDO_MGMT_NWK_UPDATE_RSP = 0xB8;
    final static int ZDO_STATE_CHANGE_IND = 0xC0;
    final static int ZDO_END_DEVICE_ANNCE_IND = 0xC1;
    final static int ZDO_MATCH_DESC_RSP_SENT = 0xC2;
    final static int ZDO_STATUS_ERROR_RSP = 0xC3;
    final static int ZDO_SRC_RTG_IND = 0xC4;
    final static int ZDO_BEACON_NOTIFY_IND = 0xC5;
    final static int ZDO_JOIN_CNF = 0xC6;
    final static int ZDO_NWK_DISCOVERY_CNF = 0xC7;
    final static int ZDO_CONCENTRATOR_IND_CB = 0xC8;
    final static int ZDO_LEAVE_IND = 0xC9;
    final static int ZDO_TC_DEVICE_IND = 0xCA;
    final static int ZDO_PERMIT_JOIN_IND = 0xCB;
    final static int ZDO_MSG_CB_INCOMING = 0xFF;

    // UTIL    final static int UTIL_GET_DEVICE_INFO = 0x00;
    final static int UTIL_GET_NV_INFO = 0x01;
    final static int UTIL_SET_PANID = 0x02;
    final static int UTIL_SET_CHANNELS = 0x03;
    final static int UTIL_SET_SECLEVEL = 0x04;
    final static int UTIL_SET_PRECFGKEY = 0x05;
    final static int UTIL_CALLBACK_SUB_CMD = 0x06;
    final static int UTIL_KEY_EVENT = 0x07;
    final static int UTIL_TIME_ALIVE = 0x09;
    final static int UTIL_LED_CONTROL = 0x0A;
    final static int UTIL_TEST_LOOPBACK = 0x10;
    final static int UTIL_DATA_REQ = 0x11;
    final static int UTIL_GPIO_SET_DIRECTION = 0x14;
    final static int UTIL_GPIO_READ = 0x15;
    final static int UTIL_GPIO_WRITE = 0x16;
    final static int UTIL_SRC_MATCH_ENABLE = 0x20;
    final static int UTIL_SRC_MATCH_ADD_ENTRY = 0x21;
    final static int UTIL_SRC_MATCH_DEL_ENTRY = 0x22;
    final static int UTIL_SRC_MATCH_CHECK_SRC_ADDR = 0x23;
    final static int UTIL_SRC_MATCH_ACK_ALL_PENDING = 0x24;
    final static int UTIL_SRC_MATCH_CHECK_ALL_PENDING = 0x25;
    final static int UTIL_ADDRMGR_EXT_ADDR_LOOKUP = 0x40;
    final static int UTIL_ADDRMGR_NWK_ADDR_LOOKUP = 0x41;
    final static int UTIL_APSME_LINK_KEY_DATA_GET = 0x44;
    final static int UTIL_APSME_LINK_KEY_NV_ID_GET = 0x45;
    final static int UTIL_ASSOC_COUNT = 0x48;
    final static int UTIL_ASSOC_FIND_DEVICE = 0x49;
    final static int UTIL_ASSOC_GET_WITH_ADDRESS = 0x4A;
    final static int UTIL_APSME_REQUEST_KEY_CMD = 0x4B;
    final static int UTIL_SRNG_GENERATE = 0x4C;
    final static int UTIL_BIND_ADD_ENTRY = 0x4D;
    final static int UTIL_ZCL_KEY_EST_INIT_EST = 0x80;
    final static int UTIL_ZCL_KEY_EST_SIGN = 0x81;
    final static int UTIL_GET_DEV_NWK_INFO = 0xA0;
    final static int UTIL_SET_DEV_NWK_INFO = 0xA1;
    final static int UTIL_SYNC_REQ = 0xE0;
    final static int UTIL_ZCL_KEY_ESTABLISH_IND = 0xE1;

    // SAPI
    final static int SAPI_START_REQ = 0x00;
    final static int SAPI_BIND_DEVICE_REQ = 0x01;
    final static int SAPI_ALLOW_BIND_REQ = 0x02;
    final static int SAPI_SEND_DATA_REQ = 0x03;
    final static int SAPI_READ_CFG_REQ = 0x04;
    final static int SAPI_WRITE_CFG_REQ = 0x05;
    final static int SAPI_GET_DEV_INFO_REQ = 0x06;
    final static int SAPI_FIND_DEV_REQ = 0x07;
    final static int SAPI_PMT_JOIN_REQ = 0x08;
    final static int SAPI_APP_REGISTER_REQ = 0x0a;
    final static int SAPI_START_CNF = 0x80;
    final static int SAPI_BIND_CNF = 0x81;
    final static int SAPI_ALLOW_BIND_CNF = 0x82;
    final static int SAPI_SEND_DATA_CNF = 0x83;
    final static int SAPI_READ_CFG_RSP = 0x84;
    final static int SAPI_FIND_DEV_CNF = 0x85;
    final static int SAPI_DEV_INFO_RSP = 0x86;
    final static int SAPI_RCV_DATA_IND = 0x87;

    // APP_CNF
    final static int APP_CNF_SET_DEFAULT_REMOTE_ENDDEVICE_TIMEOUT = 0x01;
    final static int APP_CNF_SET_ENDDEVICETIMEOUT = 0x02;
    final static int APP_CNF_SET_ALLOWREJOIN_TC_POLICY = 0x03;
    final static int APP_CNF_ADD_INSTALLCODE = 0x04;
    final static int APP_CNF_START_COMMISSIONING = 0x05;
    final static int APP_CNF_SET_JOINUSESINSTALLCODEKEY = 0x06;
    final static int APP_CNF_SET_ACTIVE_DEFAULT_CENTRALIZED_KEY = 0x07;
    final static int APP_CNF_SET_CHANNEL = 0x08;
    final static int APP_CNF_SET_TC_REQUIRE_KEY_EXCHANGE = 0x09;
    final static int APP_CNF_ZED_ATTEMPT_RECOVER_NWK = 0x0A;
    final static int APP_CNF_BDB_COMMISSIONING_NOTIFICATION = 0x80;
    final static int APP_CNF_SET_NWK_FRAME_COUNTER = 0xFF;



    final static int DEV_HOLD = 0; 	//Initialized - not started automatically
    final static int DEV_INIT = 1; 	//Initialized - not connected to anything
    final static int DEV_NWK_DISC = 2; 	//Discovering PAN's to join
    final static int DEV_NWK_JOINING = 3;	//Joining a PAN
    final static int DEV_NWK_REJOIN = 4;	//ReJoining a PAN, only for end devices
    final static int DEV_END_DEVICE_UNAUTH = 5;	//Joined but not yet authenticated by trust center
    final static int DEV_END_DEVICE = 6;	//Started as device after authentication
    final static int DEV_ROUTER = 7;	//Device joined, authenticated and is a router
    final static int DEV_COORD_STARTING = 8;	//Starting as Zigbee Coordinator
    final static int DEV_ZB_COORD = 9;	//Started as Zigbee Coordinator
    final static int DEV_NWK_ORPHAN = 10;	//Device has lost information about its parent
    final static int RX_BUFFER_SIZE = 128;
//char SYST_RST[] = {0xfe,0x01,0x41,0x00,0x00,0x40};
//char WRT_CONFG[] = {0xfe,0x03,0x26,0x05,0x03,0x01,0x03,0x21};	//8
//char EPID[] = {0xfe,0x0a,0x26,0x05,0x2d,0x08,0x00,0x00,0x00,0x10,0x77,0xc2,0x50,0x00,0xf9 };//15
//char PANID[] = {0xfe,0x04,0x26,0x05,0x83,0x02,0x12,0x34/*0xff*/,0x80};	//9
//char ZDO_STRT[] = {0xfe,0x02,0x25,0x40,0x00,0x00,0x67};	//7
//char AF_reg[] = {0xfe,0x11,0x24,0x00,0x01,0x09,0x01,0x01,0x05,0x01,0x00,0x02,0x02,0x07,0x12,0x34,0x15,0x00,0x01,0x00,0x00,0x0C}; //22
//char AF_Data[] = {0xfe,0x0b,0x24,0x01,0xff,0xff/*0xff*/,0x01,0x01,0x02,0x07,0x00,0x00,0x00,0x01,0x32,0x18};//16
//char LOG_CORD[] = {0xfe,0x03,0x26,0x05,0x87,0x01,0x00,0xa6};

    static public int processData(int action, responseStruct resp) {
        responseStruct rxPacket = resp;
        final int status = resp.dataBuffer[0] & 0xFF; // Most of the time this is the status

        switch (resp.getSubSys()) {
            case 1:
                /* SYS */
                switch (resp.cmd1 & 0xFF) {
                    case SYS_RESET_REQ: {
                    }
                    break;
                    case SYS_PING: {
                    }
                    break;
                    case SYS_VERSION: {
                    }
                    break;
                    case SYS_SET_EXTADDR: {
                    }
                    break;
                    case SYS_GET_EXTADDR: {
                    }
                    break;
                    case SYS_RAM_READ: {
                    }
                    break;
                    case SYS_RAM_WRITE: {
                    }
                    break;
                    case SYS_OSAL_NV_ITEM_INIT: {
                    }
                    break;
                    case SYS_OSAL_NV_READ: {
                    }
                    break;
                    case SYS_OSAL_NV_WRITE: {
                    }
                    break;
                    case SYS_OSAL_START_TIMER: {
                    }
                    break;
                    case SYS_OSAL_STOP_TIMER: {
                    }
                    break;
                    case SYS_RANDOM: {
                    }
                    break;
                    case SYS_ADC_READ: {
                    }
                    break;
                    case SYS_GPIO: {
                    }
                    break;
                    case SYS_STACK_TUNE: {
                    }
                    break;
                    case SYS_SET_TIME: {
                    }
                    break;
                    case SYS_GET_TIME: {
                    }
                    break;
                    case SYS_OSAL_NV_DELETE: {
                    }
                    break;
                    case SYS_OSAL_NV_LENGTH: {
                    }
                    break;
                    case SYS_SET_TX_POWER: {
                    }
                    break;
                    case SYS_JAMMER_PARAMETERS: {
                    }
                    break;
                    case SYS_SNIFFER_PARAMETERS: {
                    }
                    break;

                    case SYS_RESET_IND: {
                        if ((rxPacket.dataBuffer[0] & 0xFF) <= 1) {
                            // PowerOnn reset or external reset.
                            return (action == OP_RESET) ? 1 : 0;
                        }
                    }
                    break;
                    case SYS_OSAL_TIMER_EXPIRED: {
                    }
                    break;
                    case SYS_JAMMER_IND: {
                    }
                    break;

                }
                break;
            case 2:
                /* MAC */
                switch (resp.cmd1 & 0xFF) {
                    // MAC
                    case MAC_RESET_REQ: {
                    }
                    break;
                    case MAC_INIT: {
                    }
                    break;
                    case MAC_START_REQ: {
                    }
                    break;
                    case MAC_SYNC_REQ: {
                    }
                    break;
                    case MAC_DATA_REQ: {
                    }
                    break;
                    case MAC_ASSOCIATE_REQ: {
                    }
                    break;
                    case MAC_DISASSOCIATE_REQ: {
                    }
                    break;
                    case MAC_GET_REQ: {
                    }
                    break;
                    case MAC_SET_REQ: {
                    }
                    break;
                    case MAC_GTS_REQ: {
                    }
                    break;
                    case MAC_RX_ENABLE_REQ: {
                    }
                    break;
                    case MAC_SCAN_REQ: {
                    }
                    break;
                    case MAC_POLL_REQ: {
                    }
                    break;
                    case MAC_PURGE_REQ: {
                    }
                    break;
                    case MAC_SET_RX_GAIN_REQ: {
                    }
                    break;
                    case MAC_SECURITY_GET_REQ: {
                    }
                    break;
                    case MAC_SECURITY_SET_REQ: {
                    }
                    break;
                    case MAC_ENHANCED_ACTIVE_SCAN_REQ: {
                    }
                    break;
                    case MAC_ENHANCED_ACTIVE_SCAN_RSP: {
                    }
                    break;
                    case MAC_SRC_MATCH_ENABLE: {
                    }
                    break;
                    case MAC_SRC_MATCH_ADD_ENTRY: {
                    }
                    break;
                    case MAC_SRC_MATCH_DELETE_ENTRY: {
                    }
                    break;
                    case MAC_SRC_MATCH_ACK_ALL: {
                    }
                    break;
                    case MAC_SRC_CHECK_ALL: {
                    }
                    break;
                    case MAC_ASSOCIATE_RSP: {
                    }
                    break;
                    case MAC_ORPHAN_RSP: {
                    }
                    break;
                    case MAC_SYNC_LOSS_IND: {
                    }
                    break;
                    case MAC_ASSOCIATE_IND: {
                    }
                    break;
                    case MAC_ASSOCIATE_CNF: {
                    }
                    break;
                    case MAC_BEACON_NOTIFY_IND: {
                    }
                    break;
                    case MAC_DATA_CNF: {
                    }
                    break;
                    case MAC_DATA_IND: {
                    }
                    break;
                    case MAC_DISASSOCIATE_IND: {
                    }
                    break;
                    case MAC_DISASSOCIATE_CNF: {
                    }
                    break;
                    case MAC_GTS_CNF: {
                    }
                    break;
                    case MAC_GTS_IND: {
                    }
                    break;
                    case MAC_ORPHAN_IND: {
                    }
                    break;
                    case MAC_POLL_CNF: {
                    }
                    break;
                    case MAC_SCAN_CNF: {
                    }
                    break;
                    case MAC_COMM_STATUS_IND: {
                    }
                    break;
                    case MAC_START_CNF: {
                    }
                    break;
                    case MAC_RX_ENABLE_CNF: {
                    }
                    break;
                    case MAC_PURGE_CNF: {
                    }
                    break;
                    case MAC_POLL_IND: {
                    }
                    break;

                }
                break;
            case 3:
                /* NWK */
                switch (resp.cmd1 & 0xFF) {
                    // NWK
                    case NWK_INIT: {
                    }
                    break;
                    case NWK_DATA_REQ: {
                    }
                    break;
                    case NWK_NETWORK_FORMATION_REQ: {
                    }
                    break;
                    case NWK_PERMIT_JOINING_REQ: {
                    }
                    break;
                    case NWK_JOIN_REQ: {
                    }
                    break;
                    case NWK_LEAVE_REQ: {
                    }
                    break;
                    case NWK_RESET_REQ: {
                    }
                    break;
                    case NWK_GET_REQ: {
                    }
                    break;
                    case NWK_SET_REQ: {
                    }
                    break;
                    case NWK_NETWORK_DISCOVERY_REQ: {
                    }
                    break;
                    case NWK_ROUTE_DISCOVERY_REQ: {
                    }
                    break;
                    case NWK_DIRECT_JOIN_REQ: {
                    }
                    break;
                    case NWK_ORPHAN_JOIN_REQ: {
                    }
                    break;
                    case NWK_START_ROUTER_REQ: {
                    }
                    break;
                    case NWK_DATA_CONF: {
                    }
                    break;
                    case NWK_DATA_IND: {
                    }
                    break;
                    case NWK_NETWORK_FORMATION_CONF: {
                    }
                    break;
                    case NWK_JOIN_CONF: {
                    }
                    break;
                    case NWK_JOIN_IND: {
                    }
                    break;
                    case NWK_LEAVE_CONF: {
                    }
                    break;
                    case NWK_LEAVE_IND: {
                    }
                    break;
                    case NWK_POLL_CONF: {
                    }
                    break;
                    case NWK_SYNC_IND: {
                    }
                    break;
                    case NWK_NETWORK_DISCOVERY_CONF: {
                    }
                    break;
                    case NWK_START_ROUTER_CONF: {
                    }
                    break;

                }
                break;
            case 4:
                /* AF */
                switch (resp.cmd1 & 0xFF) {

                    // AF
                    case AF_REGISTER: {
                    }
                    break;
                    case AF_DATA_REQUEST: {
                        if (action == OP_AF_REG && status == 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                        
                        // OP_AF_DATA : return 1 was disabled
                    }
                    // no break, return above.
                    case AF_DATA_REQUEST_EXT: {
                    }
                    break;
                    case AF_DATA_REQUEST_SRCRTG: {
                    }
                    break;
                    case AF_DELETE: {
                    }
                    break;
                    case AF_INTER_PAN_CTL: {
                    }
                    break;
                    case AF_DATA_STORE: {
                    }
                    break;
                    case AF_DATA_RETRIEVE: {
                    }
                    break;
                    case AF_APSF_CONFIG_SET: {
                    }
                    break;
                    case AF_APSF_CONFIG_GET: {
                    }
                    break;
                    case AF_DATA_CONFIRM: {
                    }
                    break;
                    case AF_INCOMING_MSG: {
                    }
                    break;
                    case AF_INCOMING_MSG_EXT: {
                    }
                    break;
                    case AF_REFLECT_ERROR: {
                    }
                    break;

                }
                break;
            case 5:
                /* ZDO */
                switch (resp.cmd1 & 0xFF) {
                    // ZDO
                    case ZDO_NWK_ADDR_REQ: {
                        if (action == OP_ZDO_START && rxPacket.dataBuffer[0] == 1) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }

                    case ZDO_IEEE_ADDR_REQ: {
                    }
                    break;
                    case ZDO_NODE_DESC_REQ: {
                    }
                    break;
                    case ZDO_POWER_DESC_REQ: {
                    }
                    break;
                    case ZDO_SIMPLE_DESC_REQ: {
                    }
                    break;
                    case ZDO_ACTIVE_EP_REQ: {
                    }
                    break;
                    case ZDO_MATCH_DESC_REQ: {
                    }
                    break;
                    case ZDO_COMPLEX_DESC_REQ: {
                    }
                    break;
                    case ZDO_USER_DESC_REQ: {
                    }
                    break;
                    case ZDO_END_DEV_ANNCE: {
                    }
                    break;
                    case ZDO_USER_DESC_SET: {
                    }
                    break;
                    case ZDO_SERVICE_DISC_REQ: {
                    }
                    break;
                    case ZDO_END_DEVICE_TIMEOUT_REQ: {
                    }
                    break;
                    case ZDO_END_DEV_BIND_REQ: {
                    }
                    break;
                    case ZDO_BIND_REQ: {
                    }
                    break;
                    case ZDO_UNBIND_REQ: {
                    }
                    break;
                    case ZDO_SET_LINK_KEY: {
                    }
                    break;
                    case ZDO_REMOVE_LINK_KEY: {
                    }
                    break;
                    case ZDO_GET_LINK_KEY: {
                    }
                    break;
                    case ZDO_NWK_DISCOVERY_REQ: {
                    }
                    break;
                    case ZDO_JOIN_REQ: {
                    }
                    break;
                    case ZDO_SEND_DATA: {
                    }
                    break;
                    case ZDO_NWK_ADDR_OF_INTEREST_REQ: {
                    }
                    break;
                    case ZDO_MGMT_NWKDISC_REQ: {
                    }
                    break;
                    case ZDO_MGMT_LQI_REQ: {
                    }
                    break;
                    case ZDO_MGMT_RTG_REQ: {
                    }
                    break;
                    case ZDO_MGMT_BIND_REQ: {
                    }
                    break;
                    case ZDO_MGMT_LEAVE_REQ: {
                    }
                    break;
                    case ZDO_MGMT_DIRECT_JOIN_REQ: {
                    }
                    break;
                    case ZDO_MGMT_PERMIT_JOIN_REQ: {
                    }
                    break;
                    case ZDO_MGMT_NWK_UPDATE_REQ: {
                    }
                    break;
                    case ZDO_MSG_CB_REGISTER: {
                    }
                    break;
                    case ZDO_MSG_CB_REMOVE: {
                    }
                    break;
                    case ZDO_STARTUP_FROM_APP: {
                    }
                    break;
                    case ZDO_AUTO_FIND_DESTINATION_REQ: {
                    }
                    break;
                    case ZDO_SEC_ADD_LINK_KEY: {
                    }
                    break;
                    case ZDO_SEC_ENTRY_LOOKUP_EXT: {
                    }
                    break;
                    case ZDO_SEC_DEVICE_REMOVE: {
                    }
                    break;
                    case ZDO_EXT_ROUTE_DISC: {
                    }
                    break;
                    case ZDO_EXT_ROUTE_CHECK: {
                    }
                    break;
                    case ZDO_EXT_REMOVE_GROUP: {
                    }
                    break;
                    case ZDO_EXT_REMOVE_ALL_GROUP: {
                    }
                    break;
                    case ZDO_EXT_FIND_ALL_GROUPS_ENDPOINT: {
                    }
                    break;
                    case ZDO_EXT_FIND_GROUP: {
                    }
                    break;
                    case ZDO_EXT_ADD_GROUP: {
                    }
                    break;
                    case ZDO_EXT_COUNT_ALL_GROUPS: {
                    }
                    break;
                    case ZDO_EXT_RX_IDLE: {
                    }
                    break;
                    case ZDO_EXT_UPDATE_NWK_KEY: {
                    }
                    break;
                    case ZDO_EXT_SWITCH_NWK_KEY: {
                    }
                    break;
                    case ZDO_EXT_NWK_INFO: {
                    }
                    break;
                    case ZDO_EXT_SEC_APS_REMOVE_REQ: {
                    }
                    break;
                    case ZDO_FORCE_CONCENTRATOR_CHANGE: {
                    }
                    break;
                    case ZDO_EXT_SET_PARAMS: {
                    }
                    break;
                    case ZDO_NWK_ADDR_RSP: {
                    }
                    break;
                    case ZDO_IEEE_ADDR_RSP: {
                    }
                    break;
                    case ZDO_NODE_DESC_RSP: {
                    }
                    break;
                    case ZDO_POWER_DESC_RSP: {
                    }
                    break;
                    case ZDO_SIMPLE_DESC_RSP: {
                    }
                    break;
                    case ZDO_ACTIVE_EP_RSP: {
                    }
                    break;
                    case ZDO_MATCH_DESC_RSP: {
                    }
                    break;
                    case ZDO_COMPLEX_DESC_RSP: {
                    }
                    break;
                    case ZDO_USER_DESC_RSP: {
                    }
                    break;
                    case ZDO_USER_DESC_CONF: {
                    }
                    break;
                    case ZDO_SERVER_DISC_RSP: {
                    }
                    break;
                    case ZDO_DISCOVERY_CACHE_REQ: {
                    }
                    break;
                    case ZDO_END_DEVICE_TIMEOUT_RSP: {
                    }
                    break;
                    case ZDO_END_DEVICE_BIND_RSP: {
                    }
                    break;
                    case ZDO_BIND_RSP: {
                    }
                    break;
                    case ZDO_UNBIND_RSP: {
                    }
                    break;
                    case ZDO_MGMT_NWK_DISC_RSP: {
                    }
                    break;
                    case ZDO_MGMT_LQI_RSP: {
                    }
                    break;
                    case ZDO_MGMT_RTG_RSP: {
                    }
                    break;
                    case ZDO_MGMT_BIND_RSP: {
                    }
                    break;
                    case ZDO_MGMT_LEAVE_RSP: {
                    }
                    break;
                    case ZDO_MGMT_DIRECT_JOIN_RSP: {
                    }
                    break;
                    case ZDO_MGMT_PERMIT_JOIN_RSP: {
                    }
                    break;
                    case ZDO_MGMT_NWK_UPDATE_RSP: {
                    }
                    break;
                    case ZDO_STATE_CHANGE_IND: {
                        if (action==OP_ZDO_STATE) {
                            return status; // status byte is the same as the network status byte
                        }
                    }
                    break;
                    case ZDO_END_DEVICE_ANNCE_IND: {
                    }
                    break;
                    case ZDO_MATCH_DESC_RSP_SENT: {
                    }
                    break;
                    case ZDO_STATUS_ERROR_RSP: {
                    }
                    break;
                    case ZDO_SRC_RTG_IND: {
                    }
                    break;
                    case ZDO_BEACON_NOTIFY_IND: {
                    }
                    break;
                    case ZDO_JOIN_CNF: {
                    }
                    break;
                    case ZDO_NWK_DISCOVERY_CNF: {
                    }
                    break;
                    case ZDO_CONCENTRATOR_IND_CB: {
                    }
                    break;
                    case ZDO_LEAVE_IND: {
                    }
                    break;
                    case ZDO_TC_DEVICE_IND: {
                    }
                    break;
                    case ZDO_PERMIT_JOIN_IND: {
                    }
                    break;
                    case ZDO_MSG_CB_INCOMING: {
                    }
                    break;

                }
                break;
            case 6: // SAPI
                switch (resp.cmd1 & 0xFF) {
                    case SAPI_START_REQ: {
                    }
                    break;
                    case SAPI_BIND_DEVICE_REQ: {
                    }
                    break;
                    case SAPI_ALLOW_BIND_REQ: {
                    }
                    break;
                    case SAPI_SEND_DATA_REQ: {
                    }
                    break;
                    case SAPI_READ_CFG_REQ: {
                    }
                    break;
                    case SAPI_WRITE_CFG_REQ: {
                        if (status == 0) {
                            switch (action) {
                                case OP_WR_CFG:
                                case OP_WR_LOG_TYPE:
                                case OP_WR_PAN_ID:
                                case OP_WR_EPAN_ID:
                                    return 1;
                                default:
                                    return 0;
                            }
                        }
                    }
                    break;
                    case SAPI_GET_DEV_INFO_REQ: {
                    }
                    break;
                    case SAPI_FIND_DEV_REQ: {
                    }
                    break;
                    case SAPI_PMT_JOIN_REQ: {
                    }
                    break;
                    case SAPI_APP_REGISTER_REQ: {
                    }
                    break;
                    case SAPI_START_CNF: {
                    }
                    break;
                    case SAPI_BIND_CNF: {
                    }
                    break;
                    case SAPI_ALLOW_BIND_CNF: {
                    }
                    break;
                    case SAPI_SEND_DATA_CNF: {
                    }
                    break;
                    case SAPI_READ_CFG_RSP: {
                    }
                    break;
                    case SAPI_FIND_DEV_CNF: {
                    }
                    break;
                    case SAPI_DEV_INFO_RSP: {
                        int a = rxPacket.dataBuffer.length;
                        if (action==OP_IEEE_ADDRS&&rxPacket.dataBuffer.length == 9) {
                            return 1;
                        }
                    }
                    break;
                    case SAPI_RCV_DATA_IND: {
                    }
                    break;
                }
                break;
            case 7:
                /* UTIL */
                switch (resp.cmd1 & 0xFF) {
                    // UTIL    case UTIL_GET_DEVICE_INFO: { } break;
                    case UTIL_GET_NV_INFO: {
                    }
                    break;
                    case UTIL_SET_PANID: {
                    }
                    break;
                    case UTIL_SET_CHANNELS: {
                    }
                    break;
                    case UTIL_SET_SECLEVEL: {
                    }
                    break;
                    case UTIL_SET_PRECFGKEY: {
                    }
                    break;
                    case UTIL_CALLBACK_SUB_CMD: {
                    }
                    break;
                    case UTIL_KEY_EVENT: {
                    }
                    break;
                    case UTIL_TIME_ALIVE: {
                    }
                    break;
                    case UTIL_LED_CONTROL: {
                    }
                    break;
                    case UTIL_TEST_LOOPBACK: {
                    }
                    break;
                    case UTIL_DATA_REQ: {
                    }
                    break;
                    case UTIL_GPIO_SET_DIRECTION: {
                    }
                    break;
                    case UTIL_GPIO_READ: {
                    }
                    break;
                    case UTIL_GPIO_WRITE: {
                    }
                    break;
                    case UTIL_SRC_MATCH_ENABLE: {
                    }
                    break;
                    case UTIL_SRC_MATCH_ADD_ENTRY: {
                    }
                    break;
                    case UTIL_SRC_MATCH_DEL_ENTRY: {
                    }
                    break;
                    case UTIL_SRC_MATCH_CHECK_SRC_ADDR: {
                    }
                    break;
                    case UTIL_SRC_MATCH_ACK_ALL_PENDING: {
                    }
                    break;
                    case UTIL_SRC_MATCH_CHECK_ALL_PENDING: {
                    }
                    break;
                    case UTIL_ADDRMGR_EXT_ADDR_LOOKUP: {
                    }
                    break;
                    case UTIL_ADDRMGR_NWK_ADDR_LOOKUP: {
                    }
                    break;
                    case UTIL_APSME_LINK_KEY_DATA_GET: {
                    }
                    break;
                    case UTIL_APSME_LINK_KEY_NV_ID_GET: {
                    }
                    break;
                    case UTIL_ASSOC_COUNT: {
                    }
                    break;
                    case UTIL_ASSOC_FIND_DEVICE: {
                    }
                    break;
                    case UTIL_ASSOC_GET_WITH_ADDRESS: {
                    }
                    break;
                    case UTIL_APSME_REQUEST_KEY_CMD: {
                    }
                    break;
                    case UTIL_SRNG_GENERATE: {
                    }
                    break;
                    case UTIL_BIND_ADD_ENTRY: {
                    }
                    break;
                    case UTIL_ZCL_KEY_EST_INIT_EST: {
                    }
                    break;
                    case UTIL_ZCL_KEY_EST_SIGN: {
                    }
                    break;
                    case UTIL_GET_DEV_NWK_INFO: {
                    }
                    break;
                    case UTIL_SET_DEV_NWK_INFO: {
                    }
                    break;
                    case UTIL_SYNC_REQ: {
                    }
                    break;
                    case UTIL_ZCL_KEY_ESTABLISH_IND: {
                    }
                    break;

                }
                break;
            case 15:
                /**
                 * APP_CNF
                 */
                switch (resp.cmd1 & 0xFF) {
                    // APP_CNF
                    case APP_CNF_SET_DEFAULT_REMOTE_ENDDEVICE_TIMEOUT: {
                    }
                    break;
                    case APP_CNF_SET_ENDDEVICETIMEOUT: {
                    }
                    break;
                    case APP_CNF_SET_ALLOWREJOIN_TC_POLICY: {
                    }
                    break;
                    case APP_CNF_ADD_INSTALLCODE: {
                    }
                    break;
                    case APP_CNF_START_COMMISSIONING: {
                    }
                    break;
                    case APP_CNF_SET_JOINUSESINSTALLCODEKEY: {
                    }
                    break;
                    case APP_CNF_SET_ACTIVE_DEFAULT_CENTRALIZED_KEY: {
                    }
                    break;
                    case APP_CNF_SET_CHANNEL: {
                    }
                    break;
                    case APP_CNF_SET_TC_REQUIRE_KEY_EXCHANGE: {
                    }
                    break;
                    case APP_CNF_ZED_ATTEMPT_RECOVER_NWK: {
                    }
                    break;
                    case APP_CNF_BDB_COMMISSIONING_NOTIFICATION: {
                    }
                    break;
                    case APP_CNF_SET_NWK_FRAME_COUNTER: {
                    }
                    break;
                }
                break;
        }
        return 0;
    }
}
