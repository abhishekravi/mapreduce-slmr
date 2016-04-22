package neu.mr.a2;


/**
 * 
 * @author chintanpathak
 *
 */

// Class to hold the position of the csv data columns
// and map them to the corresponding indices in the array
// after splitting each line of the file using delimiter ","
public class FlightDataIndexes {

	public static final Integer TOTAL_NUM_OF_COLUMNS = 110;

	public static final Integer YEAR = 0;
	public static final Integer QUARTER = 1;
	public static final Integer MONTH = 2;
	public static final Integer DAYOFMONTH = 3;
	public static final Integer DAYOFWEEK = 4;
	public static final Integer FLDATE = 5;
	public static final Integer UNIQUECARRIER = 6;
	public static final Integer AIRLINEID = 7;
	public static final Integer CARRIER = 8;
	public static final Integer TAILNUM = 9;
	public static final Integer FLNUM = 10;
	public static final Integer ORIGINAIRPORTID = 11;
	public static final Integer ORIGINAIRPORTSEQID = 12;
	public static final Integer ORIGINCITYMARKETID = 13;
	public static final Integer ORIGIN = 14;
	public static final Integer ORIGINCITYNAME = 15;
	public static final Integer ORIGINSTATEABR = 16;
	public static final Integer ORIGINSTATEFIPS = 17;
	public static final Integer ORIGINSTATENM = 18;
	public static final Integer ORIGINWAC = 19;
	public static final Integer DESTAIRPORTID = 20;
	public static final Integer DESTAIRPORTSEQID = 21;
	public static final Integer DESTCITYMARKETID = 22;
	public static final Integer DEST = 23;
	public static final Integer DESTCITYNAME = 24;
	public static final Integer DESTSTATEABR = 25;
	public static final Integer DESTSTATEFIPS = 26;
	public static final Integer DESTSTATENM = 27;
	public static final Integer DESTWAC = 28;
	public static final Integer CRSDEPTIME = 29;
	public static final Integer DEPTIME = 30;
	public static final Integer DEPDELAY = 31;
	public static final Integer DEPDELAYNEW = 32;
	public static final Integer DEPDELAY15 = 33;
	public static final Integer DEPDELAYGROUP = 34;
	public static final Integer DEPTIMEBLK = 35;
	public static final Integer TAXIOUT = 36;
	public static final Integer WHEELSOFF = 37;
	public static final Integer WHEELSON = 38;
	public static final Integer TAXIIN = 39;
	public static final Integer CRSARRTIME = 40;
	public static final Integer ARRTIME = 41;
	public static final Integer ARRDELAY = 42;
	public static final Integer ARRDELAYNEW = 43;
	public static final Integer ARRDELAY15 = 44;
	public static final Integer ARRDELAYGROUP = 45;
	public static final Integer ARRTIMEBLK = 46;
	public static final Integer CANCELLED = 47;
	public static final Integer CANCELLATIONCODE = 48;
	public static final Integer DIVERTED = 49;
	public static final Integer CRSELAPSEDTIME = 50;
	public static final Integer ACTUALELAPSEDTIME = 51;
	public static final Integer AIRTIME = 52;
	public static final Integer FLIGHTS = 53;
	public static final Integer DISTANCE = 54;
	public static final Integer DISTANCEGROUP = 55;
	public static final Integer CARRIERDELAY = 56;
	public static final Integer WEATHERDELAY = 57;
	public static final Integer NASDELAY = 58;
	public static final Integer SECURITYDELAY = 59;
	public static final Integer LATEAIRCRAFTDELAY = 60;
	public static final Integer FIRSTDEPTIME = 61;
	public static final Integer TOTALADDGTIME = 62;
	public static final Integer LONGESTADDGTIME = 63;
	public static final Integer DIVAIRPORTLANDINGS = 64;
	public static final Integer DIVREACHEDDEST = 65;
	public static final Integer DIVACTUALELAPSEDTIME = 66;
	public static final Integer DIVARRDELAY = 67;
	public static final Integer DIVDISTANCE = 68;
	public static final Integer DIV1AIRPORT = 69;
	public static final Integer DIV1AIRPORTID = 70;
	public static final Integer DIV1AIRPORTSEQID = 71;
	public static final Integer DIV1WHEELSON = 72;
	public static final Integer DIV1TOTALGTIME = 73;
	public static final Integer DIV1LONGESTGTIME = 74;
	public static final Integer DIV1WHEELSOFF = 75;
	public static final Integer DIV1TAILNUM = 76;
	public static final Integer DIV2AIRPORT = 77;
	public static final Integer DIV2AIRPORTID = 78;
	public static final Integer DIV2AIRPORTSEQID = 79;
	public static final Integer DIV2WHEELSON = 80;
	public static final Integer DIV2TOTALGTIME = 81;
	public static final Integer DIV2LONGESTGTIME = 82;
	public static final Integer DIV2WHEELSOFF = 83;
	public static final Integer DIV2TAILNUM = 84;
	public static final Integer DIV3AIRPORT = 85;
	public static final Integer DIV3AIRPORTID = 86;
	public static final Integer DIV3AIRPORTSEQID = 87;
	public static final Integer DIV3WHEELSON = 88;
	public static final Integer DIV3TOTALGTIME = 89;
	public static final Integer DIV3LONGESTGTIME = 90;
	public static final Integer DIV3WHEELSOFF = 91;
	public static final Integer DIV3TAILNUM = 92;
	public static final Integer DIV4AIRPORT = 93;
	public static final Integer DIV4AIRPORTID = 94;
	public static final Integer DIV4AIRPORTSEQID = 95;
	public static final Integer DIV4WHEELSON = 96;
	public static final Integer DIV4TOTALGTIME = 97;
	public static final Integer DIV4LONGESTGTIME = 98;
	public static final Integer DIV4WHEELSOFF = 99;
	public static final Integer DIV4TAILNUM = 100;
	public static final Integer DIV5AIRPORT = 101;
	public static final Integer DIV5AIRPORTID = 102;
	public static final Integer DIV5AIRPORTSEQID = 103;
	public static final Integer DIV5WHEELSON = 104;
	public static final Integer DIV5TOTALGTIME = 105;
	public static final Integer DIV5LONGESTGTIME = 106;
	public static final Integer DIV5WHEELSOFF = 107;
	public static final Integer DIV5TAILNUM = 108;
	public static final Integer AVGTICKETPRICE = 109;
}
