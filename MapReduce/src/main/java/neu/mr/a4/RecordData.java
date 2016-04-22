package neu.mr.a4;

import java.util.Date;

/**
 * This class will hold a record data.
 * 
 * @author Abhishek Ravi Chandran
 * @author Chinmayee Vaidhya
 * 
 *
 */
public class RecordData {

	private int year;
	private int month;
	private int day;
	private boolean cancelled;
	private float avgTicketPrice;
	private Date arrTime;
	private Date depTime;
	private float arrDelay;
	private int actualElapsedTime;
	private float arrivalDelayNew;
	private float arrDel15;
	private Date crsArrTime;
	private Date crsDepTime;
	private int crsElapsedTime;
	private int destAirportId;
	private int originAirportId;
	private int destAirportSeqId;
	private int originAirportSeqId;
	private int destCityMArketId;
	private int originCityMarketId;
	private int destStateFips;
	private int OriginStateFips;
	private int DestWac;
	private int OriginWac;
	private String origin;
	private String dest;
	private String originCityName;
	private String destCityName;
	private String originStateNm;
	private String originStateAbr;
	private String destStateNm;
	private String destStateAbr;
	private String carrier;

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int i) {
		this.year = i;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public float getAvgTicketPrice() {
		return avgTicketPrice;
	}

	public void setAvgTicketPrice(float avgTicketPrice) {
		this.avgTicketPrice = avgTicketPrice;
	}

	public Date getArrTime() {
		return arrTime;
	}

	public void setArrTime(Date arrTime) {
		this.arrTime = arrTime;
	}

	public Date getDepTime() {
		return depTime;
	}

	public void setDepTime(Date depTime) {
		this.depTime = depTime;
	}

	public float getArrDelay() {
		return arrDelay;
	}

	public void setArrDelay(float arrDelay) {
		this.arrDelay = arrDelay;
	}

	public int getActualElapsedTime() {
		return actualElapsedTime;
	}

	public void setActualElapsedTime(int actualElapsedTime) {
		this.actualElapsedTime = actualElapsedTime;
	}

	public double getArrivalDelayNew() {
		return arrivalDelayNew;
	}

	public void setArrivalDelayNew(float arrivalDelayNew) {
		this.arrivalDelayNew = arrivalDelayNew;
	}

	public float getArrDel15() {
		return arrDel15;
	}

	public void setArrDel15(float arrDel15) {
		this.arrDel15 = arrDel15;
	}

	public Date getCrsArrTime() {
		return crsArrTime;
	}

	public void setCrsArrTime(Date crsArrTime) {
		this.crsArrTime = crsArrTime;
	}

	public Date getCrsDepTime() {
		return crsDepTime;
	}

	public void setCrsDepTime(Date crsDepTime) {
		this.crsDepTime = crsDepTime;
	}

	public int getCrsElapsedTime() {
		return crsElapsedTime;
	}

	public void setCrsElapsedTime(int crsElapsedTime) {
		this.crsElapsedTime = crsElapsedTime;
	}

	public int getDestAirportId() {
		return destAirportId;
	}

	public void setDestAirportId(int destAirportId) {
		this.destAirportId = destAirportId;
	}

	public int getOriginAirportId() {
		return originAirportId;
	}

	public void setOriginAirportId(int originAirportId) {
		this.originAirportId = originAirportId;
	}

	public int getDestAirportSeqId() {
		return destAirportSeqId;
	}

	public void setDestAirportSeqId(int destAirportSeqId) {
		this.destAirportSeqId = destAirportSeqId;
	}

	public int getOriginAirportSeqId() {
		return originAirportSeqId;
	}

	public void setOriginAirportSeqId(int originAirportSeqId) {
		this.originAirportSeqId = originAirportSeqId;
	}

	public int getDestCityMArketId() {
		return destCityMArketId;
	}

	public void setDestCityMArketId(int destCityMArketId) {
		this.destCityMArketId = destCityMArketId;
	}

	public int getOriginCityMarketId() {
		return originCityMarketId;
	}

	public void setOriginCityMarketId(int originCityMarketId) {
		this.originCityMarketId = originCityMarketId;
	}

	public int getDestStateFips() {
		return destStateFips;
	}

	public void setDestStateFips(int destStateFips) {
		this.destStateFips = destStateFips;
	}

	public int getOriginStateFips() {
		return OriginStateFips;
	}

	public void setOriginStateFips(int originStateFips) {
		OriginStateFips = originStateFips;
	}

	public int getDestWac() {
		return DestWac;
	}

	public void setDestWac(int destWac) {
		DestWac = destWac;
	}

	public int getOriginWac() {
		return OriginWac;
	}

	public void setOriginWac(int originWac) {
		OriginWac = originWac;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getOriginCityName() {
		return originCityName;
	}

	public void setOriginCityName(String originCityName) {
		this.originCityName = originCityName;
	}

	public String getDestCityName() {
		return destCityName;
	}

	public void setDestCityName(String destCityName) {
		this.destCityName = destCityName;
	}

	public String getOriginStateNm() {
		return originStateNm;
	}

	public void setOriginStateNm(String originStateNm) {
		this.originStateNm = originStateNm;
	}

	public String getOriginStateAbr() {
		return originStateAbr;
	}

	public void setOriginStateAbr(String originStateAbr) {
		this.originStateAbr = originStateAbr;
	}

	public String getDestStateNm() {
		return destStateNm;
	}

	public void setDestStateNm(String destStateNm) {
		this.destStateNm = destStateNm;
	}

	public String getDestStateAbr() {
		return destStateAbr;
	}

	public void setDestStateAbr(String destStateAbr) {
		this.destStateAbr = destStateAbr;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
