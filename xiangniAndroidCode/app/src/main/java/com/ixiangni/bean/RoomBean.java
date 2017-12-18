package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class RoomBean {

    /**
     * rid : 317470
     * roomtype : 0
     * hotelsupplier : zn
     * allrate : 10
     * title : 商务客房
     * adsl : 免费WIFI
     * bed : 双床1.3米
     * area : 58
     * floor : 6-25
     * status : 0
     * notes : 配有厨房（免费使用）; 6-25;
     * AvailableAmount : 有房
     * img : [{"spic":"http://tp1.znimg.com/hotelimg/39225/500x375_55440a181fc3b2a0d47f4.jpg","type":"1","title":"商务客房","imgurl":"http://tp1.znimg.com/hotelimg/39225/100x75_55440a181fc3b2a0d47f4.jpg"},{"spic":"http://tp1.znimg.com/uppic/sltpic/39225/500x375_201405151752244076.png","type":"2","title":"商务客房","imgurl":"http://tp1.znimg.com/uppic/sltpic/39225/100x75_201405151752244076.png"},{"spic":"http://tp1.znimg.com/uppic/sltpic/39225/500x375_201405151752272297.png","type":"2","title":"商务客房","imgurl":"http://tp1.znimg.com/uppic/sltpic/39225/100x75_201405151752272297.png"}]
     * plans : [{"hotelsupplier":"zn","planid":"437099","planname":"不含早","totalprice":"16464","priceCode":"RMB","iscard":"3","canceltitle":"免费取消","cancelremark":"订单提交后可随时免费取消/变更。","description":{},"AddValues":{},"date":[{"day":"2017-07-25","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-26","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-27","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-28","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-29","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-30","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-31","week":"1","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-01","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-02","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-03","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-04","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-05","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-06","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-07","week":"1","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-08","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-09","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-10","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-11","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-12","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-13","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-14","week":"1","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-15","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-16","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-17","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-18","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-19","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-20","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-21","week":"1","menshi":"-1","price":"588","jiangjin":"35"}],"avgprice":"588","jiangjin":"41","menshi":"-1","availableAmount":"有房","status":"0"},{"hotelsupplier":"zn","planid":"142432","planname":"含单早","totalprice":"19544","priceCode":"RMB","iscard":"3","canceltitle":"免费取消","cancelremark":"订单提交后可随时免费取消/变更。","description":{},"AddValues":{},"date":[{"day":"2017-07-25","week":"2","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-07-26","week":"3","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-07-27","week":"4","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-07-28","week":"5","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-07-29","week":"6","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-07-30","week":"0","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-07-31","week":"1","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-01","week":"2","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-02","week":"3","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-03","week":"4","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-04","week":"5","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-05","week":"6","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-06","week":"0","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-07","week":"1","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-08","week":"2","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-09","week":"3","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-10","week":"4","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-11","week":"5","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-12","week":"6","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-13","week":"0","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-14","week":"1","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-15","week":"2","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-16","week":"3","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-17","week":"4","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-18","week":"5","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-19","week":"6","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-20","week":"0","menshi":"-1","price":"698","jiangjin":"42"},{"day":"2017-08-21","week":"1","menshi":"-1","price":"698","jiangjin":"42"}],"avgprice":"698","jiangjin":"48","menshi":"-1","availableAmount":"有房","status":"0"}]
     */

    private String rid;
    private String roomtype;
    private String hotelsupplier;
    private String allrate;
    private String title;
    private String adsl;
    private String bed;
    private String area;
    private String floor;
    private String status;
    private String notes;
    private String AvailableAmount;
    private List<ImgBean> img;
    private List<PlansBean> plans;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdsl() {
        return adsl;
    }

    public void setAdsl(String adsl) {
        this.adsl = adsl;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAvailableAmount() {
        return AvailableAmount;
    }

    public void setAvailableAmount(String AvailableAmount) {
        this.AvailableAmount = AvailableAmount;
    }

    public List<ImgBean> getImg() {
        return img;
    }

    public void setImg(List<ImgBean> img) {
        this.img = img;
    }

    public List<PlansBean> getPlans() {
        return plans;
    }

    public void setPlans(List<PlansBean> plans) {
        this.plans = plans;
    }

    public static class ImgBean {
        /**
         * spic : http://tp1.znimg.com/hotelimg/39225/500x375_55440a181fc3b2a0d47f4.jpg
         * type : 1
         * title : 商务客房
         * imgurl : http://tp1.znimg.com/hotelimg/39225/100x75_55440a181fc3b2a0d47f4.jpg
         */

        private String spic;
        private String type;
        private String title;
        private String imgurl;

        public String getSpic() {
            return spic;
        }

        public void setSpic(String spic) {
            this.spic = spic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }

    public static class PlansBean {
        /**
         * hotelsupplier : zn
         * planid : 437099
         * planname : 不含早
         * totalprice : 16464
         * priceCode : RMB
         * iscard : 3
         * canceltitle : 免费取消
         * cancelremark : 订单提交后可随时免费取消/变更。
         * description : {}
         * AddValues : {}
         * date : [{"day":"2017-07-25","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-26","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-27","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-28","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-29","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-30","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-07-31","week":"1","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-01","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-02","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-03","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-04","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-05","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-06","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-07","week":"1","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-08","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-09","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-10","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-11","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-12","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-13","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-14","week":"1","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-15","week":"2","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-16","week":"3","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-17","week":"4","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-18","week":"5","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-19","week":"6","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-20","week":"0","menshi":"-1","price":"588","jiangjin":"35"},{"day":"2017-08-21","week":"1","menshi":"-1","price":"588","jiangjin":"35"}]
         * avgprice : 588
         * jiangjin : 41
         * menshi : -1
         * availableAmount : 有房
         * status : 0
         */

        private String hotelsupplier;
        private String planid;
        private String planname;
        private String totalprice;
        private String priceCode;
        private String iscard;
        private String canceltitle;
        private String cancelremark;
        private DescriptionBean description;
        private AddValuesBean AddValues;
        private String avgprice;
        private String jiangjin;
        private String menshi;
        private String availableAmount;
        private String status;
        private List<DateBean> date;

        public String getHotelsupplier() {
            return hotelsupplier;
        }

        public void setHotelsupplier(String hotelsupplier) {
            this.hotelsupplier = hotelsupplier;
        }

        public String getPlanid() {
            return planid;
        }

        public void setPlanid(String planid) {
            this.planid = planid;
        }

        public String getPlanname() {
            return planname;
        }

        public void setPlanname(String planname) {
            this.planname = planname;
        }

        public String getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(String totalprice) {
            this.totalprice = totalprice;
        }

        public String getPriceCode() {
            return priceCode;
        }

        public void setPriceCode(String priceCode) {
            this.priceCode = priceCode;
        }

        public String getIscard() {
            return iscard;
        }

        public void setIscard(String iscard) {
            this.iscard = iscard;
        }

        public String getCanceltitle() {
            return canceltitle;
        }

        public void setCanceltitle(String canceltitle) {
            this.canceltitle = canceltitle;
        }

        public String getCancelremark() {
            return cancelremark;
        }

        public void setCancelremark(String cancelremark) {
            this.cancelremark = cancelremark;
        }

        public DescriptionBean getDescription() {
            return description;
        }

        public void setDescription(DescriptionBean description) {
            this.description = description;
        }

        public AddValuesBean getAddValues() {
            return AddValues;
        }

        public void setAddValues(AddValuesBean AddValues) {
            this.AddValues = AddValues;
        }

        public String getAvgprice() {
            return avgprice;
        }

        public void setAvgprice(String avgprice) {
            this.avgprice = avgprice;
        }

        public String getJiangjin() {
            return jiangjin;
        }

        public void setJiangjin(String jiangjin) {
            this.jiangjin = jiangjin;
        }

        public String getMenshi() {
            return menshi;
        }

        public void setMenshi(String menshi) {
            this.menshi = menshi;
        }

        public String getAvailableAmount() {
            return availableAmount;
        }

        public void setAvailableAmount(String availableAmount) {
            this.availableAmount = availableAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<DateBean> getDate() {
            return date;
        }

        public void setDate(List<DateBean> date) {
            this.date = date;
        }

        public static class DescriptionBean {
        }

        public static class AddValuesBean {
        }

        public static class DateBean {
            /**
             * day : 2017-07-25
             * week : 2
             * menshi : -1
             * price : 588
             * jiangjin : 35
             */

            private String day;
            private String week;
            private String menshi;
            private String price;
            private String jiangjin;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getMenshi() {
                return menshi;
            }

            public void setMenshi(String menshi) {
                this.menshi = menshi;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getJiangjin() {
                return jiangjin;
            }

            public void setJiangjin(String jiangjin) {
                this.jiangjin = jiangjin;
            }
        }
    }
}
