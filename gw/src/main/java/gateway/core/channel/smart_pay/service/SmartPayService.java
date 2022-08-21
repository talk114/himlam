package gateway.core.channel.smart_pay.service;

import gateway.core.dto.PGResponse;

import java.io.IOException;

public interface SmartPayService {
    PGResponse createOrder(String request) throws IOException, IllegalArgumentException, IllegalAccessException;
    PGResponse queryOrder(String request) throws IOException, IllegalAccessException;
    String notification(String request) throws IOException, IllegalAccessException;
    PGResponse sendOTP(String request) throws IOException, IllegalAccessException;
    PGResponse verifyOTP(String request) throws IOException, IllegalAccessException;
}
