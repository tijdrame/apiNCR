package com.boa.api.request;

public class NCRRequest {
    private String branchCode;
    private String userName;
    private String batchNumber;
    private String itemSequenceNumber;
    private String payorBankRoutNumber;
    private String accountNumber;
    private String serialNumber;
    private String presentingBankRoutNumber;
    private String payerName;
    private String transactionDetails;
    private String depositorAccountNumber;
    private String payeeName;
    private String userID;
    private String userBranch;
    private String sessionID;
    private Double amount;
    private String nooper;


    public NCRRequest() {
    }

    public NCRRequest(String branchCode, String userName, String batchNumber, String itemSequenceNumber, String payorBankRoutNumber, String accountNumber, String serialNumber, String presentingBankRoutNumber, String payerName, String transactionDetails, String depositorAccountNumber, String payeeName, String userID, String userBranch, String sessionID, Double amount, String nooper) {
        this.branchCode = branchCode;
        this.userName = userName;
        this.batchNumber = batchNumber;
        this.itemSequenceNumber = itemSequenceNumber;
        this.payorBankRoutNumber = payorBankRoutNumber;
        this.accountNumber = accountNumber;
        this.serialNumber = serialNumber;
        this.presentingBankRoutNumber = presentingBankRoutNumber;
        this.payerName = payerName;
        this.transactionDetails = transactionDetails;
        this.depositorAccountNumber = depositorAccountNumber;
        this.payeeName = payeeName;
        this.userID = userID;
        this.userBranch = userBranch;
        this.sessionID = sessionID;
        this.amount = amount;
        this.nooper = nooper;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBatchNumber() {
        return this.batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getItemSequenceNumber() {
        return this.itemSequenceNumber;
    }

    public void setItemSequenceNumber(String itemSequenceNumber) {
        this.itemSequenceNumber = itemSequenceNumber;
    }

    public String getPayorBankRoutNumber() {
        return this.payorBankRoutNumber;
    }

    public void setPayorBankRoutNumber(String payorBankRoutNumber) {
        this.payorBankRoutNumber = payorBankRoutNumber;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPresentingBankRoutNumber() {
        return this.presentingBankRoutNumber;
    }

    public void setPresentingBankRoutNumber(String presentingBankRoutNumber) {
        this.presentingBankRoutNumber = presentingBankRoutNumber;
    }

    public String getPayerName() {
        return this.payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getTransactionDetails() {
        return this.transactionDetails;
    }

    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getDepositorAccountNumber() {
        return this.depositorAccountNumber;
    }

    public void setDepositorAccountNumber(String depositorAccountNumber) {
        this.depositorAccountNumber = depositorAccountNumber;
    }

    public String getPayeeName() {
        return this.payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserBranch() {
        return this.userBranch;
    }

    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNooper() {
        return this.nooper;
    }

    public void setNooper(String nooper) {
        this.nooper = nooper;
    }

    public NCRRequest branchCode(String branchCode) {
        setBranchCode(branchCode);
        return this;
    }

    public NCRRequest userName(String userName) {
        setUserName(userName);
        return this;
    }

    public NCRRequest batchNumber(String batchNumber) {
        setBatchNumber(batchNumber);
        return this;
    }

    public NCRRequest itemSequenceNumber(String itemSequenceNumber) {
        setItemSequenceNumber(itemSequenceNumber);
        return this;
    }

    public NCRRequest payorBankRoutNumber(String payorBankRoutNumber) {
        setPayorBankRoutNumber(payorBankRoutNumber);
        return this;
    }

    public NCRRequest accountNumber(String accountNumber) {
        setAccountNumber(accountNumber);
        return this;
    }

    public NCRRequest serialNumber(String serialNumber) {
        setSerialNumber(serialNumber);
        return this;
    }

    public NCRRequest presentingBankRoutNumber(String presentingBankRoutNumber) {
        setPresentingBankRoutNumber(presentingBankRoutNumber);
        return this;
    }

    public NCRRequest payerName(String payerName) {
        setPayerName(payerName);
        return this;
    }

    public NCRRequest transactionDetails(String transactionDetails) {
        setTransactionDetails(transactionDetails);
        return this;
    }

    public NCRRequest depositorAccountNumber(String depositorAccountNumber) {
        setDepositorAccountNumber(depositorAccountNumber);
        return this;
    }

    public NCRRequest payeeName(String payeeName) {
        setPayeeName(payeeName);
        return this;
    }

    public NCRRequest userID(String userID) {
        setUserID(userID);
        return this;
    }

    public NCRRequest userBranch(String userBranch) {
        setUserBranch(userBranch);
        return this;
    }

    public NCRRequest sessionID(String sessionID) {
        setSessionID(sessionID);
        return this;
    }

    public NCRRequest amount(Double amount) {
        setAmount(amount);
        return this;
    }

    public NCRRequest nooper(String nooper) {
        setNooper(nooper);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " branchCode='" + getBranchCode() + "'" +
            ", userName='" + getUserName() + "'" +
            ", batchNumber='" + getBatchNumber() + "'" +
            ", itemSequenceNumber='" + getItemSequenceNumber() + "'" +
            ", payorBankRoutNumber='" + getPayorBankRoutNumber() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", presentingBankRoutNumber='" + getPresentingBankRoutNumber() + "'" +
            ", payerName='" + getPayerName() + "'" +
            ", transactionDetails='" + getTransactionDetails() + "'" +
            ", depositorAccountNumber='" + getDepositorAccountNumber() + "'" +
            ", payeeName='" + getPayeeName() + "'" +
            ", userID='" + getUserID() + "'" +
            ", userBranch='" + getUserBranch() + "'" +
            ", sessionID='" + getSessionID() + "'" +
            ", amount='" + getAmount() + "'" +
            ", nooper='" + getNooper() + "'" +
            "}";
    }
    
    
}
