package com.gl.ceir.config.EmailSender;

public class EmailDetails {
private String fromEmail;
private String toEmail;
private String msgBody;
private String subject;
   
public String getFromEmail() {
	return fromEmail;
}
public void setFromEmail(String fromEmail) {
	this.fromEmail = fromEmail;
}
public String getToEmail() {
	return toEmail;
}
public void setToEmail(String toEmail) {
	this.toEmail = toEmail;
}
public String getMsgBody() {
	return msgBody;
}
public void setMsgBody(String msgBody) {
	this.msgBody = msgBody;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
@Override
public String toString() {
	return "EmailDetails [fromEmail=" + fromEmail + ", toEmail=" + toEmail + ", msgBody=" + msgBody + ", subject="
			+ subject + "]";
}

}
