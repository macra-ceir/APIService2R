package com.gl.ceir.config.EmailSender;

public interface MailSubject {

	// Consignment
	String CONSIGNMENT_PROCESS_SUCCESS_TO_IMPORTER_MAIL			= "Consignment processed successfully Notification for Transaction ID <XXX>";
	String CONSIGNMENT_PROCESS_SUCCESS_TO_CEIR_MAIL				= "Consignment processed successfully Notification for Transaction ID <XXX>";
	String	CONSIGNMENT_PROCESS_FAILED_TO_IMPORTER_MAIL			= "Consignment processed failed Notification for Transaction ID <XXX>";
	String Consignment_Success_CEIRAuthority_Email_Message 		= "Consignment Approval Notification for Transaction ID <XXX>";
	String Consignment_Approved_CustomImporter_Email_Message 	= "Consignment Clearance Notification for Transaction ID <XXX>";
	String Consignment_Reject_CEIRAuthority_Email_Message 		= "Consignment Rejection Notification for Transaction ID <XXX>";
	String Consignment_Rejected_Custom_Email_Message 			= "Consignment Rejection Notification for Transaction ID <XXX>";

	// Stock
	String ASSIGN_STOCK 									= "Stock Assignment Notification for Transaction ID <XXX>";
	String MAIL_TO_ANONYMOUS_ON_STOCK_UPLOAD 				= "Stock Upload Notification for Transaction ID <XXX>";
	String STOCK_APPROVED_BY_CEIR_ADMIN 					= "Stock Approval Notification for Transaction ID <XXX>";
	String STOCK_REJECT_BY_CEIR_ADMIN 						= "Stock Rejection Notification for Transaction ID <XXX>";
	String STOCK_PROCESS_SUCCESS_TO_USER_MAIL				= "Stock processed successfully Notification for Transaction ID <XXX>";
	String STOCK_PROCESS_SUCCESS_TO_CEIR_MAIL				= "Stock processed successfully Notification for Transaction ID <XXX>";
	String STOCK_PROCESS_FAILED_TO_USER_MAIL				= "Stock processed failed Notification for Transaction ID <XXX>";
	String STOCK_BY_CUSTOM_PROCESS_FAILED_TO_CUSTOM_MAIL 	= "Stock processed failed Notification for Transaction ID <XXX>";

	// Stolen/Recovery/Block/Recovery
	String STOLEN_APPROVED_BY_CEIR_ADMIN 	= "Approval of Stolen Request Notification for Transaction ID <XXX>";
	String RECOVERY_APPROVED_BY_CEIR_ADMIN 	= "Approval of Recovery Request Notification for Transaction ID <XXX>";
	String BLOCK_APPROVED_BY_CEIR_ADMIN 	= "Approval of Block Request Notification for Transaction ID <XXX>";
	String UNBLOCK_APPROVED_BY_CEIR_ADMIN 	= "Approval of Unblock Request Notification for Transaction ID <XXX>";

	String STOLEN_REJECT_BY_CEIR_ADMIN 		= "Rejection of Stolen Request Notification for Transaction ID <XXX>";
	String RECOVERY_REJECT_BY_CEIR_ADMIN 	= "Rejection of Recovery Request Notification for Transaction ID <XXX>";
	String BLOCK_REJECT_BY_CEIR_ADMIN 		= "Rejection of Block Request Notification for Transaction ID <XXX>";
	String UNBLOCK_REJECT_BY_CEIR_ADMIN 	= "Rejection of Unblock Request Notification for Transaction ID <XXX>";

	// Register Device
	String MAIL_TO_CEIR_ADMIN_ON_DEVICE_TAX_PAID 		= "Tax Paid Notification for Transaction ID <XXX>";
	String MAIL_TO_CEIR_ADMIN_ON_DEVICE_TAX_NOT_PAID 	= "Tax Not Paid Notification for Transaction ID <XXX>";
	String MAIL_TO_USER_ON_CEIR_DEVICE_APPROVAL 		= "Device Approval Notification for Transaction ID <XXX>";
	String MAIL_TO_USER_ON_CEIR_DEVICE_DISAPPROVAL 		= "Device Rejection Notification for Transaction ID <XXX>";
}
