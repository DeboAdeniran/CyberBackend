package com.example.cyberbankend.Service;

import com.example.cyberbankend.Dto.Request.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
