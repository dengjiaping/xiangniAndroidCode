package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class OrdPlaneInfo {


    /**
     * userid : 3588
     * username : jike_test
     * siteid : 65
     * amount : 870.0
     * orderremark :
     * flighttype : 0
     * goflightdata : FAE728EEE207A887231B7EF27BFA02E96B15465997FD1A198DB0D562DE91DC3D2E9C82B0CFC4E31D3AA6900F7A06712878E8F666530D05722E3EF8E70AB2AE80EE10572BE8825450578084C846EF59532D2BCBCFABEBBEE6864AF635087D99C59DDC4429A37B3199E7880036553B0F04A5A1728A9DFC67FC463304B1BFD7DB8D6F7A02B6BE1A16A975A4285569069E63D830BD1DECB2623516E93E0BFB4FA89DB712FCEB6A9A552BBEE4269B7A2D9D34AC558756EA6A8BE0D66474C891E5299A1101FCCA9B9A0CE747155D650ECAD8FE7732A3264A34BFC70279B267166C89BE412300C4548E603B1629D9C9908CFC140BAEEBA40D5CD2F29D31398DEC83403CD691AA9FF7657F89F71737EAE7018EDE964D48BC650C51571784A497BA94754D1A4AC7449880D9288F6FB9AC37D923EDB7790721C5F076C766255A8BBFF2EA6BC247372D8777D3BAEB8D8C208B79CBE51B072CFE23B7624A873BD090018DCF2CDDA8C6D872E1817D3E4C0E7B93A402F2FB49662F228D9918B1D8DFAF631226846F56CF5FC60C0EBFCA839DA7CBB4E62BB52312F0B7C048115769D80C8C4DF0DBA9EA99B84B1ADF40BCD9E4963CA3689A2D47DBA3B65A9EC9BF5AADCEC0A542A7
     * backflightdata :
     * policy : EFB3AB0B7E3736492886C53B48217ED3CA5EF17AB0886CE1132E5ABE0FB9D84FCADAD031063FE01445EFB625EA0FDCDEF81FED32B4293F2823930BD99664BDFF1F72B33C92B5E4D2FD0DC14CC00106B273600613C8D5059CDEF1F18BF252BD367E07351CC0FA08A44829542EEC7C179738887875406DF49DD14C6137C051E15E8A9DC5C0C8C7ABE8896D5F2F3891FE3FD234A19D59D110D3024AB307AED9BED2C2555ECE0B15C064A5AAC99857A914300C427E3E3EE02AE47F199BB50B80071A528CB36AC1850488EAE287C80448965B7099153C86867071E9B6E5DACD75412275FFCE77C183849F
     * passenger : [{"idno":"428866886888666889","idtype":"0","isunum":"0","mobile":"13886688888","pname":"九康","ptype":"1","birthday":"","adultTicketNo":""}]
     * contact : {"name":"九康","mobile":"13886688888","tel":"13886688888","email":""}
     * StartPortName : 北京首都国际机场
     * EndPortName : 上海浦东国际机场
     * FlightNo : MU563
     * CarrinerName : 东方航空
     * SDate : 2017-08-07
     * OffTime : 17:15:00
     * ArriveTime : 19:25:00
     * RunTime : 2小时10分钟
     * CabinName : 经济舱
     * discount : 优惠政策1
     * Tax : 50
     * Oil : 0
     * sale : 820
     */

    private String userid;
    private String username;
    private String siteid;
    private String amount;
    private String orderremark;
    private String flighttype;
    private String goflightdata;
    private String backflightdata;
    private String policy;
    private ContactBean contact;
    private String StartPortName;
    private String EndPortName;
    private String FlightNo;
    private String CarrinerName;
    private String SDate;
    private String OffTime;
    private String ArriveTime;
    private String RunTime;
    private String CabinName;
    private String discount;
    private String Tax;
    private String Oil;
    private String sale;
    private List<PassengerBean> passenger;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderremark() {
        return orderremark;
    }

    public void setOrderremark(String orderremark) {
        this.orderremark = orderremark;
    }

    public String getFlighttype() {
        return flighttype;
    }

    public void setFlighttype(String flighttype) {
        this.flighttype = flighttype;
    }

    public String getGoflightdata() {
        return goflightdata;
    }

    public void setGoflightdata(String goflightdata) {
        this.goflightdata = goflightdata;
    }

    public String getBackflightdata() {
        return backflightdata;
    }

    public void setBackflightdata(String backflightdata) {
        this.backflightdata = backflightdata;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public String getStartPortName() {
        return StartPortName;
    }

    public void setStartPortName(String StartPortName) {
        this.StartPortName = StartPortName;
    }

    public String getEndPortName() {
        return EndPortName;
    }

    public void setEndPortName(String EndPortName) {
        this.EndPortName = EndPortName;
    }

    public String getFlightNo() {
        return FlightNo;
    }

    public void setFlightNo(String FlightNo) {
        this.FlightNo = FlightNo;
    }

    public String getCarrinerName() {
        return CarrinerName;
    }

    public void setCarrinerName(String CarrinerName) {
        this.CarrinerName = CarrinerName;
    }

    public String getSDate() {
        return SDate;
    }

    public void setSDate(String SDate) {
        this.SDate = SDate;
    }

    public String getOffTime() {
        return OffTime;
    }

    public void setOffTime(String OffTime) {
        this.OffTime = OffTime;
    }

    public String getArriveTime() {
        return ArriveTime;
    }

    public void setArriveTime(String ArriveTime) {
        this.ArriveTime = ArriveTime;
    }

    public String getRunTime() {
        return RunTime;
    }

    public void setRunTime(String RunTime) {
        this.RunTime = RunTime;
    }

    public String getCabinName() {
        return CabinName;
    }

    public void setCabinName(String CabinName) {
        this.CabinName = CabinName;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String Tax) {
        this.Tax = Tax;
    }

    public String getOil() {
        return Oil;
    }

    public void setOil(String Oil) {
        this.Oil = Oil;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public List<PassengerBean> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<PassengerBean> passenger) {
        this.passenger = passenger;
    }

    public static class ContactBean {
        /**
         * name : 九康
         * mobile : 13886688888
         * tel : 13886688888
         * email :
         */

        private String name;
        private String mobile;
        private String tel;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class PassengerBean {
        /**
         * idno : 428866886888666889
         * idtype : 0
         * isunum : 0
         * mobile : 13886688888
         * pname : 九康
         * ptype : 1
         * birthday :
         * adultTicketNo :
         */

        private String idno;
        private String idtype;
        private String isunum;
        private String mobile;
        private String pname;
        private String ptype;
        private String birthday;
        private String adultTicketNo;

        public String getIdno() {
            return idno;
        }

        public void setIdno(String idno) {
            this.idno = idno;
        }

        public String getIdtype() {
            return idtype;
        }

        public void setIdtype(String idtype) {
            this.idtype = idtype;
        }

        public String getIsunum() {
            return isunum;
        }

        public void setIsunum(String isunum) {
            this.isunum = isunum;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPtype() {
            return ptype;
        }

        public void setPtype(String ptype) {
            this.ptype = ptype;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAdultTicketNo() {
            return adultTicketNo;
        }

        public void setAdultTicketNo(String adultTicketNo) {
            this.adultTicketNo = adultTicketNo;
        }
    }
}
