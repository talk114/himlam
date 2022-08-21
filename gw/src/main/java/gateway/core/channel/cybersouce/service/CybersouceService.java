/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.cybersouce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.cybersouce.request.*;
import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;

import java.util.Map;

/**
 * @author TaiND
 */
public interface CybersouceService {

    // liên kết thẻ
    PGResponse authorizeCard(final String authorizeCard) throws Exception;

    // xác thực liên kết thẻ
    PGResponse authorizeCard3D(final String authorizeCard3D) throws Exception;

    // thanh toán
    PGResponse authorize(final String authorize, ChannelFunction channelFunction) throws Exception;

    // xác thực thanh toán
    PGResponse authorize3D(final String authorize3D) throws Exception;

    // hủy liên kết thẻ
    PGResponse cancelAuthorizeCard(final String profile) throws Exception;

    // check card
    PGResponse checkEnrollment(final String checkEnrollment) throws Exception;

    // create transaction ssp
    PGResponse createTransaction(ChannelFunction channelFunction, final String sspCreateTransactionReq) throws Exception;

    //Cybersource decrypt payment credential data
    PGResponse CBSDecodePaymentCredential(ChannelFunction channelFunction, String retrieveReq) throws Exception;

    //NL decrypt payment credential data
    PGResponse NLDecodePaymentCredential(ChannelFunction channelFunction, String retrieveReq) throws Exception;

    //Notify payment result
    PGResponse notifyPaymentResult(ChannelFunction channelFunction, String notifyPaymentResultReq) throws Exception;

    //Health check
    PGResponse healthCheck() throws Exception;

    PGResponse createRequestJwt(String inputStr, String channel) throws Exception;

    PGResponse payerAuthenticationSetup3ds(String inputStr, String channel) throws Exception;

    PGResponse authorizeSubscription3D(String inputStr, String channel) throws Exception;

    PGResponse checkEnrollment3ds(String inputStr, String channel) throws Exception;

    PGResponse validate3Ds2(String inputStr, String channel) throws Exception;

    PGResponse validateJWT(PGRequest pgRequest, String channel) throws Exception;

    PGResponse decodeJWT(String inputStr, String channel) throws Exception;

    PGResponse deleteToken(String inputStr, String channel) throws Exception;
//    //Create JWT
//    PGResponse jwtCreate() throws Exception;

//    //Consumer Authentication With Cardinal
//    PGResponse cmpiLookUp(Map<String, String> payload) throws Exception;

//    //Validate Jwt in Be
//    PGResponse jwtValidate(String jwt) throws Exception;
//
//    //Jwt Decode
//    PGResponse jwtDecode(String jwt) throws Exception;
    PGResponse convertTransaction2Profile(String inputStr, String channel) throws Exception;
}
