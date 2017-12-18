package com.ixiangni.bean;

import java.util.List;

/**
 * 酒店预订情况
 * @ClassName:RoomYuding

 * @PackageName:com.ixiangni.bean

 * @Create On 2017/8/3 0003   16:25

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/8/3 0003 handongkeji All rights reserved.
 */
public class RoomYuding {


    /**
     * message : 操作成功!
     * data : {"hid":"27482","guid":"f50d59b0-f359-2671-c848-fb3a0679da43","roomtype":"0","hotelsupplier":"zn","allrate":"10","yuding":"True","tm1":"2017-08-03","tm2":"2017-08-31","allotmenttype":"","ratetype":"","supplierid":"","facepaytype":"","faceprice":"","includebreakfastqty2":"","HotelName":"","RoomName":"豪华大床房","RatePlanName":"不含早（限时抢购脱单价）","AvailableAmount":"8","roomsInfo":{"desc":"","bed":"","adsl":" ","area":"","floor":"","RatePlanName":"不含早（限时抢购脱单价）"},"prices":{"fistDayPrice":"698","TotalJiangjin":"1954","TotalPrice":"19544","cost":"19544","CurrencyCode":"RMB","daill":[{"date":"2017-08-03","price":"698"},{"date":"2017-08-04","price":"698"},{"date":"2017-08-05","price":"698"},{"date":"2017-08-06","price":"698"},{"date":"2017-08-07","price":"698"},{"date":"2017-08-08","price":"698"},{"date":"2017-08-09","price":"698"},{"date":"2017-08-10","price":"698"},{"date":"2017-08-11","price":"698"},{"date":"2017-08-12","price":"698"},{"date":"2017-08-13","price":"698"},{"date":"2017-08-14","price":"698"},{"date":"2017-08-15","price":"698"},{"date":"2017-08-16","price":"698"},{"date":"2017-08-17","price":"698"},{"date":"2017-08-18","price":"698"},{"date":"2017-08-19","price":"698"},{"date":"2017-08-20","price":"698"},{"date":"2017-08-21","price":"698"},{"date":"2017-08-22","price":"698"},{"date":"2017-08-23","price":"698"},{"date":"2017-08-24","price":"698"},{"date":"2017-08-25","price":"698"},{"date":"2017-08-26","price":"698"},{"date":"2017-08-27","price":"698"},{"date":"2017-08-28","price":"698"},{"date":"2017-08-29","price":"698"},{"date":"2017-08-30","price":"698"}]},"AddValues":{"string":"System.Collections.ArrayList"},"GaranteeRule":{"romms":"0","status":"1","norule":"0","stattime":"20:00","endtime":"06:00","desc":"担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。"},"GaranteeRule2":{"GaranteeRule":{"GaranteeRulesTypeCode":"1","Description":"担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。","RuleValues":{"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-27T00:00:00"},{"Key":"EndDate","Value":"2050-12-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"4"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"30"},{"Key":"IsChange","Value":"1"},{"Key":"TimeNum","Value":"18:00"}]}}},"ratesinfo":"jike_test^3588^1954^10|","ratedaill":"08-0308-0408-0508-0608-0708-0869869869869869869808-0908-1008-1108-1208-1308-1469869869869869869808-1508-1608-1708-1808-1908-2069869869869869869808-2108-2208-2308-2408-2508-2669869869869869869808-2708-2808-2908-30698698698698"}
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
         * hid : 27482
         * guid : f50d59b0-f359-2671-c848-fb3a0679da43
         * roomtype : 0
         * hotelsupplier : zn
         * allrate : 10
         * yuding : True
         * tm1 : 2017-08-03
         * tm2 : 2017-08-31
         * allotmenttype :
         * ratetype :
         * supplierid :
         * facepaytype :
         * faceprice :
         * includebreakfastqty2 :
         * HotelName :
         * RoomName : 豪华大床房
         * RatePlanName : 不含早（限时抢购脱单价）
         * AvailableAmount : 8
         * roomsInfo : {"desc":"","bed":"","adsl":" ","area":"","floor":"","RatePlanName":"不含早（限时抢购脱单价）"}
         * prices : {"fistDayPrice":"698","TotalJiangjin":"1954","TotalPrice":"19544","cost":"19544","CurrencyCode":"RMB","daill":[{"date":"2017-08-03","price":"698"},{"date":"2017-08-04","price":"698"},{"date":"2017-08-05","price":"698"},{"date":"2017-08-06","price":"698"},{"date":"2017-08-07","price":"698"},{"date":"2017-08-08","price":"698"},{"date":"2017-08-09","price":"698"},{"date":"2017-08-10","price":"698"},{"date":"2017-08-11","price":"698"},{"date":"2017-08-12","price":"698"},{"date":"2017-08-13","price":"698"},{"date":"2017-08-14","price":"698"},{"date":"2017-08-15","price":"698"},{"date":"2017-08-16","price":"698"},{"date":"2017-08-17","price":"698"},{"date":"2017-08-18","price":"698"},{"date":"2017-08-19","price":"698"},{"date":"2017-08-20","price":"698"},{"date":"2017-08-21","price":"698"},{"date":"2017-08-22","price":"698"},{"date":"2017-08-23","price":"698"},{"date":"2017-08-24","price":"698"},{"date":"2017-08-25","price":"698"},{"date":"2017-08-26","price":"698"},{"date":"2017-08-27","price":"698"},{"date":"2017-08-28","price":"698"},{"date":"2017-08-29","price":"698"},{"date":"2017-08-30","price":"698"}]}
         * AddValues : {"string":"System.Collections.ArrayList"}
         * GaranteeRule : {"romms":"0","status":"1","norule":"0","stattime":"20:00","endtime":"06:00","desc":"担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。"}
         * GaranteeRule2 : {"GaranteeRule":{"GaranteeRulesTypeCode":"1","Description":"担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。","RuleValues":{"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-27T00:00:00"},{"Key":"EndDate","Value":"2050-12-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"4"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"30"},{"Key":"IsChange","Value":"1"},{"Key":"TimeNum","Value":"18:00"}]}}}
         * ratesinfo : jike_test^3588^1954^10|
         * ratedaill : 08-0308-0408-0508-0608-0708-0869869869869869869808-0908-1008-1108-1208-1308-1469869869869869869808-1508-1608-1708-1808-1908-2069869869869869869808-2108-2208-2308-2408-2508-2669869869869869869808-2708-2808-2908-30698698698698
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
             * desc :
             * bed :
             * adsl :
             * area :
             * floor :
             * RatePlanName : 不含早（限时抢购脱单价）
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
             * fistDayPrice : 698
             * TotalJiangjin : 1954
             * TotalPrice : 19544
             * cost : 19544
             * CurrencyCode : RMB
             * daill : [{"date":"2017-08-03","price":"698"},{"date":"2017-08-04","price":"698"},{"date":"2017-08-05","price":"698"},{"date":"2017-08-06","price":"698"},{"date":"2017-08-07","price":"698"},{"date":"2017-08-08","price":"698"},{"date":"2017-08-09","price":"698"},{"date":"2017-08-10","price":"698"},{"date":"2017-08-11","price":"698"},{"date":"2017-08-12","price":"698"},{"date":"2017-08-13","price":"698"},{"date":"2017-08-14","price":"698"},{"date":"2017-08-15","price":"698"},{"date":"2017-08-16","price":"698"},{"date":"2017-08-17","price":"698"},{"date":"2017-08-18","price":"698"},{"date":"2017-08-19","price":"698"},{"date":"2017-08-20","price":"698"},{"date":"2017-08-21","price":"698"},{"date":"2017-08-22","price":"698"},{"date":"2017-08-23","price":"698"},{"date":"2017-08-24","price":"698"},{"date":"2017-08-25","price":"698"},{"date":"2017-08-26","price":"698"},{"date":"2017-08-27","price":"698"},{"date":"2017-08-28","price":"698"},{"date":"2017-08-29","price":"698"},{"date":"2017-08-30","price":"698"}]
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
                 * date : 2017-08-03
                 * price : 698
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
             * desc : 担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。
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
             * GaranteeRule : {"GaranteeRulesTypeCode":"1","Description":"担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。","RuleValues":{"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-27T00:00:00"},{"Key":"EndDate","Value":"2050-12-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"4"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"30"},{"Key":"IsChange","Value":"1"},{"Key":"TimeNum","Value":"18:00"}]}}
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
                 * Description : 担保条件：在17.07.27至50.12.31入住如果在20:00至次日6:00到店，需要您提供信用卡担保。客人入住日前1天18:00点前可以变更取消，之后无法变更取消，如未入住，将扣除第一晚房费作为违约金。
                 * RuleValues : {"DictionaryEntry":[{"Key":"StartDate","Value":"2017-07-27T00:00:00"},{"Key":"EndDate","Value":"2050-12-31T00:00:00"},{"Key":"DateType","Value":"1"},{"Key":"WeekSet","Value":"1,2,3,4,5,6,7,"},{"Key":"ChangeRule","Value":"4"},{"Key":"Gage","Value":"1"},{"Key":"IsArriveTimeVouch","Value":"1"},{"Key":"ArriveStatTime","Value":"20:00"},{"Key":"ArriveEndTime","Value":"6:00"},{"Key":"IsTomorrow","Value":"1"},{"Key":"IsRoomCountVouch","Value":"0"},{"Key":"RoomCount","Value":"0"},{"Key":"VouchMoneyType","Value":"1"},{"Key":"DayNum","Value":"2008-08-18"},{"Key":"HourNum","Value":"30"},{"Key":"IsChange","Value":"1"},{"Key":"TimeNum","Value":"18:00"}]}
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
                         * Value : 2017-07-27T00:00:00
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
