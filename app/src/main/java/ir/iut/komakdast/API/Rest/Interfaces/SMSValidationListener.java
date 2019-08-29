package ir.iut.komakdast.API.Rest.Interfaces;

import ir.iut.komakdast.API.Rest.Utils.SMSValidateToken;

/**
 * Created by al on 3/4/16.
 */
public interface SMSValidationListener {

    void onSMSValidateSent(SMSValidateToken smsToken);

    void onSMSValidationFail();

    void onSMSValidationCodeFail();

    void onValidatedCode(SMSValidateToken smsValidateToken);

}
