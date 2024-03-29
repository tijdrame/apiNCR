package com.boa.api.service.util;

public interface ICodeDescResponse {

    public static String SUCCES_CODE = "200";
    public static String CONSULTATION_ECHEC_CODE = "305";
    public static String VIRCOMPTE_ECHEC_CODE = "305";
    public static String VIRDOMESTIQUE_ECHEC_CODE ="305";
    public static String VIRINTERNATIONAL_ECHEC_CODE = "305";
    public static String FILIALE_ABSENT_CODE = "303";
    public static String PARAM_ABSENT_CODE = "304";
    public static String ECHEC_CODE = "301";
    public static String ECHEC_CHAMP_CODE = "306";
    public static String ECHEC_DESCRIPTION = "Operation failed.";
    public static String ECHEC_CHAMP_DESC = "Champ non trouve.";
    public static String ACCOUNT_PRINCIPAL_NON_TROUVE = "Account Principal non trouve.";
    public static String PARAM_DESCRIPTION = "Mandatory parameter (s) absent";
    public static String SUCCES_DESCRIPTION = "Operation successfully completed.";
    public static String FILIALE_ABSENT_DESC = "Proxy injoignable";
    public static String PARAM_ABSENT_DESC = "Parametre non trouve.";
    public static String SERVICE_ABSENT_DESC = "Non-parameterized service.";
    public static String DEBIT_ACCOUNT_ABSENT = "Compte debiteur absent et/ou non parametre.";
    public static String DEBIT_OUT_ABSENT = "Compte non parametre pour le Out.";
    public static String REQUEST_INWARD_IN_OUT = "Le parametre isIn doit etre In ou Out.";
    public static String RESPONSE_INC = "Quelque chose d'innatendu s'est produit, veuillez contacter l'Administrateur.";
    public static String ADRESSE_WS = "http://172.17.178.3:8089/testurl";
    public static String SERVICE_VIREMENT_DOMESTIQUE = "VirementDomestique";
    public static String SERVICE_CONSULTATION_SOLDE = "ConsultationSolde";
    public static String SERVICE_VIREMENT_ACCOUNT = "VirementCompte";
    public static String SERVICE_VIREMENT_INTERNATIONAL = "VirementInternational";
    public static String EXCEPTION_CODE = "307";
    public static Integer COMPTE_ABSENT_CODE = 406;
    public static String COMPTE_ABSENT_DESC = "Parametre compte absent";
    public static Integer INSTITUTION_ABSENT_CODE = 407;
    public static String INSTITUTION_ABSENT_DESC = "Parametre client Absent";
    public static String DEVISE_ATTENDU_GSH = "The currency must be GHS";

}