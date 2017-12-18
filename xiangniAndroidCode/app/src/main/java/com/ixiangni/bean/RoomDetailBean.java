package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class RoomDetailBean {

    /**
     * message : 操作成功!
     * data : {"hid":"39652","guid":"992739d6-a0a1-3ad2-b9c4-6d042d67500a","roomtype":"0","hotelsupplier":"zn","allrate":"10","yuding":"True","tm1":"2017-07-28","tm2":"2017-08-25","allotmenttype":"","ratetype":"","supplierid":"","facepaytype":"","faceprice":"","includebreakfastqty2":"","HotelName":"","RoomName":"商务客房","RatePlanName":"不含早（精选特惠）.","AvailableAmount":"8","roomsInfo":{"desc":"配有厨房（免费使用）","bed":"双床1.3米","adsl":"有(收费)","area":"58","floor":"6-25","RatePlanName":"不含早（精选特惠）."},"prices":{"fistDayPrice":"588","TotalJiangjin":"1646","TotalPrice":"16464","cost":"16464","CurrencyCode":"RMB","daill":[{"date":"2017-07-28","price":"588"},{"date":"2017-07-29","price":"588"},{"date":"2017-07-30","price":"588"},{"date":"2017-07-31","price":"588"},{"date":"2017-08-01","price":"588"},{"date":"2017-08-02","price":"588"},{"date":"2017-08-03","price":"588"},{"date":"2017-08-04","price":"588"},{"date":"2017-08-05","price":"588"},{"date":"2017-08-06","price":"588"},{"date":"2017-08-07","price":"588"},{"date":"2017-08-08","price":"588"},{"date":"2017-08-09","price":"588"},{"date":"2017-08-10","price":"588"},{"date":"2017-08-11","price":"588"},{"date":"2017-08-12","price":"588"},{"date":"2017-08-13","price":"588"},{"date":"2017-08-14","price":"588"},{"date":"2017-08-15","price":"588"},{"date":"2017-08-16","price":"588"},{"date":"2017-08-17","price":"588"},{"date":"2017-08-18","price":"588"},{"date":"2017-08-19","price":"588"},{"date":"2017-08-20","price":"588"},{"date":"2017-08-21","price":"588"},{"date":"2017-08-22","price":"588"},{"date":"2017-08-23","price":"588"},{"date":"2017-08-24","price":"588"}]},"AddValues":{"string":"System.Collections.ArrayList"},"GaranteeRule":{"romms":"0","status":"1","norule":"0","stattime":"20:00","endtime":"06:00","desc":"担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。"},"GaranteeRule2":{"GaranteeRule":{"GaranteeRulesTypeCode":"1","Description":"担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。","RuleValues":{"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-28T00:00:00"},{"Key":"EndDate","Value":"2017-07-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"1"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"0"},{"Key":"IsChange","Value":"2"},{"Key":"TimeNum","Value":"18:00"}]}}},"ratesinfo":"jike_test^3588^1646^10|","ratedaill":"<tr class=\"hotel-date-list-trbg\"><td>07-28<\/td><td>07-29<\/td><td>07-30<\/td><td>07-31<\/td><td>08-01<\/td><td>08-02<\/td><\/tr><tr><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><\/tr><tr class=\"hotel-date-list-trbg\"><td>08-03<\/td><td>08-04<\/td><td>08-05<\/td><td>08-06<\/td><td>08-07<\/td><td>08-08<\/td><\/tr><tr><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><\/tr><tr class=\"hotel-date-list-trbg\"><td>08-09<\/td><td>08-10<\/td><td>08-11<\/td><td>08-12<\/td><td>08-13<\/td><td>08-14<\/td><\/tr><tr><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><\/tr><tr class=\"hotel-date-list-trbg\"><td>08-15<\/td><td>08-16<\/td><td>08-17<\/td><td>08-18<\/td><td>08-19<\/td><td>08-20<\/td><\/tr><tr><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td><\/tr><tr class=\"hotel-date-list-trbg\"><td>08-21<\/td><td>08-22<\/td><td>08-23<\/td><td>08-24<\/td><tr><td>588<\/td><td>588<\/td><td>588<\/td><td>588<\/td>"}
     * status : 1
     */

    private String message;
    private DataBean data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * hid : 39652
         * guid : 992739d6-a0a1-3ad2-b9c4-6d042d67500a
         * roomtype : 0
         * hotelsupplier : zn
         * allrate : 10
         * yuding : True
         * tm1 : 2017-07-28
         * tm2 : 2017-08-25
         * allotmenttype :
         * ratetype :
         * supplierid :
         * facepaytype :
         * faceprice :
         * includebreakfastqty2 :
         * HotelName :
         * RoomName : 商务客房
         * RatePlanName : 不含早（精选特惠）.
         * AvailableAmount : 8
         * roomsInfo : {"desc":"配有厨房（免费使用）","bed":"双床1.3米","adsl":"有(收费)","area":"58","floor":"6-25","RatePlanName":"不含早（精选特惠）."}
         * prices : {"fistDayPrice":"588","TotalJiangjin":"1646","TotalPrice":"16464","cost":"16464","CurrencyCode":"RMB","daill":[{"date":"2017-07-28","price":"588"},{"date":"2017-07-29","price":"588"},{"date":"2017-07-30","price":"588"},{"date":"2017-07-31","price":"588"},{"date":"2017-08-01","price":"588"},{"date":"2017-08-02","price":"588"},{"date":"2017-08-03","price":"588"},{"date":"2017-08-04","price":"588"},{"date":"2017-08-05","price":"588"},{"date":"2017-08-06","price":"588"},{"date":"2017-08-07","price":"588"},{"date":"2017-08-08","price":"588"},{"date":"2017-08-09","price":"588"},{"date":"2017-08-10","price":"588"},{"date":"2017-08-11","price":"588"},{"date":"2017-08-12","price":"588"},{"date":"2017-08-13","price":"588"},{"date":"2017-08-14","price":"588"},{"date":"2017-08-15","price":"588"},{"date":"2017-08-16","price":"588"},{"date":"2017-08-17","price":"588"},{"date":"2017-08-18","price":"588"},{"date":"2017-08-19","price":"588"},{"date":"2017-08-20","price":"588"},{"date":"2017-08-21","price":"588"},{"date":"2017-08-22","price":"588"},{"date":"2017-08-23","price":"588"},{"date":"2017-08-24","price":"588"}]}
         * AddValues : {"string":"System.Collections.ArrayList"}
         * GaranteeRule : {"romms":"0","status":"1","norule":"0","stattime":"20:00","endtime":"06:00","desc":"担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。"}
         * GaranteeRule2 : {"GaranteeRule":{"GaranteeRulesTypeCode":"1","Description":"担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。","RuleValues":{"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-28T00:00:00"},{"Key":"EndDate","Value":"2017-07-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"1"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"0"},{"Key":"IsChange","Value":"2"},{"Key":"TimeNum","Value":"18:00"}]}}}
         * ratesinfo : jike_test^3588^1646^10|
         * ratedaill : <tr class="hotel-date-list-trbg"><td>07-28</td><td>07-29</td><td>07-30</td><td>07-31</td><td>08-01</td><td>08-02</td></tr><tr><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td></tr><tr class="hotel-date-list-trbg"><td>08-03</td><td>08-04</td><td>08-05</td><td>08-06</td><td>08-07</td><td>08-08</td></tr><tr><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td></tr><tr class="hotel-date-list-trbg"><td>08-09</td><td>08-10</td><td>08-11</td><td>08-12</td><td>08-13</td><td>08-14</td></tr><tr><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td></tr><tr class="hotel-date-list-trbg"><td>08-15</td><td>08-16</td><td>08-17</td><td>08-18</td><td>08-19</td><td>08-20</td></tr><tr><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td><td>588</td></tr><tr class="hotel-date-list-trbg"><td>08-21</td><td>08-22</td><td>08-23</td><td>08-24</td><tr><td>588</td><td>588</td><td>588</td><td>588</td>
         */

        private String hid;
        private String guid;
        private String roomtype;
        private String hotelsupplier;
        private String allrate;
        private String yuding;
        private String tm1;
        private String tm2;
        private String allotmenttype;
        private String ratetype;
        private String supplierid;
        private String facepaytype;
        private String faceprice;
        private String includebreakfastqty2;
        private String HotelName;
        private String RoomName;
        private String RatePlanName;
        private String AvailableAmount;
        private RoomsInfoBean roomsInfo;
        private PricesBean prices;
        private AddValuesBean AddValues;
        private GaranteeRuleBean GaranteeRule;
        private GaranteeRule2Bean GaranteeRule2;
        private String ratesinfo;
        private String ratedaill;

        public String getHid() {
            return hid;
        }

        public void setHid(String hid) {
            this.hid = hid;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getRoomtype() {
            return roomtype;
        }

        public void setRoomtype(String roomtype) {
            this.roomtype = roomtype;
        }

        public String getHotelsupplier() {
            return hotelsupplier;
        }

        public void setHotelsupplier(String hotelsupplier) {
            this.hotelsupplier = hotelsupplier;
        }

        public String getAllrate() {
            return allrate;
        }

        public void setAllrate(String allrate) {
            this.allrate = allrate;
        }

        public String getYuding() {
            return yuding;
        }

        public void setYuding(String yuding) {
            this.yuding = yuding;
        }

        public String getTm1() {
            return tm1;
        }

        public void setTm1(String tm1) {
            this.tm1 = tm1;
        }

        public String getTm2() {
            return tm2;
        }

        public void setTm2(String tm2) {
            this.tm2 = tm2;
        }

        public String getAllotmenttype() {
            return allotmenttype;
        }

        public void setAllotmenttype(String allotmenttype) {
            this.allotmenttype = allotmenttype;
        }

        public String getRatetype() {
            return ratetype;
        }

        public void setRatetype(String ratetype) {
            this.ratetype = ratetype;
        }

        public String getSupplierid() {
            return supplierid;
        }

        public void setSupplierid(String supplierid) {
            this.supplierid = supplierid;
        }

        public String getFacepaytype() {
            return facepaytype;
        }

        public void setFacepaytype(String facepaytype) {
            this.facepaytype = facepaytype;
        }

        public String getFaceprice() {
            return faceprice;
        }

        public void setFaceprice(String faceprice) {
            this.faceprice = faceprice;
        }

        public String getIncludebreakfastqty2() {
            return includebreakfastqty2;
        }

        public void setIncludebreakfastqty2(String includebreakfastqty2) {
            this.includebreakfastqty2 = includebreakfastqty2;
        }

        public String getHotelName() {
            return HotelName;
        }

        public void setHotelName(String HotelName) {
            this.HotelName = HotelName;
        }

        public String getRoomName() {
            return RoomName;
        }

        public void setRoomName(String RoomName) {
            this.RoomName = RoomName;
        }

        public String getRatePlanName() {
            return RatePlanName;
        }

        public void setRatePlanName(String RatePlanName) {
            this.RatePlanName = RatePlanName;
        }

        public String getAvailableAmount() {
            return AvailableAmount;
        }

        public void setAvailableAmount(String AvailableAmount) {
            this.AvailableAmount = AvailableAmount;
        }

        public RoomsInfoBean getRoomsInfo() {
            return roomsInfo;
        }

        public void setRoomsInfo(RoomsInfoBean roomsInfo) {
            this.roomsInfo = roomsInfo;
        }

        public PricesBean getPrices() {
            return prices;
        }

        public void setPrices(PricesBean prices) {
            this.prices = prices;
        }

        public AddValuesBean getAddValues() {
            return AddValues;
        }

        public void setAddValues(AddValuesBean AddValues) {
            this.AddValues = AddValues;
        }

        public GaranteeRuleBean getGaranteeRule() {
            return GaranteeRule;
        }

        public void setGaranteeRule(GaranteeRuleBean GaranteeRule) {
            this.GaranteeRule = GaranteeRule;
        }

        public GaranteeRule2Bean getGaranteeRule2() {
            return GaranteeRule2;
        }

        public void setGaranteeRule2(GaranteeRule2Bean GaranteeRule2) {
            this.GaranteeRule2 = GaranteeRule2;
        }

        public String getRatesinfo() {
            return ratesinfo;
        }

        public void setRatesinfo(String ratesinfo) {
            this.ratesinfo = ratesinfo;
        }

        public String getRatedaill() {
            return ratedaill;
        }

        public void setRatedaill(String ratedaill) {
            this.ratedaill = ratedaill;
        }

        public static class RoomsInfoBean {
            /**
             * desc : 配有厨房（免费使用）
             * bed : 双床1.3米
             * adsl : 有(收费)
             * area : 58
             * floor : 6-25
             * RatePlanName : 不含早（精选特惠）.
             */

            private String desc;
            private String bed;
            private String adsl;
            private String area;
            private String floor;
            private String RatePlanName;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getBed() {
                return bed;
            }

            public void setBed(String bed) {
                this.bed = bed;
            }

            public String getAdsl() {
                return adsl;
            }

            public void setAdsl(String adsl) {
                this.adsl = adsl;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getFloor() {
                return floor;
            }

            public void setFloor(String floor) {
                this.floor = floor;
            }

            public String getRatePlanName() {
                return RatePlanName;
            }

            public void setRatePlanName(String RatePlanName) {
                this.RatePlanName = RatePlanName;
            }
        }

        public static class PricesBean {
            /**
             * fistDayPrice : 588
             * TotalJiangjin : 1646
             * TotalPrice : 16464
             * cost : 16464
             * CurrencyCode : RMB
             * daill : [{"date":"2017-07-28","price":"588"},{"date":"2017-07-29","price":"588"},{"date":"2017-07-30","price":"588"},{"date":"2017-07-31","price":"588"},{"date":"2017-08-01","price":"588"},{"date":"2017-08-02","price":"588"},{"date":"2017-08-03","price":"588"},{"date":"2017-08-04","price":"588"},{"date":"2017-08-05","price":"588"},{"date":"2017-08-06","price":"588"},{"date":"2017-08-07","price":"588"},{"date":"2017-08-08","price":"588"},{"date":"2017-08-09","price":"588"},{"date":"2017-08-10","price":"588"},{"date":"2017-08-11","price":"588"},{"date":"2017-08-12","price":"588"},{"date":"2017-08-13","price":"588"},{"date":"2017-08-14","price":"588"},{"date":"2017-08-15","price":"588"},{"date":"2017-08-16","price":"588"},{"date":"2017-08-17","price":"588"},{"date":"2017-08-18","price":"588"},{"date":"2017-08-19","price":"588"},{"date":"2017-08-20","price":"588"},{"date":"2017-08-21","price":"588"},{"date":"2017-08-22","price":"588"},{"date":"2017-08-23","price":"588"},{"date":"2017-08-24","price":"588"}]
             */

            private String fistDayPrice;
            private String TotalJiangjin;
            private String TotalPrice;
            private String cost;
            private String CurrencyCode;
            private List<DaillBean> daill;

            public String getFistDayPrice() {
                return fistDayPrice;
            }

            public void setFistDayPrice(String fistDayPrice) {
                this.fistDayPrice = fistDayPrice;
            }

            public String getTotalJiangjin() {
                return TotalJiangjin;
            }

            public void setTotalJiangjin(String TotalJiangjin) {
                this.TotalJiangjin = TotalJiangjin;
            }

            public String getTotalPrice() {
                return TotalPrice;
            }

            public void setTotalPrice(String TotalPrice) {
                this.TotalPrice = TotalPrice;
            }

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getCurrencyCode() {
                return CurrencyCode;
            }

            public void setCurrencyCode(String CurrencyCode) {
                this.CurrencyCode = CurrencyCode;
            }

            public List<DaillBean> getDaill() {
                return daill;
            }

            public void setDaill(List<DaillBean> daill) {
                this.daill = daill;
            }

            public static class DaillBean {
                /**
                 * date : 2017-07-28
                 * price : 588
                 */

                private String date;
                private String price;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }
        }

        public static class AddValuesBean {
            /**
             * string : System.Collections.ArrayList
             */

            private String string;

            public String getString() {
                return string;
            }

            public void setString(String string) {
                this.string = string;
            }
        }

        public static class GaranteeRuleBean {
            /**
             * romms : 0
             * status : 1
             * norule : 0
             * stattime : 20:00
             * endtime : 06:00
             * desc : 担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。
             */

            private String romms;
            private String status;
            private String norule;
            private String stattime;
            private String endtime;
            private String desc;

            public String getRomms() {
                return romms;
            }

            public void setRomms(String romms) {
                this.romms = romms;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getNorule() {
                return norule;
            }

            public void setNorule(String norule) {
                this.norule = norule;
            }

            public String getStattime() {
                return stattime;
            }

            public void setStattime(String stattime) {
                this.stattime = stattime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }

        public static class GaranteeRule2Bean {
            /**
             * GaranteeRule : {"GaranteeRulesTypeCode":"1","Description":"担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。","RuleValues":{"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-28T00:00:00"},{"Key":"EndDate","Value":"2017-07-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"1"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"0"},{"Key":"IsChange","Value":"2"},{"Key":"TimeNum","Value":"18:00"}]}}
             */

            private GaranteeRuleBeanX GaranteeRule;

            public GaranteeRuleBeanX getGaranteeRule() {
                return GaranteeRule;
            }

            public void setGaranteeRule(GaranteeRuleBeanX GaranteeRule) {
                this.GaranteeRule = GaranteeRule;
            }

            public static class GaranteeRuleBeanX {
                /**
                 * GaranteeRulesTypeCode : 1
                 * Description : 担保条件：在17.07.28至17.07.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。预订后无法变更取消，如未入住，将扣除第一晚房费作为违约金。
                 * RuleValues : {"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-28T00:00:00"},{"Key":"EndDate","Value":"2017-07-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"1"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"0"},{"Key":"IsChange","Value":"2"},{"Key":"TimeNum","Value":"18:00"}]}
                 */

                private String GaranteeRulesTypeCode;
                private String Description;
                private RuleValuesBean RuleValues;

                public String getGaranteeRulesTypeCode() {
                    return GaranteeRulesTypeCode;
                }

                public void setGaranteeRulesTypeCode(String GaranteeRulesTypeCode) {
                    this.GaranteeRulesTypeCode = GaranteeRulesTypeCode;
                }

                public String getDescription() {
                    return Description;
                }

                public void setDescription(String Description) {
                    this.Description = Description;
                }

                public RuleValuesBean getRuleValues() {
                    return RuleValues;
                }

                public void setRuleValues(RuleValuesBean RuleValues) {
                    this.RuleValues = RuleValues;
                }

                public static class RuleValuesBean {
                    private List<DictionaryEntryBean> DictionaryEntry;

                    public List<DictionaryEntryBean> getDictionaryEntry() {
                        return DictionaryEntry;
                    }

                    public void setDictionaryEntry(List<DictionaryEntryBean> DictionaryEntry) {
                        this.DictionaryEntry = DictionaryEntry;
                    }

                    public static class DictionaryEntryBean {
                        /**
                         * Key : StartDate
                         * Value : 2017-07-28T00:00:00
                         */

                        private String Key;
                        private String Value;

                        public String getKey() {
                            return Key;
                        }

                        public void setKey(String Key) {
                            this.Key = Key;
                        }

                        public String getValue() {
                            return Value;
                        }

                        public void setValue(String Value) {
                            this.Value = Value;
                        }
                    }
                }
            }
        }
    }
}
