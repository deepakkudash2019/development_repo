package com.ms.WebPlatform.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ms.WebPlatform.repository.DepotRepository;
import com.ms.WebPlatform.repository.DivisionRepository;
import com.ms.WebPlatform.repository.MembershipGroupRepository;
import com.ms.WebPlatform.repository.ReportVehicleLocRepository;

@Component
public class Importer {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private DivisionRepository divisionRepo;
	@Autowired
	private DepotRepository depoRepo;
	@Autowired
	private GraphInterface graph;
	@Autowired
	private ServiceInterface service;
	@Autowired
	private ReportVehicleLocRepository reportVehicleRepository;
	@Autowired
	private RawDataInterface rawDataInterface;
	@Autowired
	private MembershipGroupRepository membershipGrpRepo;
	@Autowired
	private UptimeReport uptimeReport;
	@Autowired
	private PdfExcelInterface pdfExcel;
	@Autowired
	private QueryInterface qurInterface;
	
	//-----------getterSetter-------------------//
	
	

	public EntityManager getEm() {
		return em;
	}
	public QueryInterface getQurInterface() {
		return qurInterface;
	}
	public void setQurInterface(QueryInterface qurInterface) {
		this.qurInterface = qurInterface;
	}
	public PdfExcelInterface getPdfExcel() {
		return pdfExcel;
	}
	public void setPdfExcel(PdfExcelInterface pdfExcel) {
		this.pdfExcel = pdfExcel;
	}
	public UptimeReport getUptimeReport() {
		return uptimeReport;
	}
	public void setUptimeReport(UptimeReport uptimeReport) {
		this.uptimeReport = uptimeReport;
	}
	public MembershipGroupRepository getMembershipGrpRepo() {
		return membershipGrpRepo;
	}
	public void setMembershipGrpRepo(MembershipGroupRepository membershipGrpRepo) {
		this.membershipGrpRepo = membershipGrpRepo;
	}
	public void setEm(EntityManager em) {
		this.em = em;
	}
	public DivisionRepository getDivisionRepo() {
		return divisionRepo;
	}
	public void setDivisionRepo(DivisionRepository divisionRepo) {
		this.divisionRepo = divisionRepo;
	}
	public DepotRepository getDepoRepo() {
		return depoRepo;
	}
	public void setDepoRepo(DepotRepository depoRepo) {
		this.depoRepo = depoRepo;
	}
	public GraphInterface getGraph() {
		return graph;
	}
	public void setGraph(GraphInterface graph) {
		this.graph = graph;
	}
	public ServiceInterface getService() {
		return service;
	}
	public void setService(ServiceInterface service) {
		this.service = service;
	}
	public ReportVehicleLocRepository getReportVehicleRepository() {
		return reportVehicleRepository;
	}
	public void setReportVehicleRepository(ReportVehicleLocRepository reportVehicleRepository) {
		this.reportVehicleRepository = reportVehicleRepository;
	}
	public RawDataInterface getRawDataInterface() {
		return rawDataInterface;
	}
	public void setRawDataInterface(RawDataInterface rawDataInterface) {
		this.rawDataInterface = rawDataInterface;
	}
}
