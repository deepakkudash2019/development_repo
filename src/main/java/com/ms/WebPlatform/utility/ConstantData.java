package com.ms.WebPlatform.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.MsrtcDivisionMaster; 

public class ConstantData {		
	
	
	
	public static final String httpProtocolip  = "http://18.208.67.81:8080/";   
	
	
	
	
	public boolean isLocalSystem ;
	
	public static Map<String ,Integer> checkingSystem = new java.util.HashMap<>();
	public static Map<String ,Integer> isEmailsent = new java.util.HashMap<>(); 
	
	public static Map<String , String>namingConvention = new java.util.HashMap<>();      
	
	public static Map<String , String>namingConvention2 = new java.util.HashMap<>(); 
	
	public static Map<String,List<String>> temporaryStorage = new java.util.HashMap<>(); 
	
	public static Map<String, String> ipAdd = new java.util.HashMap<>(); 
	public static int runSch;
	

	public static Map<String, Integer> checkDurationFlag = new java.util.HashMap<>(); 
	//public static Map<String, Integer> pdfUrlFormerchant = new java.util.HashMap<>(); 
	
	
	
	
	 //------- Yahoo's SMTP server------//
	public static  String from = "info@mirrorsize.com";//change accordingly
	public static  String username = "info@mirrorsize.com";//change accordingly
	public static  String password = "Deepak18";//change accordingly	 
	public static final String host = "smtp.zoho.com";
	public static  Integer host1 = 0;
	
	//----------END-------------------//
	
  public static final String productNamae = "GetMeasured,size2fit,Both";
  public static final String subproductName = "online,store,all";

  //----------------------------databaseCredential---------------------//
  public static final String localUser = "root";
  public static final String localpassword = "zd>E6[:CKw/u";
  
  public static final String serverUser = "PlatformMS";
  public static final String serverpassword = "MirrorsizeDatabase2019";
  
  public static final String serverDbUrl = "jdbc:mysql://platformdb.cc53tbg5bdo5.us-east-1.rds.amazonaws.com:3306/msplatform";
  public static final String localDbUrl  ="jdbc:mysql://localhost:3306/msv1";
  //------------------------------END---------------------------------//
  
  public static Map<String, Object> utilityData; 
  public static Map<String, Object> serverIpAddress;   
  public static Map<String, Object> temporaryRecommendation = new HashMap<String, Object>();
  public static Map<String, List<String>> visionApiRecommendation=new HashMap<String, List<String>>(); 
  public static Map<String, Object> universalPointMap ;
  public static boolean isAvailableData  = false;
  //-------------------imageUrl------------------//
  public static final String errorImage = "https://s3.ap-south-1.amazonaws.com/commonms/errorcodeimages/";
  
  public static  String[] companyProfileList = null;//{"accountId","name","companyLogo","country","landlineNo","industryType","state","city","pinCode","companyAddress"};
  public static  String[] contactPersonList = null;//{"cpName","cpMobileNo","cpDesignation","cpEmailId","cpProfilePic"};
  
  public static Map<String,List<Date>> reportPeriodMap = null;
  
  public static List<MsrtcDivisionMaster> divListStatic = null;
  public static Map<Integer, MsrtcDivisionMaster> divListStaticMap = null;
  public static String[] strArr2 = { "00:00 - 00:59", "01:00 - 01:59", "02:00 - 02:59", "03:00 - 03:59", "04:00 - 04:59", "05:00 - 05:59", "06:00 - 06:59", "07:00 - 07:59", "08:00 - 08:59", "09:00 - 09:59", "10:00 - 10:59", "11:00 - 11:59",
		  "12:00 - 12:59", "13:00 - 13:59", "14:00 - 14:59", "15:00 - 15:59", "16:00 - 16:59", "17:00 - 17:59", "18:00 - 18:59", "19:00 - 19:59", "20:00 - 20:59", "21:00 - 21:59", "22:00 - 22:59",
			"23:00 - 23:59"  };
  
  public static String[] strArr = { "0-1", "1-2", "2-3", "3-4", "4-5", "5-6", "6-7", "7-8", "8-9", "9-10", "10-11", "11-12",
			"12-13", "13-14", "14-15", "15-16", "16-17", "17-18", "18-19", "19-20", "20-21", "21-22", "22-23",
			"23-0" };
  
  /*{ "0-1", "1-2", "2-3", "3-4", "4-5", "5-6", "6-7", "7-8", "8-9", "9-10", "10-11", "11-12",
		"12:00 - 12:59", "13:00 - 13:59", "14:00 - 14:59", "15:00 - 15:59", "16:00 - 16:59", "17:00 - 17:59", "18:00 - 18:59", "19:00 - 19:59", "20:00 - 20:59", "21:00 - 21:59", "22:00 - 22:59",
		"23:00 - 23:59" };*/

  public static List<Depot> depotList = new ArrayList<Depot>();
  
  public static List<Depot> AlldepotList = new ArrayList<Depot>();

  public static Map<String, Map<Integer, Integer>> depotScheduleCount = null;
  public static List<Map<String, String>> countMapList_forAll = null;
  public static List<Map<String, String>> countMapList_forAll_depot = null;
  public static String hostName = "http://localhost:8020";
  public static Integer totalDataCount = 0;
  public static Integer validVehicle = 0;
  public static Integer validVehicle_depotwise=0;
  public static String dbQuery_1 = "";
  public static String dbQuery_2 = "";
  public static String dbQuery_3 = "";
  public static String fileStoragePath = "";
  public static Map<String, Integer> vehicleCountInDepot = null;
  
  public static String msrtc_hostt = "http://209.190.15.106:8020";
  public static Integer temp_division_id = 0;
  public static Integer transFerredVehicleCount = 0;
  public static Integer is_server = 0;
  public static String raw_data_root_path=null;
  public static String thirdParty_Api=null;
public static ArrayList<Map<String, String>> countMapList_forAll_depot_manager = null;
public static Map<String, Integer> depotCodeVsDivisionId = new HashMap<String, Integer>();
  public static void main(String[] args) {  
}



		public boolean isLocalSystem() {
			return isLocalSystem;
		}
		
		public void setLocalSystem(boolean isLocalSystem) {
			this.isLocalSystem = isLocalSystem;
		}
		
	public static  void setNamingConvention() {

	}
	
	public static Map<String, String> setHeader = new HashMap<>();	
	
	public static ArrayList<String> depotCodesList=new ArrayList<>(Arrays.asList("NBR","SBNR","RSD","SHRL","INDPNE","KRD","CHD","SNR","GDC","KRW","CPD","KRT","SGN","BEED","ISLP","SNGO","HNV","TMS","PGR","IGT","PGNB","ABD","IDR","DGLR","UKHD","MHKR","NRYGN","TJP","DRR","YTL","DRK","PETH","GRT","PRNR","JWR","SRT","MHD","ABJ","HBL","WUD","MRS","WANI","TLH","APR","BHR","STN","WAI","BHN","OSM","THNE","BHV","AKLTWR","KJT","SNNR","PKNDL","MMD","MOD","JLGJ","NAG","KNT","PRTR","PBN","RTK","PRL","TKPR","RTN","WRR","MLKBLD","SNGR","AKLCBS","NGA","VITA","PRND","UDG","PLUS","DRW","AMN","CMR","NLG","SHVG","DGS","STR","BHM","JMN","VJD","MKTN","GGP","ALB","CHN","MWR","ICHL","GVI","RAJ","ARVI","KRG","KLN","CHNRLY","KRM2","SRS","THN","BDNR","AJR","GHR","MJL","SRR","TKRE","BSL","UNA","MHS","VJPR","KNN","NRK","HMT","AMR","KNK","PTRD","ANL","MRJ","WADA","LNJ","SLD","PRLI","SRGD","CB","SHPR","GBD","PTNG","LTR","SPR","HYB","TGNPNE","DGD","BGL","AUR","PCH","SNDK","KLPCBS","NND","PLG","MLKKLP","SFL","HGT","CHL","BMTMID","RLG","SKL","PTDA","MHR","MGPIR","ARG","CNPT","VTL","MDH","MZR","SSWD","TSGN","MYS","KWN","SWD","BILO","KOP","NSKSTN","VJP","BSMT","VDJ","PKD","PTRI","KLM","TRR","PNI","HSA","SKR","NGPCBS","MLG","PLTN","NWS","MLV","KRM1","RJGR","ERDL","KTL","PHL","AKKT","BNG","CHBZR","VSI","YWL","BLH","MCT","AMT","UMRD","KML","JAT","AKT","DRY","JNTR","GNKD","SBN","DHL","MNGN","URN","AKJ","KNWD","BMT","NSKCBS","HNGL","ASTI","GND","SLP","NER","JGT","DPL","BHKR","KHD","BHLD","NVR","KGL","ATPD","DOND","SWR","MHV","BRS","SMPR","AHR","INDO","RVR","WRD","UDYP","BVD","MNGD","RDHN","CNWD","RJP","MWD","MNG","KRJAKL","CKL","RJRA","DHN","MRD","KMG","WSM","TLGWRD","HDGN","PEN","JLN","YLA","JLG","AKKW","SMR","AUSA","VDNG","PPR","PTD","JMKD","BJP","KMHL","GDL","SYGN","NNG","MRBD","CPN","MKD","PTHN","PSD","NPR","SHDOLD","KND","KWD","PLN","PNL","DVD","JFBD","DND","BRM","PTN","VNG","BHT","SDN","LGN","CHBH","BVINC","KNDR","ROH","PG","OMG","BSR","KLMNR","SGL","MADK","AKLE","BGM","KDL","NSP","BLD"));
	
	
	
	
	
	
}
